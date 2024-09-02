package io.bootify.visitor_managment_system.repos;

import io.bootify.visitor_managment_system.domain.Address;
import io.bootify.visitor_managment_system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByAddress(Address address);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneIgnoreCase(String phone);

}
