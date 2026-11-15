# PRITHVI AI — Product Workspace

PRITHVI AI is an AI-powered digital agriculture platform. The current product scope is **software only**: a native Android app plus a responsive web application sharing Firebase services and the PRITHVI AI backend. IoT, ESP32, XiaoZhi firmware, sensors, pump controls and physical automation are intentionally excluded.

## Project structure

- `app/` — existing native Android application (Kotlin + Jetpack Compose), preserved and upgraded.
- `web/` — responsive React + TypeScript website.
- `functions/` — authenticated Firebase callable functions for Gemini chat and crop-image analysis. Gemini credentials remain server-side.
- `firestore.rules` — Firestore access rules.
- `firebase.json` — Firebase Functions, Firestore and Hosting configuration.

## Web application

Prerequisites: Node.js 20+ and npm.

1. `cd web`
2. Copy `web/.env.example` to `web/.env`.
3. Fill in the four required Firebase Web App values: `VITE_FIREBASE_API_KEY`, `VITE_FIREBASE_AUTH_DOMAIN`, `VITE_FIREBASE_PROJECT_ID`, and `VITE_FIREBASE_APP_ID`. `VITE_FIREBASE_STORAGE_BUCKET` and `VITE_FIREBASE_MESSAGING_SENDER_ID` are optional for the current website.
4. `npm install`
5. `npm run dev`
6. `npm run build` for a production build.

Enable Email/Password under Firebase Authentication and create a Cloud Firestore database. The web app deliberately shows a setup screen until Firebase configuration is supplied.

## Firebase backend

Prerequisites: Firebase CLI, access to the Firebase project, and a billing plan that supports Cloud Functions deployment. Do not enable billing without approval.

1. Copy `.firebaserc.example` to `.firebaserc` and set your Firebase project ID.
2. Set the Gemini key as a server-side secret: `firebase functions:secrets:set GEMINI_API_KEY`.
3. Install and build Functions: `cd functions && npm install && npm run build`.
4. Build the web app: `cd web && npm run build`, then return to the repository root.
5. Deploy: `firebase deploy --only functions,firestore:rules,hosting`.

Callable functions require a signed-in Firebase user. `askPrithvi` handles text chat and `analyzeCrop` handles image analysis. They enforce input limits and keep the Gemini key out of client code.

## Android application

Open the repository root in Android Studio. Add the Firebase Android app to the same Firebase project, download `google-services.json` into `app/`, and enable the Google Services Gradle plugin in `app/build.gradle.kts` and `build.gradle.kts` if required by your local setup. The Android Gemini client now calls the authenticated `askPrithvi` / `analyzeCrop` Cloud Functions; it no longer embeds a Gemini API key.

The Android app uses local Room records for several existing modules. Cloud synchronization of all Android records with the web app is not yet complete; the website currently synchronizes its farm records through Firestore.

## Data integrity and current limitations

- Weather uses Open-Meteo when the request succeeds. When unavailable, the app displays unavailable status rather than fabricated current readings.
- Mandi prices are intentionally not shown until an official, verified source integration is configured.
- Soil moisture, water levels, pump state and sensor telemetry are not available in this software-only release.
- Irrigation is an advisory planner and manual activity log only. It cannot operate physical equipment.
- Government scheme content must be checked against official portals and kept with a review date before being treated as current.
- The web application has working Firebase email/password authentication, farm record creation/listing, and callable AI/scanner clients once backend configuration is deployed. Other navigation areas are clearly identified as awaiting their verified data or cloud modules.

## Security and launch checklist

- Configure Firebase Authentication, Firestore Rules and authorized domains.
- Deploy callable Functions with `GEMINI_API_KEY` stored as a secret.
- Add App Check and rate limits before public launch.
- Test account isolation, image upload limits, abuse handling and data deletion.
- Add verified market and government data providers before displaying those records.
- Complete Android-to-web Firestore synchronization and end-to-end QA.
- Configure production signing, privacy policy, terms, support contact and backup/restore.

This repository is a development baseline, not a claim that production deployment, external credentials, provider approvals or investor traction already exist.


## Website troubleshooting

- Run the commands from the `web` folder, not the repository root.
- After creating or editing `web/.env`, stop and restart Vite. Vite reads environment variables at startup.
- The website needs the four required Firebase Web App values before sign-in can work. The setup page lists any missing values.
- Enable Email/Password in Firebase Authentication and add `localhost` under Authentication > Settings > Authorized domains.
- If the page is blank, open the browser developer console and check the first red error. A failed Firebase sign-in is different from the Vite server failing to start.
- `storageBucket` is optional for the website's current features; leaving it blank must not block startup.


## Clean web reinstall (Windows PowerShell)

Run these commands from the repository root after configuring `web/.env`:

```powershell
cd web
if (Test-Path node_modules) { Remove-Item node_modules -Recurse -Force }
if (Test-Path package-lock.json) { Remove-Item package-lock.json -Force }
npm install
npm run build
npm run dev
```

Open the exact local URL printed by Vite (normally `http://localhost:5173`). Stop Vite with Ctrl+C before changing `.env`, then restart it. Do not put the Gemini API key in `web/.env`; it belongs only in Firebase Functions secrets.

If the page opens but login fails, confirm Email/Password is enabled in Firebase Authentication and `localhost` is listed in Authorized domains. If farm records fail, deploy `firestore.rules` and verify the signed-in user's records use `ownerUid` equal to their Firebase UID.
