package app.multiplayer.session_recorder.type;

import java.util.List;
import java.util.Map;

public class SessionResponse {
    private String _id;
    private String shortId;
    private String name;
    private Map<String, Object> resourceAttributes;
    private Map<String, Object> sessionAttributes;
    private List<Session.Tag> tags;

    // Getters and setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public List<Session.Tag> getTags() {
        return tags;
    }

    public void setTags(List<Session.Tag> tags) {
        this.tags = tags;
    }
}
