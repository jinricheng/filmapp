package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by roberto on 02/01/15.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(@Param("username") String username);
    User findUserByEmail(@Param("email") String email);

}
