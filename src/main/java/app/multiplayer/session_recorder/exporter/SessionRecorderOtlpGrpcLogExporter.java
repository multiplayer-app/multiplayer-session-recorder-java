package app.multiplayer.session_recorder.exporter;

import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import io.opentelemetry.sdk.common.CompletableResultCode;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.StringJoiner;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_OTEL_DEFAULT_LOGS_GRPC_EXPORTER_URL;

public class SessionRecorderOtlpGrpcLogExporter implements LogRecordExporter {
    private OtlpGrpcLogRecordExporter _otlpGrpcLogRecordExporter;

    private static final String USER_AGENT = "Session-Recorder-OTel-GRPC-Exporter-Java-LOGS";

    public SessionRecorderOtlpGrpcLogExporter(String apiKey) {
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpGrpcLogRecordExporter = OtlpGrpcLogRecordExporter.builder()
            .setEndpoint(MULTIPLAYER_OTEL_DEFAULT_LOGS_GRPC_EXPORTER_URL)
            .addHeader("User-Agent", "value1")
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public SessionRecorderOtlpGrpcLogExporter(String apiKey, String endpoint) {
        String _endpoint = (endpoint == null || endpoint.isEmpty())
            ? MULTIPLAYER_OTEL_DEFAULT_LOGS_GRPC_EXPORTER_URL
            : endpoint;
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpGrpcLogRecordExporter = OtlpGrpcLogRecordExporter.builder()
            .setEndpoint(_endpoint)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public CompletableResultCode export(Collection<LogRecordData> logs) {
        return this._otlpGrpcLogRecordExporter.export(logs);
    }

    public CompletableResultCode flush() {
        return this._otlpGrpcLogRecordExporter.flush();
    }

    public CompletableResultCode shutdown() {
        return this._otlpGrpcLogRecordExporter.shutdown();
    }
}
