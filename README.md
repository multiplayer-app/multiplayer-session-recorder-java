![Description](.github/header-java.png)

<div align="center">
<a href="https://github.com/multiplayer-app/multiplayer-session-recorder-java">
  <img src="https://img.shields.io/github/stars/multiplayer-app/multiplayer-session-recorder-java.svg?style=social&label=Star&maxAge=2592000" alt="GitHub stars">
</a>
  <a href="https://github.com/multiplayer-app/multiplayer-session-recorder-java/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/multiplayer-app/multiplayer-session-recorder-java" alt="License">
  </a>
  <a href="https://multiplayer.app">
    <img src="https://img.shields.io/badge/Visit-multiplayer.app-blue" alt="Visit Multiplayer">
  </a>
  
</div>
<div>
  <p align="center">
    <a href="https://x.com/trymultiplayer">
      <img src="https://img.shields.io/badge/Follow%20on%20X-000000?style=for-the-badge&logo=x&logoColor=white" alt="Follow on X" />
    </a>
    <a href="https://www.linkedin.com/company/multiplayer-app/">
      <img src="https://img.shields.io/badge/Follow%20on%20LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="Follow on LinkedIn" />
    </a>
    <a href="https://discord.com/invite/q9K3mDzfrx">
      <img src="https://img.shields.io/badge/Join%20our%20Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="Join our Discord" />
    </a>
  </p>
</div>

# Multiplayer Session Recorder - OpenTelemetry Java

## Introduction

The multiplayer-session-recorder module integrates OpenTelemetry with the Multiplayer platform to enable seamless trace collection and analysis. This library helps developers monitor, debug, and document application performance with detailed trace data. It supports flexible trace ID generation, sampling strategies.

## Installation

To install the `app.multiplayer:session_recorder` module, add it to your gradle file:

```
implementation 'app.multiplayer:session_recorder:1.0.0'
```

## Set up Session Recorder client

```java
import app.multiplayer.session_recorder.SessionRecorder;
import app.multiplayer.session_recorder.type.SessionRecorderConfig;
import app.multiplayer.session_recorder.type.Session;
import app.multiplayer.session_recorder.type.SessionType;
import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;

// Configure and initialize
SessionRecorderConfig config = new SessionRecorderConfig();
config.setApiKey("{{YOUR_API_KEY}}");
config.setTraceIdGenerator(new SessionRecorderRandomIdGenerator());

// Initialize the SessionRecorder
SessionRecorder.init(config);

// Use it with static methods
Session session = new Session();
session.setName("My Session");

// Add tags if needed
session.addTag("environment", "production");
session.addTag("version", "v1.0.0");
session.addTag("feature", "session-recording");

// Add session attributes
session.addSessionAttribute("userId", "12345");
session.addSessionAttribute("environment", "production");
session.addSessionAttribute("version", "1.0.0");

// Add resource attributes
session.addResourceAttribute("service.name", "my-service");
session.addResourceAttribute("service.version", "1.0.0");
session.addResourceAttribute("deployment.environment", "production");

SessionRecorder.start(SessionType.PLAIN, session);
```

## Session Recorder trace Id generator

```java
import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;

idGenerator = new SessionRecorderRandomIdGenerator();
```

## Session Recorder trace id ratio based sampler


```java
import io.opentelemetry.sdk.trace.samplers.Sampler;
import app.multiplayer.session_recorder.trace.samplers.SessionRecorderTraceIdRatioBasedSampler;

Sampler sampler = SessionRecorderTraceIdRatioBasedSampler.create(0.5);
```

## Setup backend

### Setup opentelemetry data

Use officials opentelemetry guidence from [here](https://opentelemetry.io/docs/languages/java) or [zero-code](https://opentelemetry.io/docs/zero-code/java) approach

### Send opentelemetry data to Multiplayer

Opentelemetry data can be sent to Multiplayer's collector in few ways:

### Option 1 (Direct Exporter):

```java
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;

SpanExporter spanExporter = OtlpHttpSpanExporter.builder()
  .setEndpoint("https://otlp.multiplayer.app/v1/traces")
  .addHeader("Authorization", "{{MULTIPLAYER_OTLP_KEY}}")
  .build();
```

or

```java
import io.opentelemetry.sdk.trace.export.SpanExporter;
import app.multiplayer.session_recorder.exporter.SessionRecorderOtlpHttpSpanExporter;

SpanExporter spanExporter = new SessionRecorderOtlpHttpSpanExporter(
  "{{MULTIPLAYER_OTLP_KEY}}",
  "https://otlp.multiplayer.app/v1/traces"
);
```

### Option 2 (Collector):

Another option - send otlp data to [opentelemetry collector](https://github.com/multiplayer-app/multiplayer-otlp-collector).

Use following examples to send data to collector
```java
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;

SpanExporter spanExporter = OtlpHttpSpanExporter.builder()
  .setEndpoint("http://{{OTLP_COLLECTOR_URL}}/v1/traces")
  .addHeader("Authorization", "{{MULTIPLAYER_OTLP_KEY}}")
  .build();
```

or

```java
import io.opentelemetry.sdk.trace.export.SpanExporter;
import app.multiplayer.session_recorder.exporter.SessionRecorderOtlpHttpSpanExporter;

SpanExporter spanExporter = new SessionRecorderOtlpHttpSpanExporter(
  "{{MULTIPLAYER_OTLP_KEY}}",
  "http://{{OTLP_COLLECTOR_URL}}/v1/traces"
);
```

### Add request/response payloads

Deploy [Envoy Proxy](https://github.com/multiplayer-app/multiplayer-proxy) in front of your backend service.

## License

MIT — see [LICENSE](./LICENSE).
