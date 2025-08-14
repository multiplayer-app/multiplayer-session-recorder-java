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

    private List<String> tags = new ArrayList<>();

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Add a tag to the session
     * @param tag the tag string to add
     */
    public void addTag(String tag) {
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


}
