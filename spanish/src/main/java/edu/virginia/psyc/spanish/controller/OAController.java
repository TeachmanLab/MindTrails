package edu.virginia.psyc.spanish.controller;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import edu.virginia.psyc.spanish.persistence.OA;
import edu.virginia.psyc.spanish.persistence.OARepository;
import edu.virginia.psyc.spanish.service.SpanishEmailService;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * Overrides the basic Question Controller to the handle the OA Submssition - as
 * this form requires us to track the users status through the study and alert
 * the participant as well as send an email to the administrator.
 *
 * */
@Controller
@RequestMapping("questions/OA")
public class OAController extends QuestionController {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(OAController.class);

    @Autowired
    private OARepository oaRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private SpanishEmailService emailService;

    /**
     * Nothing much here, this just redirects back to the main QuestionControllers GET Form logic.
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showOA(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        SpanishStudy study = (SpanishStudy)p.getStudy();
        model.addAttribute("inSessions", study.inSession());
        return showForm(model,principal,"OA");
    }

    @RequestMapping(method = RequestMethod.POST)
    RedirectView handleOA(@ModelAttribute("OA") OA oa,
                          Principal principal,
                          ModelMap model,
                          Device device,
                          @RequestHeader(value="User-Agent", defaultValue="foo") String userAgent,
                          WebRequest request) throws Exception {

        // Save the form, associate it with the participant, and log the
        // process.
        handleForm("OA", request, model, principal, device, userAgent);

        // If the users score differs from there original score and places the user
        // "at-risk", then send a message to the administrator.
        Participant participant = participantService.get(principal);
        List<OA> previous = oaRepository.findByParticipant(participant);
        if(previous.size() > 0) {

            OA firstEntry = Collections.min(previous);
            //Participant participant = participantService.get(principal);
            Study study = participant.getStudy();
            if(oa.atRisk(firstEntry)) {
                if (!(study.getIncreasePercent() > 50)) { // alert admin only the first time.
                    emailService.sendAtRiskAdminEmail(participant, firstEntry, oa);
                    study.setIncreasePercent(oa.getIncrease(firstEntry));
                    participantService.save(participant);
                }

                return new RedirectView("/session/atRisk", true);
            }

        }
        return new RedirectView("/session/next", true);
    }


}
