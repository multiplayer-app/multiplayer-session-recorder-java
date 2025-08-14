package app.multiplayer.session_recorder.services;

import app.multiplayer.session_recorder.type.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_BASE_API_URL;

public class ApiService {

    private ApiServiceConfig config = new ApiServiceConfig();

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String getBaseApiUrl() {
        return this.config.getExporterApiBaseUrl() != null
            ? this.config.getExporterApiBaseUrl()
            : MULTIPLAYER_BASE_API_URL;
    }

    public void init(ApiServiceConfig config) {
        this.config = this.config.merge(config);
    }

    public void updateConfigs(ApiServiceConfig partial) {
        this.config = this.config.merge(partial);
    }

    // Async startSession
    public CompletableFuture<Session> startSession(Session requestBody) {
        return makeRequestAsync("/debug-sessions/start", "POST", requestBody, SessionResponse.class)
            .thenApply(response -> {
                Session session = new Session();
                session.setShortId(response.getShortId());
                session.setName(response.getName());
                session.setResourceAttributes(response.getResourceAttributes());
                session.setSessionAttributes(response.getSessionAttributes());
                session.setTags(response.getTags());
                return session;
            });
    }

    // Async stopSession
    public CompletableFuture<Void> stopSession(String sessionId, StopSessionRequest requestBody) {
        return makeRequestAsync("/debug-sessions/" + sessionId + "/stop", "PATCH", requestBody, Void.class);
    }

    // Async cancelSession
    public CompletableFuture<Void> cancelSession(String sessionId) {
        return makeRequestAsync("/debug-sessions/" + sessionId + "/cancel", "DELETE", null, Void.class);
    }

    public CompletableFuture<Void> startContinuousSession(Session requestBody) {
        return makeRequestAsync("/continuous-debug-sessions/start", "POST", requestBody, Void.class);
    }

    public CompletableFuture<Void> saveContinuousSession(String sessionId, Session requestBody) {
        return makeRequestAsync("/continuous-debug-sessions/" + sessionId + "/save", "POST", requestBody, Void.class);
    }

    public CompletableFuture<Void> stopContinuousSession(String sessionId) {
        return makeRequestAsync("/continuous-debug-sessions/" + sessionId + "/cancel", "DELETE", null, Void.class);
    }

    public CompletableFuture<Map<String, String>> checkRemoteSession(Session requestBody) {
        return makeRequestAsync("/remote-debug-session/check", "POST", requestBody, new TypeReference<Map<String, String>>() {});
    }

    // Overload for Class<T> deserialization
    private <T> CompletableFuture<T> makeRequestAsync(String path, String method, Object body, Class<T> responseType) {
        return makeRequestAsync(path, method, body, responseType, null);
    }

    // Overload for TypeReference<T> deserialization
    private <T> CompletableFuture<T> makeRequestAsync(String path, String method, Object body, TypeReference<T> responseType) {
        return makeRequestAsync(path, method, body, null, responseType);
    }

    // Core async request method
    private <T> CompletableFuture<T> makeRequestAsync(
        String path,
        String method,
        Object body,
        Class<T> responseType,
        TypeReference<T> typeReference
    ) {
        String url = getBaseApiUrl() + "/v0/radar" + path;

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "application/json")
            .method(method, body != null
                ? HttpRequest.BodyPublishers.ofString(serialize(body))
                : HttpRequest.BodyPublishers.noBody());

        String apiKey = config.getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.header("X-Api-Key", apiKey);
        }

        HttpRequest request = requestBuilder.build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> {
                int status = response.statusCode();
                String responseBody = response.body();

                if (status == 204) {
                    return null;
                }

                if (status < 200 || status >= 300) {
                    throw new RuntimeException("HTTP error: " + status + " " + responseBody);
                }

                try {
                    if (responseType == Void.class || (responseType == null && typeReference == null)) {
                        return null;
                    } else if (responseType != null) {
                        return objectMapper.readValue(responseBody, responseType);
                    } else if (typeReference != null) {
                        return objectMapper.readValue(responseBody, typeReference);
                    } else {
                        // Should never reach here
                        throw new RuntimeException("No response type provided for deserialization");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse response JSON", e);
                }
            });
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
    }
}
