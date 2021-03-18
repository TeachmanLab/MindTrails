package edu.virginia.psyc.spanish.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * For updating the Spanish study settings for a participant.
 */
@Data
public class SpanishStudyForm {

    private String conditioning;
    private String session;
    private int subtractDays = 0;

    public SpanishStudyForm() {}

    public SpanishStudyForm(SpanishStudy study)  {

        this.conditioning = study.getConditioning();
        this.session = study.getCurrentSession().getName();
    }

    public void updateStudy(SpanishStudy study) {
        study.setConditioning(this.conditioning);
        study.setCurrentSession(this.session);
        study.setLastSessionDate(DateTime.now().minus(Days.days(subtractDays)).toDate());
    }

}
