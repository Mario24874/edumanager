package work.mariomoreno.edumanager.subject;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String name;
    private String description;
    @NotBlank private String teacher;

    protected Subject() {}

    public Subject(String name, String description, String teacher) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTeacher() { return teacher; }
    public void setName(String v) { this.name = v; }
    public void setDescription(String v) { this.description = v; }
    public void setTeacher(String v) { this.teacher = v; }
}
