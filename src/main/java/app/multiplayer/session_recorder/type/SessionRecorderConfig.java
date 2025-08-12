package app.multiplayer.session_recorder.type;

import app.multiplayer.session_recorder.trace.SessionRecorderRandomIdGenerator;
import java.util.Map;
import java.util.function.Supplier;

public class SessionRecorderConfig {

    private String apiKey;
    private SessionRecorderRandomIdGenerator traceIdGenerator;
    private Map<String, Object> resourceAttributes;
    // private Boolean generateSessionShortIdLocallyFlag;
    // private Supplier<String> generateSessionShortIdLocallySupplier;

    // Getters and setters

    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public SessionRecorderRandomIdGenerator getTraceIdGenerator() {
        return traceIdGenerator;
    }

    public void setTraceIdGenerator(SessionRecorderRandomIdGenerator traceIdGenerator) {
        this.traceIdGenerator = traceIdGenerator;
    }

    public Map<String, Object> getResourceAttributes() {
        return resourceAttributes;
    }
    public void setResourceAttributes(Map<String, Object> resourceAttributes) {
        this.resourceAttributes = resourceAttributes;
    }

    // This handles the optional boolean or function (() => string) from TS
    // public Boolean getGenerateSessionShortIdLocallyFlag() {
    //     return generateSessionShortIdLocallyFlag;
    // }
    // public void setGenerateSessionShortIdLocallyFlag(Boolean flag) {
    //     this.generateSessionShortIdLocallyFlag = flag;
    // }

    // public Supplier<String> getGenerateSessionShortIdLocallySupplier() {
    //     return generateSessionShortIdLocallySupplier;
    // }
    // public void setGenerateSessionShortIdLocallySupplier(Supplier<String> supplier) {
    //     this.generateSessionShortIdLocallySupplier = supplier;
    // }

    /**
     * Helper method to get the session short ID generator string.
     * If a Supplier is provided, it uses that; otherwise, if the boolean flag is true, 
     * you can implement fallback logic here.
     */
    // public String generateSessionShortIdLocally() {
    //     if (generateSessionShortIdLocallySupplier != null) {
    //         return generateSessionShortIdLocallySupplier.get();
    //     } else if (Boolean.TRUE.equals(generateSessionShortIdLocallyFlag)) {
    //         // fallback default implementation if needed
    //         return "local-" + System.currentTimeMillis();
    //     }
    //     return null;
    // }
}
