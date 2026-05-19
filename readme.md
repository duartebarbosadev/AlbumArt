# AlbumArt

AlbumArt is a native Android application built with Kotlin and Jetpack Compose

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="200" alt="AlbumArt app icon">
</p>

# Todo:

- Instead of loading message, show skeleton
- Install splashscreen

## Features

- Fetches albums directly from the iTunes Top Albums RSS API:
  `https://itunes.apple.com/us/rss/topalbums/limit=100/json`
- Displays a responsive scrollable album grid with album artwork, album name, artist, and genre.
- Opens a detail screen for each album.
- Shows loading and error states.
- Caches the latest album list in memory
- Includes unit tests for mapping, repository behavior, and ViewModels.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Navigation 3
- Hilt
- OkHttp
- Kotlinx Serialization
- Coil
- JUnit, MockK, and kotlinx-coroutines-test

## Project Structure

- `app`: Application entry point, theme, and top-level navigation.
- `core:network`: iTunes RSS API request and DTO models.
- `core:data`: Domain model, DTO mapping, and album repository.
- `core:ui`: Shared UI helpers and preview annotations.
- `feature:albumlist:api`: Album list navigation contract.
- `feature:albumlist:impl`: Album list ViewModel and Compose UI.
- `feature:albumdetails:api`: Album details navigation contract.
- `feature:albumdetails:impl`: Album details ViewModel and Compose UI.
