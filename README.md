![Description](./docs/img/header-java.png)

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

# Multiplayer Full Stack Session Recorder

The Multiplayer Full Stack Session Recorder is a powerful tool that offers deep session replays with insights spanning frontend screens, platform traces, metrics, and logs. It helps your team pinpoint and resolve bugs faster by providing a complete picture of your backend system architecture. No more wasted hours combing through APM data; the Multiplayer Full Stack Session Recorder does it all in one place.

## Install

Add to your gradle file:

```
implementation 'app.multiplayer:session_recorder:1.0.0'
```

## Set up backend services

### Route traces and logs to Multiplayer

Multiplayer Full Stack Session Recorder is built on top of OpenTelemetry.

### New to OpenTelemetry?

No problem. You can set it up in a few minutes. If your services don't already use OpenTelemetry, you'll first need to install the OpenTelemetry libraries. Detailed instructions for this can be found in the [OpenTelemetry documentation](https://opentelemetry.io/docs/).

### Already using OpenTelemetry?

You have two primary options for routing your data to Multiplayer:

***Direct Exporter***: This option involves using the Multiplayer Exporter directly within your services. It's a great choice for new applications or startups because it's simple to set up and doesn't require any additional infrastructure. You can configure it to send all session recording data to Multiplayer while optionally sending a sampled subset of data to your existing observability platform.

***OpenTelemetry Collector***: For large, scaled platforms, we recommend using an OpenTelemetry Collector. This approach provides more flexibility by having your services send all telemetry to the collector, which then routes specific session recording data to Multiplayer and other data to your existing observability tools.


### Option 1: Direct Exporter

Send OpenTelemetry data from your services to Multiplayer and optionally other destinations (e.g., OpenTelemetry Collectors, observability platforms, etc.).

This is the quickest way to get started, but consider using an OpenTelemetry Collector (see [Option 2](#option-2-opentelemetry-collector) below) if you're scalling or a have a large platform.

```java
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import app.multiplayer.session_recorder.exporter.SessionRecorderLogsExporterWrapper;
import app.multiplayer.session_recorder.exporter.SessionRecorderTraceExporterWrapper;

// # set up Multiplayer exporters. Note: GRPC exporters are also available.
// # see: `SessionRecorderOtlpHttpSpanExporter` and `SessionRecorderOtlpGrpcLogExporter`


// Multiplayer exporter wrappers filter out session recording atrtributes before passing to provided exporter
SpanExporter spanExporter = SessionRecorderTraceExporterWrapper(
  // add any OTLP trace exporter
  OtlpHttpSpanExporter.builder()
    .setEndpoint("...")
    .build()
);
LogRecordExporter logsExporter = SessionRecorderLogsExporterWrapper(
  // add any OTLP log exporter
  OtlpHttpLogRecordExporter.builder()
    .setEndpoint("...")
    .build()
);
```

### Option 2: OpenTelemetry Collector

If you're scalling or a have a large platform, consider running a dedicated collector. See the Multiplayer OpenTelemetry collector [repository](https://github.com/multiplayer-app/multiplayer-otlp-collector) which shows how to configure the standard OpenTelemetry Collector to send data to Multiplayer and optional other destinations.

Add standard [OpenTelemetry code](https://opentelemetry.io/docs/languages/java/configuration/#configurablespanexporterprovider) to export OTLP data to your collector.

See a basic example below:

```java
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;

SpanExporter spanExporter = OtlpHttpSpanExporter.builder()
    .setEndpoint("http://<OTLP_COLLECTOR_URL>/v1/traces")
    .build();
LogRecordExporter logsExporter = OtlpHttpLogRecordExporter.builder()
    .setEndpoint("http://<OTLP_COLLECTOR_URL>/v1/logs")
    .build();
```

### Capturing request/response and header content

In addition to sending traces and logs, you need to capture request and response content. We offer solution for this:

***Multiplayer Proxy:*** Alternatively, you can run a [Multiplayer Proxy](https://github.com/multiplayer-app/multiplayer-proxy) to handle this outside of your services. This is ideal for large-scale applications and supports all languages, including those like Java that don't allow for in-service request/response hooks. The proxy can be deployed in various ways, such as an Ingress Proxy, a Sidecar Proxy, or an Embedded Proxy, to best fit your architecture.

### Multiplayer Proxy

The Multiplayer Proxy enables capturing request/response and header content without changing service code. See instructions at the [Multiplayer Proxy repository](https://github.com/multiplayer-app/multiplayer-proxy).

## Set up CLI app

The Multiplayer Full Stack Session Recorder can be used inside the CLI apps.

The [Multiplayer Time Travel Demo](https://github.com/multiplayer-app/multiplayer-time-travel-platform) includes an example [java CLI app](https://github.com/multiplayer-app/multiplayer-time-travel-platform/tree/main/clients/java-cli-app).

See an additional example below.

### Quick start

Use the following code below to initialize and run the session recorder.

Example for Session Recorder initialization relies on [OpenTelemetry.java](./examples/src/main/java/app/multiplayer/session_recorder/OpenTelemetry.java) file. Copy that file and put next to quick start code.

```java
// IMPORTANT: set up OpenTelemetry
// for an example see ./examples/src/main/java/app/multiplayer/session_recorder/OpenTelemetry.java
// NOTE: for the code below to work, copy ./examples/src/main/java/app/multiplayer/session_recorder/OpenTelemetry.java to ./OpenTelemetry.java
import app.multiplayer.session_recorder.SessionRecorder;
import app.multiplayer.session_recorder.type.SessionRecorderConfig;
import app.multiplayer.session_recorder.type.Session;
import app.multiplayer.session_recorder.type.SessionType;
import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;

// Configure and initialize
SessionRecorderConfig config = new SessionRecorderConfig();
config.setApiKey("MULTIPLAYER_OTLP_KEY"); // note: replace with your Multiplayer OTLP key
config.setTraceIdGenerator(OpenTelemetry.getIdGenerator());

// Initialize the SessionRecorder
SessionRecorder.init(config);

// create session 
Session session = new Session();
session.setName("This is test session");

session.addTag("environment", "production");
session.addTag("version", "v1.0.0");
session.addTag("feature", "session-recording");

session.addSessionAttribute("userId", "12345");
session.addSessionAttribute("environment", "production");
session.addSessionAttribute("version", "1.0.0");

session.addResourceAttribute("service.name", "my-service");
session.addResourceAttribute("service.version", "1.0.0");
session.addResourceAttribute("deployment.environment", "production");

SessionRecorder.start(SessionType.PLAIN, session);

// do something here

Session stopSession = new Session();
stopSession.addSessionAttribute("status", "completed");

SessionRecorder.stop(stopSession);
```

## License

MIT — see [LICENSE](./LICENSE).
