package org.openguardrail.errorframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceExceptionTest
{
    private static final ServiceError TEST_ERROR = new ServiceError(100, TestGroup.TEST, "Something failed", "TEST-100", 400, false);
    private static final ServiceError RECOVERABLE_ERROR = new ServiceError(101, TestGroup.TEST, "Timeout occurred", "TEST-101", 503, true);

    @Test
    void exceptionCarriesError()
    {
        ServiceException ex = new ServiceException(TEST_ERROR);

        assertEquals("Something failed", ex.getMessage());
        assertEquals("TEST-100", ex.getExternalErrorCode());
        assertEquals(400, ex.getHttpStatus());
        assertFalse(ex.isRecoverable());
        assertEquals(TEST_ERROR, ex.getError());
    }

    @Test
    void exceptionWithCustomMessage()
    {
        ServiceException ex = new ServiceException("Custom detail", TEST_ERROR);

        assertEquals("Custom detail", ex.getMessage());
        assertEquals(TEST_ERROR, ex.getError());
    }

    @Test
    void exceptionWithCause()
    {
        RuntimeException cause = new RuntimeException("root cause");
        ServiceException ex = new ServiceException("Wrapped", cause, TEST_ERROR);

        assertEquals("Wrapped", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void errorResponseFromError()
    {
        ErrorResponse response = ErrorResponse.from(TEST_ERROR);

        assertEquals("TEST-100", response.getCode());
        assertEquals("Something failed", response.getMessage());
        assertEquals(400, response.getStatus());
        assertFalse(response.isRecoverable());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void errorResponseFromException()
    {
        ServiceException ex = new ServiceException("Detail message", RECOVERABLE_ERROR);
        ErrorResponse response = ErrorResponse.from(ex);

        assertEquals("TEST-101", response.getCode());
        assertEquals("Detail message", response.getMessage());
        assertEquals(503, response.getStatus());
        assertTrue(response.isRecoverable());
    }

    @Test
    void errorResponseOf()
    {
        ErrorResponse response = ErrorResponse.of("CUSTOM-1", "Custom error", 422);

        assertEquals("CUSTOM-1", response.getCode());
        assertEquals("Custom error", response.getMessage());
        assertEquals(422, response.getStatus());
        assertFalse(response.isRecoverable());
    }

    @Test
    void exceptionRejectsNullError()
    {
        assertThrows(NullPointerException.class, () -> new ServiceException((ServiceError) null));
        assertThrows(NullPointerException.class, () -> new ServiceException("msg", (ServiceError) null));
    }

    @Test
    void errorResponseRejectsNullInput()
    {
        assertThrows(NullPointerException.class, () -> ErrorResponse.from((ServiceError) null));
        assertThrows(NullPointerException.class, () -> ErrorResponse.from((ServiceException) null));
        assertThrows(NullPointerException.class, () -> ErrorResponse.of(null, "msg", 400));
        assertThrows(NullPointerException.class, () -> ErrorResponse.of("CODE", null, 400));
    }

    @Test
    void errorResponseToMap()
    {
        ErrorResponse response = ErrorResponse.from(TEST_ERROR);
        java.util.Map<String, Object> map = response.toMap();

        assertEquals("TEST-100", map.get("code"));
        assertEquals("Something failed", map.get("message"));
        assertEquals(400, map.get("status"));
        assertEquals(false, map.get("recoverable"));
        assertNotNull(map.get("timestamp"));
    }

    private enum TestGroup implements ErrorGroup
    {
        TEST(100, 199, "Test errors", "Test");

        private final int start, end;
        private final String description, label;

        TestGroup(int start, int end, String description, String label)
        {
            this.start = start;
            this.end = end;
            this.description = description;
            this.label = label;
        }

        @Override
        public int getCodeRangeStart()
        {
            return start;
        }

        @Override public int getCodeRangeEnd()
        {
            return end;
        }

        @Override public String getDescription()
        {
            return description;
        }

        @Override public String getLabel()
        {
            return label;
        }
    }
}
