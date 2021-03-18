package edu.virginia.psyc.spanish.controller;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import org.mindtrails.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class SpanishAccountController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SpanishAccountController.class);

    @RequestMapping(path="/public/spanish", method = RequestMethod.GET)
    public String setCondition(HttpSession session,
                               final @RequestParam(value = "condition", required = false) String condition) {

        if (condition == null || condition.length() == 0) {
            return "eligibility";
        }

        // Check if one of the six randomization conditions
        // Protects against someone trying to join study without being randomize
        // boolean isAllowableCondition = Arrays.stream(KaiserStudy.CONDITION.values()).anyMatch((t) -> t.name().equals(condition));
        boolean isAllowableCondition = SpanishStudy.conditionMappings.containsKey(condition);
        if (!isAllowableCondition) {
            return "eligibility";
        } else {

            // In case of Kaiser study, store condition as session attribute then
            // continue into regular account creation method in the AccountController
            session.setAttribute("condition", SpanishStudy.conditionMappings.get(condition).name());
            LOG.info("Condition is " + session.getAttribute("condition"));
            return "redirect:/account/create";
        }
    }
}


