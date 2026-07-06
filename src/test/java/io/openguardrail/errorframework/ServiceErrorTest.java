package io.openguardrail.errorframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceErrorTest
{
    @Test
    void createValidError()
    {
        ServiceError error = new ServiceError(100, TestGroup.TEST, "Test message", "TEST-100", 400, false);

        assertEquals(100, error.getErrorCode());
        assertEquals(TestGroup.TEST, error.getErrorGroup());
        assertEquals("Test message", error.getErrorMessage());
        assertEquals("TEST-100", error.getExternalErrorCode());
        assertEquals(400, error.getHttpStatus());
        assertFalse(error.isRecoverable());
    }

    @Test
    void createRecoverableError()
    {
        ServiceError error = new ServiceError(150, TestGroup.TEST, "Timeout", "TEST-150", 503, true);

        assertTrue(error.isRecoverable());
    }

    @Test
    void rejectCodeBelowRange()
    {
        assertThrows(IllegalArgumentException.class, () -> new ServiceError(99, TestGroup.TEST, "Bad", "TEST-99", 400, false));
    }

    @Test
    void rejectCodeAboveRange()
    {
        assertThrows(IllegalArgumentException.class, () -> new ServiceError(200, TestGroup.TEST, "Bad", "TEST-200", 400, false));
    }

    @Test
    void rejectNullGroup()
    {
        assertThrows(NullPointerException.class, () -> new ServiceError(100, null, "Bad", "TEST-100", 400, false));
    }

    @Test
    void rejectNullMessage()
    {
        assertThrows(NullPointerException.class, () -> new ServiceError(100, TestGroup.TEST, null, "TEST-100", 400, false));
    }

    @Test
    void rejectNullExternalCode()
    {
        assertThrows(NullPointerException.class, () -> new ServiceError(100, TestGroup.TEST, "Msg", null, 400, false));
    }

    @Test
    void equalityByCodeAndGroup()
    {
        ServiceError a = new ServiceError(100, TestGroup.TEST, "First", "TEST-100", 400, false);
        ServiceError b = new ServiceError(100, TestGroup.TEST, "Second", "TEST-100", 500, true);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void inequalityDifferentCode()
    {
        ServiceError a = new ServiceError(100, TestGroup.TEST, "First", "TEST-100", 400, false);
        ServiceError b = new ServiceError(101, TestGroup.TEST, "Second", "TEST-101", 400, false);

        assertNotEquals(a, b);
    }

    @Test
    void boundaryCodesAccepted()
    {
        assertDoesNotThrow(() -> new ServiceError(100, TestGroup.TEST, "Start", "TEST-100", 400, false));
        assertDoesNotThrow(() -> new ServiceError(199, TestGroup.TEST, "End", "TEST-199", 400, false));
    }

    @Test
    void rejectHttpStatusBelowRange()
    {
        assertThrows(IllegalArgumentException.class, () -> new ServiceError(100, TestGroup.TEST, "Bad", "TEST-100", 99, false));
    }

    @Test
    void rejectHttpStatusAboveRange()
    {
        assertThrows(IllegalArgumentException.class, () -> new ServiceError(100, TestGroup.TEST, "Bad", "TEST-100", 600, false));
    }

    @Test
    void boundaryHttpStatusAccepted()
    {
        assertDoesNotThrow(() -> new ServiceError(100, TestGroup.TEST, "Ok", "TEST-100", 100, false));
        assertDoesNotThrow(() -> new ServiceError(101, TestGroup.TEST, "Ok", "TEST-101", 599, false));
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

        @Override public int getCodeRangeStart()
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
