package app.multiplayer.session_recorder.sdk;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;

public class CaptureException {

    /**
     * Add error to current active span.
     *
     * @param error Exception to record
     */
    public static void captureException(Throwable error) {
        if (error == null) return;

        Span span = Span.fromContext(Context.current());
        if (!span.getSpanContext().isValid()) return;

        span.recordException(error);
        span.setStatus(StatusCode.ERROR, error.getMessage());
    }
}
