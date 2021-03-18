package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Random;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="Demographics")
@EqualsAndHashCode(callSuper = true)
@Data
public class Demographics extends LinkedQuestionnaireData {
    @NotNull
    private Integer BirthYear;
    @NotNull
    private String Ethnicity;
    @NotNull
    private String Country;
    @NotNull
    private String Education;
    @NotNull
    private String MaritalStat;
    @NotNull
    private String EmploymentStat;
    @NotNull
    private String Income;
    @NotNull
    private String PtpReason;
    @NotNull
    private String PtpReasonOther;
    @NotNull
    private String Gender;

    @ElementCollection
    @CollectionTable(name = "demographics_race", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "race")
    @NotNull
    private List<String> Race;

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.


    public String calculateSegmentation() {
        if(this.getGender().toLowerCase().equals("male")) {
            return "male";
        } else if (this.getGender().toLowerCase().equals("female")) {
            return "female";
        } else {
            // Randomly assign all other gender responses to male or female.
           if(RANDOM.nextInt(2) == 1) {  // Will be either 0 or 1.
               return "male";
           } else {
               return "female";
           }
        }
    }
}