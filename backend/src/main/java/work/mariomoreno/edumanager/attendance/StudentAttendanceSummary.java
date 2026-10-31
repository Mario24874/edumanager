package work.mariomoreno.edumanager.attendance;

public record StudentAttendanceSummary(Long studentId, String studentName, long present, long absent, long late,
                                       double attendanceRate) {
    /** PRESENT + LATE cuentan como asistencia; sin registros la tasa es 0. */
    public static StudentAttendanceSummary of(Long studentId, String studentName, long present, long absent, long late) {
        long total = present + absent + late;
        double rate = total == 0 ? 0.0 : Math.round(1000.0 * (present + late) / total) / 10.0;
        return new StudentAttendanceSummary(studentId, studentName, present, absent, late, rate);
    }
}
