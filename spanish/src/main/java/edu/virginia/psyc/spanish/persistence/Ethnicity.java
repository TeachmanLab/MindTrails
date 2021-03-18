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
@Table(name="Ethnicity")
@EqualsAndHashCode(callSuper = true)
@Data
public class Ethnicity extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order = 1, desc = "I have spent time trying to find out more about my ethnic group, such as its history, traditions, and customs.", group = "block1")
    private Integer history;
    @NotNull
    @MeasureField(order = 2, desc = "I am active in organizations or social groups that include mostly members of my own ethnic group.", group = "block1")
    private Integer organizations;
    @NotNull
    @MeasureField(order = 3, desc = "I have a clear sense of my ethnic background and what it means for me.", group = "block1")
    private Integer background;
    @NotNull
    @MeasureField(order = 4, desc = "I think a lot about how my life will be affected by my ethnic group membership.", group = "block1")
    private Integer life;
    @NotNull
    @MeasureField(order = 5, desc = "I am happy that I am a member of the group I belong to.", group = "block1")
    private Integer happy;
    @NotNull
    @MeasureField(order = 6, desc = "I have a strong sense of belonging to my own ethnic group.", group = "block1")
    private Integer belonging;
    @NotNull
    @MeasureField(order = 7, desc = "I understand pretty well what my ethnic group membership means to me.", group = "block2")
    private Integer understand;
    @NotNull
    @MeasureField(order = 8, desc = "In order to learn about my ethnic background, I have often talked to other people about my ethnic group.", group = "block2")
    private Integer learn;
    @NotNull
    @MeasureField(order = 9, desc = "I have a lot of pride in my ethnic group.", group = "block2")
    private Integer pride;
    @NotNull
    @MeasureField(order = 10, desc = "I participate in cultural practices of my own group, such as special food, music, or customs.", group = "block2")
    private Integer practices;
    @NotNull
    @MeasureField(order = 11, desc = "I feel a strong attachment towards my own ethnic group.", group = "block2")
    private Integer attachment;
    @NotNull
    @MeasureField(order = 12, desc = "I feel good about my cultural or ethnic background.", group = "block2")
    private Integer feel;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
                tmpScale.put(1, "strongly_disagree");
                tmpScale.put(2, "disagree");
                tmpScale.put(3, "in_the_middle");
                tmpScale.put(4, "agree");
                tmpScale.put(5, "strongly_agree");
        return Collections.unmodifiableMap(tmpScale);
    }
}



