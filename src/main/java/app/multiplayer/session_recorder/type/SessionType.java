package app.multiplayer.session_recorder.type;

public enum SessionType {
    CONTINUOUS("CONTINUOUS"),
    PLAIN("MANUAL");

    private final String value;

    SessionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
