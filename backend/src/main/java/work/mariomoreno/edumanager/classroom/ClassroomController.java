package work.mariomoreno.edumanager.classroom;

import org.springframework.web.bind.annotation.*;
import work.mariomoreno.edumanager.CrudSupport;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController extends CrudSupport<Classroom> {
    public ClassroomController(ClassroomRepository repo) { super(repo); }

    @Override
    protected void copyInto(Classroom t, Classroom s) {
        t.setName(s.getName());
        t.setCapacity(s.getCapacity());
        t.setLocation(s.getLocation());
    }
}
