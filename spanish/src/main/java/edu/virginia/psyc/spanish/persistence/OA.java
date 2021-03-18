package edu.virginia.psyc.spanish.persistence;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This Oasis questionnaire is used over time in the study to determine
 * participant progress.  So while it can be exported through the admin
 * panel, the data should not be deleted.  To help make things a little
 * safer, we obscure the names of the fields while the data is stored in
 * the database.
 */
@Entity
@Table(name="OA")
@Data
@EqualsAndHashCode(callSuper = true)
@DoNotDelete
public class OA extends LinkedQuestionnaireData implements Comparable<OA> {

    private static final Logger LOG = LoggerFactory.getLogger(OA.class);

    public static int NO_ANSWER = 555;
    public static final int MAX_SCORE = 4;

    protected String sessionId; // OA and DASS are both used for eligibility with TET study extension to R01

    @Column(name="AXF")
    @NotNull
    private Integer anxious_freq;
    @Column(name="AXS")
    @NotNull
    private Integer anxious_sev;
    @Column(name="AVO")
    @NotNull
    private Integer avoid;
    @Column(name="WRK")
    @NotNull
    private Integer interfere;
    @Column(name="SOC")
    @NotNull
    private Integer interfere_social;

    public OA(int anxious_freq, int anxious_sev, int avoid, int interfere, int interfere_social) {
        this.anxious_freq=anxious_freq;
        this.anxious_sev = anxious_sev;
        this.avoid = avoid;
        this.interfere = interfere;
        this.interfere_social = interfere_social;
        this.date = new Date();
    }

    public OA(){}

    public double score() {

        int sum = 0;
        double total = 0.0;

        if(anxious_freq != NO_ANSWER) { sum += anxious_freq; total++;}
        if(anxious_sev != NO_ANSWER) { sum += anxious_sev; total++;}
        if(avoid != NO_ANSWER) { sum += avoid; total++;}
        if(interfere != NO_ANSWER) { sum += interfere; total++;}
        if(interfere_social != NO_ANSWER) { sum += interfere_social; total++;}

        if (total == 0) return 0;
        return(sum / total) * 5;
    }

    public boolean eligible() {
        return(this.score() >= 6 );
    }


    private boolean noAnswers() {
        return(
                anxious_freq == NO_ANSWER &&
                anxious_sev == NO_ANSWER &&
                avoid == NO_ANSWER &&
                interfere == NO_ANSWER &&
                interfere_social == NO_ANSWER
        );
    }

    public boolean atRisk(OA original) {
      if(original.noAnswers()) return false;
      return (score() / original.score()) > 1.5;
    }

    public double getIncrease(OA original) {
        return (score() / original.score());
    }

    @Override
    public int compareTo(OA o) {
        return date.compareTo(o.date);
    }

    @Override
    public Map<String,Object> modelAttributes(Participant p) {
        Map<String, Object> attributes = new HashMap<>();

        if (p.getStudy() instanceof SpanishStudy) {
            SpanishStudy study = (SpanishStudy) p.getStudy();
            attributes.put("inSessions", study.inSession());
        } else {
            attributes.put("inSessions", false);
        }
        return attributes;
    }

}
