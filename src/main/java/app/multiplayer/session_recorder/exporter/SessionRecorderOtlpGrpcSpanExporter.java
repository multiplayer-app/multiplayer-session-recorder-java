package app.multiplayer.session_recorder.exporter;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_OTEL_DEFAULT_TRACES_GRPC_EXPORTER_URL;

public class SessionRecorderOtlpGrpcSpanExporter implements SpanExporter {
    private OtlpGrpcSpanExporter _otlpGrpcSpanExporter;

    private static final String USER_AGENT = "Session-Recorder-OTel-GRPC-Exporter-Java-SPANS";

    public SessionRecorderOtlpGrpcSpanExporter(String apiKey) {
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint(MULTIPLAYER_OTEL_DEFAULT_TRACES_GRPC_EXPORTER_URL)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public SessionRecorderOtlpGrpcSpanExporter(String apiKey, String endpoint) {
        String _endpoint = (endpoint == null || endpoint.isEmpty())
            ? MULTIPLAYER_OTEL_DEFAULT_TRACES_GRPC_EXPORTER_URL
            : endpoint;
        String _apiKey = (apiKey == null) ? "" : apiKey;

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        if (!_apiKey.isEmpty()) {
            headers.put("Authorization", _apiKey);
        }

        this._otlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint(_endpoint)
            .setHeaders(() -> headers)
            .build();
    }

    public CompletableResultCode export(Collection<SpanData> spans) {
        return this._otlpGrpcSpanExporter.export(spans);
    }

    public CompletableResultCode flush() {
        return this._otlpGrpcSpanExporter.flush();
    }

    public CompletableResultCode shutdown() {
        return this._otlpGrpcSpanExporter.shutdown();
    }
}