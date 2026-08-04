# Changelog

This file records consumer-visible BEEZ changes. Versions remain unreleased until a corresponding artifact and release record are published.

## Unreleased

### Added

- DTCG token sources, generated Kotlin schemes, Light and Dark themes, and an alternate test brand.
- Semantic color contrast validation for registered text and non-text pairs.
- Experimental Action Button, Text Field, and binary Checkbox components with shared Compose implementations, accessibility behavior, tests, visual baselines, catalog scenarios, and specifications.
- Experimental non-interactive Surface with Flat/Raised/Floating semantic elevation, shared tests, visual baselines, specification, and Catalog scenarios.
- Proposed semantic Text implementation with six typography roles, four foreground tones, shared layout and semantics tests, a specification, and a bilingual Catalog guide. Remote Catalog and visual validation remain in progress.
- Responsive component overview cards and bilingual guided detail screens with interactive playgrounds, anatomy, public properties, variants or states, layout rules, usage guidance, accessibility notes, and commonMain API examples in the Compose Catalog.
- Catalog-specific Korean font coverage validation and a generated Noto Sans KR subset for all current Korean guide copy.
- Compose Multiplatform Web/Wasm catalog and GitHub Pages deployment.
- Repository-local Maven staging publication for the aligned `beez-tokens`, `beez-foundation`, and `beez-components` modules.
- Standalone Android and Desktop KMP consumer fixture that resolves the published Components artifact and its transitive dependencies without Material.
- CI validation for supported snapshot, pre-release, and stable version formats, with manually requested staging repositories retained for seven days.
- Publication auditing for the complete artifact set, identities, aligned internal dependencies, primary/source artifacts, checksums, and Material-independent metadata.

### Release readiness

- No BEEZ library artifact has been externally published.
- The project license, external POM metadata, GitHub Packages upload workflow, tag protection, and package retention policy remain unresolved before `0.1.0-alpha.1`.
