package app.multiplayer.session_recorder.exporter;

import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import io.opentelemetry.sdk.common.CompletableResultCode;

import java.util.Collection;
import java.util.stream.Collectors;

import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_DEBUG_PREFIX;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX;

/**
 * A wrapper for LogRecordExporter that filters out log records whose trace ID does not start with
 * MULTIPLAYER_TRACE_DEBUG_PREFIX or MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX.
 */
public class SessionRecorderLogsExporterWrapper implements LogRecordExporter {
    private final LogRecordExporter delegate;

    public SessionRecorderLogsExporterWrapper(LogRecordExporter logRecordExporter) {
        this.delegate = logRecordExporter;
    }

    @Override
    public CompletableResultCode export(Collection<LogRecordData> logs) {
        // Filter log records to only include those with trace IDs starting with the required prefixes
        Collection<LogRecordData> filteredLogs = logs.stream()
            .filter(log -> {
                String traceId = log.getSpanContext().getTraceId();
                return !traceId.startsWith(MULTIPLAYER_TRACE_DEBUG_PREFIX) &&
                       !traceId.startsWith(MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX);
            })
            .collect(Collectors.toList());

        // If no log records match the filter, return a successful result without calling the delegate
        if (filteredLogs.isEmpty()) {
            return CompletableResultCode.ofSuccess();
        }

        return delegate.export(filteredLogs);
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
