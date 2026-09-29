# AI Usage and Trade-off Notes

This document records how AI tooling (Claude Code) was used while building this
project, and captures implementation-level trade-offs that aren't already
covered by the "Architectural Trade-offs" section in [architecture.md](./architecture.md).

## 1. How AI was used

Claude Code (Anthropic's CLI agent) was used as a pair-programmer throughout
backend and frontend development. The workflow was milestone-based:

1. The full assessment brief was reviewed and broken into an ordered list of
   milestones (search/filter, sorting/pagination, CRUD forms, delete
   confirmation, dashboard analytics, navigation, tests, polish, docs, etc.).
2. Each milestone was implemented, compiled/tested, and committed on its own
   before moving to the next — one commit per milestone, verified with
   `mvnw compile`/`mvnw test` (backend) and `ng build`/`ng test` (frontend).
3. UI behavior itself (browser testing) was verified manually by the developer,
   not by the AI — the AI's job was implementation and build verification, not
   acting as a browser QA agent.
4. Bugs found during manual testing (e.g. "Edit Employee" appearing stuck on
   "Loading...") were reported back in plain language and root-caused/fixed by
   the AI in the same session.

Representative instructions given to the AI during this project (paraphrased):

- "Let me know what's left to be done, and let's plan to do it in different
  milestones, then make a commit and move further. Your job is to implement
  and build and make a commit, I will do the UI testing on my own."
- "When I click edit, it says 'loading employee...' and nothing happens" (bug
  report that led to the zoneless change-detection fix, see below).
- "Don't write lots of tests, just minimal" (scoped the test milestone to
  fixing existing broken specs rather than adding new coverage).
- "Skip this milestone, let's make the UI more modern where possible, but
  don't make so many changes — just small improvements" (scoped the polish
  pass deliberately small).

## 2. Trade-offs made during implementation

These are decisions made while building individual milestones, in addition to
the higher-level architecture trade-offs already documented in
`architecture.md` section 11.

### Merged Add/Edit into one `EmployeeFormComponent`

The initial "Add Employee" form and the later "Edit Employee" requirement had
identical fields and validation. Rather than duplicating the component, it was
renamed into a shared `EmployeeFormComponent` that loads and patches existing
data when an `id` route param is present, and calls create vs. update based on
that mode.

**Trade-off:** slightly more branching inside one component vs. two small,
simpler components. Chosen because the two forms had zero meaningful
divergence — duplicating them would only have created a second place to keep
validation rules in sync.

### CSS bar charts instead of a charting library

The dashboard needed department/country/distribution breakdowns. No charting
library was already installed.

**Trade-off:** a real charting library (e.g. Chart.js, ngx-charts) would give
richer visuals (tooltips, animations, axes) at the cost of a new dependency,
bundle size, and API surface to learn. Plain CSS bars (`width: %` driven by
each row's share of the max value) were chosen instead since the requirement
was "show relative comparisons," not full interactive charting.

### Zoneless change detection requires explicit `detectChanges()`

This frontend has no `zone.js` dependency, so Angular does not automatically
re-render after async work (HTTP responses, RxJS `subscribe` callbacks). This
was discovered as a live bug: the Edit Employee form loaded data successfully
but the view never updated, appearing stuck on "Loading...".

**Trade-off / lesson:** every component that mutates template-bound state
inside a `subscribe` callback must inject `ChangeDetectorRef` and call
`.detectChanges()` explicitly. This is more manual than zone-based apps, but
it was already the existing pattern in `EmployeeListComponent`; the fix was to
apply it consistently everywhere else (form, dialog, dashboard) rather than
add `zone.js` back in.

### Minimal test scope

Two pre-existing spec files (`employee.spec.ts`, `employee-list.spec.ts`) were
broken (wrong imports, missing DI providers) and failed to even build under
`ng test`. Per explicit instruction, the fix was scoped to making the existing
five spec files pass again as smoke tests — no new spec files or deeper
behavioral coverage were added.

**Trade-off:** lower test coverage than an idealized implementation, in favor
of matching the time/scope the developer asked for. Noted here so it's a
deliberate, visible decision rather than an accidental gap.

### Deferred backend cleanup/performance review

A planned "backend cleanup and performance review" milestone was skipped in
favor of a small, contained UI polish pass (background/shadows/spinners),
per explicit instruction. The backend was left as-is from earlier milestones;
this is a deliberate deferral, not an oversight, and can be revisited if time
allows.
