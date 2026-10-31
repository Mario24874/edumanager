package work.mariomoreno.edumanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import work.mariomoreno.edumanager.attendance.StudentAttendanceSummary;

class StudentAttendanceSummaryTest {
    @Test
    void lateCountsAsAttendedAndEmptyIsZero() {
        assertEquals(75.0, StudentAttendanceSummary.of(1L, "A", 2, 1, 1).attendanceRate());
        assertEquals(0.0, StudentAttendanceSummary.of(1L, "A", 0, 0, 0).attendanceRate());
    }
}
