package io.bootify.visitor_managment_system.repos;

import io.bootify.visitor_managment_system.domain.Flat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlatRepository extends JpaRepository<Flat, Long> {
    Flat findByNumber(String number);
}
