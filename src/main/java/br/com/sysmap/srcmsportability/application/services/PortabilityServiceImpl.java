package br.com.sysmap.srcmsportability.application.services;

import br.com.sysmap.srcmsportability.application.ports.in.PortabilityService;
import br.com.sysmap.srcmsportability.application.ports.out.PortabilityRepository;
import br.com.sysmap.srcmsportability.domain.Address;
import br.com.sysmap.srcmsportability.domain.Line;
import br.com.sysmap.srcmsportability.domain.Portability;
import br.com.sysmap.srcmsportability.domain.User;
import br.com.sysmap.srcmsportability.domain.enums.PortabilityStatus;
import br.com.sysmap.srcmsportability.framework.adapters.in.dtos.InputPortability;
import br.com.sysmap.srcmsportability.framework.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

public class PortabilityServiceImpl implements PortabilityService {

    private final PortabilityRepository portabilityRepository;
    private final ModelMapper modelMapper;

    public PortabilityServiceImpl(PortabilityRepository portabilityRepository, ModelMapper modelMapper) {
        this.portabilityRepository = portabilityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Portability newPortability(InputPortability inputPortability) {
        Portability portability = Portability.builder()
                .user(modelMapper.map(inputPortability.getUser(), User.class))
                .portabilityStatus(PortabilityStatus.PROCESSANDO_PORTABILIDADE)
                .source(inputPortability.getPortability().getSource())
                .target(inputPortability.getPortability().getTarget())
                .build();

        return this.portabilityRepository.save(portability);
    }

    @Override
    public void putPortability(UUID id, PortabilityStatus portabilityStatus) {
        try {
            Optional<Portability> entity = portabilityRepository.findById(id);
            entity.get().setPortabilityStatus(portabilityStatus);
            portabilityRepository.save(entity.get());
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Portabilidade não encontrada na base de dados!");
        }
    }
}
