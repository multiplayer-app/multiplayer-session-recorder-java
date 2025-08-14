package app.multiplayer.session_recorder;

import app.multiplayer.session_recorder.type.SessionRecorderConfig;
import app.multiplayer.session_recorder.type.Session;
import app.multiplayer.session_recorder.type.SessionType;
import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;

import java.util.concurrent.CompletableFuture;

/**
 * Example demonstrating how to use the SessionRecorder library
 */
public class Main {
    
    // Using the Config class from the same package
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting Multiplayer Time Travel CLI App...");
        
        try {
            // Initialize SessionRecorder
            SessionRecorderConfig config = new SessionRecorderConfig();
            config.setApiKey(Config.MULTIPLAYER_OTLP_KEY);
            config.setTraceIdGenerator(OpenTelemetry.getIdGenerator());
            
            SessionRecorder.init(config);
            System.out.println("✅ SessionRecorder initialized successfully");

            // Create and configure session
            Session session = new Session();

            // Add resource attributes (service-level metadata)
            session.addResourceAttribute("service.name", "my-service");
            session.addResourceAttribute("service.version", "1.0.0");
            session.addResourceAttribute("deployment.environment", "production");
            session.addResourceAttribute("host.name", "server-01");

            // Add session attributes (session-specific data)
            session.addSessionAttribute("userId", "12345");
            session.addSessionAttribute("environment", "production");
            session.addSessionAttribute("version", "1.0.0");
            session.addSessionAttribute("feature", "session-recording");
            
            // Add tags
            session.addTag("environment", "production");
            session.addTag("version", "v1.0.0");
            session.addTag("feature", "session-recording");
            session.setName("Multiplayer Time Travel CLI Session");
            
            System.out.println("📡 Starting session recording...");
            
            // Start session recording and await the result
            try {
                // SessionRecorder.start returns a CompletableFuture
                CompletableFuture<Void> startFuture = SessionRecorder.start(SessionType.PLAIN, session);
                startFuture.join(); // Wait for completion
                System.out.println("✅ Session recording started successfully");
                
                // Simulate some work
                System.out.println("🔄 Performing some work...");
                Thread.sleep(2000); // Simulate 2 seconds of work
                
                // Stop the session
                System.out.println("🛑 Stopping session recording...");
                Session stopSession = new Session();
                stopSession.addSessionAttribute("status", "completed");
                
                CompletableFuture<Void> stopFuture = SessionRecorder.stop(stopSession);
                stopFuture.join(); // Wait for completion
                System.out.println("✅ Session recording stopped successfully");
                
            } catch (Exception e) {
                System.err.println("❌ Failed to start session recording: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize SessionRecorder: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Example of using continuous sessions
     */
    public static void continuousSessionExample() {
        System.out.println("\n🔄 Continuous Session Example:");
        
        try {
            // Initialize if not already done
            if (!SessionRecorder.isInitialized()) {
                SessionRecorderConfig config = new SessionRecorderConfig();
                config.setApiKey(Config.MULTIPLAYER_OTLP_KEY);
                config.setTraceIdGenerator(OpenTelemetry.getIdGenerator());
                SessionRecorder.init(config);
            }
            
            // Create session for continuous recording
            Session session = new Session();
            session.setName("Continuous Debug Session");
            session.addTag("mode", "continuous");
            session.addTag("type", "debug");
            session.addSessionAttribute("mode", "continuous");
            
            // Start continuous session
            CompletableFuture<Void> startFuture = SessionRecorder.start(SessionType.CONTINUOUS, session);
            startFuture.join();
            System.out.println("✅ Continuous session started");
            
            // Simulate saving data multiple times
            for (int i = 1; i <= 3; i++) {
                Thread.sleep(1000); // Wait 1 second
                
                Session saveData = new Session();
                saveData.setName("Save point " + i);
                saveData.addTag("type", "save-point");
                saveData.addSessionAttribute("iteration", String.valueOf(i));
                
                CompletableFuture<Void> saveFuture = SessionRecorder.save(saveData);
                saveFuture.join();
                System.out.println("💾 Saved data point " + i);
            }
            
            // Cancel the continuous session
            CompletableFuture<Void> cancelFuture = SessionRecorder.cancel();
            cancelFuture.join();
            System.out.println("✅ Continuous session cancelled");
            
        } catch (Exception e) {
            System.err.println("❌ Continuous session error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Example of error handling and session cancellation
     */
    public static void errorHandlingExample() {
        System.out.println("\n⚠️ Error Handling Example:");
        
        try {
            // Initialize if not already done
            if (!SessionRecorder.isInitialized()) {
                SessionRecorderConfig config = new SessionRecorderConfig();
                config.setApiKey(Config.MULTIPLAYER_OTLP_KEY);
                config.setTraceIdGenerator(OpenTelemetry.getIdGenerator());
                SessionRecorder.init(config);
            }
            
            // Start a session
            Session session = new Session();
            session.setName("Error Handling Test");
            session.addTag("type", "error-test");
            
            CompletableFuture<Void> startFuture = SessionRecorder.start(SessionType.PLAIN, session);
            startFuture.join();
            System.out.println("✅ Session started for error handling test");
            
            // Simulate an error condition
            boolean errorOccurred = true; // Simulate error
            
            if (errorOccurred) {
                System.out.println("⚠️ Error occurred, cancelling session...");
                
                try {
                    CompletableFuture<Void> cancelFuture = SessionRecorder.cancel();
                    cancelFuture.join();
                    System.out.println("✅ Session cancelled due to error");
                } catch (Exception cancelError) {
                    System.err.println("❌ Failed to cancel session: " + cancelError.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error handling example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
