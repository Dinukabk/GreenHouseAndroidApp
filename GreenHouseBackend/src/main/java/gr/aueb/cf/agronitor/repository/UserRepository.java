package gr.aueb.cf.agronitor.repository;

import gr.aueb.cf.agronitor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT U FROM User U WHERE U.username = ?1 AND U.password = ?2")
    User isUserValid(String username, String password);

    @Query("SELECT count(*) > 0 FROM User U WHERE U.username = ?1")
    boolean usernameExists(String username);

    User findUserById(Long id);

    List<User> findUserByUsernameStartingWith(String username);

    void deleteById(Long id);
}
