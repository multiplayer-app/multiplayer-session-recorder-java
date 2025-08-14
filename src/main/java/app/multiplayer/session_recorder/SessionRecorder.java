package app.multiplayer.session_recorder;

import app.multiplayer.session_recorder.services.ApiService;
import app.multiplayer.session_recorder.type.*;
import app.multiplayer.session_recorder.sdk.DateUtils;
import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;
import app.multiplayer.session_recorder.sdk.SaveContinuousSession;

// import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.*;

public class SessionRecorder {

    // Singleton instance
    private static volatile SessionRecorder instance;
    private static final Object lock = new Object();

    private boolean isInitialized = false;
    private String shortSessionId = "";

    private SessionRecorderRandomIdGenerator traceIdGenerator;
    private SessionType sessionType = SessionType.PLAIN;
    private SessionState sessionState = SessionState.STOPPED;
    private final ApiService apiService = new ApiService();

    private Map<String, Object> resourceAttributes = new HashMap<>();

    // Private constructor to prevent instantiation
    private SessionRecorder() {}

    /**
     * Initialize the SessionRecorder singleton with the given configuration
     * @param config the configuration for the SessionRecorder
     */
    public static void init(SessionRecorderConfig config) {
        synchronized (lock) {
            if (instance == null) {
                instance = new SessionRecorder();
            }
            instance._initialize(config);
        }
    }

    /**
     * Check if the SessionRecorder is initialized
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return instance != null && instance.isInitialized;
    }

    /**
     * Reset the singleton instance (useful for testing or re-initialization)
     */
    public static void reset() {
        synchronized (lock) {
            instance = null;
        }
    }

    private void _initialize(SessionRecorderConfig config) {
        this.resourceAttributes = config.getResourceAttributes() != null
            ? config.getResourceAttributes()
            : Map.of(ATTR_MULTIPLAYER_SESSION_RECORDER_VERSION, SESSION_RECORDER_VERSION);

        this.isInitialized = true;

        if (config.getApiKey() == null || config.getApiKey().isEmpty()) {
            throw new IllegalArgumentException("API key not provided");
        }

        if (config.getTraceIdGenerator() == null) {
            throw new IllegalArgumentException("Incompatible trace ID generator");
        }

        this.traceIdGenerator = config.getTraceIdGenerator();

        ApiServiceConfig apiServiceConfig = new ApiServiceConfig();
        apiServiceConfig.setApiKey(config.getApiKey());
        this.apiService.init(apiServiceConfig);
    }

    /**
     * Start a session
     * @param type the session type
     * @param sessionPayload the session payload
     * @return CompletableFuture that completes when the session starts
     */
    public static CompletableFuture<Void> start(SessionType type, Session sessionPayload) {
        if (instance == null || !instance.isInitialized) {
            throw new IllegalStateException("SessionRecorder is not initialized. Call init() first.");
        }
        return instance._start(type, sessionPayload);
    }

    private CompletableFuture<Void> _start(SessionType type, Session sessionPayload) {
        if (!isInitialized)
            throw new IllegalStateException("Call init() before performing any actions.");

        if (sessionPayload != null && sessionPayload.getShortId() != null &&
                sessionPayload.getShortId().length() != MULTIPLAYER_TRACE_DEBUG_SESSION_SHORT_ID_LENGTH) {
            throw new IllegalArgumentException("Invalid short session ID");
        }

        if (sessionPayload == null) {
            sessionPayload = new Session();
        }

        if (sessionState != SessionState.STOPPED)
            throw new IllegalStateException("Session should be ended before starting new one.");

        this.sessionType = type;

        if (sessionPayload.getName() == null) {
            sessionPayload.setName("Session on " + DateUtils.getFormattedDate(System.currentTimeMillis()));
        }

        Map<String, Object> mergedAttrs = new HashMap<>(resourceAttributes);
        if (sessionPayload.getResourceAttributes() != null) {
            mergedAttrs.putAll(sessionPayload.getResourceAttributes());
        }
        sessionPayload.setResourceAttributes(mergedAttrs);

        if (type == SessionType.CONTINUOUS) {
            // No session returned, so just mark as started without shortSessionId
            return apiService.startContinuousSession(sessionPayload)
                .thenRun(() -> {
                    this.shortSessionId = ""; // Or generate if you have a local generator
                    this.traceIdGenerator.setSessionId(this.shortSessionId, this.sessionType);
                    this.sessionState = SessionState.STARTED;
                });
        } else {
            // Normal session returns Session object with shortId
            return apiService.startSession(sessionPayload)
                .thenAccept(session -> {
                    this.shortSessionId = session.getShortId();
                    this.traceIdGenerator.setSessionId(session.getShortId(), this.sessionType);
                    this.sessionState = SessionState.STARTED;
                });
        }
    }

    public static void save(String reason) {
        SaveContinuousSession.saveContinuousSession(reason);
    }

    /**
     * Save session data
     * @param sessionData the session data to save
     * @return CompletableFuture that completes when the data is saved
     */
    public static CompletableFuture<Void> save(Session sessionData) {
        if (instance == null || !instance.isInitialized) {
            throw new IllegalStateException("SessionRecorder is not initialized. Call init() first.");
        }
        return instance._save(sessionData);
    }

    /**
     * Stop a session
     * @param sessionData the session data
     * @return CompletableFuture that completes when the session stops
     */
    public static CompletableFuture<Void> stop(Session sessionData) {
        if (instance == null || !instance.isInitialized) {
            throw new IllegalStateException("SessionRecorder is not initialized. Call init() first.");
        }
        return instance._stop(sessionData);
    }

    /**
     * Cancel a session
     * @return CompletableFuture that completes when the session is cancelled
     */
    public static CompletableFuture<Void> cancel() {
        if (instance == null || !instance.isInitialized) {
            throw new IllegalStateException("SessionRecorder is not initialized. Call init() first.");
        }
        return instance._cancel();
    }

    /**
     * Check remote continuous session
     * @param sessionPayload the session payload
     * @return CompletableFuture that completes when the check is done
     */
    public static CompletableFuture<Void> checkRemoteContinuousSession(Session sessionPayload) {
        if (instance == null || !instance.isInitialized) {
            throw new IllegalStateException("SessionRecorder is not initialized. Call init() first.");
        }
        return instance._checkRemoteContinuousSession(sessionPayload);
    }

    private CompletableFuture<Void> _save(Session sessionData) {
        if (!isInitialized)
            throw new IllegalStateException("Call init() before performing any actions.");

        if (sessionState == SessionState.STOPPED || this.shortSessionId.isEmpty())
            throw new IllegalStateException("Session should be active or paused.");

        if (sessionType != SessionType.CONTINUOUS)
            throw new IllegalStateException("Invalid session type");

        if (sessionData == null) {
            sessionData = new Session();
        }

        if (sessionData.getName() == null) {
            sessionData.setName("Session on " + DateUtils.getFormattedDate(System.currentTimeMillis()));
        }

        return apiService.saveContinuousSession(this.shortSessionId, sessionData);
    }

    private CompletableFuture<Void> _stop(Session sessionData) {
        if (!isInitialized)
            throw new IllegalStateException("Call init() before performing any actions.");

        if (sessionState == SessionState.STOPPED || shortSessionId.isEmpty())
            throw new IllegalStateException("Session should be active or paused.");

        if (sessionType != SessionType.PLAIN)
            throw new IllegalStateException("Invalid session type");

        StopSessionRequest stopRequest = new StopSessionRequest();
        if (sessionData != null) {
            stopRequest.setSessionAttributes(sessionData.getSessionAttributes());
        }

        return apiService.stopSession(shortSessionId, stopRequest)
            .thenRun(() -> {
                traceIdGenerator.setSessionId("", SessionType.PLAIN);
                shortSessionId = "";
                sessionState = SessionState.STOPPED;
            });
    }

    private CompletableFuture<Void> _cancel() {
        if (!isInitialized)
            throw new IllegalStateException("Call init() before performing any actions.");

        if (sessionState == SessionState.STOPPED || shortSessionId.isEmpty())
            throw new IllegalStateException("Session should be active or paused.");

        CompletableFuture<Void> future;
        if (sessionType == SessionType.CONTINUOUS) {
            future = apiService.stopContinuousSession(shortSessionId);
        } else {
            future = apiService.cancelSession(shortSessionId);
        }

        return future.thenRun(() -> {
            traceIdGenerator.setSessionId("", SessionType.PLAIN);
            shortSessionId = "";
            sessionState = SessionState.STOPPED;
        });
    }

    private CompletableFuture<Void> _checkRemoteContinuousSession(Session sessionPayload) {
        if (!isInitialized)
            throw new IllegalStateException("Call init() before performing any actions.");

        if (sessionPayload == null) {
            sessionPayload = new Session();
        }

        Map<String, Object> mergedAttrs = new HashMap<>(sessionPayload.getResourceAttributes() != null
            ? sessionPayload.getResourceAttributes()
            : Map.of());

        mergedAttrs.putAll(resourceAttributes);
        sessionPayload.setResourceAttributes(mergedAttrs);

         final Session finalSessionPayload = sessionPayload;

        return apiService.checkRemoteSession(sessionPayload)
            .thenCompose(response -> {
                String state = response.get("state");
                if ("START".equals(state) && sessionState != SessionState.STARTED) {
                    return this.start(SessionType.CONTINUOUS, finalSessionPayload);
                } else if ("STOP".equals(state) && sessionState != SessionState.STOPPED) {
                    return this.stop(null);
                }
                return CompletableFuture.completedFuture(null);
            });
    }

    enum SessionState {
        STARTED, STOPPED, PAUSED
    }
}
