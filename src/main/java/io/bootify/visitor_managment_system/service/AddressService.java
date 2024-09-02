package io.bootify.visitor_managment_system.service;

import io.bootify.visitor_managment_system.domain.Address;
import io.bootify.visitor_managment_system.domain.User;
import io.bootify.visitor_managment_system.domain.Visitor;
import io.bootify.visitor_managment_system.model.AddressDTO;
import io.bootify.visitor_managment_system.repos.AddressRepository;
import io.bootify.visitor_managment_system.repos.UserRepository;
import io.bootify.visitor_managment_system.repos.VisitorRepository;
import io.bootify.visitor_managment_system.util.NotFoundException;
import io.bootify.visitor_managment_system.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final VisitorRepository visitorRepository;
    private final UserRepository userRepository;

    public AddressService(final AddressRepository addressRepository,
            final VisitorRepository visitorRepository, final UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.visitorRepository = visitorRepository;
        this.userRepository = userRepository;
    }

    public List<AddressDTO> findAll() {
        final List<Address> addresses = addressRepository.findAll(Sort.by("id"));
        return addresses.stream()
                .map(address -> mapToDTO(address, new AddressDTO()))
                .toList();
    }

    public AddressDTO get(final Long id) {
        return addressRepository.findById(id)
                .map(address -> mapToDTO(address, new AddressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getId();
    }

    public void update(final Long id, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    public void delete(final Long id) {
        addressRepository.deleteById(id);
    }

    public AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setLine1(address.getLine1());
        addressDTO.setLine2(address.getLine2());
        addressDTO.setPincode(address.getPincode());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setLine1(addressDTO.getLine1());
        address.setLine2(addressDTO.getLine2());
        address.setPincode(addressDTO.getPincode());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        return address;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Visitor addressVisitor = visitorRepository.findFirstByAddress(address);
        if (addressVisitor != null) {
            referencedWarning.setKey("address.visitor.address.referenced");
            referencedWarning.addParam(addressVisitor.getId());
            return referencedWarning;
        }
        final User addressUser = userRepository.findFirstByAddress(address);
        if (addressUser != null) {
            referencedWarning.setKey("address.user.address.referenced");
            referencedWarning.addParam(addressUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
