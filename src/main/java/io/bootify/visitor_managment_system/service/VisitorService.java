package io.bootify.visitor_managment_system.service;

import io.bootify.visitor_managment_system.domain.Address;
import io.bootify.visitor_managment_system.domain.Visit;
import io.bootify.visitor_managment_system.domain.Visitor;
import io.bootify.visitor_managment_system.model.AddressDTO;
import io.bootify.visitor_managment_system.model.VisitorDTO;
import io.bootify.visitor_managment_system.repos.AddressRepository;
import io.bootify.visitor_managment_system.repos.VisitRepository;
import io.bootify.visitor_managment_system.repos.VisitorRepository;
import io.bootify.visitor_managment_system.util.NotFoundException;
import io.bootify.visitor_managment_system.util.ReferencedWarning;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final AddressRepository addressRepository;
    private final VisitRepository visitRepository;

    public VisitorService(final VisitorRepository visitorRepository,
            final AddressRepository addressRepository, final VisitRepository visitRepository) {
        this.visitorRepository = visitorRepository;
        this.addressRepository = addressRepository;
        this.visitRepository = visitRepository;
    }

    @Autowired
    private AddressService addressService;

    public List<VisitorDTO> findAll() {
        final List<Visitor> visitors = visitorRepository.findAll(Sort.by("id"));
        return visitors.stream()
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .toList();
    }

    public VisitorDTO get(final Long id) {
        return visitorRepository.findById(id)
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VisitorDTO visitorDTO) {
        final Visitor visitor = new Visitor();
        mapToEntity(visitorDTO, visitor);
        return visitorRepository.save(visitor).getId();
    }

    public void update(final Long id, final VisitorDTO visitorDTO) {
        final Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitorDTO, visitor);
        visitorRepository.save(visitor);
    }

    public void delete(final Long id) {
        visitorRepository.deleteById(id);
    }

    private VisitorDTO mapToDTO(final Visitor visitor, final VisitorDTO visitorDTO) {
        visitorDTO.setId(visitor.getId());
        visitorDTO.setEmail(visitor.getEmail());
        visitorDTO.setName(visitor.getName());
        visitorDTO.setPhone(visitor.getPhone());
        visitorDTO.setIdNumber(visitor.getIdNumber());
        AddressDTO addressDTO = new AddressDTO();
        addressService.mapToDTO(visitor.getAddress(), addressDTO);
        visitorDTO.setAddress(addressDTO);
        return visitorDTO;
    }

    private Visitor mapToEntity(final VisitorDTO visitorDTO, final Visitor visitor) {
        visitor.setEmail(visitorDTO.getEmail());
        visitor.setName(visitorDTO.getName());
        visitor.setPhone(visitorDTO.getPhone());
        visitor.setIdNumber(visitorDTO.getIdNumber());
        final Address address = new Address();
        addressService.mapToEntity(visitorDTO.getAddress(),address);
        addressRepository.save(address);
        visitor.setAddress(address);
        return visitor;
    }

    public boolean emailExists(final String email) {
        return visitorRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneExists(final String phone) {
        return visitorRepository.existsByPhoneIgnoreCase(phone);
    }

    public boolean idNumberExists(final String idNumber) {
        return visitorRepository.existsByIdNumberIgnoreCase(idNumber);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Visit visitorVisit = visitRepository.findFirstByVisitor(visitor);
        if (visitorVisit != null) {
            referencedWarning.setKey("visitor.visit.visitor.referenced");
            referencedWarning.addParam(visitorVisit.getId());
            return referencedWarning;
        }
        return null;
    }

}
