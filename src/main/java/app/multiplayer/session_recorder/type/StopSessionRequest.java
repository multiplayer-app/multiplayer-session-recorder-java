package app.multiplayer.session_recorder.type;

import java.util.List;
import java.util.Map;

public class StopSessionRequest {
    private SessionAttributes sessionAttributes;

    // Getter and setter

    public static class SessionAttributes {
        private String email;
        private String comment;

        // Getters and setters
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getComment() {
            return comment;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public SessionAttributes getSessionAttributes() {
        return sessionAttributes;
    }
    public void setSessionAttributes(SessionAttributes sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }
}
