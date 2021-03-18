package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.mindtrails.domain.questionnaire.MeasureField;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Entity
@Table(name="SessionReview")
@EqualsAndHashCode(callSuper = true)
@Data
public class SessionReview extends LinkedQuestionnaireData {

    @NotNull
    private Integer location;
    private String otherLocationDesc;

    @NotNull
    @MeasureField(order=1, desc="I found the program was easy to interact with.", group="group1")
    private Integer easyInteract;

    @NotNull
    @MeasureField(order=2, desc="While completing the session, I was:", group="group2")
    private Integer distracted;

    @ElementCollection
    @CollectionTable(name = "sessionReview_distractions", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "distractions")
    private List<String> distractions;
    private String otherDistraction;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        if (group.equals("group1")) {
            tmpScale.put(1, "Totally disagree");
            tmpScale.put(2, "empty");
            tmpScale.put(3, "empty");
            tmpScale.put(4, "empty");
            tmpScale.put(5, "Totally agree");
        } else {
            tmpScale.put(1, "Not distracted at all");
            tmpScale.put(2, "empty");
            tmpScale.put(3, "empty");
            tmpScale.put(4, "empty");
            tmpScale.put(5, "Very distracted");
        }
        return Collections.unmodifiableMap(tmpScale);
    }

}
