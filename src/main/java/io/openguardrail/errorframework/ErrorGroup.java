package io.openguardrail.errorframework;

import java.io.Serializable;

/**
 * Interface for error group definitions.
 * Each group owns a numeric code range and categorizes related errors.
 *
 * <p>Implementors must ensure that {@code getCodeRangeStart() <= getCodeRangeEnd()}.
 * Violations will cause all error construction within the group to fail.
 */
public interface ErrorGroup extends Serializable
{
    /**
     * Returns the first error code allowed in this group (inclusive).
     * Must be less than or equal to {@link #getCodeRangeEnd()}.
     */
    int getCodeRangeStart();

    /**
     * Returns the last error code allowed in this group (inclusive).
     * Must be greater than or equal to {@link #getCodeRangeStart()}.
     */
    int getCodeRangeEnd();

    /**
     * Returns a human-readable description of this error group.
     */
    String getDescription();

    /**
     * Returns a short label for this group.
     */
    String getLabel();
}
