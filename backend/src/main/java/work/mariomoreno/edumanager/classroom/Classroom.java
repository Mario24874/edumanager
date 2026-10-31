package work.mariomoreno.edumanager.classroom;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Classroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String name;
    @Min(1) private int capacity;
    private String location;

    protected Classroom() {}

    public Classroom(String name, int capacity, String location) {
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public void setName(String v) { this.name = v; }
    public void setCapacity(int v) { this.capacity = v; }
    public void setLocation(String v) { this.location = v; }
}
