# QA evidence — <task ID / feature>

Date: <test date>
Repository: <repository>
Build: <variant, source revision and local-edit status; APK/build artifact if applicable>
Milestone commits: <new short hashes, purpose, validation status, and any remaining checks>
Overall result: <Pass / Fail / Partially blocked>

## Results

| Case | Device / API | Check type | Expected | Observed | Status | Evidence |
| --- | --- | --- | --- | --- | --- | --- |
| <actual case> | <actual device> | <automated / manual emulator / external> | <expected> | <observed> | <Pass / Fail / Blocked / Not run> | <absolute Markdown artifact links> |

## Commands and logs

Record each relevant build/test/install/launch command and terminal result/exit code. Link sanitized logs and build/test summaries. A command that was only started is still running, not passed.

## Screenshots

Link and caption the actual screenshots: device, case, state, and what they demonstrate. Include only captures from this run. State any missing evidence.

## Failures and remaining checks

Record reproducible steps, observed failures, untested cases, and external prerequisites. Keep actual purchase testing separate from emulator UI checks.

## Emulator cleanup

| AVD / serial | Task ownership | Cleanup result | Reason / next action if retained |
| --- | --- | --- | --- |
| <actual instance> | <owned / shared> | <closed and verified / retained / failed> | <reason if applicable> |

## User reply

Reply in the owning chat with the result summary, per-device statuses, a clickable report link, links to relevant evidence, representative inline screenshot previews where supported, outstanding checks, and cleanup status. Verify artifact paths exist before sending.
