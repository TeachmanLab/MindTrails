package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name="Acculturation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Acculturation extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order = 1, desc = "In general, what languages(s) do you read and speak?", group = "languages1")
    private Integer general;
    @NotNull
    @MeasureField(order = 2, desc = "What was the language(s) you used as a child?", group = "languages2")
    private Integer child;
    @NotNull
    @MeasureField(order = 3, desc = "What language(s) do you usually speak at home?", group = "languages2")
    private Integer home;
    @NotNull
    @MeasureField(order = 4, desc = "In which language(s) do you usually think?", group = "languages2")
    private Integer think;
    @NotNull
    @MeasureField(order = 5, desc = "What language(s) do you usually speak with your friends?", group = "languages2")
    private Integer speak;
    @NotNull
    @MeasureField(order = 6, desc = "In what language(s) are the TV programs you usually watch?", group = "languages2")
    private Integer tv;
    @NotNull
    @MeasureField(order = 7, desc = "In what language(s) are the radio programs you usually listen to?", group = "languages2")
    private Integer radio;
    @NotNull
    @MeasureField(order = 8, desc = "In general, in what language(s) are the movies, TV and radio programs you prefer to watch and listen to?", group = "languages2")
    private Integer prefer;
    @NotNull
    @MeasureField(order = 9, desc = "Your close friends are:", group = "friends")
    private Integer friends;
    @NotNull
    @MeasureField(order = 10, desc = "You prefer going to social gatherings/parties at which the people are:", group = "friends")
    private Integer parties;
    @NotNull
    @MeasureField(order = 11, desc = "The persons you visit or who visit you are:", group = "friends")
    private Integer visit;
    @NotNull
    @MeasureField(order = 12, desc = "If you could choose your children’s friends, you would want them to be:", group = "friends")
    private Integer choose;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        switch (group) {
            case ("languages1"):
                tmpScale.put(1, "only_spanish");
                tmpScale.put(2, "spanish_better_than_english");
                tmpScale.put(3, "both_equally");
                tmpScale.put(4, "english_better_than_spanish");
                tmpScale.put(5, "only_english");
                break;
            case ("languages2"):
                tmpScale.put(1, "only_spanish");
                tmpScale.put(2, "more_spanish_than_english");
                tmpScale.put(3, "both_equally");
                tmpScale.put(4, "more_english_than_spanish");
                tmpScale.put(5, "only_english");
                break;
            case ("friends"):
                tmpScale.put(1, "all_latinos_hispanics");
                tmpScale.put(2, "more_latinos_than_hispanics");
                tmpScale.put(3, "about_half_and_half");
                tmpScale.put(4, "more_americans_than_hispanics");
                tmpScale.put(5, "all_americans");
                break;
        }
        return Collections.unmodifiableMap(tmpScale);
    }
}



