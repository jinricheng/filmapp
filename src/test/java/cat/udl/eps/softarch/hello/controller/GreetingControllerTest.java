package cat.udl.eps.softarch.hello.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cat.udl.eps.softarch.hello.config.GreetingsAppTestContext;
import cat.udl.eps.softarch.hello.model.Film;
import cat.udl.eps.softarch.hello.model.Userfilm;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import com.google.common.primitives.Ints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GreetingsAppTestContext.class)
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GreetingControllerTest {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired GreetingRepository greetingRepository;
    @Autowired UserRepository     userRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Date greetingDate;

    @Before
    public void setup() throws ParseException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.greetingDate = df.parse("2015-01-01");

        if (greetingRepository.count() == 0) {
            Film g = new Film("test1", "1956","R","1H","hjkhj","hello.jng","test1@gmail.com",greetingDate);
            greetingRepository.save(g);
            Userfilm u = new Userfilm("testuser", "test@example.org");
            u.addFilm(g);
            userRepository.save(u);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    //TODO: Add tests for email and date greeting fields on retrieve/create/update, validation errors...

    @Test
    public void testList() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(get("/films").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("films"))
                .andExpect(forwardedUrl("/WEB-INF/views/films.jsp"))
                .andExpect(model().attributeExists("films"))
                .andExpect(model().attribute("films", hasSize(startSize)))
                .andExpect(model().attribute("films", hasItem(allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("title", is("test1")),
                        hasProperty("year", is("1956")),
                        hasProperty("mpaa_rating",is("R")),
                        hasProperty("runtime",is("1H")),
                        hasProperty("synopsis",is("hjkhj")),
                        hasProperty("poster",is("hello.jng"))))));
    }

    @Test
    public void testRetrieveExisting() throws Exception {
        mockMvc.perform(get("/films/{id}", 1L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("film"))
                .andExpect(forwardedUrl("/WEB-INF/views/film.jsp"))
                .andExpect(model().attributeExists("film"))
                .andExpect(model().attribute("film", allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("title", is("test1")),
                        hasProperty("year", is("1956")),
                        hasProperty("runtime", is("1H")))));
    }
/*
    @Test
    public void testRetrieveNonExisting() throws Exception {
        mockMvc.perform(get("/films/{id}", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));
    }

    @Test
    public void testCreate() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/films")
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "newtest")
                .param("year", "1325")
                .param("duration", "1h")
                .param("email", "newtest@example.org")
                .param("date", df.format(new Date())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/films/" + (startSize + 1)))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("film", hasProperty("title", is("newtest"))));

        assertEquals(startSize+1, greetingRepository.count());
    }

    @Test
    public void testCreateEmpty() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(post("/films")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("film", "title"))
                .andExpect(model().attribute("film", hasProperty("title", isEmptyOrNullString())));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testCreateForm() throws Exception {
        mockMvc.perform(get("/films/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().attribute("film", hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    public void testUpdate() throws Exception {
        Film tobeupdated = greetingRepository.save(new Film("tobeupdated","1956","1h", "test@example.org", new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/films/{id}", tobeupdated.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "updated")
                        .param("year", "1956")
                        .param("duration", "1h")
                        .param("email", "test@example.org")
                        .param("date", df.format(new Date())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/films/"+tobeupdated.getId()))
                .andExpect(model().hasNoErrors());

        assertEquals("updated", greetingRepository.findOne(tobeupdated.getId()).getTitle());
        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateEmpty() throws Exception {
        Film tobeupdated = greetingRepository.save(new Film("tobeupdated", "1956","1h","test@example.org", new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/films/{id}", tobeupdated.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("film", "title"))
                .andExpect(model().attribute("film", hasProperty("title", isEmptyOrNullString())));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateNonExisting() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(put("/films/{id}", 999L)
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "updated")
                        .param("year", "1956")
                        .param("duration", "1h")
                        .param("email", "test@example.org")
                        .param("date", df.format(new Date())))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));

        assertEquals(startSize, greetingRepository.count());
    }

    @Test
    public void testUpdateForm() throws Exception {
        mockMvc.perform(get("/films/{id}/form", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/WEB-INF/views/form.jsp"))
                .andExpect(model().attribute("film", hasProperty("title", is("test1"))));
    }

    @Test
    public void testUpdateFormNonExisting() throws Exception {
        mockMvc.perform(get("/films/{id}/form", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));
    }

    @Test
    public void testDeleteExisting() throws Exception {
        Film toBeRemoved = greetingRepository.save(new Film("toberemoved", "1285","1h","test@example.org", new Date()));
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/films/{id}", toBeRemoved.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/films"));

        assertEquals(startSize-1, greetingRepository.count());
        assertThat(greetingRepository.findAll(), not(hasItem(toBeRemoved)));
    }

    @Test
    public void testDeleteNonExisting() throws Exception {
        int startSize = Ints.checkedCast(greetingRepository.count());

        mockMvc.perform(delete("/films/{id}", 999L).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(forwardedUrl("/WEB-INF/views/error.jsp"));

        assertEquals(startSize, greetingRepository.count());
    }*/
}
