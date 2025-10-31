package com.kimothorick.plashr.utils.tooling

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "small-phone", showSystemUi = true, device = Devices.PIXEL_4A)
@Preview(name = "phone", showSystemUi = true, device = Devices.PHONE)
@Preview(name = "landscape", showSystemUi = true, device = "spec:width=640dp,height=360dp,dpi=480")
@Preview(name = "tablet", showSystemUi = true, device = Devices.TABLET)
annotation class DevicePreviews(
    val name: String,
)

@Preview(name = "phone", showSystemUi = true, device = Devices.PHONE)
annotation class DevicePreview(
    val name: String,
)

@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
annotation class ComponentPreviews
