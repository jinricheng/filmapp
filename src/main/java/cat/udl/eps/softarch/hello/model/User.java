package cat.udl.eps.softarch.hello.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by roberto on 29/12/14.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "E-mail cannot be blank")
    @Email(message = "E-mail should be valid")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Film> films ;

    public User() { }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        films = new ArrayList<>();
    }

    public long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<Film> getFilms() {
        return films;
    }

    public void addFilm(Film newFilm) {
        films.add(newFilm);
    }

    public void removeFilm(Film film) {films.remove(film);
    }
}
