package org.openguardrail.errorframework;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Standardized error response body for REST APIs.
 *
 * <p>This class is immutable, serializable, and thread-safe.
 */
public final class ErrorResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final int status;
    private final boolean recoverable;
    private final Instant timestamp;

    private ErrorResponse(String code, String message, int status, boolean recoverable, Instant timestamp)
    {
        this.code = code;
        this.message = message;
        this.status = status;
        this.recoverable = recoverable;
        this.timestamp = timestamp;
    }

    /**
     * Create an ErrorResponse from a ServiceError.
     *
     * @param error the error definition
     * @return a new ErrorResponse
     * @throws NullPointerException if error is null
     */
    public static ErrorResponse from(ServiceError error)
    {
        Objects.requireNonNull(error, "ServiceError must not be null");
        return new ErrorResponse(
                error.getExternalErrorCode(),
                error.getErrorMessage(),
                error.getHttpStatus(),
                error.isRecoverable(),
                Instant.now()
        );
    }

    /**
     * Create an ErrorResponse from a ServiceException.
     *
     * @param ex the exception
     * @return a new ErrorResponse
     * @throws NullPointerException if ex is null
     */
    public static ErrorResponse from(ServiceException ex)
    {
        Objects.requireNonNull(ex, "ServiceException must not be null");
        return new ErrorResponse(
                ex.getExternalErrorCode(),
                ex.getMessage(),
                ex.getHttpStatus(),
                ex.isRecoverable(),
                Instant.now()
        );
    }

    /**
     * Create an ErrorResponse with custom values.
     *
     * @param code    external error code
     * @param message human-readable message
     * @param status  HTTP status code
     * @return a new ErrorResponse
     * @throws NullPointerException if code or message is null
     */
    public static ErrorResponse of(String code, String message, int status)
    {
        Objects.requireNonNull(code, "Code must not be null");
        Objects.requireNonNull(message, "Message must not be null");
        return new ErrorResponse(code, message, status, false, Instant.now());
    }

    public String getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    public int getStatus()
    {
        return status;
    }

    public boolean isRecoverable()
    {
        return recoverable;
    }

    public Instant getTimestamp()
    {
        return timestamp;
    }

    /**
     * Returns this response as an unmodifiable map for framework-agnostic serialization.
     * Compatible with any JSON library (Jackson, Gson, etc.) via map serialization.
     *
     * @return map with keys: code, message, status, recoverable, timestamp
     */
    public Map<String, Object> toMap()
    {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("message", message);
        map.put("status", status);
        map.put("recoverable", recoverable);
        map.put("timestamp", timestamp.toString());
        return Collections.unmodifiableMap(map);
    }

    @Override
    public String toString()
    {
        return String.format("ErrorResponse{code='%s', message='%s', status=%d, recoverable=%s, timestamp=%s}", code, message, status, recoverable, timestamp);
    }

    /**
     * Two ErrorResponses are equal if they have the same code, message, status, and recoverability.
     * Timestamp is excluded from equality to allow comparison of semantically identical errors
     * that occurred at different times.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        ErrorResponse other = (ErrorResponse) obj;
        return status == other.status && recoverable == other.recoverable && Objects.equals(code, other.code) && Objects.equals(message, other.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(code, message, status, recoverable);
    }
}
