# WeatherYou

<a href='https://play.google.com/store/apps/details?id=com.rodrigmatrix.weatheryou&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="220" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

WeatherYou is an open source weather app build entirely using Jetpack Compose. The design language is built on Material You and on Android 12+ devices, the app colors matches the wallpapers. You can also pick from defined colors in settings. The app was build to work on Phones, Tablets and Foldables(with adapted interface), Android TV and WearOS.

## Phone Screenshots
<p float="center">
      <img width="200" src="https://github.com/user-attachments/assets/36c2f196-8a3f-4adb-a4a8-0a9b0f064fcb">
    <img width="200" src="https://github.com/user-attachments/assets/6cb0a593-8eac-4fa7-a257-02bad15877df">
    <img width="200" src="https://github.com/user-attachments/assets/15292782-3be9-420d-980b-75872cb593d5">
    <img width="200" src="https://github.com/user-attachments/assets/bbc235c3-f4fd-45f2-9960-c1db0e1f8981">
    <img width="200" src="https://github.com/user-attachments/assets/d389a1de-f03b-44a8-b7eb-f2c157567a79">
    <img width="200" src="https://github.com/user-attachments/assets/982c2ef5-f33c-4ad5-baee-14f36b4c5f12">
    <img width="200" src="https://github.com/user-attachments/assets/65b31e36-3f2f-4dd3-aa69-0f2bba982888">
    <img width="200" src="https://github.com/user-attachments/assets/18f3977f-3d5e-47bd-ac4e-ecdb774ce745">
</p>

## Tablet and Foldables Screenshots
<p float="center">
    <img width="500" src="https://github.com/user-attachments/assets/cd1561ac-7488-4311-b759-c4e76dd5c930">
    <img width="500" src="https://github.com/user-attachments/assets/eda8cb27-6676-49e8-bd2e-f2a2e6e68c51">
    <img width="500" src="https://github.com/user-attachments/assets/6bfd78f1-c086-491d-939c-c2984ee1c54f">
    <img width="500" src="https://github.com/user-attachments/assets/d6c86715-6585-43e3-bb5c-4bb635d9a3a7">
    <img width="500" src="https://github.com/user-attachments/assets/7fb58bfb-1c4a-4d7b-ac33-1f585c470e2f">
    <img width="500" src="https://github.com/user-attachments/assets/e10bb530-18eb-4539-8f05-408f42c9bd16">
    <img width="500" src="https://github.com/user-attachments/assets/b76947d7-01b8-4795-ba43-80027afeda16">
</p>

## Android TV Screenshots
<p float="center">
    <img width="500" src="https://github.com/user-attachments/assets/98026d74-abce-43c4-abc5-a6637e22d6a9">
    <img width="500" src="https://github.com/user-attachments/assets/0a9f7557-d1c9-45a4-a27e-6e6630140810">
    <img width="500" src="https://github.com/user-attachments/assets/d5adc18f-6e16-4a97-a739-5b6fdb8843e1">
    <img width="500" src="https://github.com/user-attachments/assets/3459a4e4-ac00-41df-ba8b-43f3b778ed94">
    <img width="500" src="https://github.com/user-attachments/assets/732b676b-595a-4165-9d40-46daa233885c">
    <img width="500" src="https://github.com/user-attachments/assets/543bf161-4f3e-4836-ba93-201326f4d7d1">
    <img width="500" src="https://github.com/user-attachments/assets/f62ddcce-201a-433e-b55d-931bc0da23bf">
</p>


## Widgets Screenshots
<p float="center">
    <img width="200" src="https://github.com/rodrigmatrix/weather-you/assets/7853887/84c0a70b-329e-40a8-8509-90432fc4aa58">
    <img width="200" src="https://github.com/rodrigmatrix/weather-you/assets/7853887/9875f1b4-152e-4b67-a78c-f253b9639e04">
    <img width="200" src="https://github.com/rodrigmatrix/weather-you/assets/7853887/06b99b62-b335-4845-9de0-b24229fc99df">

</p>

## WearOS Screenshots
<p float="center">
    <img width="231" src="https://user-images.githubusercontent.com/7853887/162537675-29ec372f-585c-4c43-9c32-ff6b8dcaf09e.png">
    <img width="231" src="https://user-images.githubusercontent.com/7853887/162537677-62f59a77-0b8e-407b-946c-77d96889d63c.png">
    <img width="231" src="https://user-images.githubusercontent.com/7853887/162537679-ee3d9405-03d0-4a08-a1cb-4215e2cbc411.png">
</p>

### Modules Structure
<img width="231" src="https://user-images.githubusercontent.com/7853887/162535941-e6ac1841-024a-4c18-b905-44a63f3ef1b7.png">

The architecture was based upon Clean Architecture and the presentation layer is using MVI with Jetpack Compose. The app uses a shared domain across features modules and different device types to provide data.

