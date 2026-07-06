/**
 * OpenGuardrail Error Framework.
 *
 * <p>Provides a standardized error handling structure for Java services:
 * <ul>
 *   <li>{@link org.openguardrail.errorframework.ErrorGroup} - defines code ranges for error categories</li>
 *   <li>{@link org.openguardrail.errorframework.ServiceError} - immutable error definition with validation</li>
 *   <li>{@link org.openguardrail.errorframework.ServiceException} - runtime exception carrying an error</li>
 *   <li>{@link org.openguardrail.errorframework.ErrorResponse} - API response body</li>
 *   <li>{@link org.openguardrail.errorframework.ErrorBase} - interface for error enum entries</li>
 * </ul>
 */
package org.openguardrail.errorframework;
