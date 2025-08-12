package app.multiplayer.session_recorder.sdk;

import com.google.gson.*;
import io.opentelemetry.api.trace.Span;
import static app.multiplayer.session_recorder.internal.SessionRecorderConstants.MASK_PLACEHOLDER;
import java.util.*;

public class MaskingUtils {
    private static final int MAX_DEPTH = 8;

    public static final Set<String> sensitiveFields = new HashSet<>(Arrays.asList(
        "password", "pass", "passwd", "pwd", "token", "access_token", "accessToken", "refresh_token",
        "refreshToken", "secret", "api_key", "apiKey", "authorization", "auth_token", "authToken", "jwt",
        "session_id", "sessionId", "sessionToken", "client_secret", "clientSecret", "private_key",
        "privateKey", "public_key", "publicKey", "key", "encryption_key", "encryptionKey", "credit_card",
        "creditCard", "card_number", "cardNumber", "cvv", "cvc", "ssn", "sin", "pin", "security_code",
        "securityCode", "bank_account", "bankAccount", "iban", "swift", "bic", "routing_number",
        "routingNumber", "license_key", "licenseKey", "otp", "mfa_code", "mfaCode", "phone_number",
        "phoneNumber", "email", "address", "dob", "tax_id", "taxId", "passport_number", "passportNumber",
        "driver_license", "driverLicense", "set-cookie", "cookie", "proxyAuthorization"
    ));

    public static final Set<String> sensitiveHeaders = new HashSet<>(Arrays.asList(
        "set-cookie", "cookie", "authorization", "proxyAuthorization"
    ));

    public static String mask(Object value, List<String> keysToMask, Span span) {
        JsonElement payloadJson;

        try {
            payloadJson = JsonParser.parseString(value.toString());
        } catch (JsonSyntaxException e) {
            return value.toString();
        }

        JsonElement masked;
        if (keysToMask != null && !keysToMask.isEmpty()) {
            masked = maskSelected(payloadJson, new HashSet<>(keysToMask));
        } else {
            masked = maskAll(payloadJson, 0);
        }

        return masked.isJsonPrimitive() && masked.getAsJsonPrimitive().isString()
                ? masked.getAsString()
                : masked.toString();
    }

    private static JsonElement maskAll(JsonElement element, int depth) {
        if (depth > MAX_DEPTH) return JsonNull.INSTANCE;

        if (element.isJsonArray()) {
            JsonArray array = new JsonArray();
            for (JsonElement item : element.getAsJsonArray()) {
                array.add(maskAll(item, depth + 1));
            }
            return array;
        }

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                obj.add(entry.getKey(), maskAll(entry.getValue(), depth + 1));
            }
            return obj;
        }

        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return new JsonPrimitive(MASK_PLACEHOLDER);
        }

        return element;
    }

    private static JsonElement maskSelected(JsonElement element, Set<String> keysToMask) {
        if (element.isJsonArray()) {
            JsonArray array = new JsonArray();
            for (JsonElement item : element.getAsJsonArray()) {
                array.add(maskSelected(item, keysToMask));
            }
            return array;
        }

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                String key = entry.getKey();
                JsonElement masked = keysToMask.contains(key)
                        ? new JsonPrimitive(MASK_PLACEHOLDER)
                        : maskSelected(entry.getValue(), keysToMask);
                obj.add(key, masked);
            }
            return obj;
        }

        return element;
    }
}
