package io.bootify.visitor_managment_system.service;

import io.bootify.visitor_managment_system.domain.Flat;
import io.bootify.visitor_managment_system.domain.User;
import io.bootify.visitor_managment_system.domain.Visit;
import io.bootify.visitor_managment_system.domain.Visitor;
import io.bootify.visitor_managment_system.model.VisitDTO;
import io.bootify.visitor_managment_system.model.VisitStatus;
import io.bootify.visitor_managment_system.repos.FlatRepository;
import io.bootify.visitor_managment_system.repos.UserRepository;
import io.bootify.visitor_managment_system.repos.VisitRepository;
import io.bootify.visitor_managment_system.repos.VisitorRepository;
import io.bootify.visitor_managment_system.util.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static io.bootify.visitor_managment_system.model.VisitStatus.*;


@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;
    private final FlatRepository flatRepository;
    private final UserRepository userRepository;

    public VisitService(final VisitRepository visitRepository,
            final VisitorRepository visitorRepository, final FlatRepository flatRepository,
            final UserRepository userRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
        this.flatRepository = flatRepository;
        this.userRepository = userRepository;
    }

    public List<VisitDTO> findAll() {
        final List<Visit> visits = visitRepository.findAll(Sort.by("id"));
        return visits.stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .toList();
    }

    public List<VisitDTO> findAllWaitingRequests() {
        final List<Visit> visits = visitRepository.findByvisit(WAITING);
        return visits.stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .toList();
    }

    public VisitDTO get(final Long id) {
        return visitRepository.findById(id)
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .orElseThrow(NotFoundException::new);
    }



    public Long create(final VisitDTO visitDTO) throws BadRequestException {
        final Visit visit = new Visit();
        visitDTO.setStatus(VisitStatus.WAITING);
        mapToEntity(visitDTO, visit);
        return visitRepository.save(visit).getId();
    }



    public void update(final Long id, final VisitDTO visitDTO) throws BadRequestException {
        final Visit visit = visitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitDTO, visit);
        visitRepository.save(visit);
    }

    public void updateInTIme(Long visitId){
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if (visit == null){
            throw  new NotFoundException();
        }
        if(visit.getStatus().equals(APPROVED)){
            visit.setInTime(LocalDateTime.now());
            visit.setStatus(INPROGRESS);
            visitRepository.save(visit);
        }
    }

    public void updateOutTime(Long visitId){
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if (visit == null){
            throw  new NotFoundException();
        }
        if(visit.getStatus().equals(INPROGRESS)){
            visit.setOutTIme(LocalDateTime.now());
            visit.setStatus(COMPLETED);
            visitRepository.save(visit);
        }
    }

    public void approve(Long visitId){
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if (visit == null){
            throw  new NotFoundException();
        }
        if(visit.getStatus().equals(WAITING)){
            visit.setStatus(APPROVED);
            visitRepository.save(visit);
        }
    }

    public void reject(Long visitId){
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if (visit == null){
            throw  new NotFoundException();
        }
        if(visit.getStatus().equals(WAITING)){
            visit.setStatus(REJECTED);
            visitRepository.save(visit);
        }
    }



    public void delete(final Long id) {
        visitRepository.deleteById(id);
    }

    private VisitDTO mapToDTO(final Visit visit, final VisitDTO visitDTO) {
        visitDTO.setId(visit.getId());
        visitDTO.setStatus(visit.getStatus());
        visitDTO.setInTime(visit.getInTime());
        visitDTO.setOutTIme(visit.getOutTIme());
        visitDTO.setPurpose(visit.getPurpose());
        visitDTO.setUrlOfImage(visit.getUrlOfImage());
        visitDTO.setNoOfPeople(visit.getNoOfPeople());
        visitDTO.setVisitor(visit.getVisitor() == null ? null : visit.getVisitor().getId());
//        visitDTO.setFlatNumber(visit.getFlat() == null ? null : visit.getFlat().getNumber());
        return visitDTO;
    }

    private Visit mapToEntity(final VisitDTO visitDTO, final Visit visit) throws BadRequestException {
        visit.setStatus(visitDTO.getStatus());
        visit.setInTime(visitDTO.getInTime());
        visit.setOutTIme(visitDTO.getOutTIme());
        visit.setPurpose(visitDTO.getPurpose());
        visit.setUrlOfImage(visitDTO.getUrlOfImage());
        visit.setNoOfPeople(visitDTO.getNoOfPeople());
        final Visitor visitor = visitDTO.getVisitor() == null ? null : visitorRepository.findById(visitDTO.getVisitor())
                .orElseThrow(() -> new NotFoundException("visitor not found"));


        visit.setVisitor(visitor);
        final Flat flat =  flatRepository.findByNumber(visitDTO.getFlatNumber());
        if(flat == null){
            throw  new BadRequestException("Invalid Flat Number");
        }
        visit.setFlat(flat);
        return visit;
    }

    public boolean flatExists(final Long id) {
        return visitRepository.existsByFlatId(id);
    }

    public boolean userExists(final Long id) {
        return visitRepository.existsByUserId(id);
    }

}
