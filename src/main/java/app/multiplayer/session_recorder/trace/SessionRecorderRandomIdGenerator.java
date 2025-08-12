package app.multiplayer.session_recorder.trace;

import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.TraceId;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.internal.RandomSupplier;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import java.util.Random;
import java.util.function.Supplier;
import app.multiplayer.session_recorder.trace.samplers.SessionRecorderTraceIdRatioBasedSampler;
import app.multiplayer.session_recorder.type.SessionType;

import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_DEBUG_PREFIX;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX;

public class SessionRecorderRandomIdGenerator implements IdGenerator {
  private static final SamplingResult POSITIVE_SAMPLING_RESULT = SamplingResult.recordAndSample();

  private static final long INVALID_ID = 0;
  private static final Supplier<Random> randomSupplier = RandomSupplier.platformDefault();

  private String sessionShortId = "";
  private SessionType sessionType;

  public SessionRecorderRandomIdGenerator() {}

  public String generateSpanId() {
    long id;
    Random random = randomSupplier.get();
    do {
      id = random.nextLong();
    } while (id == INVALID_ID);
    return SpanId.fromLong(id);
  }

  public String generateTraceId() {
    Random random = randomSupplier.get();
    long idHi = random.nextLong();
    long idLo;
    do {
      idLo = random.nextLong();
    } while (idLo == INVALID_ID);

    String _traceId = TraceId.fromLongs(idHi, idLo);


    if (this.sessionShortId != null && !this.sessionShortId.isEmpty()) {
      String prefix;
      switch (this.sessionType) {
          case CONTINUOUS:
            prefix = MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX + this.sessionShortId;
            break;
          default:
            prefix = MULTIPLAYER_TRACE_DEBUG_PREFIX + this.sessionShortId;
            break;
      }

      return prefix + _traceId.substring(prefix.length());
    }

    return _traceId;
  }

  public void setSessionId(String sessionShortId, SessionType sessionType) {
      this.sessionShortId = sessionShortId;
      this.sessionType = sessionType != null ? sessionType : SessionType.PLAIN;
  }

  public String toString() {
    return "SessionRecorderRandomIdGenerator{}";
  }
}
