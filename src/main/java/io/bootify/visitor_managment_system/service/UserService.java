package io.bootify.visitor_managment_system.service;

import io.bootify.visitor_managment_system.domain.Address;
import io.bootify.visitor_managment_system.domain.Flat;
import io.bootify.visitor_managment_system.domain.User;
import io.bootify.visitor_managment_system.domain.Visit;
import io.bootify.visitor_managment_system.model.AddressDTO;
import io.bootify.visitor_managment_system.model.UserDTO;
import io.bootify.visitor_managment_system.model.UserStatus;
import io.bootify.visitor_managment_system.repos.AddressRepository;
import io.bootify.visitor_managment_system.repos.FlatRepository;
import io.bootify.visitor_managment_system.repos.UserRepository;
import io.bootify.visitor_managment_system.repos.VisitRepository;
import io.bootify.visitor_managment_system.util.NotFoundException;
import io.bootify.visitor_managment_system.util.ReferencedWarning;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final FlatRepository flatRepository;
    private final AddressRepository addressRepository;
    private final VisitRepository visitRepository;

    public UserService(final UserRepository userRepository, final FlatRepository flatRepository,
            final AddressRepository addressRepository, final VisitRepository visitRepository) {
        this.userRepository = userRepository;
        this.flatRepository = flatRepository;
        this.addressRepository = addressRepository;
        this.visitRepository = visitRepository;
    }

    @Autowired
    public AddressService addressService;

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void markInactive(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new NotFoundException("User does not exist");
        }
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);

    }

    public void markActive(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new NotFoundException("User does not exist");
        }
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {

        userRepository.deleteById(id);

    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setRole(user.getRole());
        userDTO.setStatus(user.getStatus());
        Flat flat = user.getFlat();
        if(flat!=null){
            userDTO.setFlatNumber(flat.getNumber());
        }

        AddressDTO addressDTO = new AddressDTO();
        addressService.mapToDTO(user.getAddress(), addressDTO);
        userDTO.setAddress(addressDTO);
        return userDTO;
    }


    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());
        user.setStatus(userDTO.getStatus());
        Flat flat = flatRepository.findByNumber(userDTO.getFlatNumber());
        if(flat == null){
            flat = new Flat();
            flat.setNumber(userDTO.getFlatNumber());
            flatRepository.save(flat);
        }

        user.setFlat(flat);
        final Address address = new Address();
        addressService.mapToEntity(userDTO.getAddress(), address);
        addressRepository.save(address);
        user.setAddress(address);
        return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneExists(final String phone) {
        return userRepository.existsByPhoneIgnoreCase(phone);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Visit userVisit = visitRepository.findFirstByUser(user);
        if (userVisit != null) {
            referencedWarning.setKey("user.visit.user.referenced");
            referencedWarning.addParam(userVisit.getId());
            return referencedWarning;
        }
        return null;
    }

}
