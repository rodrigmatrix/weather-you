# Dot-source this script in each command session: . .agents/scripts/android-environment.ps1
$ErrorActionPreference = 'Stop'
$deliveryRepository = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
$deliveryGradleRoot = $deliveryRepository
if (Test-Path -LiteralPath (Join-Path $deliveryRepository 'weather-you\gradlew.bat')) {
    $deliveryGradleRoot = Join-Path $deliveryRepository 'weather-you'
}
if (-not (Test-Path -LiteralPath (Join-Path $deliveryGradleRoot 'gradlew.bat'))) {
    throw 'Could not find the repository Gradle wrapper.'
}
$deliveryJavaHome = 'H:\Development\Java\temurin-21\jdk-21.0.12.1+1'
$deliverySdkHome = 'H:\Development\Android\Sdk'
$deliveryAvdHome = 'H:\Development\Android\avd'
foreach ($deliveryRequired in @(
    (Join-Path $deliveryJavaHome 'bin\java.exe'),
    (Join-Path $deliverySdkHome 'platform-tools\adb.exe'),
    (Join-Path $deliverySdkHome 'emulator\emulator.exe')
)) {
    if (-not (Test-Path -LiteralPath $deliveryRequired)) { throw "Missing workflow tool: $deliveryRequired" }
}
$env:JAVA_HOME = $deliveryJavaHome
$env:ANDROID_HOME = $deliverySdkHome
$env:ANDROID_SDK_ROOT = $deliverySdkHome
$env:ANDROID_AVD_HOME = $deliveryAvdHome
$env:GRADLE_USER_HOME = 'H:\Gradle'
$env:PATH = "$deliveryJavaHome\bin;$deliverySdkHome\platform-tools;$deliverySdkHome\emulator;$env:PATH"
$AndroidDelivery = [pscustomobject]@{
    Repository = $deliveryRepository
    GradleRoot = $deliveryGradleRoot
    JavaHome = $deliveryJavaHome
    SdkHome = $deliverySdkHome
    AvdHome = $deliveryAvdHome
    Adb = Join-Path $deliverySdkHome 'platform-tools\adb.exe'
    Emulator = Join-Path $deliverySdkHome 'emulator\emulator.exe'
    Antigravity = 'C:\Users\rodri\AppData\Local\agy\bin\agy.exe'
    Artifacts = Join-Path 'H:\Development\Android\artifacts' (Split-Path $deliveryRepository -Leaf)
}
