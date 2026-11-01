# Contributing to Devfolio Android

Thank you for your interest in contributing to Devfolio Android! This is an open source community-driven client for Devfolio built with Kotlin Multiplatform (KMP) and Jetpack Compose.

Since this project is currently in early alpha (`v0.1.1-alpha`), there is plenty of room for improvement, feature expansion, and bug fixing. We appreciate all kinds of contributions, whether it's reporting an issue, proposing design enhancements, or submitting code.

---

## Code of Conduct

We strive to maintain a welcoming, respectful, and inclusive community for everyone. Be courteous, constructive in feedback, and supportive of fellow contributors.

---

## How Can You Contribute?

Here are some areas where your help would be very valuable:
- **Bug Fixes**: Addressing UI glitches, token refresh edge cases, or navigation hiccups.
- **Application Flow**: Expanding team creation, invite management, and organizer-specific custom form queries.
- **Adaptive Layouts**: Implementing responsive 2-pane / multi-pane Compose layouts for foldables, tablets, and Chromebooks.
- **iOS Client (Swift / SwiftUI)**: Building a native iOS frontend consuming the shared KMP core framework.
- **Testing**: Writing unit tests for ViewModels, repositories, and UI test harnesses.

---

## Getting Started

### 1. Prerequisites
- **JDK 17** (Temurin or OpenJDK recommended)
- **Android Studio** (Ladybug or newer) or Command-line tools
- Android SDK configured with `compileSdk 37`, `minSdk 26`

### 2. Fork and Clone
```bash
git clone https://github.com/YOUR_USERNAME/devfolio-android.git
cd devfolio-android
```

### 3. Build & Verify Locally
Ensure everything builds and passes tests on your machine before making changes:

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Assemble debug build
./gradlew :androidApp:assembleDebug
```

---

## Development Guidelines

1. **Architecture & Separation of Concerns**:
   - `shared`: Contains platform-agnostic networking (Ktor), domain models, and repositories. Zero Android dependencies belong here.
   - `androidApp`: Jetpack Compose UI with Material 3 Expressive standards, type-safe navigation, and reactive state management.
2. **Typography & Styling**:
   - Use semantic Material 3 typography tokens (`MaterialTheme.typography.*`).
   - Respect user settings for dynamic colors, AMOLED pure black, and Google Sans font preference.
3. **Tactile Haptics**:
   - Use `DevfolioHaptics` for interactive touch elements (card clicks, toggles, copies).
4. **Clean Git Practices**:
   - Write clear, concise commit messages following conventional commits (e.g. `feat:`, `fix:`, `refactor:`, `docs:`).
   - Keep pull requests focused on a single feature or bug fix.

---

## Submitting a Pull Request (PR)

1. **Branch Naming**:
   - `feature/your-feature-name`
   - `fix/issue-description`
   - `docs/what-changed`
2. **Push and Create PR**:
   - Ensure your code builds locally: `./gradlew :androidApp:assembleDebug`.
   - Ensure all tests pass: `./gradlew testDebugUnitTest`.
   - Open a PR against the `main` branch.
   - Fill out the PR template with what changed, why, and screenshots/screen recordings if there are UI changes.
3. **Automated CI**:
   - Every PR triggers GitHub Actions CI which compiles the project, runs unit tests, and validates lint checks automatically.

---

## Questions or Need Help?

Feel free to open a discussion or issue on GitHub. Happy coding!
