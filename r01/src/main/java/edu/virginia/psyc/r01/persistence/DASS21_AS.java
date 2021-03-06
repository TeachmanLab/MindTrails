package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.map.HashedMap;
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
    @MeasureField(order=1, desc="I was aware of dryness of my mouth.")
    @NotNull
    private Integer dryness;

    @Column(name="BRE")
    @MeasureField(order=2, desc="I experienced breathing difficulty (e.g., excessively rapid breathing, breathlessness in the absence of physical exertion).")
    @NotNull
    private Integer breathing;

    @Column(name="TRE")
    @MeasureField(order=3, desc="I experienced trembling (e.g., in the hands).")
    @NotNull
    private Integer trembling;

    @Column(name="WOR")
    @MeasureField(order=4, desc="I was worried about situations in which I might panic and make a fool of myself.")
    @NotNull
    private Integer worry;

    @Column(name="PAN")
    @MeasureField(order=5, desc="I felt I was close to panic.")
    @NotNull
    private Integer panic;

    @Column(name="HEA")
    @MeasureField(order=6, desc="I was aware of my heart's action in the absence of exercise (e.g., felt heart rate increase, heart missing a beat).")
    @NotNull
    private Integer heart;

    @Column(name="SCA")
    @MeasureField(order=7, desc="I felt scared without any good reason.")
    @NotNull
    private Integer scared;



    @Override
    public Map<Integer, String> getScale(String scale) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "Not at all");
        tmpScale.put(1, "Sometimes");
        tmpScale.put(2, "A lot of the time");
        tmpScale.put(3, "Most of the time");
        return Collections.unmodifiableMap(tmpScale);
    }

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("", "Over the last week, how often have you been bothered by any of the following problems?");
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

