## Context

The project currently generates an OpenAPI specification file at `openapi_specs/openapi.json` during the build process. While this file is committed to the repository, it is not easily consumable by external tools or teams who may not want to clone the entire source repository. Publishing this specification to GitHub Packages allows it to be treated as a versioned artifact, enabling downstream consumers to fetch specific versions of the API contract for client generation, contract testing, or API gateway configuration.

## Goals / Non-Goals

**Goals:**
- Automate the upload of `openapi_specs/openapi.json` to the GitHub Packages Maven repository.
- Ensure the uploaded artifact is versioned according to the project version defined in `pom.xml`.
- Gate the upload process so it only occurs in CI environments, preventing local builds from failing due to missing credentials.

**Non-Goals:**
- Rendering the OpenAPI spec into a human-readable HTML portal.
- Modifying the existing OpenAPI generation logic.
- Implementing a custom upload script outside of the Maven ecosystem.

## Decisions

### 1. Upload Mechanism: `maven-deploy-plugin`
We will use the `maven-deploy-plugin`'s `deploy-file` goal. This is the standard Maven way to upload an artifact to a remote repository. 
- **Rationale**: It integrates directly with Maven's dependency and repository management, utilizing `settings.xml` for authentication and `pom.xml` for versioning.
- **Alternative**: A custom `curl` script. Rejected because it bypasses Maven's built-in repository handling and authentication mechanisms.

### 2. CI Gating: Maven Profile
The upload logic will be encapsulated within a Maven profile (e.g., `publish-spec`).
- **Rationale**: This allows the CI pipeline to explicitly trigger the upload using `-Ppublish-spec`, while local developers can run builds without needing GitHub Packages credentials.

### 3. Authentication: `GITHUB_TOKEN`
Authentication will be handled via the `settings.xml` file in the CI environment, mapping the repository ID to the `GITHUB_TOKEN` provided by GitHub Actions.
- **Rationale**: This is the secure, standard method for authenticating with GitHub Packages in a CI/CD pipeline.

## Risks / Trade-offs

- **[Risk]** GitHub Packages Maven repository may require a POM file for every uploaded artifact.
  - **Mitigation**: If the `deploy-file` goal fails without a POM, we will use the `maven-assembly-plugin` to package the JSON file into a small JAR with a generated POM.
- **[Risk]** Version collisions if the project version in `pom.xml` is not incremented.
  - **Mitigation**: GitHub Packages generally does not allow overwriting existing versions of an artifact. The CI pipeline should fail if the version has not been bumped, alerting the developer to update the version.
