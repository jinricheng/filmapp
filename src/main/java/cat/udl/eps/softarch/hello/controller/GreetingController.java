package cat.udl.eps.softarch.hello.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cat.udl.eps.softarch.hello.model.Film;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.service.UserGreetingsService;
import cat.udl.eps.softarch.hello.service.XQueryHelper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import javax.xml.xquery.XQException;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/")
public class GreetingController {
    final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    GreetingRepository greetingRepository;
    @Autowired
    UserGreetingsService userGreetingsService;

    // HOME
    @RequestMapping(value = "/",method = RequestMethod.GET, produces = "text/html")
    public ModelAndView homeHTML() {
        return new ModelAndView("home");
    }

    // LIST
    @RequestMapping(value = "/films", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Film> list(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "10") int size) {
        PageRequest request = new PageRequest(page, size);
        return greetingRepository.findAll(request).getContent();
    }

    @RequestMapping(value = "/films",method = RequestMethod.GET, produces = "text/html")
    public ModelAndView listHTML(@RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "10") int size) {
        return new ModelAndView("films", "films", list(page, size));
    }

    // RETRIEVE
    @RequestMapping(value = "/films/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Film retrieve(@PathVariable("id") Long id) {
        logger.info("Retrieving Film number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Film with id %s not found", id);
        return greetingRepository.findOne(id);
    }
    @RequestMapping(value = "/films/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("film", "film", retrieve(id));
    }

    // CREATE
    @RequestMapping(value = "/films",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Film create(@Valid @RequestBody Film greeting, HttpServletResponse response) {
        logger.info("Creating greeting with content'{}'", greeting.getTitle());
        Film newGreeting = userGreetingsService.addFilmToUser(greeting);
        response.setHeader("Location", "/films/" + newGreeting.getId());
        return newGreeting;
    }
    @RequestMapping(value = "/films",method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("film") Film greeting, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/films/"+create(greeting, response).getId();
    }
    // Create form
    @RequestMapping(value = "/films/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for film creation");
        Film emptyFilm = new Film();
        emptyFilm.setDate(new Date());
        return new ModelAndView("form", "film", emptyFilm);
    }

    //SEARCH
    @RequestMapping(value = "/result/{result}",method = RequestMethod.GET)
    @ResponseBody
    public List<Film> search(@PathVariable("result" ) String title)throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, IOException, JAXBException {
        logger.info("film search with content'{}'", title);
        List<Film> r =  greetingRepository.findByTitleContaining(title);
        if(r.size()==0){
            XQueryHelper x = new XQueryHelper();
            r = x.getListFilm(title);
            for(Film film:r){
                film.setDate(new Date());
                userGreetingsService.addFilmToUser(film);
            }
        }
        return r;
    }

    @RequestMapping(value = "/result/{result}",method = RequestMethod.GET, produces = "text/html")
    public ModelAndView listSearchResult(@PathVariable("result" )String title) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, IOException, JAXBException{
        logger.info("Generating result from search.");
        return new ModelAndView("result", "films", search(title));
    }

    @RequestMapping(value = "/result",method = RequestMethod.GET,produces="text/html")
    public String searchHTML(@RequestParam("result")String title) {
        logger.info("Redirecting from Search Form.");
        return "redirect:/result/"+title;
    }
    // Search form
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView searchForm() {
        logger.info("Generating form for film searching");
        String title = new String();
        return new ModelAndView("searchForm", "films", title);
    }



    // UPDATE
    @RequestMapping(value = "/films/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Film update(@PathVariable("id") Long id, @Valid @RequestBody Film film) {
        logger.info("Updating film {}, new title is '{}'", id, film.getTitle());
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Film with id %s not found", id);
        return userGreetingsService.updateFilmFromUser(film, id);
    }
    @RequestMapping(value = "/films/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.OK)
    public String updateHTML(@PathVariable("id") Long id, @Valid @ModelAttribute("film") Film film,
                         BindingResult binding) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/films/"+update(id, film).getId();
    }
    // Update form
    @RequestMapping(value = "/films/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Film form for updating film number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "film with id %s not found", id);
        return new ModelAndView("form", "film", greetingRepository.findOne(id));
    }

    // DELETE
    @RequestMapping(value = "/films/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting film number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Film with id %s not found", id);
       // greetingRepository.delete(id);
        userGreetingsService.removeFilmFromUser(id);
    }
    @RequestMapping(value = "/films/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        delete(id);
        return "redirect:/films";
    }

}
