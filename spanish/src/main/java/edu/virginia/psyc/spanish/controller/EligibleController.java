package edu.virginia.psyc.spanish.controller;

import edu.virginia.psyc.spanish.persistence.DASS21_AS;
import edu.virginia.psyc.spanish.persistence.DASS21_ASRepository;
import edu.virginia.psyc.spanish.persistence.OA;
import edu.virginia.psyc.spanish.persistence.OARepository;
import org.mindtrails.controller.BaseController;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;


/**
* Verify that a new person is eligible for participating in the study.
 */
@Controller
public class EligibleController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(EligibleController.class);

    // Eligibility form is saved to the session for retrieval when the user it found.
    private static final String BIAS_SESSION = "bias";

    @Autowired
    private DASS21_ASRepository dassRepository;

    @Autowired
    private OARepository oaRepository;

    @Autowired
    private ParticipantService participantService;


    private Validator validator;

    public EligibleController() {
        super();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @RequestMapping("public/eligibility")
    public String showEligibility(ModelMap model) {
        // Template will set a difference form action if this variable is set to true.
        OA oa = new OA();
        model.addAttribute("model", oa);
        model.addAttribute("eligibility", true);
        return "questions/OA";
    }


    @RequestMapping(value = "public/eligibilityCheck", method = RequestMethod.POST)
    public String checkEligibility(@ModelAttribute("OA") OA oa,
                                   ModelMap model, HttpSession session) {

        oa.setSessionId(session.getId());
        oa.setDate(new Date());
        oaRepository.save(oa);

        DASS21_AS dass = new DASS21_AS();
        model.addAttribute("model", dass);
        model.addAttribute("eligibility", true);
        return "questions/DASS21_AS";
    }

    @RequestMapping(value = "public/eligibilityCheckStep2", method = RequestMethod.POST)
    public String checkEligibilityStep2(@ModelAttribute("DASS21as") DASS21_AS dass,
                                        ModelMap model, HttpSession session) {

        dass.setSessionId(session.getId());
        dass.setDate(new Date());
        if (!dass.getOver18().equals("true")) {  // Deal with null and empty responses.
            dass.setOver18("false");
        }
        Set<ConstraintViolation<DASS21_AS>> violations = validator.validate(dass);
        if (!violations.isEmpty()) {
            model.addAttribute("model", dass);
            model.addAttribute("eligibility", true);
            model.addAttribute("error", "Please complete all required fields.");
            return "questions/DASS21_AS";
        } else {
            dassRepository.save(dass);
            if (participantService.isEligible(session)) {
                return "invitation";
            } else {
                return "ineligible";
            }
        }
    }
}