package edu.virginia.psyc.spanish.controller;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import edu.virginia.psyc.spanish.domain.SpanishStudyForm;
import org.mindtrails.controller.BaseController;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SpanishStudyController extends BaseController {

    @Autowired
    ParticipantService participantService;

    @Autowired
    ParticipantRepository participantRepository;

    private static final String FORM_NAME="studyForm";

    @RequestMapping(value="/admin/study/{id}", method= RequestMethod.GET)
    public String studyUpdateForm(ModelMap model,
                              @PathVariable("id") long id) {

        Participant p = participantRepository.findOne(id);
        SpanishStudyForm form = new SpanishStudyForm((SpanishStudy) p.getStudy());
        model.addAttribute("participant", p);
        model.addAttribute(FORM_NAME, form);
        return "admin/studyUpdate";
    }

    @RequestMapping(value="/admin/study/{id}", method= RequestMethod.POST)
    public String updateStudy(ModelMap model,
                              @PathVariable("id") long id,
                              @ModelAttribute(FORM_NAME) SpanishStudyForm form) {

        Participant p = participantRepository.findOne(id);
        form.updateStudy((SpanishStudy) p.getStudy());
        participantService.save(p);
        return "redirect:/admin";
    }
}
