package org.mindtrails.controller;

import org.mindtrails.domain.AngularTraining.AngularTraining;
import org.mindtrails.domain.AngularTraining.AngularTrainingList;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.NoPastProgressException;
import org.mindtrails.domain.RestExceptions.WrongFormException;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.persistence.AngularTrainingRepository;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by dan on 7/7/16.
 */
@Controller
@RequestMapping("/angular")
public class AngularTrainingController extends BaseController {

    @Autowired
    ParticipantService participantService;

    @Autowired
    AngularTrainingRepository trainingRepository;

    private static final Logger LOG = LoggerFactory.getLogger(AngularTrainingController.class);

    @RequestMapping()
    public String showTraining(ModelMap model, Principal principal) {
//        Participant p = participantService.get(principal);
//        model.addAttribute("sessionName", p.getStudy().getCurrentSession().getName());
        return "angularTraining";
    }

    @ExportMode
    @RequestMapping(value="api", method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody
    ResponseEntity<Void> saveProgress(Principal principal,
                 @RequestBody AngularTrainingList list) {

        LOG.info("Recording Progress: " + list.toArray().toString());

        Participant p = getParticipant(principal);

        for(AngularTraining training: list) {
            training.setParticipant(p);
            training.setDate(new Date());
            this.trainingRepository.save(training);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value="api", method = RequestMethod.GET)
    public @ResponseBody AngularTraining getLastRecord(Principal principal) {

        Participant participant = participantService.findByEmail(principal.getName());

        List<AngularTraining> trials = trainingRepository.findAllByParticipantAndSessionOrderByDate(participant,
                participant.getStudy().getCurrentSession().getName());

        if (trials.size() == 0) {
            throw new NoPastProgressException();
        } else {
            return (trials.get(trials.size() - 1));
        }
    }

    @RequestMapping(value="api/scenarios", method = RequestMethod.GET)
    public @ResponseBody List<AngularTraining> getScenarios(Principal principal) {

        Participant participant = participantService.findByEmail(principal.getName());
        String sessionName = participant.getStudy().getCurrentSession().getName();

        List<AngularTraining> trials = trainingRepository.findAllByParticipantAndSessionAndStepTitleOrderById(participant, sessionName, "scenario");
        return trials;
    }


    @RequestMapping(value="api/all_scenarios/{type}", method = RequestMethod.GET)
    public @ResponseBody List<AngularTraining> getScenarios(@PathVariable String type,
                                                            Principal principal) {

        Participant participant = participantService.findByEmail(principal.getName());

        List<AngularTraining> trials = trainingRepository.findAllByParticipantAndTrialType(participant, type);
        return trials;
    }

    @RequestMapping(value = "api/study", method = RequestMethod.GET)
    public @ResponseBody
    Study getCurrentStudy(Principal principal) {
        Participant participant = participantService.findByEmail(principal.getName());

        return(participant.getStudy());

    }


    @RequestMapping("completed")
    public RedirectView markComplete(ModelMap model, Principal principal,
                                     Device device,
                                     @RequestHeader(value="User-Agent", defaultValue="foo") String userAgent) {
        Participant participant = participantService.get(principal);

        // If the data submitted, isn't the data the user should be completing right now,
        // throw an exception and prevent them from moving forward.
        String taskType = participant.getStudy().getCurrentSession().getCurrentTask().getType().toString();
        if(!taskType.equals("angular") && !participant.isAdmin()) {
            String error = "The current task for this participant is : " + taskType + " however, they completed the angular Training";
            LOG.info(error);
            throw new WrongFormException(error);
        }

        // Are they really complete?

        // Fixme: Calculate time spent on the training session
        participant.getStudy().completeCurrentTask(0, device, userAgent);
        participantService.save(participant);

        return new RedirectView("/session/next", true);

    }


}

