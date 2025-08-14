package app.multiplayer.session_recorder.type;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class StopSessionRequest {
    private Map<String, Object> sessionAttributes = new HashMap<>();

    // Getters and setters
    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    /**
     * Add a session attribute
     * @param key the attribute key
     * @param value the attribute value
     */
    public void addSessionAttribute(String key, Object value) {
        this.sessionAttributes.put(key, value);
    }


}
