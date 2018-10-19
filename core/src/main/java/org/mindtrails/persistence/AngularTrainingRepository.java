package org.mindtrails.persistence;

import org.mindtrails.domain.AngularTraining.AngularTraining;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AngularTrainingRepository extends JpaRepository<AngularTraining, Long> {

    List<AngularTraining> findAllByParticipantAndSessionOrderByDate(Participant participant,
                                                            String session);
    List<AngularTraining> findDistinctByParticipantIn(List<Long> participants);
}
