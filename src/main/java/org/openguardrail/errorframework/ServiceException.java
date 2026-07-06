package org.openguardrail.errorframework;

import java.util.Objects;

/**
 * Base runtime exception carrying a {@link ServiceError}.
 *
 * <p>Provides direct access to error code, HTTP status, and recoverability
 * without needing to unwrap the error object.
 *
 * <p>This class is designed for extension. Projects may subclass it
 * (e.g., {@code TelosException extends ServiceException}) to add
 * project-specific context.
 */
public class ServiceException extends RuntimeException
{

    private static final long serialVersionUID = 1L;
    private final ServiceError error;

    public ServiceException(ServiceError error)
    {
        super(Objects.requireNonNull(error, "ServiceError must not be null").getErrorMessage());
        this.error = error;
    }

    public ServiceException(String message, ServiceError error)
    {
        super(message);
        this.error = Objects.requireNonNull(error, "ServiceError must not be null");
    }

    public ServiceException(String message, Throwable cause, ServiceError error)
    {
        super(message, cause);
        this.error = Objects.requireNonNull(error, "ServiceError must not be null");
    }

    public ServiceException(Throwable cause, ServiceError error)
    {
        super(cause);
        this.error = Objects.requireNonNull(error, "ServiceError must not be null");
    }

    public ServiceError getError()
    {
        return error;
    }

    /**
     * Returns the external error code (e.g., "TELOS-100").
     */
    public String getExternalErrorCode()
    {
        return error.getExternalErrorCode();
    }

    /**
     * Returns the HTTP status code for this error.
     */
    public int getHttpStatus()
    {
        return error.getHttpStatus();
    }

    /**
     * Returns whether this error is recoverable (retryable).
     */
    public boolean isRecoverable()
    {
        return error.isRecoverable();
    }
}
