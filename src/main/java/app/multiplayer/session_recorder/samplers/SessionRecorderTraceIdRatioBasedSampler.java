package app.multiplayer.session_recorder.trace.samplers;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.internal.OtelEncodingUtils;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_DEBUG_PREFIX;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX;

public class SessionRecorderTraceIdRatioBasedSampler implements Sampler {

  private static final SamplingResult POSITIVE_SAMPLING_RESULT = SamplingResult.recordAndSample();

  private static final SamplingResult NEGATIVE_SAMPLING_RESULT = SamplingResult.drop();

  private final long idUpperBound;
  private final String description;

  public static SessionRecorderTraceIdRatioBasedSampler create(double ratio) {
    if (ratio < 0.0 || ratio > 1.0) {
      throw new IllegalArgumentException("ratio must be in range [0.0, 1.0]");
    }
    long idUpperBound;

    if (ratio == 0.0) {
      idUpperBound = Long.MIN_VALUE;
    } else if (ratio == 1.0) {
      idUpperBound = Long.MAX_VALUE;
    } else {
      idUpperBound = (long) (ratio * Long.MAX_VALUE);
    }
    return new SessionRecorderTraceIdRatioBasedSampler(ratio, idUpperBound);
  }

  SessionRecorderTraceIdRatioBasedSampler(double ratio, long idUpperBound) {
    this.idUpperBound = idUpperBound;
    description = "MultiplayerTraceIdRatioBased{" + decimalFormat(ratio) + "}";
  }

  public SamplingResult shouldSample(
      @Nullable Context parentContext,
      String traceId,
      @Nullable String name,
      @Nullable SpanKind spanKind,
      @Nullable Attributes attributes,
      @Nullable List<LinkData> parentLinks
  ) {

    if (
      traceId.startsWith(MULTIPLAYER_TRACE_CONTINUOUS_DEBUG_PREFIX)
      || traceId.startsWith(MULTIPLAYER_TRACE_DEBUG_PREFIX)
    ) {
      return POSITIVE_SAMPLING_RESULT;
    }

    return Math.abs(getTraceIdRandomPart(traceId)) < idUpperBound
      ? POSITIVE_SAMPLING_RESULT
      : NEGATIVE_SAMPLING_RESULT;
  }

  public String getDescription() {
    return description;
  }

  public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof SessionRecorderTraceIdRatioBasedSampler)) {
      return false;
    }
    SessionRecorderTraceIdRatioBasedSampler that = (SessionRecorderTraceIdRatioBasedSampler) obj;
    return idUpperBound == that.idUpperBound;
  }

  public int hashCode() {
    return Long.hashCode(idUpperBound);
  }

  public String toString() {
    return getDescription();
  }

  long getIdUpperBound() {
    return idUpperBound;
  }

  private static long getTraceIdRandomPart(String traceId) {
    return OtelEncodingUtils.longFromBase16String(traceId, 16);
  }

  private static String decimalFormat(double value) {
    DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ROOT);
    decimalFormatSymbols.setDecimalSeparator('.');

    DecimalFormat decimalFormat = new DecimalFormat("0.000000", decimalFormatSymbols);
    return decimalFormat.format(value);
  }
}
