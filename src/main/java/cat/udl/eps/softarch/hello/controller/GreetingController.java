package cat.udl.eps.softarch.hello.controller;

import java.util.Date;

import cat.udl.eps.softarch.hello.model.Film;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.service.UserGreetingsService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    // LIST
    @RequestMapping(value = {"/films","/films/json"},method = RequestMethod.GET)
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

    @RequestMapping(value = "/films/json",method = RequestMethod.GET, produces = "application/json")
    public ModelAndView listJSON(@RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "10") int size) {
        return new ModelAndView("films", "films", list(page, size));
    }
// RETRIEVE
    @RequestMapping(value = {"/films/{id}","/films/{id}/json"}, method = RequestMethod.GET)
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
    @RequestMapping(value = "/films/{id}/json", method = RequestMethod.GET, produces = "application/json")
    public ModelAndView retrieveJSON(@PathVariable( "id" ) Long id) {
        return new ModelAndView("film", "film", retrieve(id));
    }

    // CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Film create(@Valid @RequestBody Film greeting, HttpServletResponse response) {
        logger.info("Creating greeting with content'{}'", greeting.getTitle());
       // greetingRepository.save(greeting);

        Film newGreeting = userGreetingsService.addFilmToUser(greeting);
        response.setHeader("Location", "/films/" + newGreeting.getId());
        return newGreeting;
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("film") Film greeting, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/films/"+create(greeting, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for film creation");
        Film emptyFilm = new Film();
        emptyFilm.setDate(new Date());
        return new ModelAndView("form", "film", emptyFilm);
    }

// UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Film update(@PathVariable("id") Long id, @Valid @RequestBody Film film) {
        logger.info("Updating film {}, new title is '{}'", id, film.getTitle());
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Film with id %s not found", id);
        return userGreetingsService.updateFilmFromUser(film, id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
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
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Film form for updating film number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "film with id %s not found", id);
        return new ModelAndView("form", "film", greetingRepository.findOne(id));
    }

// DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting film number {}", id);
        Preconditions.checkNotNull(greetingRepository.findOne(id), "Film with id %s not found", id);
       // greetingRepository.delete(id);
        userGreetingsService.removeFilmFromUser(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        delete(id);
        return "redirect:/films";
    }

}
