package app.multiplayer.session_recorder.exporter;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_OTEL_DEFAULT_TRACES_HTTP_EXPORTER_URL;

public class SessionRecorderOtlpHttpSpanExporter implements SpanExporter {
    private OtlpHttpSpanExporter _otlpHttpSpanExporter;

    private static final String USER_AGENT = "Session-Recorder-OTel-OTLP-Exporter-Java-SPANS";

    public SessionRecorderOtlpHttpSpanExporter(String apiKey) {
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpHttpSpanExporter = OtlpHttpSpanExporter.builder()
            .setEndpoint(MULTIPLAYER_OTEL_DEFAULT_TRACES_HTTP_EXPORTER_URL)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public SessionRecorderOtlpHttpSpanExporter(String apiKey, String endpoint) {
        String _endpoint = (endpoint == null || endpoint.isEmpty())
          ? MULTIPLAYER_OTEL_DEFAULT_TRACES_HTTP_EXPORTER_URL
          : endpoint;
        String _apiKey = (apiKey == null) ? "" : apiKey;

        this._otlpHttpSpanExporter = OtlpHttpSpanExporter.builder()
            .setEndpoint(_endpoint)
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Authorization", _apiKey)
            .build();
    }

    public CompletableResultCode export(Collection<SpanData> spans) {
        return this._otlpHttpSpanExporter.export(spans);
    }

    public CompletableResultCode flush() {
        return this._otlpHttpSpanExporter.flush();
    }

    public CompletableResultCode shutdown() {
        return this._otlpHttpSpanExporter.shutdown();
    }
}
