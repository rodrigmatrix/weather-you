# WeatherYou

<a href='https://play.google.com/store/apps/details?id=com.rodrigmatrix.weatheryou&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="220" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

WeatherYou is an open source weather app build entirely using Jetpack Compose. The design language is built on Material You and on Android 12+ devices, the app colors matches the wallpapers. You can also pick from defined colors in settings. The app was build to work on Phones, Tablets and Foldables(with adapted interface), Android TV and WearOS.

## Phone
<p float="center">
    <img width="200" src="https://github.com/user-attachments/assets/cfc40019-a456-4cf3-b7f7-799330016b96">
    <img width="200" src="https://github.com/user-attachments/assets/4c5758b8-6247-4eae-9352-0df9d0bd8c87">
    <img width="200" src="https://github.com/user-attachments/assets/026fcc3c-7bfa-4e71-8ffa-c7a8bbec0423"> 
    <img width="200" src="https://github.com/user-attachments/assets/e13c9fc7-d2ca-44b0-a38f-3a2670e67a23">
    <img width="200" src="https://github.com/user-attachments/assets/c333f688-9b48-4f06-97cf-74e5a150212b">
    <img width="200" src="https://github.com/user-attachments/assets/cf5798ca-2aa2-4ab0-a142-80f4bf242c0d">
    <img width="200" src="https://github.com/user-attachments/assets/def56a55-c4a6-4e0e-812d-7cde90773d7b">
    <img width="200" src="https://github.com/user-attachments/assets/9832ba68-7447-46f5-9f31-dc07a11a8042">
</p>

## Tablet and Foldables
<p float="center">
    <img width="500" src="https://github.com/user-attachments/assets/243356e0-323d-4bce-a5b0-eb9bc8d31de1">
    <img width="500" src="https://github.com/user-attachments/assets/6a67c46c-f37b-4c36-8876-7a831acdae69">
    <img width="500" src="https://github.com/user-attachments/assets/4d8c90b2-be3d-4ad5-8483-09de5c199ce3">
    <img width="500" src="https://github.com/user-attachments/assets/b5c5d730-009f-4e7b-8fbe-c6578865d433">
    <img width="500" src="https://github.com/user-attachments/assets/5a6fc3ca-6f96-413c-9572-ae7ca9e916c7">
    <img width="500" src="https://github.com/user-attachments/assets/c46c268b-1a4e-4971-98ee-c13387f9edf6">
    <img width="500" src="https://github.com/user-attachments/assets/774db401-cdf6-4e24-893e-9e7325932b7a">
</p>

## Android TV
<p float="center">
    <img width="500" src="https://github.com/user-attachments/assets/98026d74-abce-43c4-abc5-a6637e22d6a9">
    <img width="500" src="https://github.com/user-attachments/assets/0a9f7557-d1c9-45a4-a27e-6e6630140810">
    <img width="500" src="https://github.com/user-attachments/assets/d5adc18f-6e16-4a97-a739-5b6fdb8843e1">
    <img width="500" src="https://github.com/user-attachments/assets/3459a4e4-ac00-41df-ba8b-43f3b778ed94">
    <img width="500" src="https://github.com/user-attachments/assets/732b676b-595a-4165-9d40-46daa233885c">
    <img width="500" src="https://github.com/user-attachments/assets/543bf161-4f3e-4836-ba93-201326f4d7d1">
    <img width="500" src="https://github.com/user-attachments/assets/f62ddcce-201a-433e-b55d-931bc0da23bf">
</p>


## Widgets
<p float="center">
    <img width="200" src="https://github.com/rodrigmatrix/weather-you/assets/7853887/84c0a70b-329e-40a8-8509-90432fc4aa58">
    <img width="200" src="https://github.com/rodrigmatrix/weather-you/assets/7853887/9875f1b4-152e-4b67-a78c-f253b9639e04">
    <img width="231" src="https://github.com/user-attachments/assets/e68a869c-5e51-4671-90d4-2c2ca5beef2e">
    <img width="200" src="https://github.com/rodrigmatrix/weather-you/assets/7853887/06b99b62-b335-4845-9de0-b24229fc99df">

</p>

## WearOS
<p float="center">
    <img width="231" src="https://user-images.githubusercontent.com/7853887/162537675-29ec372f-585c-4c43-9c32-ff6b8dcaf09e.png">
    <img width="231" src="https://user-images.githubusercontent.com/7853887/162537677-62f59a77-0b8e-407b-946c-77d96889d63c.png">
    <img width="231" src="https://user-images.githubusercontent.com/7853887/162537679-ee3d9405-03d0-4a08-a1cb-4215e2cbc411.png">
</p>

### Modules Structure
<img width="231" src="https://user-images.githubusercontent.com/7853887/162535941-e6ac1841-024a-4c18-b905-44a63f3ef1b7.png">

The architecture was based upon Clean Architecture and the presentation layer is using MVI with Jetpack Compose. The app uses a shared domain across features modules and different device types to provide data.

