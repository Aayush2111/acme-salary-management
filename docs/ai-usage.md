# AI Usage and Implementation Notes

Claude Code (Anthropic's CLI agent) was used as a pair-programmer throughout
backend and frontend development, in addition to the higher-level
architecture trade-offs already covered in
[architecture.md](./architecture.md#trade-offs).

## Scope of AI Involvement

AI assistance was concentrated on two areas: **frontend UI implementation**
(components, forms, dashboard, navigation) and **debugging issues hit while
getting the app running in Docker** — a Node version mismatch in the
frontend build image, a MySQL startup race that crashed the backend on
first boot, and a host port conflict on 3306.

The **overall architecture and design** — layering, package structure, the
entity/DTO model, and the trade-off decisions themselves — were made
manually, without AI assistance. AI was used afterward to help write up and
document those already-made decisions here and in `architecture.md`, not to
make them.

## Implementation-level trade-offs

**Shared `EmployeeFormComponent` for add + edit** — the two forms were
identical in fields and validation, so one component handles both modes
(loading and patching data when editing) instead of duplicating logic that
would drift out of sync.

**CSS bar charts instead of a charting library** — the dashboard's
department/country/distribution breakdowns needed relative comparisons, not
full interactive charts, so plain `width: %` bars avoided a new dependency.

**Zoneless change detection needs explicit `detectChanges()`** — this
frontend has no `zone.js`, so Angular doesn't auto-render after async work.
Discovered as a live bug (Edit Employee stuck on "Loading..."); fixed by
injecting `ChangeDetectorRef` and calling `.detectChanges()` in every
subscribe callback that mutates template state.

**Deferred backend cleanup, skipped public deployment** — a planned backend
cleanup/performance review milestone and a public deployment milestone were
both explicitly skipped to prioritize other work; noted here as visible
decisions rather than gaps.
