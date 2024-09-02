package io.bootify.visitor_managment_system.repos;

import io.bootify.visitor_managment_system.domain.Address;
import io.bootify.visitor_managment_system.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor findFirstByAddress(Address address);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneIgnoreCase(String phone);

    boolean existsByIdNumberIgnoreCase(String idNumber);

}
