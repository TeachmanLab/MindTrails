package edu.virginia.psyc.spanish.service;


import edu.virginia.psyc.spanish.domain.SpanishStudy;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpanishTwilioService extends TwilioServiceImpl implements TwilioService {
    public List<ScheduledEvent> messageTypes() {
        List<ScheduledEvent> messages = new ArrayList<>();
        List<String> core_sessions = Arrays.asList(
                SpanishStudy.SECOND_SESSION);


        messages.add(new TextMessage("7day",
                "Time to start your next MindTrails Session! Visit :" + serverUrl,
                null, core_sessions, 7, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("10day",
                "Complete your next MindTrails session soon! Visit :" + serverUrl,
                null, core_sessions, 10, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("14day",
                "It has been two weeks since your last MindTrails session.  Start Now :" + serverUrl,
                null, core_sessions, 14, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("18day",
                "Final reminder about your MindTrails account!  You can start back up here:" + serverUrl,
                null, core_sessions, 18, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("21day",
                "MindTrails account closed. Would you mind completing a quick survey?"
                        + serverUrl + "'/questions/ReasonsForEnding'",
                null, core_sessions, 21, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        return messages;
    }
}

