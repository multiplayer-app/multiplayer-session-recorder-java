package app.multiplayer.session_recorder.exporter;

import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;

import java.util.Collection;
import java.util.stream.Collectors;

import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_DEBUG_PREFIX;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX;

public class SessionRecorderTraceExporterWrapper implements SpanExporter {
    private final SpanExporter delegate;

    public SessionRecorderTraceExporterWrapper(SpanExporter spanExporter) {
        this.delegate = spanExporter;
    }

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        // Filter spans to only include those with trace IDs starting with the required prefixes
        Collection<SpanData> filteredSpans = spans.stream()
            .filter(span -> {
                String traceId = span.getTraceId();
                return !traceId.startsWith(MULTIPLAYER_TRACE_DEBUG_PREFIX) &&
                       !traceId.startsWith(MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX);
            })
            .collect(Collectors.toList());

        // If no spans match the filter, return a successful result without calling the delegate
        if (filteredSpans.isEmpty()) {
            return CompletableResultCode.ofSuccess();
        }

        return delegate.export(filteredSpans);
    }

    @Override
    public CompletableResultCode flush() {
        return delegate.flush();
    }

    @Override
    public CompletableResultCode shutdown() {
        return delegate.shutdown();
    }
}
