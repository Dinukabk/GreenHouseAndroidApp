package gr.aueb.cf.agronitor.repository;

import gr.aueb.cf.agronitor.model.UVRadiation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UVRadiationRepository extends JpaRepository<UVRadiation, Long> {

    UVRadiation findUVRadiationById(Long id);

    @Query("SELECT U FROM UVRadiation U WHERE U.value = (SELECT MAX(U2.value) FROM UVRadiation U2 WHERE U2.greenhouse.id = ?1)")
    UVRadiation getMaxUVRadiation(Long greenhouseId);

    @Query("SELECT U FROM UVRadiation U WHERE U.value = (SELECT MIN(U2.value) FROM UVRadiation U2 WHERE U2.greenhouse.id = ?1)")
    UVRadiation getMinUVRadiation(Long greenhouseId);

    @Query("SELECT U FROM UVRadiation U WHERE U.greenhouse.id = ?1 ORDER BY U.timestamp DESC")
    List<UVRadiation> findLastUVRadiation(Long greenhouseId);
}
