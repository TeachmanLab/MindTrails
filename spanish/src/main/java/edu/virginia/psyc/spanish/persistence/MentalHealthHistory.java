package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name="MentalHealthHistory")
@EqualsAndHashCode(callSuper = true)
@Data
public class MentalHealthHistory extends LinkedQuestionnaireData {

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "disorders")
    private List<String> disorders;
    private String otherDisorder;

    @ElementCollection
    @CollectionTable(name = "mental_health_help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "help")
    private List<String> help;

    private int therapy;
    private int medications;
    private int social_support;
    private int self_help;
    private int otc_medications;
    private int other_help;
    private String help_other_text = "";

    @ElementCollection
    @CollectionTable(name = "mental_health_change_help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "change_help")
    private List<String> change_help;
    private String change_help_text;

    @ElementCollection
    @CollectionTable(name = "mental_health_why_no_help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "why_no_help")
    private List<String> why_no_help;
    private String other_why_no_help;

    @NotNull
    private Integer anxiety_duration;
}


