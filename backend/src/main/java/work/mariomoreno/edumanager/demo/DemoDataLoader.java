package work.mariomoreno.edumanager.demo;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import work.mariomoreno.edumanager.attendance.*;
import work.mariomoreno.edumanager.classroom.*;
import work.mariomoreno.edumanager.student.*;
import work.mariomoreno.edumanager.subject.*;

/** Datos de ejemplo (ficticios y deterministas) solo con el perfil `demo`. */
@Component
@Profile("demo")
public class DemoDataLoader implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DemoDataLoader.class);

    private final StudentRepository students;
    private final SubjectRepository subjects;
    private final ClassroomRepository classrooms;
    private final AttendanceRepository attendances;

    public DemoDataLoader(StudentRepository students, SubjectRepository subjects,
                          ClassroomRepository classrooms, AttendanceRepository attendances) {
        this.students = students;
        this.subjects = subjects;
        this.classrooms = classrooms;
        this.attendances = attendances;
    }

    @Override
    public void run(String... args) {
        if (students.count() > 0) return;
        var ss = students.saveAll(List.of(
                new Student("Ana", "Lopez", "ana@example.com", "5to"),
                new Student("Luis", "Perez", "luis@example.com", "5to"),
                new Student("Marta", "Diaz", "marta@example.com", "6to"),
                new Student("Carlos", "Ruiz", "carlos@example.com", "6to"),
                new Student("Sofia", "Mora", "sofia@example.com", "4to")));
        var sub = subjects.saveAll(List.of(
                new Subject("Matematicas", "Algebra y geometria", "Prof. Salazar"),
                new Subject("Lengua", "Lectura y escritura", "Prof. Gil")));
        var rooms = classrooms.saveAll(List.of(
                new Classroom("Aula 101", 30, "Edificio A"),
                new Classroom("Aula 202", 25, "Edificio B")));
        AttendanceStatus[] pattern = {AttendanceStatus.PRESENT, AttendanceStatus.PRESENT, AttendanceStatus.LATE,
                AttendanceStatus.PRESENT, AttendanceStatus.ABSENT};
        LocalDate start = LocalDate.of(2026, 9, 1);
        for (int d = 0; d < 10; d++) {
            for (int i = 0; i < ss.size(); i++) {
                attendances.save(new Attendance(ss.get(i).getId(), rooms.get(i % 2).getId(),
                        sub.get(d % 2).getId(), start.plusDays(d), pattern[(d * (i + 2) + i) % pattern.length]));
            }
        }
        log.info("Demo: {} estudiantes, {} materias, {} aulas, {} asistencias",
                students.count(), subjects.count(), classrooms.count(), attendances.count());
    }
}
