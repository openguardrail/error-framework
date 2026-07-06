/**
 * OpenGuardrail Error Framework.
 *
 * <p>Provides a standardized error handling structure for Java services:
 * <ul>
 *   <li>{@link io.openguardrail.errorframework.ErrorGroup} - defines code ranges for error categories</li>
 *   <li>{@link io.openguardrail.errorframework.ServiceError} - immutable error definition with validation</li>
 *   <li>{@link io.openguardrail.errorframework.ServiceException} - runtime exception carrying an error</li>
 *   <li>{@link io.openguardrail.errorframework.ErrorResponse} - API response body</li>
 *   <li>{@link io.openguardrail.errorframework.ErrorBase} - interface for error enum entries</li>
 * </ul>
 */
package io.openguardrail.errorframework;
