package com.kimothorick.plashr

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.window.core.layout.WindowSizeClass
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.search.presentation.components.FilterOptions
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.utils.WindowWidthClass
import com.kimothorick.plashr.utils.connectivityObserver.ConnectivityObserver
import com.kimothorick.plashr.utils.windowWidthSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * A data class that holds configuration values for screen layouts,
 * which adapt based on window size and selected photo layout type.
 *
 * @param contentPadding The general padding applied to the main content area.
 * @param gridEndPadding The padding applied to the end of a grid layout, useful for larger screens.
 * @param photoCornerRadius A boolean indicating whether photos should have rounded corners.
 * @param photoContentPadding The padding applied within each individual photo item.
 * @param headerContentPadding The padding for header elements within certain layouts (e.g., List).
 */
data class LayoutConfig(
    val contentPadding: Dp,
    val gridEndPadding: Dp,
    val photoCornerRadius: Boolean,
    val photoContentPadding: Dp,
    val headerContentPadding: Dp,
)

/**
 * A data class that defines the spacing and padding for a grid layout.
 *
 * This configuration is used to adapt the layout of photo grids based on the
 * screen size (window size class) and the selected layout type (e.g., Cards, Staggered Grid).
 *
 * @property verticalSpacing The vertical spacing between items in the grid.
 * @property horizontalSpacing The horizontal spacing between items in the grid.
 * @property itemStartPadding The padding applied to the start of each grid item.
 * @property itemEndPadding The padding applied to the end of each grid item.
 */
data class GridSpacing(
    val verticalSpacing: Dp,
    val horizontalSpacing: Dp,
    val itemStartPadding: Dp,
    val itemEndPadding: Dp,
)

/**
 * Represents the overall configuration for the app's UI layout, which adapts to different
 * window sizes and user-selected layout preferences.
 *
 * This data class aggregates more specific configuration objects to provide a single source
 * of truth for layout parameters used across various composables.
 *
 * @param layoutConfig General layout parameters like padding and corner radius.
 * @param gridSpacing Spacing details specifically for grid-based layouts.
 * @param adaptiveMinSize The minimum size for items in an adaptive grid, used to determine
 *                        the number of columns.
 */
data class AppConfig(
    val layoutConfig: LayoutConfig,
    val gridSpacing: GridSpacing,
    val adaptiveMinSize: Dp,
)

/**
 * ViewModel for the main application screen.
 *
 * This ViewModel is responsible for managing the global UI state of the application,
 * including layout configurations, search functionality, filter options, and user session state.
 * It adapts the UI layout based on window size and user preferences, handles the visibility
 * of various bottom sheets (login, filters, account management), and observes network connectivity.
 *
 * @property appConfig Exposes the current [AppConfig] which dictates the layout, spacing, and sizing
 *                     of UI components, adapting to different screen sizes and layout types.
 * @property showFilterBottomSheet A [StateFlow] that controls the visibility of the filter bottom sheet.
 * @property filterOptions A [StateFlow] holding the currently applied [FilterOptions] for content filtering.
 * @property searchQuery A [StateFlow] holding the current search query string.
 * @property showLoginBottomSheet A [StateFlow] that controls the visibility of the login bottom sheet.
 * @property showManageAccountBottomSheet A [StateFlow] that controls the visibility of the manage account bottom sheet.
 * @property isConnected A [StateFlow] that emits the current network connectivity status.
 *
 * @param connectivityObserver An observer that provides the network connection status.
 */
@HiltViewModel
class MainViewModel
    @Inject constructor(
        connectivityObserver: ConnectivityObserver,
    ) : ViewModel() {
        val isConnected = connectivityObserver.isConnected.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false,
        )
        private val _appConfig = MutableStateFlow(
            AppConfig(
                layoutConfig = LayoutConfig(
                    contentPadding = 16.dp,
                    gridEndPadding = 0.dp,
                    photoCornerRadius = false,
                    photoContentPadding = 16.dp,
                    headerContentPadding = 0.dp,
                ),
                gridSpacing = GridSpacing(0.dp, 0.dp, 0.dp, 0.dp),
                adaptiveMinSize = Constants.LayoutValues.MIN_ADAPTIVE_SIZE,
            ),
        )

        val appConfig: StateFlow<AppConfig> = _appConfig.asStateFlow()

        /**
         * Updates the application's layout configuration based on the device's window size
         * and the user's selected photo layout preference.
         *
         * This function calculates the appropriate layout settings, grid spacings, and
         * adaptive grid item sizes, then emits a new [AppConfig] state.
         *
         * @param windowSizeClass The current [WindowSizeClass] of the application window,
         * used to determine responsive layout adjustments.
         * @param photoLayoutType The user's preferred layout style from [PhotoLayoutType]
         * (e.g., CARDS, STAGGERED_GRID, LIST).
         */
        fun updateConfig(
            windowSizeClass: WindowSizeClass,
            photoLayoutType: PhotoLayoutType,
        ) {
            val newLayoutConfig = getLayoutConfig(windowSizeClass, photoLayoutType)
            val newGridSpacing = getGridSpacings(windowSizeClass, photoLayoutType)
            val newAdaptiveMinSize = getAdaptiveMinSize(windowSizeClass, photoLayoutType)
            _appConfig.value = AppConfig(newLayoutConfig, newGridSpacing, newAdaptiveMinSize)
        }

        private val _showFilterBottomSheet = MutableStateFlow(false)
        val showFilterBottomSheet: StateFlow<Boolean> = _showFilterBottomSheet.asStateFlow()

        /**
         * Sets the visibility of the filter bottom sheet.
         *
         * This function is used to programmatically show or hide the bottom sheet that contains
         * the search filter options.
         *
         * @param show A boolean value indicating whether to show (`true`) or hide (`false`) the bottom sheet.
         */
        fun setShowFilterBottomSheet(
            show: Boolean,
        ) {
            _showFilterBottomSheet.value = show
        }

        private val _filterOptions = MutableStateFlow(FilterOptions())
        val filterOptions: StateFlow<FilterOptions> = _filterOptions.asStateFlow()

        /**
         * Updates the current search filter options with a new set of filters.
         *
         * This function is typically called when the user applies new filter settings
         * from the filter bottom sheet or a similar UI component. The new filters
         * will be observed by UI components that display filtered content, triggering a
         * refresh of the data.
         *
         * @param newFilters The [FilterOptions] object containing the new filter values to be applied.
         */
        fun applyFilters(
            newFilters: FilterOptions,
        ) {
            _filterOptions.value = newFilters
        }

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        /**
         * Updates the current search query.
         *
         * This function sets the value of the internal `_searchQuery` state flow,
         * which is exposed as `searchQuery`. This is typically called when the user
         * types in a search bar.
         *
         * @param query The new search string to set.
         */
        fun setSearchQuery(
            query: String,
        ) {
            _searchQuery.value = query
        }

        private val _showLoginBottomSheet = MutableStateFlow(false)

        /**
         * Updates the state to show or hide the login bottom sheet.
         *
         * @param value `true` to show the bottom sheet, `false` to hide it.
         */
        fun setShowLoginBottomSheet(
            value: Boolean,
        ) {
            _showLoginBottomSheet.value = value
        }

        val showLoginBottomSheet: StateFlow<Boolean> = _showLoginBottomSheet.asStateFlow()

        private val _showManageAccountBottomSheet = MutableStateFlow(false)

        /**
         * Sets the visibility of the "Manage Account" bottom sheet.
         *
         * This bottom sheet typically contains options for logged-in users, such as
         * viewing their profile, managing settings, or logging out.
         *
         * @param value `true` to show the bottom sheet, `false` to hide it.
         */
        fun setShowManageAccountBottomSheet(
            value: Boolean,
        ) {
            _showManageAccountBottomSheet.value = value
        }

        val showManageAccountBottomSheet: StateFlow<Boolean> = _showManageAccountBottomSheet.asStateFlow()

        /**
         * Checks if the user is authorized and shows the login bottom sheet if they are not.
         * This is used to prompt unauthenticated users to log in when they try to access
         * a feature that requires authorization.
         *
         * @param isAppAuthorized A boolean indicating whether the user is currently authorized.
         */
        fun checkAndShowLoginBottomSheet(
            isAppAuthorized: Boolean,
        ) {
            if (!isAppAuthorized) {
                _showLoginBottomSheet.value = true
            }
        }

        /**
         * Determines the minimum size for items in an adaptive grid layout.
         *
         * This function calculates the appropriate minimum size (`Dp`) for grid items based on the
         * current window size and the selected photo layout type. It is primarily used for
         * `LazyVerticalGrid` with `GridCells.Adaptive`.
         *
         * For the [PhotoLayoutType.STAGGERED_GRID], it returns a smaller size on compact screens
         * to allow for more columns, and a larger size on medium/expanded screens. For all other
         * layout types, it returns a default large size, effectively making them non-adaptive
         * in the same way.
         *
         * @param windowSizeClass The current window size classification (Compact, Medium, Expanded).
         * @param photoLayoutType The type of layout selected by the user (e.g., Staggered Grid, Cards, List).
         * @return The minimum size in `Dp` for an adaptive grid item.
         */
        private fun getAdaptiveMinSize(
            windowSizeClass: WindowSizeClass,
            photoLayoutType: PhotoLayoutType,
        ): Dp {
            if (photoLayoutType != PhotoLayoutType.STAGGERED_GRID) return 361.dp
            return when (windowSizeClass.windowWidthSize()) {
                WindowWidthClass.COMPACT -> 180.dp
                else -> 361.dp
            }
        }

        /**
         * Calculates and returns the appropriate grid spacing values based on the screen size and
         * selected layout type.
         *
         * This function determines the vertical and horizontal spacing between grid items, as well
         * as the start and end padding for the items, to ensure an optimal and responsive layout
         * across different devices and layout configurations (Cards, Staggered Grid, List).
         *
         * @param windowSizeClass The current window size class of the device, indicating if the
         *   width is compact, medium, or expanded.
         * @param layoutType The selected layout style for displaying photos (e.g., CARDS,
         *   STAGGERED_GRID, LIST).
         * @return A [GridSpacing] data object containing the calculated spacing and padding values.
         */
        private fun getGridSpacings(
            windowSizeClass: WindowSizeClass,
            layoutType: PhotoLayoutType,
        ): GridSpacing =
            when (layoutType) {
                PhotoLayoutType.CARDS -> when (windowSizeClass.windowWidthSize()) {
                    WindowWidthClass.COMPACT -> GridSpacing(
                        verticalSpacing = 12.dp,
                        horizontalSpacing = 8.dp,
                        itemStartPadding = 16.dp,
                        itemEndPadding = 16.dp,
                    )

                    WindowWidthClass.MEDIUM -> GridSpacing(
                        verticalSpacing = 12.dp,
                        horizontalSpacing = 8.dp,
                        itemStartPadding = 16.dp,
                        itemEndPadding = 0.dp,
                    )

                    WindowWidthClass.EXPANDED -> GridSpacing(
                        verticalSpacing = 12.dp,
                        horizontalSpacing = 8.dp,
                        itemStartPadding = 0.dp,
                        itemEndPadding = 0.dp,
                    )
                }

                PhotoLayoutType.STAGGERED_GRID -> when (windowSizeClass.windowWidthSize()) {
                    WindowWidthClass.COMPACT -> GridSpacing(
                        verticalSpacing = 6.dp,
                        horizontalSpacing = 6.dp,
                        itemStartPadding = 16.dp,
                        itemEndPadding = 16.dp,
                    )

                    WindowWidthClass.MEDIUM -> GridSpacing(
                        verticalSpacing = 6.dp,
                        horizontalSpacing = 6.dp,
                        itemStartPadding = 16.dp,
                        itemEndPadding = 0.dp,
                    )

                    WindowWidthClass.EXPANDED -> GridSpacing(
                        verticalSpacing = 6.dp,
                        horizontalSpacing = 6.dp,
                        itemStartPadding = 0.dp,
                        itemEndPadding = 0.dp,
                    )
                }

                PhotoLayoutType.LIST -> when (windowSizeClass.windowWidthSize()) {
                    WindowWidthClass.COMPACT -> GridSpacing(
                        verticalSpacing = 0.dp,
                        horizontalSpacing = 6.dp,
                        itemStartPadding = 0.dp,
                        itemEndPadding = 0.dp,
                    )

                    WindowWidthClass.MEDIUM -> GridSpacing(
                        verticalSpacing = 0.dp,
                        horizontalSpacing = 6.dp,
                        itemStartPadding = 16.dp,
                        itemEndPadding = 0.dp,
                    )

                    WindowWidthClass.EXPANDED -> GridSpacing(
                        verticalSpacing = 6.dp,
                        horizontalSpacing = 6.dp,
                        itemStartPadding = 0.dp,
                        itemEndPadding = 0.dp,
                    )
                }
            }

        /**
         * Determines the appropriate [LayoutConfig] based on the device's screen size and the
         * selected photo layout preference. This configuration dictates various UI padding and styling
         * options to create an adaptive layout.
         *
         * @param windowSizeClass The current window size class of the device (e.g., Compact, Medium, Expanded).
         * @param photoLayoutType The user's selected layout style for displaying photos (e.g., Cards, Staggered Grid, List).
         * @return A [LayoutConfig] object with specific dimension and style values tailored for the
         * given screen size and layout type.
         */
        private fun getLayoutConfig(
            windowSizeClass: WindowSizeClass,
            photoLayoutType: PhotoLayoutType,
        ): LayoutConfig =
            when (windowSizeClass.windowWidthSize()) {
                WindowWidthClass.COMPACT -> when (photoLayoutType) {
                    PhotoLayoutType.CARDS -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 0.dp,
                        photoCornerRadius = false,
                        photoContentPadding = 16.dp,
                        headerContentPadding = 0.dp,
                    )

                    PhotoLayoutType.STAGGERED_GRID -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 0.dp,
                        photoCornerRadius = true,
                        photoContentPadding = 4.dp,
                        headerContentPadding = 0.dp,
                    )

                    PhotoLayoutType.LIST -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 0.dp,
                        photoCornerRadius = false,
                        photoContentPadding = 0.dp,
                        headerContentPadding = 16.dp,
                    )
                }

                WindowWidthClass.MEDIUM -> when (photoLayoutType) {
                    PhotoLayoutType.CARDS -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 16.dp,
                        photoCornerRadius = false,
                        photoContentPadding = 16.dp,
                        headerContentPadding = 0.dp,
                    )

                    PhotoLayoutType.STAGGERED_GRID -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 0.dp,
                        photoCornerRadius = true,
                        photoContentPadding = 8.dp,
                        headerContentPadding = 0.dp,
                    )

                    PhotoLayoutType.LIST -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 16.dp,
                        photoCornerRadius = false,
                        photoContentPadding = 0.dp,
                        headerContentPadding = 16.dp,
                    )
                }

                WindowWidthClass.EXPANDED -> when (photoLayoutType) {
                    PhotoLayoutType.CARDS -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 16.dp,
                        photoCornerRadius = false,
                        photoContentPadding = 16.dp,
                        headerContentPadding = 0.dp,
                    )

                    PhotoLayoutType.STAGGERED_GRID -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 0.dp,
                        photoCornerRadius = true,
                        photoContentPadding = 8.dp,
                        headerContentPadding = 0.dp,
                    )

                    PhotoLayoutType.LIST -> LayoutConfig(
                        contentPadding = 16.dp,
                        gridEndPadding = 16.dp,
                        photoCornerRadius = false,
                        photoContentPadding = 0.dp,
                        headerContentPadding = 16.dp,
                    )
                }
            }
    }
