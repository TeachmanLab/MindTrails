package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="Affect")
@EqualsAndHashCode(callSuper = true)
@Data
public class Affect extends LinkedQuestionnaireData {

    @NotNull
    private Integer PosFeelings;

}

