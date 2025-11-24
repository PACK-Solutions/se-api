# SE API

This repository is a multi-module Kotlin/JVM project built with Gradle. It uses:

- Kotest for testing
- Detekt (with the formatting plugin) for static analysis and style, configured to auto-correct safe issues
- EditorConfig for consistent formatting across IDEs/tools

Below are the most common developer commands and usage notes.

## Prerequisites

- JDK 21
- Use the provided Gradle Wrapper (`./gradlew`) — no local Gradle required

## Running tests (Kotest)

- Run tests for the whole project:

```sh
./gradlew test
```

- Run tests only for a specific module (e.g., core):

```sh
./gradlew :core:test
```

- Run a single test class:

```sh
./gradlew :core:test --tests "com.ps.personne.AppTest"
```

- Run tests with a name filter (Kotest supports Gradle’s --tests pattern):

```sh
./gradlew :core:test --tests "*App*"
```

Notes:

- Kotest engine and assertions are already configured in the core module.
- Gradle will compile with Java toolchain 21 as configured.

## Static analysis (Detekt)

This project uses Detekt with a shared configuration file at detekt.yml and the formatting plugin to enforce and auto-correct many style rules.

- Run Detekt on the whole project:

```sh
./gradlew detekt
```

- Run Detekt on a single module (e.g., core):

```sh
./gradlew :core:detekt
```

### Auto-correct

- Auto-correct is enabled in the Gradle Detekt configuration (autoCorrect = true) and the detekt-formatting plugin is applied. When you run detekt, safe fixes
  will be applied automatically to your sources.
- If you prefer to preview issues without corrections, you can temporarily disable auto-correct by toggling the setting in the module’s build.gradle.kts (
  DetektExtension.autoCorrect) or running on a clean clone.

### Baseline (optional)

To create/update a baseline file (useful to temporarily whitelist existing findings):

```sh
./gradlew :core:detektBaseline
```

This generates a detekt-baseline.xml in the module, which detekt will then use to ignore pre-existing issues. Prefer fixing issues over growing the baseline
where feasible.

## Style and formatting

- .editorconfig defines the code style for Kotlin files (kt/kts) and aligns with ktlint_official rules where applicable.
- detekt.yml fine-tunes rule activation and thresholds (e.g., line length, parameter list sizes). The formatting section configures rules provided by
  detekt-formatting.

## Typical workflow

1) Clean and build everything:

```sh
./gradlew clean build
```

2) Run static analysis with auto-correct:

```sh
./gradlew detekt
```

3) Run unit tests:

```sh
./gradlew test
```

You can combine steps, for example:

```sh
./gradlew clean detekt build
```

## Modules

- core: domain and tests currently wired with Kotest and Detekt.
- rest, database, assembly: other modules exist; apply tasks with :<module>:<task> if/when necessary.

## Troubleshooting

- If detekt reports unexpected rules, check detekt.yml at the project root.
- Ensure you are using JDK 21 (the project uses Gradle toolchains set to 21).
- If you use IntelliJ IDEA, enable Kotlin style = Official and allow trailing commas to match the repository settings.

## Versioning: single source of truth and propagation

- The project’s single source of truth for the application version is Gradle’s project.version, defined in gradle.properties (key: version).
- The REST module writes this version into the JAR Manifest as Implementation-Version.
- Datadog Java agent can infer the version directly from the application JAR manifest.
- The Docker image LABEL version can be set at build time via the APP_VERSION build-arg (optional).

Examples:

- Build Docker image and set the label from the Gradle version (manual):
    - APP_VERSION=$(./gradlew -q properties | awk -F': ' '/^version:/ {print $2; exit}')
    - docker build --build-arg APP_VERSION="$APP_VERSION" -t se-api:${APP_VERSION} .

Notes:

- OpenAPI documentation.yaml currently contains an explicit version. Keep it in sync or consider tooling to generate it from project.version if needed.

## Datadog agent enablement

- Runtime toggle: set DD_AGENT_ENABLED to true/false in your .env.
- The Datadog sidecar (datadog-agent service) is gated behind a Docker Compose profile named `dd-agent`. Compose will start it only when that profile is
  enabled.
- When building via Compose, the Dockerfile build arg DD_AGENT_ENABLED is automatically set from your .env so the application image includes the Java agent only
  if DD_AGENT_ENABLED=true at build time. At runtime, entrypoint.sh will use the agent only if it’s present and DD_AGENT_ENABLED=true.

Examples (Compose):

- Enable Datadog (agent service + Java agent):
    - echo "DD_AGENT_ENABLED=true" >> .env
    - export COMPOSE_PROFILES=dd-agent
    - docker compose build app
    - docker compose up app
- Disable Datadog (no agent service, no Java agent at runtime):
    - export -n COMPOSE_PROFILES || true # or unset COMPOSE_PROFILES
    - echo "DD_AGENT_ENABLED=false" >> .env
    - docker compose build app
    - docker compose up app

Plain Docker (without compose):

- Build without agent (default):
    - docker build -t se-api:latest .
- Build with agent included:
    - docker build --build-arg DD_AGENT_ENABLED=true -t se-api:dd .
- Run with/without agent:
    - docker run -p 8080:8080 -e DD_AGENT_ENABLED=false se-api:latest
    - docker run -p 8080:8080 -e DD_AGENT_ENABLED=true se-api:dd

## Updating dependency versions (Version Catalog)

This project uses Gradle Version Catalogs. To discover and apply available dependency updates:

1) Generate/update the proposed versions file using the pre-defined mise task:

```sh
mise tasks run version-catalog-update
```

This runs `./gradlew versionCatalogUpdate --interactive --no-configuration-cache` and writes recommendations to `gradle/libs.versions.updates.toml`.

2) Apply the recommended updates to `gradle/libs.versions.toml`:

```sh
./gradlew versionCatalogApplyUpdates
```

Notes:

- Ensure you have mise installed and are using the repo’s tool versions (Java 21, Gradle via wrapper). If needed: `mise install` then use the provided
  `./gradlew`.
- Review the changes in `gradle/libs.versions.toml` before committing.
