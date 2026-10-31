package work.mariomoreno.edumanager.attendance;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import work.mariomoreno.edumanager.CrudSupport;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController extends CrudSupport<Attendance> {
    private final AttendanceReportSource reports;

    public AttendanceController(AttendanceRepository repo, AttendanceReportSource reports) {
        super(repo);
        this.reports = reports;
    }

    @Override
    protected void copyInto(Attendance t, Attendance s) {
        t.setStudentId(s.getStudentId());
        t.setClassroomId(s.getClassroomId());
        t.setSubjectId(s.getSubjectId());
        t.setDate(s.getDate());
        t.setStatus(s.getStatus());
    }

    /** Resumen por estudiante; alimenta el grafico del dashboard. */
    @GetMapping("/summary")
    public List<StudentAttendanceSummary> summary() { return reports.summaryByStudent(); }
}
