package edu.virginia.psyc.spanish.service;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import edu.virginia.psyc.spanish.persistence.*;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.Conditions.RandomConditionRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.MissingEligibilityException;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.mindtrails.service.TangoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class SpanishParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final String ELIGIBLE_SESSION = "ELIGIBLE";

    String UNASSIGNED_SEGMENT = "unassigned";

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    DASS21_ASRepository dass21RRepository;

    @Autowired
    OARepository oaRepository;

    @Autowired
    DemographicsRepository demographicsRepository;

    @Autowired
    RandomConditionRepository randomBlockRepository;


    @Autowired
    TangoService tangoService;

    @Override
    public Participant create() {
        Participant p = new Participant();
        SpanishStudy study = new SpanishStudy();
        p.setReceiveGiftCards(tangoService.getEnabled());
        study.setReceiveGiftCards(tangoService.getEnabled());
        p.setStudy(study);
        return p;
    }

     @Override
     public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        // No segmentation needed on our end for Spanish - will be pre-assigned.
         throw new NoNewConditionException();
     }

    @Override
    public void markConditionAsUsed(RandomCondition rc) {
        // Not used. Conditions are pre-assigned and passed in.
    }


    @Override
    public Page<Participant> findEligibleForCoaching(Pageable pageable) {
        // Returns nothing, as Spanish doesn't do coaching
        return this.participantRepository.findEligibleForCoachingWithOptIn("NA", pageable);
    }

    @Override
    public Page<Participant> searchEligibleForCoaching(Pageable pageable, String searchTerm) {
        // Returns nothing, as Spanish doesn't do coaching
        return this.participantRepository.searchEligibleForCoachingWithOptIn("NA", pageable, searchTerm);
    }


    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new SpanishStudy());
        return studies;
    }

    @Override
    public boolean isEligible(HttpSession session) {
        // Safeguard in case someone finds the original account creation path, account/create
        // This will kick them back into the AccountController, which is set to redirect to the eligibilityCheck,
        // which simply produces an error for the Kaiser study.
        if (session.getAttribute("condition") == null) {
            return false;
        }
        return true; 
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {

        // Set the participants condition based on the session attribute.
        p.getStudy().setConditioning((String)session.getAttribute("condition"));
        save(p); // Just save the participant

        // Generally we would connect any elegibility scores back to the participant at this point, but kaiser does not have this issue.
    }
}