package org.openguardrail.errorframework;

/**
 * Base interface for error enum entries.
 *
 * <p>Each project defines its error enums implementing this interface.
 * This enables uniform access to the underlying {@link ServiceError}.
 */
public interface ErrorBase
{
    /**
     * Returns the underlying error definition.
     */
    ServiceError getError();
}
