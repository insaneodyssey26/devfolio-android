# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
