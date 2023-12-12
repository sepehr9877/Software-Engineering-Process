package ca.concordia.soen6011.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import ca.concordia.soen6011.model.Company;

/** 
 * Employers are a type of User. They are saved in the repository.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
