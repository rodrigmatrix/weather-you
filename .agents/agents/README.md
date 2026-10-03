# Android agent team

These Antigravity workspace agents are shared between WeatherYou and Material Player. They read the active repository first and tailor work to its actual modules.

## Roles

- android-engineer: scoped Kotlin/Compose implementation across device types.
- weather-you-multidevice: WeatherYou specialist for phone, tablet, foldable, Android TV, and Wear OS.
- android-qa: reproducible regression investigation and device-matrix coverage.
- android-architect: pragmatic SOLID, module, and design-pattern review.
- backend-engineer: API/backend contracts and service changes where source exists.
- release-deploy: release preparation with an explicit per-release human go-ahead before external production changes.

## Model and delegation

Select Gemini 3.8 Flash in Antigravity’s model selector for these workspace agents. Antigravity agent Markdown frontmatter does not pin the reasoning model. Keep Codex as the project orchestrator: ask it to delegate bounded codebase audits or review work to these Antigravity specialists, then have Codex reconcile results and own final integration/review.

## QA storage plan

Put Android SDK images, emulator AVDs, Gradle caches, and build artifacts on H:\Development or another H: development folder after confirming the active SDK paths. Keep repositories in their existing locations initially. Limit simultaneous emulators based on memory and workload; phone, foldable, Android TV, and Wear OS form the initial matrix, with tablet coverage through a large-screen profile where it adds distinct coverage.

## Account and secret handling

No Google Play Console integration is configured in the current Codex tool set. Use Google’s supported Developer API/CI authorization when access is set up, with a least-privilege account and secrets stored in the platform credential store. Do not put service-account JSON, upload keys, or signing credentials in these agent files.



## Required repository workflow

Codex loads the root AGENTS.md instructions. Read .agents/workflows/android-delivery.md for mandatory completion checks and explicit H: tool paths. The scripts in .agents/scripts configure each process environment and invoke the installed Antigravity CLI with a timeout and result validation. These profiles provide instructions; use the CLI explicitly to execute a specialist.

- Emulator cleanup: close task-owned instances immediately after their device checks finish, and on task completion/failure/cancellation or a wait that needs no running device. Preserve evidence, use graceful shutdown, verify serial/PID exit, and record cleanup status. Retain only for an immediate follow-up check or explicit debugging request; record the reason and coordinate shared instances with their owner.

- QA evidence reply: after every QA pass, publish the actual per-device/check results in the owning chat with clickable absolute-path links to qa-report.md, relevant screenshots and sanitized build/test/runtime logs; show representative screenshot previews where supported. Include failed/blocked/not-run checks, reproduction steps, remaining prerequisites, and emulator cleanup status. Collect evidence before shutdown and verify linked artifacts exist.

- Git milestone commits: the user authorizes local commits after completed features/fixes and coherent important checkpoints. Codex commits only task-owned files/hunks after the required review/checks, preserving unrelated staged/unstaged work. Label partial checkpoints and outstanding checks honestly. Record the commit hash and validation status in the checkpoint, QA report, and final reply; subagents commit only when explicitly delegated an isolated workspace.
