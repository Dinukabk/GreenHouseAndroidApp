package gr.aueb.cf.agronitor.repository;

import gr.aueb.cf.agronitor.model.Humidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumidityRepository extends JpaRepository<Humidity, Long> {

    Humidity findHumidityById(Long Id);

    @Query("SELECT H FROM Humidity H WHERE H.value = (SELECT MAX(H2.value) FROM Humidity H2 WHERE H2.greenhouse.id = ?1)")
    Humidity getMaxHumidity(Long greenhouseId);

    @Query("SELECT H FROM Humidity H WHERE H.value = (SELECT MIN(H2.value) FROM Humidity H2 WHERE H2.greenhouse.id = ?1)")
    Humidity getMinHumidity(Long greenhouseId);

    @Query("SELECT H FROM Humidity H WHERE H.greenhouse.id = ?1 ORDER BY H.timestamp DESC")
    List<Humidity> findLastHumidity(Long greenhouseId);
}
