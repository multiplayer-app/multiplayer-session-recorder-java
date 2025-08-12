package app.multiplayer.session_recorder.type;

import java.util.List;
import java.util.Map;

public class StartSessionRequest {
    private String name;
    private Map<String, Object> resourceAttributes;
    private Map<String, Object> sessionAttributes;
    private List<Tag> tags;

    // Getters and setters

    public static class Tag {
        private String key;
        private String value;

        // Getters and setters

        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<String, Object> getResourceAttributes() {
        return resourceAttributes;
    }
    public void setResourceAttributes(Map<String, Object> resourceAttributes) {
        this.resourceAttributes = resourceAttributes;
    }
    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }
    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }
    public List<Tag> getTags() {
        return tags;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
