package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.UVRadiation;
import gr.aueb.cf.agronitor.repository.UVRadiationRepository;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UVRadiationServiceImpl implements IUVRadiationService {

    private final UVRadiationRepository uvRadiationRepository;

    @Autowired
    public UVRadiationServiceImpl(UVRadiationRepository uvRadiationRepository) {
        this.uvRadiationRepository = uvRadiationRepository;
    }

    @Transactional
    @Override
    public UVRadiation insertUVRadiation(UVRadiation uvRadiation) {
        return uvRadiationRepository.save(uvRadiation);
    }

    @Transactional
    @Override
    public void deleteUVRadiation(Long id) throws EntityNotFoundException {
        uvRadiationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UVRadiation getUVRadiationById(Long id) throws EntityNotFoundException {
        UVRadiation uvRadiation;
        uvRadiation = uvRadiationRepository.findUVRadiationById(id);
        if (uvRadiation == null) throw new EntityNotFoundException(UVRadiation.class, 0L);
        return uvRadiation;
    }

    @Transactional
    @Override
    public UVRadiation getMaxUVRadiation(Long greenhouseId) throws EntityNotFoundException {
        UVRadiation uvRadiation;
        uvRadiation = uvRadiationRepository.getMaxUVRadiation(greenhouseId);
        if (uvRadiation == null) throw new EntityNotFoundException(UVRadiation.class, 0L);
        return uvRadiation;
    }

    @Transactional
    @Override
    public UVRadiation getMinUVRadiation(Long greenhouseId) throws EntityNotFoundException {
        UVRadiation uvRadiation;
        uvRadiation = uvRadiationRepository.getMinUVRadiation(greenhouseId);
        if (uvRadiation == null) throw new EntityNotFoundException(UVRadiation.class, 0L);
        return uvRadiation;
    }

    @Transactional
    @Override
    public List<UVRadiation> getGreenhouseLastUV(Long greenhouseId) throws EntityNotFoundException {
        List<UVRadiation> uvRadiations = uvRadiationRepository.findLastUVRadiation(greenhouseId);
        if (uvRadiations.isEmpty()) throw new EntityNotFoundException(UVRadiation.class, 0L);
        return uvRadiations;
    }
}
