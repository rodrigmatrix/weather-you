param([Parameter(Mandatory)][string[]]$GradleArguments)
$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot 'android-environment.ps1')
Push-Location -LiteralPath $AndroidDelivery.GradleRoot
try {
    & .\gradlew.bat @GradleArguments
    $deliveryGradleExit = $LASTEXITCODE
    if ($deliveryGradleExit -ne 0) { throw "Gradle failed with exit code $deliveryGradleExit in $($AndroidDelivery.GradleRoot)." }
} finally {
    Pop-Location
}
