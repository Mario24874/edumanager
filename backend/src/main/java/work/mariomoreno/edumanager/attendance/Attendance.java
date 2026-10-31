package work.mariomoreno.edumanager.attendance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Attendance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull private Long studentId;
    @NotNull private Long classroomId;
    @NotNull private Long subjectId;
    @NotNull private LocalDate date;
    @NotNull @Enumerated(EnumType.STRING) private AttendanceStatus status;

    protected Attendance() {}

    public Attendance(Long studentId, Long classroomId, Long subjectId, LocalDate date, AttendanceStatus status) {
        this.studentId = studentId;
        this.classroomId = classroomId;
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Long getClassroomId() { return classroomId; }
    public Long getSubjectId() { return subjectId; }
    public LocalDate getDate() { return date; }
    public AttendanceStatus getStatus() { return status; }
    public void setStudentId(Long v) { this.studentId = v; }
    public void setClassroomId(Long v) { this.classroomId = v; }
    public void setSubjectId(Long v) { this.subjectId = v; }
    public void setDate(LocalDate v) { this.date = v; }
    public void setStatus(AttendanceStatus v) { this.status = v; }
}
