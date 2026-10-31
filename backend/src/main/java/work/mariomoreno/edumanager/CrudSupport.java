package work.mariomoreno.edumanager;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** CRUD REST base compartido por los cuatro recursos; cada subclase solo fija la ruta y copia campos en update. */
public abstract class CrudSupport<T> {
    private final JpaRepository<T, Long> repo;

    protected CrudSupport(JpaRepository<T, Long> repo) { this.repo = repo; }

    protected abstract void copyInto(T target, T source);

    @GetMapping
    public List<T> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public T get(@PathVariable Long id) { return find(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public T create(@jakarta.validation.Valid @RequestBody T body) { return repo.save(body); }

    @PutMapping("/{id}")
    public T update(@PathVariable Long id, @jakarta.validation.Valid @RequestBody T body) {
        T current = find(id);
        copyInto(current, body);
        return repo.save(current);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.delete(find(id));
    }

    private T find(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el recurso con id " + id));
    }
}
