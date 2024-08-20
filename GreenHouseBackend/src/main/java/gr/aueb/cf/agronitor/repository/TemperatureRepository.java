package gr.aueb.cf.agronitor.repository;

import gr.aueb.cf.agronitor.model.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {

    Temperature findTemperatureById(Long id);

    @Query("SELECT T FROM Temperature T WHERE T.value = (SELECT MAX(T2.value) FROM Temperature T2 WHERE T2.greenhouse.id = ?1)")
    Temperature getMaxTemperature(Long greenhouseId);

    @Query("SELECT T FROM Temperature T WHERE T.value = (SELECT MIN(T2.value) FROM Temperature T2 WHERE T2.greenhouse.id = ?1)")
    Temperature getMinTemperature(Long greenhouseId);

    @Query("SELECT T FROM Temperature T WHERE T.greenhouse.id = ?1 ORDER BY T.timestamp DESC")
    List<Temperature> findLastTemperature(Long greenhouseId);
}
