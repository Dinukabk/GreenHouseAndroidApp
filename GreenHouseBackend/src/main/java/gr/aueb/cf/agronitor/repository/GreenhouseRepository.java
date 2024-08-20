package gr.aueb.cf.agronitor.repository;

import gr.aueb.cf.agronitor.model.Greenhouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GreenhouseRepository extends JpaRepository<Greenhouse, Long> {

    List<Greenhouse> findAll();

    @Query("SELECT G FROM Greenhouse G WHERE G.user.id = ?1")
    List<Greenhouse> findGreenhousesByUserId(Long id);

    List<Greenhouse> findGreenhouseByGreenhouseNameStartingWith(String greenhouseName);

    Greenhouse findGreenhouseById(Long id);

    void deleteById(Long Id);
}
