# Session Recorder Opentelemetry Java
============================================================================
##  Introduction
The multiplayer-session-recorder module integrates OpenTelemetry with the Multiplayer platform to enable seamless trace collection and analysis. This library helps developers monitor, debug, and document application performance with detailed trace data. It supports flexible trace ID generation, sampling strategies.

## Installation

To install the `app.multiplayer:session_recorder` module, add it to your gradle file:

```
implementation 'app.multiplayer:session_recorder:1.0.0'
```

## Usage

The SessionRecorder provides a static API for easy access throughout your application.

### Basic Usage

```java
import app.multiplayer.session_recorder.SessionRecorder;
import app.multiplayer.session_recorder.type.SessionRecorderConfig;
import app.multiplayer.session_recorder.type.Session;
import app.multiplayer.session_recorder.type.SessionType;
import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;

// Configure and initialize
SessionRecorderConfig config = new SessionRecorderConfig();
config.setApiKey("your-api-key-here");
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

### Advanced Usage

```java
// Check if already initialized
if (!SessionRecorder.isInitialized()) {
    // Initialize if needed
    SessionRecorderConfig config = new SessionRecorderConfig();
    config.setApiKey("your-api-key-here");
    config.setTraceIdGenerator(new SessionRecorderRandomIdGenerator());
    SessionRecorder.init(config);
}

// Start a continuous session
Session session = new Session();
session.setName("Continuous Session");
session.addTag("mode", "continuous");
session.addTag("type", "debug");

SessionRecorder.start(SessionType.CONTINUOUS, session)
    .thenRun(() -> {
        // Save data to continuous session
        Session sessionData = new Session();
        sessionData.setName("Updated Data");
        sessionData.addTag("status", "saved");
        SessionRecorder.save(sessionData);
    });

// Stop a session
SessionRecorder.stop(session);

// Cancel a session
SessionRecorder.cancel();
```

## Examples

See the `examples/` directory for comprehensive examples demonstrating:

- Basic session recording
- Continuous sessions
- Error handling
- Async operations

To run the examples:

```bash
# Build the main library first
./gradlew build

# Run the examples
./gradlew :examples:run
```

**Note:** The examples are for documentation purposes and are not included in the published library JAR.

