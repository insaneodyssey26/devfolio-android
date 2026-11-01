# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.1.1-alpha] - 2026-11-02

### Added
- **Session Cookie Inspector**:
  - Restored full Devfolio session cookies viewer (`rawCookies`) alongside the session access token on the Portfolio screen with show/hide masking and one-tap clipboard copy.
- **Direct Portfolio Account Integration**:
  - Automatically identifies active Devfolio web session on the unlinked hero screen and offers a 1-tap "Sync @username's Portfolio" button.
- **Hardware-Backed Encrypted Storage**:
  - Integrated `androidx.security:security-crypto:1.1.0` with Android Keystore AES256-GCM / AES256-SIV `EncryptedSharedPreferences` for user session tokens and cookies with automatic legacy migration.
- **Persistent Bookmarks Driver**:
  - Implemented multiplatform `BookmarkStorageDriver` with Android Preferences storage driver to keep bookmarked hackathons persistent across app restarts and updates.
- **Automated Backup & Data Extraction Security**:
  - Added strict XML backup rules and data extraction exclusion rules ensuring session keys and sensitive tokens are never backed up to cloud or ADB backups.

### Changed
- **Zero-Flicker Action Bar**:
  - Eliminated the loading circle spinner during passive background status checks in hackathon detail screens. Buttons render their cached or default state immediately from frame 1.
- **Relocated Account Management**:
  - Moved web session, username identity, token inspector, and cookie inspector entirely to the Portfolio screen, keeping the Settings screen exclusively dedicated to Themes, AMOLED mode, Fonts, and Haptics.
- **Cleaned Sign-Out Lifecycle**:
  - Complete session wipe on sign-out including Android `CookieManager` cookie flush, removal from encrypted preferences, and memory eviction.

### Fixed
- **Edge-to-Edge Double Inset Padding**:
  - Removed duplicate navigation bar and status bar padding on the main screen container.
- **ProGuard / R8 Build Rules**:
  - Added `-dontwarn com.google.errorprone.annotations.**` rule resolving release R8 compilation errors.

---

## [0.1.0-alpha] - 2026-11-02

### Added
- **Hackathon Discovery**:
  - Live discovery feed with pull-to-refresh and tab filters (Open, Upcoming, Ended).
  - Search hackathons by title, city, or tags with responsive debouncing.
  - Form factor and venue badge chips (In-Person / Online) with high-contrast legibility.
- **Event Details & Native Markdown**:
  - Schedule timeline, problem statements, FAQs, and eligibility criteria.
  - Native markdown parsing with styled bold, bullet lists, headers, and code snippets.
  - Human-friendly relative date formatting (e.g. *Nov 20, 2026 · 2:30 PM*).
- **Calendar Synchronization**:
  - One-tap sync to schedule registration closes and hackathon start dates into device calendar.
- **Offline Bookmarks**:
  - Save hackathons locally to view details and schedules anytime without internet access.
- **Projects Showcase**:
  - Gallery of winning and featured hackathon submissions.
  - Native project detail view with GitHub repository links, demo video embeds, and creator tags.
- **Devfolio Authentication & Application Management**:
  - Native in-app sign-in flow for Devfolio accounts.
  - Real-time application status tracking (Submitted, Team info, RSVP status).
  - In-memory status caching to eliminate UI flicker when navigating between screens.
  - Confirmation safeguard dialog before withdrawing applications to prevent accidental de-registration.
- **Profile & Portfolio Showcase**:
  - Sync public builder portfolios by username with skills, past wins, and hackathons attended.
- **Settings & Theming**:
  - Theme mode options (System Default, Pure Dark, Crisp Light).
  - Pure Black (AMOLED true black) toggle for ultra-deep OLED battery saving.
  - Dynamic Color accents and Material 3 Expressive button group toggles.
  - Typography switcher between Google Sans Flex and System Font with universal typography scaling.
  - Subtle tactile micro-feedback toggle powered by `DevfolioHaptics`.
  - Account session access token inspector with show/hide toggle and one-tap clipboard copy.
- **Official Brand Identity**:
  - Integrated official Devfolio brand assets, adaptive 3D vector launcher icons, and square mipmap icons.
  - Native About screen with authentic platform attribution and direct links to Devfolio and GitHub repo.
- **Testing & CI**:
  - Comprehensive unit test suite covering DTOs, ViewModels, Repositories, Dates, and Auth cookies.
  - Automated GitHub Actions CI workflow for test validation, Android Lint, and compilation.
  - Automated GitHub Release workflow for tag pushes (`v*`).

### Notice
- This is an **Early Alpha Preview**. Users should expect occasional UI glitches, incomplete organizer queries, and ongoing API iterations. Contributions and feedback are warmly welcomed.
