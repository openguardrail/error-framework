package io.openguardrail.errorframework;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single error in a service.
 *
 * <p>Each error has:
 * <ul>
 *   <li>A numeric code within a validated range</li>
 *   <li>An error group for categorization</li>
 *   <li>A human-readable message</li>
 *   <li>An external error code (e.g., "TELOS-100", "AGT-200")</li>
 *   <li>An HTTP status code for API responses (100-599)</li>
 *   <li>A recoverability flag for retry logic</li>
 * </ul>
 *
 * <p>This class is immutable and thread-safe.
 */
public final class ServiceError implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final int HTTP_STATUS_MIN = 100;
    private static final int HTTP_STATUS_MAX = 599;

    private static final String RANGE_ERROR_TEMPLATE = "Error code %d out of range for group %s. Expected range is [%d,%d]";
    private static final String HTTP_STATUS_ERROR_TEMPLATE = "HTTP status %d is invalid. Must be between %d and %d";

    private final int errorCode;
    private final ErrorGroup errorGroup;
    private final String errorMessage;
    private final String externalErrorCode;
    private final int httpStatus;
    private final boolean recoverable;

    /**
     * Construct a new ServiceError.
     *
     * @param errorCode         numeric code, must be within the group's range
     * @param errorGroup        category this error belongs to
     * @param errorMessage      human-readable default message
     * @param externalErrorCode external code for API responses (e.g., "SVC-100")
     * @param httpStatus        HTTP status code (100-599)
     * @param recoverable       whether clients should retry on this error
     * @throws NullPointerException     if errorGroup, errorMessage, or externalErrorCode is null
     * @throws IllegalArgumentException if errorCode is outside group range or httpStatus is invalid
     */
    public ServiceError(int errorCode, ErrorGroup errorGroup, String errorMessage, String externalErrorCode, int httpStatus, boolean recoverable)
    {
        Objects.requireNonNull(errorGroup, "Error group must not be null");
        Objects.requireNonNull(errorMessage, "Error message must not be null");
        Objects.requireNonNull(externalErrorCode, "External error code must not be null");
        validateCodeRange(errorCode, errorGroup);
        validateHttpStatus(httpStatus);
        this.errorCode = errorCode;
        this.errorGroup = errorGroup;
        this.errorMessage = errorMessage;
        this.externalErrorCode = externalErrorCode;
        this.httpStatus = httpStatus;
        this.recoverable = recoverable;
    }

    private static void validateCodeRange(int errorCode, ErrorGroup errorGroup)
    {
        if (errorCode < errorGroup.getCodeRangeStart() || errorCode > errorGroup.getCodeRangeEnd())
        {
            throw new IllegalArgumentException(String.format(RANGE_ERROR_TEMPLATE, errorCode, errorGroup, errorGroup.getCodeRangeStart(), errorGroup.getCodeRangeEnd()));
        }
    }

    private static void validateHttpStatus(int httpStatus)
    {
        if (httpStatus < HTTP_STATUS_MIN || httpStatus > HTTP_STATUS_MAX)
        {
            throw new IllegalArgumentException(String.format(HTTP_STATUS_ERROR_TEMPLATE, httpStatus, HTTP_STATUS_MIN, HTTP_STATUS_MAX));
        }
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public ErrorGroup getErrorGroup()
    {
        return errorGroup;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public String getExternalErrorCode()
    {
        return externalErrorCode;
    }

    public int getHttpStatus()
    {
        return httpStatus;
    }

    public boolean isRecoverable()
    {
        return recoverable;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(errorCode, errorGroup);
    }

    /**
     * Two ServiceErrors are equal if they share the same error code and error group.
     * Other fields (message, httpStatus, recoverability) are not considered for equality.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        ServiceError other = (ServiceError) obj;
        return errorCode == other.errorCode && errorGroup.equals(other.errorGroup);
    }

    @Override
    public String toString()
    {
        return String.format("ServiceError{group=%s, code=%d, externalCode=%s, message='%s', httpStatus=%d, recoverable=%s}", errorGroup.getLabel(), errorCode, externalErrorCode, errorMessage, httpStatus, recoverable);
    }
}
