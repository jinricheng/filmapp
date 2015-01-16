package cat.udl.eps.softarch.hello.model;

import java.util.Date;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
public class Film implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 128, message = "Title maximum length is {max} characters long")
    private String title;


    @NotBlank(message = "duration cannot be blank")
    @Size(max = 56, message = "Duration maximum length is {max} characters long")
    private String year;

    @NotBlank(message = "duration cannot be blank")
    @Size(max = 56, message = "Duration maximum length is {max} characters long")
    private String duration;

    @NotBlank(message = "E-mail cannot be blank")
    @Email(message = "E-mail should be valid")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    public Film() {}

    public Film(String title, String year,String duration,String email, Date date) {
        this.title = title;
        this.year = year;
        this.date = date;

        this.duration = duration;

        this.email =email;
    }

    public long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }
}
