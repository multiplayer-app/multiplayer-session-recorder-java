package app.multiplayer.session_recorder.type;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Session {
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String shortId;
    private String name;

    private Map<String, Object> resourceAttributes = new HashMap<>();
    private Map<String, Object> sessionAttributes = new HashMap<>();

    private List<Tag> tags = new ArrayList<>();

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
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

    /**
     * Add a tag to the session
     * @param key the tag key
     * @param value the tag value
     */
    public void addTag(String key, String value) {
        this.tags.add(new Tag(key, value));
    }

    /**
     * Add a tag to the session
     * @param tag the tag object to add
     */
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    /**
     * Add a session attribute
     * @param key the attribute key
     * @param value the attribute value
     */
    public void addSessionAttribute(String key, Object value) {
        this.sessionAttributes.put(key, value);
    }

    /**
     * Get a session attribute
     * @param key the attribute key
     * @return the attribute value
     */
    public Object getSessionAttribute(String key) {
        return this.sessionAttributes.get(key);
    }

    /**
     * Add a resource attribute
     * @param key the attribute key
     * @param value the attribute value
     */
    public void addResourceAttribute(String key, Object value) {
        this.resourceAttributes.put(key, value);
    }

    /**
     * Get a resource attribute
     * @param key the attribute key
     * @return the attribute value
     */
    public Object getResourceAttribute(String key) {
        return this.resourceAttributes.get(key);
    }

    // --- Inner class for Tag ---

    public static class Tag {
        private String key;
        private String value;

        public Tag() {}

        public Tag(String key, String value) {
            this.key = key;
            this.value = value;
        }

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
}
