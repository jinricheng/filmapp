package cat.udl.eps.softarch.hello.repository;

import java.util.List;

import cat.udl.eps.softarch.hello.model.Film;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by http://rhizomik.net/~roberto/
 */

public interface GreetingRepository extends PagingAndSortingRepository<Film, Long> {

    // PagingAndSortingRepository provides:
    // exists(ID id), delete(T entity), findAll(Pageable), findAll(Sort), findOne(ID id), save(T entity),...
    // http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html

    List<Film> findByTitleContaining(@Param("title") String title);
}
