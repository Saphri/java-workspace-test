## Why

The OpenAPI spec generated at build time (`openapi_specs/openapi.json`) is currently only stored locally and in the repo. Publishing it to GitHub Packages makes the spec machine-readable for external consumers, enables versioned API documentation, and supports downstream tooling (client codegen, API gateways, contract testing) without requiring a repo clone.

## What Changes

- Add a Maven build step that uploads the generated OpenAPI spec to GitHub Packages (GitHub Container Registry / GitHub Packages Maven) as a versioned artifact.
- The upload is gated by a CI-only property so local builds are unaffected.
- No breaking changes to existing behavior.

## Capabilities

### New Capabilities
- `openapi-package-upload`: Upload the generated OpenAPI spec (`openapi_specs/openapi.json`) to GitHub Packages as a versioned artifact after each successful build, using the project version from `pom.xml`.

### Modified Capabilities
- (none)

## Impact

- `pom.xml`: new plugin or configuration for uploading to GitHub Packages.
- CI pipeline: new environment variable or secret for authentication (`GITHUB_TOKEN`).
- No changes to Java source code, REST endpoints, or NATS messaging.
