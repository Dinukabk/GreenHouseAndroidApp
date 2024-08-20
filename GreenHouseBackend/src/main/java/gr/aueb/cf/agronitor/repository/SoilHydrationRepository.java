package gr.aueb.cf.agronitor.repository;

import gr.aueb.cf.agronitor.model.SoilHydration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoilHydrationRepository extends JpaRepository<SoilHydration, Long> {

    SoilHydration findSoilHydrationById(Long id);

    @Query("SELECT S FROM SoilHydration S WHERE S.value = (SELECT MAX(S2.value) FROM SoilHydration S2 WHERE S2.greenhouse.id = ?1)")
    SoilHydration getMaxSoilHydration(Long greenhouseId);

    @Query("SELECT S FROM SoilHydration S WHERE S.value = (SELECT MIN(S2.value) FROM SoilHydration S2 WHERE S2.greenhouse.id = ?1)")
    SoilHydration getMinSoilHydration(Long greenhouseId);

    @Query("SELECT S FROM SoilHydration S WHERE S.greenhouse.id = ?1 ORDER BY S.timestamp DESC")
    List<SoilHydration> findLastSoilHydration(Long greenhouseId);
}