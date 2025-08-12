package app.multiplayer.session_recorder.exporter;

import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import io.opentelemetry.sdk.common.CompletableResultCode;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_OTEL_DEFAULT_LOGS_HTTP_EXPORTER_URL;

public class SessionRecorderOtlpHttpLogExporter implements LogRecordExporter {
    private OtlpHttpLogRecordExporter _otlpHttpLogRecordExporter;

    private static final String USER_AGENT = "Session-Recorder-OTel-OTLP-Exporter-Java-LOGS";

    public SessionRecorderOtlpHttpLogExporter(String apiKey) {
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpHttpLogRecordExporter = OtlpHttpLogRecordExporter.builder()
            .setEndpoint(MULTIPLAYER_OTEL_DEFAULT_LOGS_HTTP_EXPORTER_URL)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public SessionRecorderOtlpHttpLogExporter(String apiKey, String endpoint) {
        String _endpoint = (endpoint == null || endpoint.isEmpty())
            ? MULTIPLAYER_OTEL_DEFAULT_LOGS_HTTP_EXPORTER_URL
            : endpoint;
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpHttpLogRecordExporter = OtlpHttpLogRecordExporter.builder()
            .setEndpoint(_endpoint)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public CompletableResultCode export(Collection<LogRecordData> logs) {
        return this._otlpHttpLogRecordExporter.export(logs);
    }

    public CompletableResultCode flush() {
        return this._otlpHttpLogRecordExporter.flush();
    }

    public CompletableResultCode shutdown() {
        return this._otlpHttpLogRecordExporter.shutdown();
    }
}
