# Devfolio Android

An open source Android client for Devfolio built with Kotlin Multiplatform and native Jetpack Compose.

> [!WARNING]
> **Early Alpha Version (v0.1.0-alpha)**  
> This is a very early, experimental community preview. You should expect bugs, incomplete features, and ongoing API iterations. We are actively polishing the UX and architecture. Contributions, bug reports, and feature requests are very welcome!

---

## Why Devfolio Android?

Devfolio does not offer an official native mobile app. This client addresses that gap by providing a modern, smooth mobile experience for discovering hackathons, tracking deadlines, reviewing timelines, setting calendar reminders, and exploring past submissions.

---

## Devfolio Attribution and Credits

This project is an independent community client developed out of admiration for Devfolio.

* All hackathon listings, organizer logos, project submissions, developer profiles, and event timelines are owned, managed, and hosted by **Devfolio (NSB Classic PTE LTD)**.
* Official Devfolio brand assets and logos used under Devfolio brand guidelines from [devfolioco/brand-assets](https://github.com/devfolioco/brand-assets).
* This application connects to Devfolio's web and API endpoints to display hackathon details and hacker application dashboards.
* We thank the Devfolio team for creating and nurturing an exceptional developer ecosystem. If you represent Devfolio and have any questions or feedback regarding this project, please open an issue or reach out directly.
* Visit the official Devfolio platform at [https://devfolio.co](https://devfolio.co).

---

## Features & Highlights

- **Hackathon Discovery**: Real-time search and status filtering (Open, Upcoming, Ended) powered by Devfolio search APIs.
- **Event Details & Markdown**: Schedules, FAQs, eligibility criteria, problem statements rendered natively with markdown formatting and relative dates.
- **Offline Bookmarks**: Save hackathons locally to access details anytime without an active internet connection.
- **Google Calendar Sync**: Add registration closes and event start dates to your device calendar with one tap.
- **Projects Showcase & Native Details**: Browse past hackathon submissions with tags, demo video links, and GitHub repositories.
- **Account & Application Status**:
  - Secure in-app sign-in flow.
  - Real-time application tracking (Submitted, Team info, RSVP status) with instant cache to prevent UI flashing.
  - Safeguarded application withdrawal confirmation dialog.
  - Session token inspector and one-tap copy directly in Settings.
- **Material 3 Expressive Design**:
  - Dynamic Color, Pure Black (AMOLED) mode, and expressive button groups.
  - Google Sans typography support with instant font switching.
  - Predictive back gesture support and micro-tactile haptic feedback.

---

## Architecture

The project follows a clean Kotlin Multiplatform (KMP) architecture:

- `shared/` module: Contains all networking (Ktor 3.6), serialization (Kotlinx Serialization), and domain repositories in `commonMain`. 100% platform-agnostic with zero Android dependencies.
- `androidApp/` module: Pure Jetpack Compose UI adhering to Material 3 Expressive guidelines with unidirectional data flow and type-safe Navigation Compose routes.

```
shared/
└── src/commonMain/kotlin/com/masum/devfolio/
    ├── model/         # Immutable serializable DTOs and domain mappings
    ├── network/       # Ktor 3.6 HTTP client and Devfolio API service
    └── repository/    # DevfolioRepository and BookmarkRepository

androidApp/
└── src/main/kotlin/com/masum/devfolio/
    ├── data/          # AuthSessionManager and HackathonApplicationRepository
    ├── navigation/    # Type-safe Navigation Compose routes
    ├── theme/         # Material 3 tokens, Google Sans typography, and Haptics
    ├── ui/
    │   ├── about/     # Project information, mission, and credits
    │   ├── auth/      # Sign-in dialog and credential handling
    │   ├── components/# Reusable components, MarkdownText, and FloatingNavBar
    │   ├── detail/    # Event details, split action bar, and status badge
    │   ├── discover/  # Hackathon feed with pull-to-refresh
    │   ├── projects/  # Project showcase and native detail view
    │   ├── saved/     # Offline bookmarks
    │   └── settings/  # Appearance, typography, haptics, and account token
    └── util/          # Date formatting, Chrome Custom Tabs, and Calendar intents
```

---

## Tech Stack

- **Kotlin**: 2.4.21
- **Android Gradle Plugin**: 9.1.1
- **Jetpack Compose Material 3**: 1.5.0-beta02 (Material 3 Expressive)
- **Navigation Compose**: 2.10.2 (type-safe routes)
- **Ktor Multiplatform**: 3.6.0
- **Kotlinx Serialization**: 1.8.0
- **Kotlinx DateTime**: 0.8.0
- **Coil 3**: 3.6.3
- **AndroidX Browser**: 1.10.0 (Chrome Custom Tabs)

---

## Building and Running

### Prerequisites
- JDK 17 or higher
- Android SDK (compileSdk 37, minSdk 26)

### Compile Debug Build
```bash
./gradlew compileDebugSources
```

### Run Tests
```bash
./gradlew testDebugUnitTest
```

### Build Debug APK
```bash
./gradlew :androidApp:assembleDebug
```
The debug APK is generated at:  
`androidApp/build/outputs/apk/debug/androidApp-debug.apk`

### Build Release APK
```bash
./gradlew :androidApp:assembleRelease
```
The release APK is generated at:  
`androidApp/build/outputs/apk/release/`

---

## Contributing & Community

Contributions are warmly welcomed! Since this is an alpha release, there is substantial room for improvements:

- Polishing edge cases in application form interactions
- Expanding team management & invite flows
- Adding tablet / foldable adaptive multi-pane layouts
- Native Swift / SwiftUI frontend for iOS (consuming the shared KMP core)

To contribute:
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

*Disclaimer: Devfolio is a registered trademark of NSB Classic PTE LTD. This project is an independent community client and is not officially affiliated with or endorsed by Devfolio.*
