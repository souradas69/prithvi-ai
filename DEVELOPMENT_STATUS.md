# PRITHVI AI — Development Status

## Product decisions applied

- Website + the existing native Android app.
- Preserve the Kotlin / Jetpack Compose app rather than replace it.
- English-only user experience.
- Software-only product scope. No ESP32, XiaoZhi, sensor gateway, pump control, or physical irrigation automation.

## Implemented in this development pass

### Android
- Replaced the former simulated hardware/pump screen with a software-only Irrigation Planner and manual watering activity action.
- Removed the visible language picker from onboarding, sign-in and settings; the chat language choices are English-only.
- Removed the synthetic mandi price generator; the Market screen now shows a clear not-configured state instead of fabricated prices.
- Changed weather fallback data to an unavailable state; dashboard and weather widgets no longer present synthetic fallback values as live weather.
- Removed seeded sample community conversations/posts from startup.
- Removed fabricated crop-scan diagnosis and default confidence score when AI is unavailable or does not provide a confidence value.
- Moved the Android Gemini client to authenticated Firebase callable Functions; no Gemini key is read from BuildConfig or packaged from `.env`.

### Website
- Added a responsive React + TypeScript web application with desktop and mobile layouts.
- Added Firebase email/password registration and sign-in.
- Added authenticated Firestore farm creation and farm listing.
- Added PRITHVI AI chat and Universal Crop Scanner clients that call Firebase Functions.
- Added Firestore persistence for chat transcripts and crop-scan analysis records.
- Added transparent not-configured states for weather, mandi prices, schemes, community and finance modules rather than sample live data.

### Backend and deployment scaffold
- Added authenticated Firebase callable Functions `askPrithvi` and `analyzeCrop`.
- Gemini API key is read as a Firebase Functions secret, not from browser or Android client configuration.
- Added Firebase Hosting, Firestore rules and a composite index for farm records.

## Not yet complete / needs project owner configuration

- No Firebase project credentials were present in the uploaded ZIP. Add Android `google-services.json` and Web Firebase config before live sign-in or cloud operations can work.
- Deploy the Firebase Functions and set `GEMINI_API_KEY` as a Functions secret.
- Android and web currently share the Firebase account project once configured, but full Android Room ↔ Firestore farm, crop-scan, finance, calendar and community synchronization is not implemented yet.
- Android Room data remains local for its existing modules; web farm records are in Firestore.
- Weather provider calls are present in Android. The website weather provider integration is not yet implemented.
- Official mandi-price and government-scheme data providers are not configured; those sections intentionally show no fabricated records.
- Web community, farm finance, calendar, saved items and full admin moderation are not yet implemented as live cloud modules.
- Add Firebase App Check, abuse/rate controls, admin claims, privacy policy, terms, account deletion and production support workflows before public launch.

## Validation performed

- Parsed web and Functions TypeScript/TSX source with the installed TypeScript transpiler (syntax checks passed).
- Parsed JSON configuration files successfully.
- Ran Kotlin compiler parser checks on modified Kotlin sources; no Kotlin syntax errors were reported. A full Android Gradle build could not be run because this environment does not contain Gradle/Gradle Wrapper dependencies.
- `npm install` did not complete in this environment, so a full web type-check/build has not been verified.

This is an upgraded development baseline, not yet a deployed production release or a fully launch-certified investment product.
