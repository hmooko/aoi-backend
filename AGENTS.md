# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java`: Spring Boot services and domain modules under `com.koo.aoi.*` (e.g., `quota`, `auth`).
- `src/main/resources`: configuration (YAML/properties), schema assets, and static resources.
- `src/test/java`: JUnit 5 test suites mirroring the main package layout.
- `build.gradle`, `settings.gradle`, and the `gradle/` wrapper define dependencies and toolchain.

## Build, Test, and Development Commands
- `./gradlew build`: compile Java 17 sources, run tests, and assemble the Spring Boot artifact.
- `./gradlew bootRun`: launch the API locally with the default profile.
- `./gradlew test`: execute unit and integration tests via the JUnit Platform.
- `./gradlew clean`: remove cached build outputs when dependencies or generated sources change.

## Coding Style & Naming Conventions
- Follow standard Java 17 + Spring Boot conventions with 4-space indentation and Lombok for boilerplate.
- Package by feature (`com.koo.aoi.<feature>`), grouping domain entities under `domain` and transport layers under `presentation`.
- Name repositories and services after their aggregates (`UsageCounterRepository`, `QuotaService`); suffix DTOs with `Request`/`Response`.
- Maintain descriptive REST endpoint mappings and annotate security boundaries explicitly.

## Testing Guidelines
- Use Spring Boot Starter Test with Mockito, MockWebServer, and Reactor Test for reactive clients.
- Place tests under `src/test/java`, matching the production package and ending classes with `*Test`.
- Cover both success and failure paths; integration tests should use `@SpringBootTest` or `@DataJpaTest` as appropriate.
- Run `./gradlew test` before committing and ensure regressions are caught locally.

## Commit & Pull Request Guidelines
- Write imperative, concise subjects; prefer Conventional Commit prefixes (`feat:`, `fix:`, `chore:`) consistent with history.
- Link issues or feature tickets in the body and summarize scope, risk, and testing notes.
- Keep pull requests reviewable with clear descriptions, screenshots or API examples when behavior changes.
- Rebase on `main` before opening a PR and ensure CI passes.

## Communication Standards
- 모든 커밋 메시지, PR 설명, 리뷰 코멘트, 기술 문서는 반드시 한글로 작성합니다.
- 외부 자료를 인용할 때는 핵심 내용을 한글로 요약해 팀 구성원이 쉽게 이해하도록 합니다.
