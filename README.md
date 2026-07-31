# BEEZ Design System

BEEZ is a specification-first design system for Compose Multiplatform.

It provides one shared component API for Android, iOS, Desktop, and Web while keeping the core independent from Material.

## Status

BEEZ is in its initial architecture and foundation stage. No stable component artifact has been released.

## Modules

```text
beez-components → beez-foundation → beez-tokens
```

- `beez-tokens`: token values, schemes, and generated token APIs
- `beez-foundation`: theme, CompositionLocal, and shared UI foundations
- `beez-components`: public BEEZ component APIs and implementations

Catalog, icons, adapters, and documentation tooling will be added when a validated use case requires them.

## Principles

- Specification before implementation
- Semantic tokens before raw values
- Material-independent core
- Shared `commonMain` components
- Accessibility as a completion requirement
- Documentation, tests, and catalog scenarios evolve with implementation

## Requirements

- JDK 17 or newer
- Gradle through the checked-in wrapper
- Android SDK for Android target tasks
- Xcode for Apple target binaries and tests
- A WasmGC-capable browser for Web runtime tests

See [compatibility](docs/compatibility.md) for the selected toolchain and platform baseline.

## Build

List available projects and tasks:

```bash
./gradlew projects
./gradlew tasks
```

Compile the Desktop variants without an Android or Apple SDK:

```bash
./gradlew compileKotlinDesktop
```

Run common tests through the Desktop target:

```bash
./gradlew desktopTest
```

## Documentation

- [Project charter](docs/charter.md)
- [Token taxonomy](docs/token-taxonomy.md)
- [Component specification template](docs/component-template.md)
- [Definition of Done](docs/definition-of-done.md)
- [Platform policy](docs/platform-policy.md)
- [Compatibility](docs/compatibility.md)
- [Architecture decisions](docs/decisions/)

## Agent workflow

Repository-wide agent rules are in [AGENTS.md](AGENTS.md). The project-local BEEZ skill is in [`skills/beez-design-system`](skills/beez-design-system/).
