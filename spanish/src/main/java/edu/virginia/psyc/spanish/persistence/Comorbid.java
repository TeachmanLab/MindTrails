package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


@Entity
@Table(name="comorbid")
@EqualsAndHashCode(callSuper = true)
@Data
public class Comorbid extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order=1, desc="Little interest or pleasure in doing things", group="bothered")
    private Integer pleasure;
    @NotNull
    @MeasureField(order=2, desc="Feeling down, depressed, or hopeless", group="bothered")
    private Integer depressed;

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("bothered", "x");
        return Collections.unmodifiableMap(desc);
    }

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        switch (group) {
            case("bothered"):
                tmpScale.put(1, "comorbid_not_at_all");
                tmpScale.put(2, "several_days");
                tmpScale.put(3, "more_than_half_of_the_days");
                tmpScale.put(4, "nearly_every_day");
                break;
        }
        return Collections.unmodifiableMap(tmpScale);
    }
}

