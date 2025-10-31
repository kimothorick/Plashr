<p align="center">
  <img width="150" height="150" src="docs/images/AppLogo.png" alt="Logo">
</p>

<h1 align="center">Plashr</h1>

<p align="center">
  An unofficial Unsplash client for Android, built with modern Android development tools.
</p>

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.kimothorick.plashr"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" width="200"/></a>
</p>
<p align="center">
  <img src="https://img.shields.io/badge/Coming%20Soon-424242?style=for-the-badge" alt="Coming Soon">
</p>
<p align="center">
    <a href="https://www.gnu.org/licenses/gpl-3.0"><img src="https://img.shields.io/badge/License-GPLv3-blue.svg" alt="License: GPL v3"></a>
    <a href="https://solo.to/kimothorick"><img src="https://img.shields.io/badge/Made_by_Rick_Kimotho-000000?logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjBweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyMHB4IiBmaWxsPSIjZThlYWVkIj48cGF0aCBkPSJNNDgwLTQ4MHEtNjAgMC0xMDItNDJ0LTQyLTEwMnEwLTYwIDQyLTEwMnQxMDItNDJxNjAgMCAxMDIgNDJ0NDIgMTAycTAgNjAtNDIgMTAydC0xMDIgNDJaTTE5Mi0xOTJ2LTk2cTAtMjMgMTIuNS00My41VDIzOS0zNjZxNTUtMzIgMTE2LjUtNDlUNDgwLTQzMnE2MyAwIDEyNC41IDE3VDcyMS0zNjZxMjIgMTMgMzQuNSAzNHQxMi41IDQ0djk2SDE5MloiLz48L3N2Zz4%3D" alt="Made by Rick Kimotho"></a>
    <a href="https://buymeacoffee.com/kimothorick"><img src="https://img.shields.io/badge/Buy_Me_A_Coffee-FFDD00?logo=buymeacoffee&logoColor=000" alt="Buy Me A Coffee"></a>
</p>

## About

Plashr is a modern Android application for browsing the vast collection of beautiful photos
from [Unsplash](https://unsplash.com). This is
an unofficial client and is not created by or affiliated with Unsplash.

Organize your favorite photos into collections, like photos from other users, and view them in
stunning detail.

## Screenshots

<p>
  <img src="/docs/images/home.png" width="250" alt="Home screen"/>
  <img src="/docs/images/topic_details.png" width="250" alt="Topic details screen"/>
  <img src="/docs/images/search_mobile.png" width="250" alt="Search screen"/>
  <img src="/docs/images/photo_details.png" width="250" alt="Photo details screen"/>
</p>

## Features

- 🖼️ **Browse and search** for high-quality photos.
- 🔍 View **photo details**, including EXIF data.
- ❤️ **Like** photos and add them to your profile.
- 📂 Create and manage **photo collections**.
- 👤 View **user profiles**, including their photos, likes, and collections.
- 🔄 Infinite scrolling powered by the Unsplash API.

## Tech Stack

Plashr is built using the latest Android development technologies:

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative
  UI.
- **Language:** 100% [Kotlin](https://kotlinlang.org/).
- **Asynchronicity:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
  and [Flow](https://developer.android.com/kotlin/flow) for managing background threads and data
  streams.
- **Architecture:** MVVM (Model-View-ViewModel) with Android Architecture
  Components ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)).
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for managing
  dependencies.
- **Networking:** [Retrofit](https://square.github.io/retrofit/) for making API calls to the
  Unsplash service.

## Getting Started

To build the app, you will need to get an API key from Unsplash.

### Unsplash

1. Register for a developer account at [unsplash.com/developers](https://unsplash.com/developers) to
   obtain your API credentials.
2. Create a file named `apikey.properties` in the root directory of the project.
3. Add your Unsplash API credentials to the `apikey.properties` file:

```properties
client_id="<Your Unsplash Access Key>"
client_secret="<Your Unsplash Secret Key>"
callback_url="<Your Unsplash Callback URL>"
```

### Firebase

This project uses Firebase Crashlytics and Analytics for crash reporting and usage statistics.

1. Go to the [Firebase console](https://console.firebase.google.com/) and create a new project.
2. Follow the setup guide to add an Android app to your project.
3. Once the app is registered, download the `google-services.json` configuration file and place it
   in the `app/` directory.

## Acknowledgments

A big thank you to [Unsplash](https://unsplash.com) for providing the amazing photo API that powers
this app.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file
for details.

## Privacy Policy

Please refer to the [privacy policy](PRIVACY_POLICY.md).
