package edu.virginia.psyc.spanish.persistence;

import org.mindtrails.persistence.QuestionnaireRepository;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/19/14
 * Time: 4:42 PM
 * This causes Spring to automatically create CRUD operations for the
 * given object:
 *    delete(T entity) which deletes the entity given as a parameter.
 *    findAll() which returns a list of entities.
 *    findOne(ID id) which returns the entity using the id given a parameter as a search criteria.
 *    save(T entity) which saves the entity given as a parameter.
 * Additional methods will be provided automatically by following a standard
 * naming convention, as is the case with findByParticipantDAO
 */
public interface MentalHealthHistoryRepository extends QuestionnaireRepository<MentalHealthHistory> {}
