package io.bootify.visitor_managment_system.repos;

import io.bootify.visitor_managment_system.domain.User;
import io.bootify.visitor_managment_system.domain.Visit;
import io.bootify.visitor_managment_system.domain.Visitor;
import io.bootify.visitor_managment_system.model.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {

    Visit findFirstByVisitor(Visitor visitor);

    Visit findFirstByUser(User user);

    boolean existsByFlatId(Long id);

    boolean existsByUserId(Long id);

    List<Visit> findByvisit(VisitStatus status);

}
