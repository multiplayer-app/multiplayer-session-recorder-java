package app.multiplayer.session_recorder.internal;

public final class SessionRecorderConstants {

    private SessionRecorderConstants() {
        // restrict instantiation
    }

    public static final String MULTIPLAYER_TRACE_DEBUG_PREFIX = "debdeb";
    public static final String MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX = "cdbcdb";
    public static final String MULTIPLAYER_OTEL_DEFAULT_TRACES_HTTP_EXPORTER_URL = "https://api.multiplayer.app/v1/traces";
    public static final String MULTIPLAYER_OTEL_DEFAULT_LOGS_HTTP_EXPORTER_URL = "https://api.multiplayer.app/v1/logs";
    public static final String MULTIPLAYER_OTEL_DEFAULT_TRACES_GRPC_EXPORTER_URL = "https://api.multiplayer.app:4317/v1/traces";
    public static final String MULTIPLAYER_OTEL_DEFAULT_LOGS_GRPC_EXPORTER_URL = "https://api.multiplayer.app:4317/v1/logs";
    public static final String MULTIPLAYER_BASE_API_URL = "https://api.multiplayer.app";
    public static final String MULTIPLAYER_ATTRIBUTE_PREFIX = "multiplayer.";
    public static final String MASK_PLACEHOLDER = "***MASKED***";
    public static final int MULTIPLAYER_TRACE_DEBUG_SESSION_SHORT_ID_LENGTH = 16;
    public static final String SESSION_RECORDER_VERSION = "1.0.0";
    public static final String ATTR_MULTIPLAYER_WORKSPACE_ID = "multiplayer.workspace.id";
    public static final String ATTR_MULTIPLAYER_PROJECT_ID = "multiplayer.project.id";
    public static final String ATTR_MULTIPLAYER_PLATFORM_ID = "multiplayer.platform.id";
    public static final String ATTR_MULTIPLAYER_CONTINUOUS_SESSION_AUTO_SAVE = "multiplayer.session.auto-save";
    public static final String ATTR_MULTIPLAYER_CONTINUOUS_SESSION_AUTO_SAVE_REASON = "multiplayer.session.auto-save.reason";
    public static final String ATTR_MULTIPLAYER_PLATFORM_NAME = "multiplayer.platform.name";
    public static final String ATTR_MULTIPLAYER_CLIENT_ID = "multiplayer.client.id";
    public static final String ATTR_MULTIPLAYER_INTEGRATION_ID = "multiplayer.integration.id";
    public static final String ATTR_MULTIPLAYER_SESSION_ID = "multiplayer.session.id";
    public static final String ATTR_MULTIPLAYER_HTTP_REQUEST_BODY = "multiplayer.http.request.body";
    public static final String ATTR_MULTIPLAYER_HTTP_RESPONSE_BODY = "multiplayer.http.response.body";
    public static final String ATTR_MULTIPLAYER_HTTP_REQUEST_HEADERS = "multiplayer.http.request.headers";
    public static final String ATTR_MULTIPLAYER_HTTP_RESPONSE_HEADERS = "multiplayer.http.response.headers";
    public static final String ATTR_MULTIPLAYER_HTTP_RESPONSE_BODY_ENCODING = "multiplayer.http.response.body.encoding";
    public static final String ATTR_MULTIPLAYER_RPC_REQUEST_MESSAGE = "multiplayer.rpc.request.message";
    public static final String ATTR_MULTIPLAYER_RPC_REQUEST_MESSAGE_ENCODING = "multiplayer.rpc.request.message.encoding";
    public static final String ATTR_MULTIPLAYER_RPC_RESPONSE_MESSAGE = "multiplayer.rpc.response.message";
    public static final String ATTR_MULTIPLAYER_GRPC_REQUEST_MESSAGE = "multiplayer.rpc.grpc.request.message";
    public static final String ATTR_MULTIPLAYER_GRPC_REQUEST_MESSAGE_ENCODING = "multiplayer.rpc.request.message.encoding";
    public static final String ATTR_MULTIPLAYER_GRPC_RESPONSE_MESSAGE = "multiplayer.rpc.grpc.response.message";
    public static final String ATTR_MULTIPLAYER_MESSAGING_MESSAGE_BODY = "multiplayer.messaging.message.body";
    public static final String ATTR_MULTIPLAYER_MESSAGING_MESSAGE_BODY_ENCODING = "multiplayer.messaging.message.body.encoding";
    public static final String ATTR_MULTIPLAYER_SESSION_RECORDER_VERSION = "multiplayer.session-recorder.version";
}
