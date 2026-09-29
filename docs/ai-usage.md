# AI Usage and Implementation Notes

Claude Code (Anthropic's CLI agent) was used as a pair-programmer throughout
backend and frontend development, in addition to the higher-level
architecture trade-offs already covered in
[architecture.md](./architecture.md#trade-offs).

## Workflow

Work was broken into milestones (search/filter, sorting/pagination, CRUD
forms, delete confirmation, dashboard, navigation, tests, polish, docs,
Docker...). Each milestone was implemented, then compiled/tested (`mvnw
test`, `ng build`/`ng test`) before moving on. UI behavior itself was
verified manually in the browser by the developer, not by the AI. Bugs
found during manual testing were reported back in plain language and fixed
in the same session.

Representative instructions given during the build (paraphrased):

- "Let's do this in milestones — implement, build, and I'll test the UI myself."
- "When I click edit, it says loading and nothing happens" → led to the zoneless change-detection fix below.
- "Don't write lots of tests, just minimal" → scoped the test milestone to fixing existing broken specs only.
- "Skip this milestone, make small UI improvements" → a deliberately small, contained polish pass instead of a redesign.
- "Skip public deployment, I don't have AWS" → scoped the final milestones to what's runnable locally/via Docker.

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

**Minimal test scope** — two pre-existing specs were broken (wrong imports,
missing DI providers). Per instruction, the fix restored them as smoke
tests only, rather than adding new coverage.

**Deferred backend cleanup, skipped public deployment** — a planned backend
cleanup/performance review milestone and a public deployment milestone were
both explicitly skipped to prioritize other work; noted here as visible
decisions rather than gaps.
