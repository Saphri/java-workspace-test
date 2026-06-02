# Agent Instructions for `fun` — Quarkus Java Project

## Stack
- **Language**: Java 25 (Project Loom / Virtual Threads)
- **Framework**: Quarkus 3.35.4
- **Build**: Maven (wrapper `./mvnw`)
- **Messaging**: NATS JetStream (`quarkus-messaging-nats-jetstream`)
- **Observability**: OpenTelemetry (metrics + logs)
- **API**: REST (`quarkus-rest` + `quarkus-rest-jackson`), OpenAPI (`quarkus-smallrye-openapi`)
- **Infrastructure**: Docker Compose (NATS, Grafana LGTM for OTel)

## Extensions in Use
| Extension | Purpose |
|---|---|
| `quarkus-rest` / `quarkus-rest-jackson` | REST endpoints with JSON support |
| `quarkus-scheduler` | Scheduled/cron tasks |
| `quarkus-opentelemetry` | Tracing, metrics, logs |
| `quarkus-messaging-nats-jetstream` | Reactive messaging via NATS JetStream |
| `quarkus-smallrye-openapi` | OpenAPI spec generation |
| `quarkus-arc` | CDI injection |
| `quarkus-smallrye-health` | Health checks |

## Coding Conventions

### Style
- Prefer `var` for local variables when the type is obvious.
- Mark classes and fields `final` where possible.
- Use `@Nullable` / `@NonNull` from `javax.annotation` for null-safety contracts.
- Write Javadoc on public APIs.
- Use `@ApplicationScoped` for CDI beans.
- Use `@RunOnVirtualThread` on REST resources to leverage Project Loom.

### REST Resources
```java
@Path("/hello")
@ApplicationScoped
@RunOnVirtualThread
public class GreetingResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void seedTasks() { ... }
}
```

### Reactive Messaging (NATS JetStream)
- **Outgoing**: Inject `Emitter<T>` via `@Channel("channel-name")`.
- **Incoming**: Annotate a method with `@Incoming("channel-name")` returning `Uni<Void>`.
- Channels are configured in `application.properties` under `mp.messaging.*`.

```java
@Incoming("data-in")
public Uni<Void> onResourceEvent(Message<String> msg) {
    return Uni.createFrom().completionStage(msg.ack());
}
```

### Scheduled Tasks
- Use `@Scheduled` or `@Scheduled(cron = "...")` on methods.
- Inject NATS `Connection` directly when low-level JetStream access is needed.

### Logging
- Use `org.jboss.logging.Logger` (build-time generated via `Logger.getLogger(Class)`).
- Prefer `log.infof()` / `log.errorf()` for parameterized messages.

### Testing
- Use `@QuarkusTest` for integration tests.
- Use RestAssured (`io.rest-assured`) for HTTP endpoint testing.
- Use AssertJ for assertions.
- Dev Services for NATS is enabled by default (`quarkus.messaging.nats.devservices.enabled=true`).

## OpenAPI
- Spec is stored under `openapi_specs/openapi.json`.
- OpenAPI drift is validated during `mvn verify` via `openapi-diff-maven`.
- Config: `quarkus.smallrye-openapi.store-schema-directory=openapi_specs`.

## OpenSpec Workflow
This project uses a **spec-driven** change management workflow defined in `openspec/config.yaml`.

### Change Lifecycle
1. **Propose** — Generate proposal with design doc, spec, and task list.
2. **Apply** — Implement tasks one by one on a dedicated branch (named after the change folder).
3. **Archive** — Finalize and archive the completed change.

### Rules
- Every change lives in a dedicated git branch named after the change folder, created from `main`.
- Specs describe *what* and *why*, not *how*.
- Design docs detail architecture, data models, and API contracts.
- Tasks are atomic, actionable, and include a Definition of Done.

### Skills Available
| Skill | Purpose |
|---|---|
| `openspec-propose` | Generate a full proposal (design + spec + tasks) in one step |
| `openspec-apply-change` | Implement tasks from an existing change |
| `openspec-explore` | Think through requirements before or during a change |
| `openspec-archive-change` | Finalize and archive a completed change |

## Key Commands
| Action | Command |
|---|---|
| Run in dev mode | `./mvnw compile quarkus:dev` |
| Package (JVM) | `./mvnw package` |
| Package (native) | `./mvnw package -Dnative` |
| Run tests | `./mvnw test` |
| Validate OpenAPI drift | `./mvnw verify` |
| Start infrastructure | `docker compose up -d` |
| Dev UI | `http://localhost:8080/q/dev/` |

## Configuration
- Main config: `src/main/resources/application.properties`
- NATS Dev Services: auto-started unless `quarkus.messaging.nats.devservices.enabled=false`
- OpenTelemetry: enabled for metrics and logs; export to LGTM at `localhost:4317`/`4318`
- OpenAPI spec output: `openapi_specs/openapi.json`

## File Layout
```
src/
├── main/
│   ├── java/org/mjelle/
│   │   ├── rest/          # REST resources
│   │   └── scheduler/     # Scheduled tasks + NATS consumers
│   ├── openapi/           # Reference OpenAPI spec (for drift detection)
│   └── resources/
│       └── application.properties
└── test/java/             # Unit + integration tests
openapi_specs/             # Generated OpenAPI output
```

## Gotchas
- Never run `mvn clean` while Quarkus dev mode is active — it deletes `target/test-classes` and breaks the test runner.
- When adding extensions, always use Quarkus extension catalog (`quarkus_searchTools` / `quarkus_searchDocs`) rather than manual dependency addition.
- NATS stream/subject config lives in `application.properties` under `quarkus.messaging.nats.streams.*`.
