package ca.concordia.soen6011.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.concordia.soen6011.model.Interview;

public interface InterviewRepository extends JpaRepository<Interview,Integer> {

}
