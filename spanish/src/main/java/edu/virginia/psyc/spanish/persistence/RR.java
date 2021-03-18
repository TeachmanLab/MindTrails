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

/**
 * Created with IntegeregerelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="RR")
@EqualsAndHashCode(callSuper = true)
@Data
public class RR extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order=1, desc="You think that the elevator will probably break down while you are on it.", group="elevator")
    private Integer elevator_NS;
    @NotNull
    @MeasureField(order=2, desc="You think that you are going to like your friend’s new apartment.", group="elevator")
    private Integer elevator_PF;
    @NotNull
    @MeasureField(order=3, desc="You think about how smelly the lobby is.", group="elevator")
    private Integer elevator_NF;
    @NotNull
    @MeasureField(order=4, desc="You think that riding the elevator will be safe.", group="elevator")
    private Integer elevator_PS;

    @NotNull
    @MeasureField(order=5, desc="People in the audience laugh in appreciation.", group="wedding")
    private Integer wedding_PS;
    @NotNull
    @MeasureField(order=6, desc="You notice a friend you were hoping to see walk into the reception.", group="wedding")
    private Integer wedding_PF;
    @NotNull
    @MeasureField(order=7, desc="People in the audience find your efforts laughable.", group="wedding")
    private Integer wedding_NS;
    @NotNull
    @MeasureField(order=8, desc="You notice someone you do not like just walked into the reception.", group="wedding")
    private Integer wedding_NF;

    @NotNull
    @MeasureField(order=9, desc="And know that you can rely on your savings.", group="job")
    private Integer job_PS;
    @NotNull
    @MeasureField(order=10, desc="And are excited about not having to set an alarm.", group="job")
    private Integer job_PF;
    @NotNull
    @MeasureField(order=11, desc="And worry about becoming broke.", group="job")
    private Integer job_NS;
    @NotNull
    @MeasureField(order=12, desc="And are sad about leaving your current coworkers.", group="job")
    private Integer job_NF;

    @NotNull
    @MeasureField(order=13, desc="You feel happy, and think about how lovely your house is.", group="noise")
    private Integer noise_PF;
    @NotNull
    @MeasureField(order=14, desc="You feel afraid, and worry that you cannot handle the fear.", group="noise")
    private Integer noise_NS;
    @NotNull
    @MeasureField(order=15, desc="You feel afraid, but you know that you can tolerate the feeling.", group="noise")
    private Integer noise_PS;
    @NotNull
    @MeasureField(order=16, desc="You feel cold, and think about how the house needs better heating.", group="noise")
    private Integer noise_NF;

    @NotNull
    @MeasureField(order=17, desc="Order your favorite snack.", group="friend")
    private Integer meeting_friend_PF;
    @NotNull
    @MeasureField(order=18, desc="Notice the bar smells gross.", group="friend")
    private Integer meeting_friend_NF;
    @NotNull
    @MeasureField(order=19, desc="Get a call from your friend who is on her way, but running late.", group="friend")
    private Integer meeting_friend_PS;
    @NotNull
    @MeasureField(order=20, desc="Think your friend decided she did not want to see you.", group="friend")
    private Integer meeting_friend_NS;

    @NotNull
    @MeasureField(order=21, desc="Because she thinks you are a slob.", group="lunch")
    private Integer lunch_NS;
    @NotNull
    @MeasureField(order=22, desc="And you frown because you forgot to bring water to lunch.", group="lunch")
    private Integer lunch_NF;
    @NotNull
    @MeasureField(order=23, desc="Because she is paying attention as you describe your weekend plans.", group="lunch")
    private Integer lunch_PS;
    @NotNull
    @MeasureField(order=24, desc="And you smile because your lunch tastes good.", group="lunch")
    private Integer lunch_PF;

    @NotNull
    @MeasureField(order=25, desc="And you think it will probably get seriously infected.", group="scrape")
    private Integer scrape_NS;
    @NotNull
    @MeasureField(order=26, desc="And you are frustrated because you tore your shorts.", group="scrape")
    private Integer scrape_NF;
    @NotNull
    @MeasureField(order=27, desc="But you know you will be okay.", group="scrape")
    private Integer scrape_PS;
    @NotNull
    @MeasureField(order=28, desc="But you are happy that you are getting exercise.", group="scrape")
    private Integer scrape_PF;

    @NotNull
    @MeasureField(order=29, desc="And think you are probably coming down with the strange illness.", group="shopping")
    private Integer shopping_NS;
    @NotNull
    @MeasureField(order=30, desc="And think you are unlikely to catch the strange illness.", group="shopping")
    private Integer shopping_PS;
    @NotNull
    @MeasureField(order=31, desc="And smile because you enjoy shopping.", group="shopping")
    private Integer shopping_PF;
    @NotNull
    @MeasureField(order=32, desc="And feel bored of shopping.", group="shopping")
    private Integer shopping_NF;

    @NotNull
    @MeasureField(order=33, desc="And you think about how nice your doctor is.", group="blood")
    private Integer blood_test_PF;
    @NotNull
    @MeasureField(order=34, desc="And you are annoyed because your doctor is not very friendly.", group="blood")
    private Integer blood_test_NF;
    @NotNull
    @MeasureField(order=35, desc="And you think that you will not be able to stand your anxiety while you wait.", group="blood")
    private Integer blood_test_NS;
    @NotNull
    @MeasureField(order=36, desc="And you know that you can handle your anxiety while you wait.", group="blood")
    private Integer blood_test_PS;

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("elevator", "x");
        desc.put("wedding", "x");
        desc.put("job", "x");
        desc.put("noise", "x");
        desc.put("friend", "x");
        desc.put("lunch", "x");
        desc.put("scrape", "x");
        desc.put("shopping", "x");
        desc.put("blood", "x");
        return Collections.unmodifiableMap(desc);
    }

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "very_different");
        tmpScale.put(2, "different");
        tmpScale.put(3, "similar");
        tmpScale.put(4, "very_similar");
        return Collections.unmodifiableMap(tmpScale);
    }
}
