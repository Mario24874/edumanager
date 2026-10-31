package work.mariomoreno.edumanager.student;

import org.springframework.web.bind.annotation.*;
import work.mariomoreno.edumanager.CrudSupport;

@RestController
@RequestMapping("/api/students")
public class StudentController extends CrudSupport<Student> {
    public StudentController(StudentRepository repo) { super(repo); }

    @Override
    protected void copyInto(Student t, Student s) {
        t.setFirstName(s.getFirstName());
        t.setLastName(s.getLastName());
        t.setEmail(s.getEmail());
        t.setGrade(s.getGrade());
    }
}
