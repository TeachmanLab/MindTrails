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
@Table(name="Evaluation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Evaluation extends LinkedQuestionnaireData {

    @NotNull
    private Integer sessionDuration;

    @ElementCollection
    @CollectionTable(name= "evaluation_preferredPlatform", joinColumns = @JoinColumn(name="id"))
    @Column(name = "preferredPlatform")
    private List<String> preferredPlatform;

    @ElementCollection
    @CollectionTable(name = "evaluation_devices", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "devices")
    private List<String> devices;

    @NotNull
    @MeasureField(order = 1, desc = "How helpful did you find MindTrails for reducing your anxiety?", group = "block1")
    private Integer helpful;
    @NotNull
    @MeasureField(order = 2, desc = "How much did MindTrails improve your overall quality of life?", group = "block1")
    private Integer quality;
    @NotNull
    @MeasureField(order = 3, desc = "How much did MindTrails help improve your overall mood (e.g., feeling happier)?", group = "block1")
    private Integer overallMood;
    @NotNull
    @MeasureField(order = 4, desc = "How likely would you be to recommend MindTrails to others with similar anxiety difficulties?", group="block1")
    private Integer recommend;
    @NotNull
    @MeasureField(order = 5, desc = "How likely would you be to recommend MindTrails to others who are Hispanic and have anxiety difficulties?", group="block1")
    private Integer recommendHispanic;
    @NotNull
    @MeasureField(order = 6, desc = "To what extent did these stories reflect situations that are important to you?", group="block1")
    private Integer situationsYou;
    @NotNull
    @MeasureField(order = 7, desc = "To what extent did these stories reflect situations that are important to your family and community?", group="block1")
    private Integer situationsFamily;
    @NotNull
    @MeasureField(order = 8, desc = "How easy was MindTrails to use?", group = "block1")
    private Integer easy;
    @NotNull
    @MeasureField(order = 9, desc = "How much did MindTrails keep your interest and attention?", group = "block1")
    private Integer interest;
    @NotNull
    @MeasureField(order = 10, desc = "How much did you like MindTrails in general?", group = "block1")
    private Integer likeGral;
    @NotNull
    @MeasureField(order = 11, desc = "How much did you like the way MindTrails looked?", group = "block1")
    private Integer likedLooks;
    @NotNull
    @MeasureField(order = 12, desc = "How worried were you about your privacy in using MindTrails?", group = "block2")
    private Integer privacy;
    @NotNull
    @MeasureField(order = 13, desc = "How easy were the assessments to understand?", group = "block2")
    private Integer understandAssessment;
    @NotNull
    @MeasureField(order = 14, desc = "How easy was the training program (including instructions and materials) to understand?", group = "block2")
    private Integer understandTraining;
    @NotNull
    @MeasureField(order = 15, desc = "How much did you feel you could trust the information?", group = "block2")
    private Integer trustInfo;
    @NotNull
    @MeasureField(order = 16, desc = "On average, while you were completing the training sessions, how focused were you?", group = "block2")
    private Integer focused;
    @NotNull
    @MeasureField(order = 17, desc = "On average, while you were completing the training sessions, how  distracted were you?", group = "block2")
    private Integer distracted;

    @NotNull
    private Integer problems;

    private String problemsDesc;

    @NotNull
    private String idealSessions;
    @NotNull
    private Integer similarProgram;
    @NotNull
    private Integer otherTreatment;

    @ElementCollection
    @CollectionTable(name = "evaluation_places", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "places")
    private List<String> places;
    private String otherPlace;

    @Column(name="evalCondition") // 'Condition' is a reserved word in some databases.
    @NotNull
    private Integer condition = 999;  // 999 = No longer collected

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
            tmpScale.put(1, "not_at_all");
            tmpScale.put(2, "slightly");
            tmpScale.put(3, "somewhat");
            tmpScale.put(4, "mostly");
            tmpScale.put(5, "very");

        return Collections.unmodifiableMap(tmpScale);
    }
}



