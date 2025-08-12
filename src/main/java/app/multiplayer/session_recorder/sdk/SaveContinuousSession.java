package app.multiplayer.session_recorder.sdk;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.ATTR_MULTIPLAYER_CONTINUOUS_SESSION_AUTO_SAVE;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.ATTR_MULTIPLAYER_CONTINUOUS_SESSION_AUTO_SAVE_REASON;

public class SaveContinuousSession {

    /**
     * Set auto-save attribute and optional reason event on the current active span.
     *
     * @param reason Optional reason for auto-saving
     */
    public static void saveContinuousSession(String reason) {
        Span span = Span.fromContext(Context.current());

        if (!span.getSpanContext().isValid()) {
            return;
            // Optionally: create a new span if none is active
        }

        span.setAttribute(ATTR_MULTIPLAYER_CONTINUOUS_SESSION_AUTO_SAVE, true);

        if (reason != null && !reason.isEmpty()) {
            span.addEvent(
                ATTR_MULTIPLAYER_CONTINUOUS_SESSION_AUTO_SAVE_REASON,
                Attributes.of(io.opentelemetry.api.common.AttributeKey.stringKey("reason"), reason)
            );
        }
    }
}
