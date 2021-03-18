package edu.virginia.psyc.spanish.controller;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import edu.virginia.psyc.spanish.persistence.OA;
import edu.virginia.psyc.spanish.persistence.OARepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.mindtrails.controller.BaseController;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/graph")
public class GraphController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(GraphController.class);

    @Autowired private OARepository oaRepository;
    @Autowired private ParticipantService participantService;

    @RequestMapping
    public String graph(ModelMap model, Principal principal) {

        Participant p = participantService.get(principal);
        List<OA> oaList    = oaRepository.findByParticipant(p);
        List<List<Object>> points = new ArrayList();
        List<List<Object>> regressionPoints = new ArrayList();
        SpanishStudy study = (SpanishStudy)p.getStudy();

        Collections.sort(oaList);
        SimpleRegression regression;

        OA original = oaList.get(0);
        OA last     = oaList.get(oaList.size() - 1);

        regression = new SimpleRegression();
        double counter = 0;
        for(OA oa : oaList) {
                // don't include the post assessment when calculating the regression.
                 if (!oa.getSession().startsWith("POST")) {
                    regression.addData(counter, oa.score());
                    counter++;
                }
            }

        // Create plot points
        List<Object> point;
        for(OA oa : oaList) {
            point = new ArrayList<>();
            Session session = study.getSession(oa.getSession());
            point.add(session.getDisplayName());
            point.add(oa.score());
            points.add(point);
            if(oa.equals(original)) {
                ArrayList<Object> rPoint = new ArrayList<>(point);
                rPoint.set(1, regression.getIntercept());
                regressionPoints.add(rPoint);
            }
            if(oa.equals(last)) {
                ArrayList<Object> rPoint = new ArrayList<>(point);
                rPoint.set(1, regression.predict(oaList.size()));
                regressionPoints.add(rPoint);
            }
        }

        int improvement = new Double((regression.getIntercept() - regression.predict(oaList.size()))/regression.getIntercept() * 100).intValue();
        String status = "";
        if(Math.abs(improvement) < 15) status = "same";
        else if (improvement > 30) status = "lot";
        else if (improvement > 15) status = "little";
        else if (improvement < -15) status = "worse";

        model.addAttribute("points", points);
        model.addAttribute("regressionPoints", regressionPoints);
        model.addAttribute("improvement", improvement);
        model.addAttribute("status", status);

        return "graph";
    }

}


