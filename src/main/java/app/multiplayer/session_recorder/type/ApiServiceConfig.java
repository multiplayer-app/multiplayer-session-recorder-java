package app.multiplayer.session_recorder.type;

import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;
import java.util.Map;
import java.util.function.Supplier;

public class ApiServiceConfig {
    private String apiKey;
    private SessionRecorderRandomIdGenerator traceIdGenerator;
    private Object resourceAttributes;
    private Object generateSessionShortIdLocally; // Can be Boolean or Supplier<String>
    private String exporterApiBaseUrl;

    public ApiServiceConfig() {
    }

    public ApiServiceConfig(String apiKey,
                            SessionRecorderRandomIdGenerator traceIdGenerator,
                            Object resourceAttributes,
                            Object generateSessionShortIdLocally,
                            String exporterApiBaseUrl) {
        this.apiKey = apiKey;
        this.traceIdGenerator = traceIdGenerator;
        this.resourceAttributes = resourceAttributes;
        this.generateSessionShortIdLocally = generateSessionShortIdLocally;
        this.exporterApiBaseUrl = exporterApiBaseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public SessionRecorderRandomIdGenerator getTraceIdGenerator() {
        return traceIdGenerator;
    }

    public Object getResourceAttributes() {
        return resourceAttributes;
    }

    public Object getGenerateSessionShortIdLocally() {
        return generateSessionShortIdLocally;
    }

    public String getExporterApiBaseUrl() {
        return exporterApiBaseUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setTraceIdGenerator(SessionRecorderRandomIdGenerator traceIdGenerator) {
        this.traceIdGenerator = traceIdGenerator;
    }

    public void setResourceAttributes(Object resourceAttributes) {
        this.resourceAttributes = resourceAttributes;
    }

    public void setGenerateSessionShortIdLocally(Object generateSessionShortIdLocally) {
        this.generateSessionShortIdLocally = generateSessionShortIdLocally;
    }

    public void setExporterApiBaseUrl(String exporterApiBaseUrl) {
        this.exporterApiBaseUrl = exporterApiBaseUrl;
    }

    public ApiServiceConfig merge(ApiServiceConfig other) {
        return new ApiServiceConfig(
            other.apiKey != null ? other.apiKey : this.apiKey,
            other.traceIdGenerator != null ? other.traceIdGenerator : this.traceIdGenerator,
            other.resourceAttributes != null ? other.resourceAttributes : this.resourceAttributes,
            other.generateSessionShortIdLocally != null ? other.generateSessionShortIdLocally : this.generateSessionShortIdLocally,
            other.exporterApiBaseUrl != null ? other.exporterApiBaseUrl : this.exporterApiBaseUrl
        );
    }
}