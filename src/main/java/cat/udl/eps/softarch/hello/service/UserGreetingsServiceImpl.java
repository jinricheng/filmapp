package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Film;
import cat.udl.eps.softarch.hello.model.Userfilm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;

import java.util.List;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Service
public class UserGreetingsServiceImpl implements UserGreetingsService {
    final Logger logger = LoggerFactory.getLogger(UserGreetingsServiceImpl.class);

    @Autowired
    GreetingRepository greetingRepository;
    @Autowired
    UserRepository     userRepository;

    @Transactional(readOnly = true)
    @Override
    public Userfilm getUserAndFilms(Long userId) {
        Userfilm u = userRepository.findOne(userId);
        logger.info("Userfilm {} has {} greetings", u.getUsername(), u.getFilms().size());
        return u;
    }

    @Transactional
    @Override
    public Film addFilmToUser(Film g) {
        Userfilm u = userRepository.findUserByEmail(g.getEmail());
        if (u == null) {
            String email = g.getEmail();
            String username = email.substring(0, email.indexOf('@'));
            u = new Userfilm(username, email);
        }
        greetingRepository.save(g);
        u.addFilm(g);
        userRepository.save(u);
        return g;
    }

    @Transactional
    @Override
    public Film updateFilmFromUser(Film updateFilm, Long filmId) {
        Film oldFilm = greetingRepository.findOne(filmId);
        oldFilm.setTitle(updateFilm.getTitle());
        oldFilm.setDate(updateFilm.getDate());
        oldFilm.setYear(updateFilm.getYear());
        oldFilm.setMpaa_rating(updateFilm.getMpaa_rating());
        oldFilm.setSynopsis(updateFilm.getSynopsis());
        oldFilm.setPoster(updateFilm.getPoster());
        oldFilm.setRuntime(updateFilm.getRuntime());

        if (!updateFilm.getEmail().equals(oldFilm.getEmail())) {
            throw new GreetingEmailUpdateException("Email different, cannot be updated");
        }

        return greetingRepository.save(oldFilm);
    }

    @Transactional
    @Override
    public void removeFilmFromUser(Long filmId) {
        Film g = greetingRepository.findOne(filmId);
        Userfilm u = userRepository.findUserByEmail(g.getEmail());
        if (u != null) {
            u.removeFilm(g);
            userRepository.save(u);
        }
        greetingRepository.delete(g);
    }
}
