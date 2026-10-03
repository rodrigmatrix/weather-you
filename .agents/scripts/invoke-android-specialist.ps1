param(
    [Parameter(Mandatory)][ValidateSet('android-engineer','android-architect','android-qa','backend-engineer','release-deploy','weather-you-multidevice')][string]$Agent,
    [Parameter(Mandatory)][ValidatePattern('^[A-Za-z0-9_-]+$')][string]$TaskId,
    [Parameter(Mandatory)][string]$PromptFile,
    [ValidateSet('plan','accept-edits')][string]$Mode = 'plan',
    [ValidateRange(1,15)][int]$TimeoutMinutes = 5,
    [string]$ConversationId
)
$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot 'android-environment.ps1')
if (-not (Test-Path -LiteralPath $AndroidDelivery.Antigravity)) { throw 'Antigravity CLI executable is missing at the configured path.' }
$deliveryFlatProfile = Join-Path $AndroidDelivery.Repository ".agents\agents\$Agent.md"
$deliveryNestedProfile = Join-Path $AndroidDelivery.Repository ".agents\agents\$Agent\agent.md"
if (-not ((Test-Path -LiteralPath $deliveryFlatProfile) -or (Test-Path -LiteralPath $deliveryNestedProfile))) { throw "Specialist profile is missing: $Agent" }
$deliveryPrompt = [IO.File]::ReadAllText((Resolve-Path -LiteralPath $PromptFile).Path)
$deliveryPrompt += "`nFollow AGENTS.md and .agents/workflows/android-delivery.md. Never read, print, or send local.properties, keystore.properties, .env, credential stores, signing keys or service-account files. Do not access production accounts or deploy. Return findings and concrete evidence; do not claim checks that were not run."
$deliveryTaskDirectory = Join-Path $AndroidDelivery.Artifacts $TaskId
New-Item -ItemType Directory -Path $deliveryTaskDirectory -Force | Out-Null
$deliveryStamp = Get-Date -Format 'yyyyMMdd-HHmmss-fff'
$deliveryResultPath = Join-Path $deliveryTaskDirectory "$Agent-$deliveryStamp.json"
$deliveryErrorPath = Join-Path $deliveryTaskDirectory "$Agent-$deliveryStamp.stderr.log"
$deliveryStreamPath = Join-Path $deliveryTaskDirectory "$Agent-$deliveryStamp.jsonl"
$deliveryInfo = [Diagnostics.ProcessStartInfo]::new()
$deliveryInfo.FileName = $AndroidDelivery.Antigravity
$deliveryInfo.WorkingDirectory = $AndroidDelivery.Repository
$deliveryInfo.UseShellExecute = $false
$deliveryInfo.CreateNoWindow = $true
$deliveryInfo.RedirectStandardInput = $true
$deliveryInfo.RedirectStandardOutput = $true
$deliveryInfo.RedirectStandardError = $true
$deliveryInfo.StandardInputEncoding = [Text.UTF8Encoding]::new($false)
$deliveryInfo.StandardOutputEncoding = [Text.UTF8Encoding]::new($false)
$deliveryInfo.StandardErrorEncoding = [Text.UTF8Encoding]::new($false)
foreach ($deliveryArgument in @('--agent',$Agent,'--model','gemini-3.8-flash-medium','--mode',$Mode,'--input-format','stream-json','--output-format','stream-json','--print-timeout',"${TimeoutMinutes}m")) {
    $deliveryInfo.ArgumentList.Add($deliveryArgument)
}
if ($ConversationId) { $deliveryInfo.ArgumentList.Add('--conversation'); $deliveryInfo.ArgumentList.Add($ConversationId) }
$deliveryProcess = [Diagnostics.Process]::new()
$deliveryProcess.StartInfo = $deliveryInfo
try {
    if (-not $deliveryProcess.Start()) { throw 'Antigravity process did not start.' }
    $deliveryPid = $deliveryProcess.Id
    $deliveryOutTask = $deliveryProcess.StandardOutput.ReadToEndAsync()
    $deliveryErrTask = $deliveryProcess.StandardError.ReadToEndAsync()
    $deliveryInput = @{event='user';message=@{content=$deliveryPrompt}} | ConvertTo-Json -Compress -Depth 4
    $deliveryProcess.StandardInput.WriteLine($deliveryInput)
    $deliveryProcess.StandardInput.Close()
    $deliveryTimedOut = -not $deliveryProcess.WaitForExit(($TimeoutMinutes * 60000) + 30000)
    if ($deliveryTimedOut) {
        # Terminate this owned CLI process only; never kill unrelated/shared emulator or Gradle processes.
        $deliveryProcess.Kill()
        $null = $deliveryProcess.WaitForExit(5000)
    }
    $deliveryExitCode = if ($deliveryProcess.HasExited) { $deliveryProcess.ExitCode } else { $null }
    $deliveryRaw = if ($deliveryOutTask.Wait(5000)) { $deliveryOutTask.Result } else { '' }
    $deliveryErrors = if ($deliveryErrTask.Wait(5000)) { $deliveryErrTask.Result } else { 'stderr stream did not close after process termination.' }
    [IO.File]::WriteAllText($deliveryStreamPath, $deliveryRaw, [Text.UTF8Encoding]::new($false))
    [IO.File]::WriteAllText($deliveryErrorPath, $deliveryErrors, [Text.UTF8Encoding]::new($false))
    $deliveryResult = $null
    $deliveryInitId = $null
    foreach ($deliveryLine in ($deliveryRaw -split '\r?\n')) {
        if ([string]::IsNullOrWhiteSpace($deliveryLine)) { continue }
        try {
            $deliveryEvent = $deliveryLine | ConvertFrom-Json
            if ($deliveryEvent.event -eq 'init') { $deliveryInitId = $deliveryEvent.conversation_id }
            if ($deliveryEvent.event -eq 'result') { $deliveryResult = $deliveryEvent.result }
        } catch { continue }
    }
    $deliveryStatus = if ($deliveryTimedOut) { 'TIMEOUT' } elseif ($deliveryExitCode -ne 0) { 'ERROR' } elseif ($deliveryResult) { $deliveryResult.status } else { 'INVALID' }
    if ($deliveryStatus -eq 'SUCCESS' -and [string]::IsNullOrWhiteSpace($deliveryResult.response)) { $deliveryStatus = 'INVALID' }
    $deliverySummary = [pscustomobject]@{
        Status = $deliveryStatus
        NeedsAttention = ($deliveryStatus -ne 'SUCCESS')
        ExitCode = $deliveryExitCode
        ProcessId = $deliveryPid
        Agent = $Agent
        Model = 'gemini-3.8-flash-medium'
        ConversationId = $(if ($deliveryResult.conversation_id) { $deliveryResult.conversation_id } else { $deliveryInitId })
        ResultFile = $deliveryResultPath
        EventFile = $deliveryStreamPath
        StderrFile = $deliveryErrorPath
        Response = $deliveryResult.response
    }
    [IO.File]::WriteAllText($deliveryResultPath, ($deliverySummary | ConvertTo-Json -Depth 6), [Text.UTF8Encoding]::new($false))
    $deliverySummary
} finally {
    $deliveryProcess.Dispose()
}
