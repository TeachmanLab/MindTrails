package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * This DASS21 questionnaire is used to gage eligibility in the study
 * This data should not be deleted after export, since there is a slight
 * risk that it would get exported between the time it is completed and the
 * moment when we determine eligibility.  Field names in the database are
 * obscured for this reason.
 *
 *
 *  A Note on Translations:  For questionnaires like the DASS21 where the content for
 *  the page is loaded from the Measure Field annotations, you will need to create
 *  entires in the message.properties file that is named specifically after the class
 *  name and the field name (DASS21_AS.dryness=....) These translations should be picked
 *  up automatically without needing to make translations here.
 *  The Scale and Group Descriptions should contain translation keys instead of text.
 *  If you look blow the scale values look like "a_lot_of_the_time" rather than
 *  "A lot of the time".  These are now looked up in the message.properties file. So
 *  that part remains a little manual.
 *
 *
 */
@Entity
@Table(name="DASS21_AS")
@Data
@EqualsAndHashCode(callSuper = true)
@DoNotDelete
public class DASS21_AS extends LinkedQuestionnaireData {


    @Column(name="over18")
    protected String over18 = "N/A";  // Only populated when used for eligibility
    protected String sessionId;

    @Column(name="DRY")
    @MeasureField(order=1, desc="I was aware of dryness of my mouth.", group="problems")
    @NotNull
    private Integer dryness;

    @Column(name="BRE")
    @MeasureField(order=2, desc="I experienced breathing difficulty (e.g., excessively rapid breathing, breathlessness in the absence of physical exertion).", group="problems")
    @NotNull
    private Integer breathing;

    @Column(name="TRE")
    @MeasureField(order=3, desc="I experienced trembling (e.g., in the hands).", group="problems")
    @NotNull
    private Integer trembling;

    @Column(name="WOR")
    @MeasureField(order=4, desc="I was worried about situations in which I might panic and make a fool of myself.", group="problems")
    @NotNull
    private Integer worry;

    @Column(name="PAN")
    @MeasureField(order=5, desc="I felt I was close to panic.", group="problems")
    @NotNull
    private Integer panic;

    @Column(name="HEA")
    @MeasureField(order=6, desc="I was aware of my heart's action in the absence of exercise (e.g., felt heart rate increase, heart missing a beat).", group="problems")
    @NotNull
    private Integer heart;

    @Column(name="SCA")
    @MeasureField(order=7, desc="I felt scared without any good reason.", group="problems")
    @NotNull
    private Integer scared;



    @Override
    public Map<Integer, String> getScale(String scale) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "not_at_all");
        tmpScale.put(1, "sometimes");
        tmpScale.put(2, "a_lot_of_the_time");
        tmpScale.put(3, "most_of_the_time");
        return Collections.unmodifiableMap(tmpScale);
    }

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("problems", "x");
        return Collections.unmodifiableMap(desc);
    }

    public DASS21_AS() {}

    public DASS21_AS(Integer dryness, Integer breathing, Integer trembling, Integer worry, Integer panic, Integer heart, Integer scared) {
        this.dryness = dryness;
        this.breathing = breathing;
        this.trembling = trembling;
        this.worry = worry;
        this.panic = panic;
        this.heart = heart;
        this.scared = scared;
    }

    /**
     * Calculates the eligibility of a participant to be in a particular study
     * You take the average of the 7 item scores, and then multiply by 14
     * (we take the average, rather than the sum to account for missing data).
     * If the resulting # is 10 or higher, they are eligible to participate.
     * NOTE:  a "-1" indicates the question was not answered.
     */
    public double score() {
        Integer    sum   = 0;
        double total = 0.0;

        if(dryness    != 555) { sum += dryness; total++; }
        if(breathing  != 555) { sum += breathing; total++; }
        if(trembling  != 555) { sum += trembling; total++; }
        if(worry      != 555) { sum += worry; total++; }
        if(panic      != 555) { sum += panic; total++; }
        if(heart      != 555) { sum += heart; total++; }
        if(scared     != 555) { sum += scared; total++; }
        if(total == 0) return 0; // Avoid division by 0, the user has a 0 score.
        return((sum / total) * 14.0);
    }

    public boolean eligible() {
        return(this.over18.equals("true") && this.score() > 10 );
    }


    public String calculateSegmentation() {
        if (score() < 26) return "med";
        else return "high";
    }
}

