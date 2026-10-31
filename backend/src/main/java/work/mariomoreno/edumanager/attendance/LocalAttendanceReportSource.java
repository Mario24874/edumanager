package work.mariomoreno.edumanager.attendance;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LocalAttendanceReportSource implements AttendanceReportSource {
    private final AttendanceRepository repo;
    private final work.mariomoreno.edumanager.student.StudentRepository students;

    public LocalAttendanceReportSource(AttendanceRepository repo,
                                       work.mariomoreno.edumanager.student.StudentRepository students) {
        this.repo = repo;
        this.students = students;
    }

    @Override
    public List<StudentAttendanceSummary> summaryByStudent() {
        var all = repo.findAll();
        return students.findAll().stream().map(s -> {
            long p = all.stream().filter(a -> a.getStudentId().equals(s.getId()) && a.getStatus() == AttendanceStatus.PRESENT).count();
            long ab = all.stream().filter(a -> a.getStudentId().equals(s.getId()) && a.getStatus() == AttendanceStatus.ABSENT).count();
            long l = all.stream().filter(a -> a.getStudentId().equals(s.getId()) && a.getStatus() == AttendanceStatus.LATE).count();
            return StudentAttendanceSummary.of(s.getId(), s.getFirstName() + " " + s.getLastName(), p, ab, l);
        }).toList();
    }
}
