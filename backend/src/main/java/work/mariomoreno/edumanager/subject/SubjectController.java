package work.mariomoreno.edumanager.subject;

import org.springframework.web.bind.annotation.*;
import work.mariomoreno.edumanager.CrudSupport;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController extends CrudSupport<Subject> {
    public SubjectController(SubjectRepository repo) { super(repo); }

    @Override
    protected void copyInto(Subject t, Subject s) {
        t.setName(s.getName());
        t.setDescription(s.getDescription());
        t.setTeacher(s.getTeacher());
    }
}
