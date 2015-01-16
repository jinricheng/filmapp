package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Film;
import cat.udl.eps.softarch.hello.model.Userfilm;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public interface UserGreetingsService {
    Userfilm getUserAndFilms(Long userId);

    Film addFilmToUser(Film greeting);

    Film updateFilmFromUser(Film updateGreeting, Long greetingId);

    void removeFilmFromUser(Long greetingId);
}
