# Devfolio Mobile

An open source Android client for Devfolio built with Kotlin Multiplatform and native Jetpack Compose.

Devfolio does not offer an official native mobile app. This client addresses that gap by providing an offline first mobile experience for discovering hackathons, reviewing timelines, setting registration reminders, and exploring past submissions.

## Devfolio Attribution and Credits

This project is an independent community client developed out of admiration for Devfolio.

All hackathon listings, organizer logos, project submissions, developer profiles, and event timelines are owned, managed, and hosted by Devfolio (NSB Classic PTE LTD).

This application accesses publicly available API endpoints to display hackathon details and directs all event applications through Devfolio official web endpoints via secure Chrome Custom Tabs.

We thank the Devfolio team for creating and nurturing an exceptional developer and hackathon ecosystem. If you represent Devfolio and have any questions or feedback regarding this project, please open an issue or reach out directly.

For more details on Devfolio, visit https://devfolio.co.

## Architecture

The project follows a clean Kotlin Multiplatform architecture with strict unidirectional data flow:

* `shared` module: Contains all networking, domain models, and repositories in `commonMain`. It is 100 percent platform agnostic and uses zero Android imports.
* `androidApp` module: A native Jetpack Compose presentation layer styled with Material 3 Expressive guidelines, fluid spring physics, and pervasive haptic feedback.

```
shared/
└── src/commonMain/kotlin/com/masum/devfolio/
    ├── model/         # Immutable serializable DTOs and domain mappings
    ├── network/       # Ktor 3.6 HTTP client and Devfolio API service
    └── repository/    # DevfolioRepository and BookmarkRepository

androidApp/
└── src/main/kotlin/com/masum/devfolio/
    ├── navigation/    # Type-safe Navigation Compose routes
    ├── theme/         # Material 3 Expressive tokens, shapes, and haptics
    ├── ui/
    │   ├── components/# Reusable micro-composables and attribution dialog
    │   ├── discover/  # Feed screen with pull-to-refresh
    │   ├── detail/    # Event details with split button apply bridge
    │   ├── projects/  # Showcase gallery with GitHub links
    │   └── saved/     # Offline bookmarks screen
    └── util/          # Chrome Custom Tabs and Calendar intent helpers
```

## Tech Stack

* Kotlin: 2.4.21
* Android Gradle Plugin: 9.1.1
* Jetpack Compose Material 3: 1.5.0-beta02 (Material 3 Expressive)
* Navigation Compose: 2.10.2 (type-safe routes)
* Ktor Multiplatform: 3.6.0
* Kotlinx Serialization: 1.8.0
* Kotlinx DateTime: 0.8.0
* Coil 3: 3.6.3
* AndroidX Browser: 1.10.0 (Chrome Custom Tabs)

## Key Features

1. Hackathon Discovery: Real time search and status filtering (Open, Upcoming, Ended) powered by Devfolio search APIs.
2. Event Details: Schedule timeline, FAQs, participant counts, and problem statement descriptions.
3. Offline Bookmarks: Save hackathons locally to access details without an internet connection.
4. Google Calendar Reminders: One tap to schedule registration deadline alerts directly into your device calendar.
5. Projects Showcase: Browse past winning hackathon projects with tags and links to source code and demo videos.
6. Apply Bridge: Secure application submission using high performance Chrome Custom Tabs with zero webview overhead.
7. Material 3 Expressive Polish: Polygon morphing loading indicators, floating action toolbars, and tactile haptic feedback.
8. Complete Attribution: In-app credits dialog and source attribution linking directly to Devfolio platform.

## Adding the iOS Target

Because the shared module contains zero Android dependencies, an iOS client can be added directly:

1. Open `shared/build.gradle.kts`.
2. Add the iOS target targets block:

```kotlin
listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
).forEach { iosTarget ->
    iosTarget.binaries.framework {
        baseName = "DevfolioShared"
        isStatic = true
    }
}
```

3. Build the XCFramework or Swift package and consume the shared repositories directly in SwiftUI.

## Building and Running

Prerequisites: JDK 17 or higher and Android SDK (compileSdk 37).

To compile debug sources:
```bash
./gradlew compileDebugSources
```

To run unit tests:
```bash
./gradlew :shared:testAndroidHostTest
```

To package a debug APK:
```bash
./gradlew assembleDebug
```

The output APK will be generated at `androidApp/build/outputs/apk/debug/androidApp-debug.apk`.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
Devfolio is a registered trademark of NSB Classic PTE LTD. This project is an independent community client and is not officially affiliated with Devfolio.
