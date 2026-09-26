# PalaeoKids Mobile

Official mobile-app source for the PalaeoKids educational platform at https://www.indianpalaeodb.in/

## Android app
- App name: PalaeoKids
- Package ID: `in.indianpalaeodb.palaeokids`
- Minimum SDK: 24
- Target SDK: 36
- Permissions in v1: Internet + network state only
- Native home screen, offline mini-glossary, controlled web learning area, Home/Back/Refresh/Share controls

## Automatic APK build
Open **Actions → Build PalaeoKids APK → Run workflow**.

After the workflow finishes:
1. Open the completed workflow run.
2. Download the **PalaeoKids-APK** artifact.
3. Unzip it.
4. Install `app-debug.apk` on an Android device for testing.

This debug APK is for direct testing. Google Play distribution should use a signed release AAB/APK with a protected release keystore.

## Website / PWA
The `pwa/` folder contains a web app manifest and service worker starter for installable PalaeoKids web access.

## Privacy
Version 1 deliberately avoids camera, microphone, contacts, SMS, call log, location and broad storage permissions. Review `STORE_CHECKLIST.md` before store submission.
