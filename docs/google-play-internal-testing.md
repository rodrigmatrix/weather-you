# Google Play internal testing from GitHub Actions

The workflow at `.github/workflows/google-play-internal.yml` is manually triggered and uploads only to the Google Play **internal** test track. It is limited to `main` and `dev`, requires an explicit `confirm_upload` checkbox, and asks for a version code. If confirmation is unchecked, it reports that no upload was made and skips the release job. It does not publish to production.

## One-time setup

1. In GitHub repository settings, create the environment `google-play-internal`.
2. Restrict that environment to the `main` and `dev` branches. Add required reviewers if your GitHub plan supports environment approvals.
3. Add these values as **environment secrets** for `google-play-internal`:

   - `WEATHER_YOU_GOOGLE_SERVICES_JSON_BASE64`: Base64-encoded Google Services configuration JSON for the Google Play Firebase app. The workflow materializes it only for the build and removes it afterwards.
   - `WEATHER_YOU_UPLOAD_KEYSTORE_BASE64`: Base64-encoded **upload** keystore registered in Play Console. Do not use the Google-held Play app-signing key.
   - `WEATHER_YOU_UPLOAD_KEY_ALIAS`: Alias for the upload key.
   - `WEATHER_YOU_UPLOAD_KEY_PASSWORD`: Password for the key.
   - `WEATHER_YOU_UPLOAD_STORE_PASSWORD`: Keystore password.

   Enter these directly in GitHub's environment secret editor. Never commit them, add them to Remote Config, paste them into issues/chat, or store them in workflow files. The workflow creates temporary Firebase config and upload-key files on the hosted runner and removes them after the job. Google authentication uses short-lived OIDC federation, not a stored service-account JSON key.

4. Add these **environment variables** to `google-play-internal`. They are already configured for WeatherYou:

   - `GCP_WORKLOAD_IDENTITY_PROVIDER`: `projects/139154958049/locations/global/workloadIdentityPools/github-actions-for-weatheryou/providers/weatheryou-github-actions`
   - `GCP_WEATHER_YOU_PLAY_SERVICE_ACCOUNT`: `weather-you-play@weatheryou-cfaf5.iam.gserviceaccount.com`

   These are resource identifiers, not credentials. The workflow uses GitHub OIDC federation instead of storing a long-lived Google service-account JSON key.

5. The Google Cloud side is set up in project `weatheryou-cfaf5` (WeatherYou): the Google Play Android Developer API is enabled, and the dedicated `weather-you-play` service account has no user-managed key. Its Workload Identity Pool is `github-actions-for-weatheryou`; provider `weatheryou-github-actions` trusts `https://token.actions.githubusercontent.com` and maps the GitHub subject, repository, ref, and environment claims. The provider condition only accepts `rodrigmatrix/weather-you`, the `main` or `dev` branches, and the `google-play-internal` environment. The repository principal set can impersonate only this service account with `roles/iam.workloadIdentityUser`.

6. The service account is active in Play Console with permissions scoped to the Weather You app: read-only app information plus **Release apps to testing tracks**. It has no production release, financial, store-presence, or tester-list permissions.
7. Play App Signing is configured with an upload key. CI must use the registered **upload key**; Google Play uses its app-signing key for delivered APKs. The upload keystore was not present in the local workspace when this guide was written, so its GitHub secrets still need to be supplied.

## Upload a test build

1. Merge the workflow to `main` or `dev`.
2. Open **Actions → Upload WeatherYou to Google Play internal testing → Run workflow**.
3. Choose `main` or `dev`, enter a positive version code greater than both `520066` and the highest version code already uploaded to Play, then check `confirm_upload`.
4. After the upload completes, add testers to the Play internal testing list and install WeatherYou from the Play opt-in link. Use the Play-installed build to verify product prices, checkout, purchase restoration, ad removal, the 10-location limit, and phone-to-watch entitlement sync.

The workflow uses GitHub-hosted Ubuntu and Java 21, matching the repository's Android delivery toolchain. A local debug install is not a substitute for validating Google Play Billing; it does not prove that Play product details or checkout work.
