package cat.udl.eps.softarch.hello.model;

import java.util.Date;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
@XmlRootElement
public class Film implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 128, message = "Title maximum length is {max} characters long")
    private String title;


    @NotBlank(message = "Year field cannot be blank")
    @Size(max = 56, message = "Year maximum length is {max} characters long")
    private String year;

    @NotBlank(message = "MPAA Rating field cannot be blank")
    @Size(max = 56, message = "mpaa_rating maximum length is {max} characters long")
    private String mpaa_rating;

    @NotBlank(message = "Runtime cannot be blank")
    @Size(max = 56, message = "Runtime maximum length is {max} characters long")
    private String runtime;

    @NotBlank(message = "Synopsis cannot be blank")
    private String synopsis;

    private String poster;

    @NotBlank(message = "E-mail cannot be blank")
    @Email(message = "E-mail should be valid")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    public Film() {}

    public Film(String title, String year,String mpaa_rating,String runtime,String synopsis,String poster,String email, Date date) {
        this.title = title;
        this.year = year;
        this.mpaa_rating = mpaa_rating;
        this.runtime = runtime;
        this.synopsis = synopsis;
        this.poster = poster;
        this.date = date;
        this.email =email;
    }

    public long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getMpaa_rating() { return mpaa_rating; }
    public void setMpaa_rating(String mpaa_rating) { this.mpaa_rating = mpaa_rating; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRuntime() { return runtime; }
    public void setRuntime(String runtime) { this.runtime = runtime; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
