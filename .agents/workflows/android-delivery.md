# Android App Delivery Workflow — repository execution

This runbook applies to implementation requests in this repository. Codex orchestrates the work and owns acceptance; Antigravity CLI supplies the requested specialists. This is a procedure to execute during a task, not a background scheduler. The shared pickup board is in the Workflow chat; record task checkpoints in `.agents/task-status/<task-id>.md` so another chat can resume from evidence.

## Start and preflight

1. Read repository `AGENTS.md`, inspect the current Git status, and preserve other active work. Give the request a task ID. Record scope, affected modules/form factors, current stage, and next action in its checkpoint. A `dubious ownership` message can arise because Codex runs under a separate sandbox Windows account. For these explicitly authorized repositories, use a command-scoped `git -c safe.directory=<absolute-repository-path> -C <absolute-repository-path> status --short` or the applicable execution escalation; do not add wildcard/global trust. Check that Git actually succeeded.
2. Dot-source `.agents/scripts/android-environment.ps1` in every new PowerShell command session that runs Java, Gradle, ADB, emulators, or Antigravity. Environment changes in a previous command are not inherited by later commands. The script sets the process environment and exposes `$AndroidDelivery` with absolute tool paths and the actual Gradle root. For WeatherYou the Gradle root is the nested `weather-you` directory. For Material Player it is the repository root.
3. Run Java and wrapper version checks from that Gradle root. Use JDK 21 on H:, not Android Studio's bundled Java 25 with the existing Gradle 8.x wrappers. Use `gradlew.bat` on Windows. Prefer `.agents/scripts/invoke-android-gradle.ps1 -GradleArguments @('--version')` (or the required build tasks); it sets the process environment, runs from the real Gradle root, and fails on a nonzero native exit. For Java/ADB/emulator commands, inspect `$LASTEXITCODE` immediately after invocation. Inspect error output too; do not mask a failed command with a succeeding command afterward.
4. List AVDs using the H: emulator path with `ANDROID_AVD_HOME` set. The five profiles are Phone, Tablet, Foldable, AndroidTV, and WearOS. Missing PATH entries or an empty C: AVD list do not prove a tool/profile is unavailable. If a command is denied by the Codex sandbox, retry through its escalation mechanism for the authorized operation and report any real rejection.

## Codex usage routing

Use `get_usage_limits` when available. For the applicable `codex` limit bucket, compute `remaining = max(0, 100 - usedPercent)` for each reported non-null primary and secondary window. Route implementation to Antigravity CLI if either window has less than 40% remaining. At exactly 40% or above in both windows, Codex may implement directly. Missing values are unknown, not zero. Preserve the last verified routing decision during a temporary read failure, mark it stale, and continue independent preflight/review; ask for a percentage only if no verified decision exists and routing is necessary. Recheck at task start and before a new implementation stage after a long build or compaction; do not interrupt a running compiler solely to change routing.

Use `gemini-3.8-flash-medium` after verifying that slug is available through the installed CLI. At either usage level, run the requested independent Android architecture and QA reviews through Antigravity when accessible. Below 40%, Codex keeps planning, queue updates, final diff review, and result reporting; Antigravity does repository implementation.

## Antigravity execution

Use `.agents/scripts/invoke-android-specialist.ps1 -Agent android-architect -TaskId <id> -PromptFile <file> -Mode plan` for a read-only review. The script uses the installed `C:\Users\rodri\AppData\Local\agy\bin\agy.exe`, an explicit model, JSON streaming over stdin, a five-minute CLI timeout, and an outer process deadline. Prompt contents are kept out of native command-line arguments. Engineer implementation must explicitly use `-Mode accept-edits` with a bounded prompt. Prompts must identify this runbook, task scope, files to inspect, allowed changes, and expected output. Keep standard permission checks enabled.

The installed CLI's `agents` listing has returned empty even while an explicit `--agent android-architect` invocation succeeded. Do not declare the profiles missing from that listing alone; use a harmless no-tools probe of the named profile and inspect the terminal result. Both flat profile Markdown and `<name>/agent.md` formats are supported by the documented CLI. Preserve these existing flat profiles.

Run the wrapper through the normal command tool, which may return a running execution session ID while the PowerShell process waits. Retain that tool session ID and poll it at bounded intervals; do not confuse it with the CLI conversation ID returned at the end. The wrapper returns structured Status, NeedsAttention, exit code, process/conversation ID, and artifact paths for terminal waits/errors as well as success. `SUCCESS` plus a usable response means the CLI returned; `WAITING`, `RUNNING`, timeout, nonzero exit, or empty/malformed output requires inspection. Use `-ConversationId <id>` to resume a known conversation after resolving its wait. Record permission/authentication waits, retry a recoverable failure once, and retain the task checkpoint. Never report an agent review that did not return findings. If CLI execution remains unavailable, continue safe preflight and Codex review, explicitly mark the independent review incomplete, and report the concrete blocker.

## Completion checks for implementation

1. **Code and architecture review:** Review the final diff against the request and existing module patterns. Use Android Architect for Android dependency/lifecycle/flavor decisions and the product specialist where relevant. Record findings and resolve material issues before completion.
2. **Build:** Compile each affected app/variant and relevant modules with the existing wrapper. For WeatherYou, mobile Google Play and F-Droid are separate variants; TV and Wear OS are separate apps. Wait for a terminal build result. Do not mark a task Done after merely launching Gradle or changing source. Run focused tests/checks appropriate to the change and record the result; avoid unrelated broad suites.
3. **Install and device QA:** Build success must precede APK installation. Before starting an emulator, inspect current ADB devices and listening ports. Choose an unused even console port (for example 5560 or 5562), pass `-port <port>` when launching, and record the owned emulator PID, AVD name, and `emulator-<port>` serial in the task checkpoint. Use `adb -s <serial>` for install, launch, checks, screenshots, and shutdown; recheck the AVD name before targeting it. Reuse another task's emulator only by coordinated agreement and never stop it. Install the matching APK, launch it, confirm the process/activity is usable, and exercise the changed flow. Save useful screenshots and filtered logs under the H: task artifacts folder. Cover every affected form factor: phone/tablet/foldable for adaptive mobile UI, Android TV for TV/shared changes, and Wear OS plus phone pairing for entitlement/sync changes. Boot completed alone is infrastructure evidence, not app QA. Start with two concurrent emulators and stop only instances owned by this task.
4. **Billing limitations:** Real product price, purchase/restore, acknowledgement, and phone/watch purchase sync checks need configured Play products and the appropriate Play test distribution/account. Describe emulator UI checks separately from real purchase verification. Record missing Play prerequisites without claiming that billing passed.
5. **Report and checkpoint:** Provide the changed files, architecture review outcome, command exit results, per-device QA evidence, and outstanding prerequisites. Mark Done only when the required checks pass. If source work is complete but build/device checks are blocked, record `Review — validation blocked` with the exact blocker and next action. Continue independent checks that remain possible.

## Git commits at milestones

The user authorizes local Git commits for completed changes/features and meaningful development milestones. Make those commits as part of the task without asking for confirmation again. Codex owns integration and committing after reconciling specialist results; Antigravity/subagents stage or commit only when Codex explicitly delegates that responsibility in an isolated workspace. This avoids concurrent edits to a shared Git index.

Commit when a feature/fix has passed its required checks, or when a coherent milestone is complete: a reviewed architecture step, an independently useful build/tooling repair, a tested implementation stage, QA-driven corrections, or a finished workflow/documentation update. Keep one clear purpose per commit. If external prerequisites block final acceptance but the completed milestone is useful to preserve, make a clearly labelled checkpoint commit and describe exactly which checks remain blocked. A checkpoint commit does not mean the task or feature is Done.

Before each commit:

1. Recheck Git status and the staged-path list against the task's baseline. Identify task-owned files/hunks; preserve existing staged and unstaged work from the user or other chats.
2. Stage only explicit task-owned paths/hunks. When unrelated work is already staged, use a scoped commit (such as `git commit --only -- <explicit-paths>` for fully task-owned files), or a separate worktree/index strategy that preserves that work. For a mixed-ownership file, isolate the owned hunks; do not commit the whole file by assumption. If ownership cannot be established, record the overlap and continue independent work until it can be resolved.
3. Review the selected patch and run `git diff --cached --check -- <explicit-paths>`. Ensure the selected files contain no credentials, private configuration, signing material, accidental generated outputs, or unrelated changes. Keep screenshots/logs/APKs in the H: evidence directory unless the task specifically calls for a sanitized repository fixture.
4. Record the actual validation performed. Use the repository's existing commit convention where present; otherwise use descriptive `feat:`, `fix:`, `refactor:`, `docs:`, or `chore:` subjects. A checkpoint subject/body must identify the milestone and remaining checks. Use the configured Git identity; never invent an author or hide a commit failure.

Create the local commit, verify its resulting hash and changed-path list, and confirm unrelated staged work remains intact. Save the hash in the task checkpoint/QA report. The user-facing completion reply must include each new commit's short hash, purpose, and validation status alongside the QA evidence and emulator cleanup result. A failed commit remains pending with its concrete error; do not report it as saved. Remote pushes, publishing, history rewrites, and unrelated repository changes follow the user's explicit task authorization.


## QA evidence in the final reply

After each QA pass, reply in the owning task chat with evidence the user can inspect. Do this for passing, failing, and partially blocked runs. Saving files or receiving a subagent response alone does not satisfy the reporting step: Codex must present the results and evidence to the user.

Save a task-specific `qa-report.md` under `H:\Development\Android\artifacts\<repository>\<task-id>\`, using `.agents/templates/qa-report.md` as a starting point. Identify the tested build/variant and source revision (including whether local edits were present), test date, device/AVD and Android API, and the actual checks performed. For each case, record expected behavior, observed behavior, and Pass, Fail, Blocked, or Not run. Distinguish automated checks, manual emulator checks, and external prerequisites such as real Play purchase verification.

Collect screenshots of the affected UI flows and failures, relevant filtered/redacted logs, and build/test summaries with commands and exit codes before closing emulators. For foldable/layout checks capture each exercised state; for TV record the remote/focus steps; for phone/watch checks identify the paired devices and synchronization steps. Link actual files for every evidence item. If no screenshot or log was collected, state that gap instead of inventing an artifact or using an old image as current evidence.

The final chat reply must contain:

- A concise result summary and a per-device/check list or compact table showing Pass, Fail, Blocked, and Not run accurately.
- Clickable Markdown links with absolute paths to the QA report, relevant screenshots, and build/test/runtime logs. Show representative screenshots inline when the chat supports local image previews; keep the full evidence matrix in the linked report.
- Reproduction steps for failures, outstanding prerequisites, and emulator cleanup status (closed, intentionally retained with reason, or cleanup failed).

Confirm linked files exist and screenshots depict the claimed check before replying. Include only useful, sanitized evidence; never disclose credentials, private configuration, purchase tokens, or personal account details in logs/screenshots. An agent's review text is review evidence, not proof that app runtime testing passed.


## Emulator lifecycle and cleanup

Close task-owned emulators as soon as their device checks finish, whether checks pass or fail. Also clean up before ending, cancelling, or blocking a task when no immediate device check still needs the running instance. Waiting for code review, a new build, Play products, account access, or user input normally does not require an emulator to stay running. Preserve screenshots, filtered logs, and useful reproduction notes before shutdown.

Wrap device-check execution in a `try/finally` cleanup step (or equivalent task cleanup) so a failed install, launch, or assertion still triggers teardown. Keep an instance running only for an immediate follow-up check that needs the same live state, or an explicit user debugging request. Record the retention reason, owner, serial/PID, and next action in the task checkpoint; revisit retention when that action finishes. Checkpoints must say which emulators were stopped and which remain intentionally running.

For each owned instance, verify its recorded serial and AVD identity, then request graceful shutdown with `adb -s <owned-serial> emu kill`. Check the native exit code, poll for that serial to disappear from ADB, and verify the recorded emulator PID has exited. Allow a bounded shutdown wait (up to 30 seconds). If graceful shutdown fails, inspect the owned PID and its command line to confirm it still matches the task's AVD/port before considering termination of that specific process through the permitted execution mechanism. Record a concrete cleanup failure if it cannot be stopped.

Stop only task-owned instances; coordinate any shared/reused instance with its owner. Never kill all emulator processes, use a process-name wildcard, shut down another task's device, delete AVD files, or wipe device data as cleanup. Task completion reporting must include emulator cleanup status.


## Secrets and external actions

Never print or send the contents of `local.properties`, `keystore.properties`, `.env`, signing keys, service-account files, credential stores, or full environment dumps to any agent/tool transcript. Do not run broad searches that include those files. SDK pointers may be maintained by a scoped script that changes only `sdk.dir`, preserves all other lines, and emits no file values. Subagent prompts must carry this rule explicitly. Runtime build processes may use existing credentials locally; do not echo them.

Production Play uploads/rollouts, backend migrations/deployments, and account changes require the user's explicit authorization for the concrete action. Prepare reviewable artifacts first. Maintain the user's existing app decisions, including WeatherYou's current client-only donation choice; a workflow review is not authorization to redesign that feature.

References: [Codex repository instructions](https://learn.chatgpt.com/docs/agent-configuration/agents-md), [Antigravity custom agents](https://antigravity.google/docs/subagents?tab=cli), [CLI terminal results](https://www.antigravity.google/docs/cli/headless/), [Gradle Java compatibility](https://docs.gradle.org/current/userguide/compatibility.html).

## Standing WeatherYou PR and Internal Track Delivery

The user authorized this standing workflow on 2026-10-04: for each requested WeatherYou Android app change, prepare and push a focused branch, open a pull request targeting `dev`, and publish the merged build to Google Play's **Internal testing** track after the required checks pass. This standing authorization covers internal testing only; it does not authorize Production releases or other Play tracks.

1. Create a task-specific branch from the current `dev` branch and preserve unrelated checkout changes. Include a concise change summary and actual build/device QA limitations in the PR description.
2. Wait for required pull-request CI checks when the repository has them, and complete the repository-mandated review, builds, and QA. If there are no automatic PR checks, use the local focused builds/reviews as the pre-merge gate; do not mistake the manual Play upload workflow for PR CI. Do not merge if compilation or an available required check fails. Record runtime QA blockers and their exact scope. If only a local debug signing/configuration issue prevents fresh-weather runtime QA, preserve the production WeatherKit path and do not alter it to accommodate local credentials. For an explicitly authorized Internal-testing release, the signed release build/upload workflow becomes the release gate, and the unresolved debug-only limitation must be reported for device validation.
3. After the merge completes, dispatch `.github/workflows/google-play-internal.yml` from `dev` with `confirm_upload=true` and the next unused Android version code. Verify the completed GitHub Actions run and that its release appears on Play Console's Internal testing track. The workflow remains manual-dispatch-only; never publish automatically from every `dev` push, since not every push necessarily belongs to an authorized WeatherYou change.
4. Report the PR, merge commit, upload run, Play release name/version code, QA evidence, and emulator cleanup. Keep the local task checkpoint and QA report current. Do not claim the upload completed until both the workflow and Play track confirm it.

Do not re-request authorization for this routine internal-test flow unless the target app/track or requested scope materially changes. Production publishing still requires separate explicit authorization.

