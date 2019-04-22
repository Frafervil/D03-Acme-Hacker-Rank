
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
	
	@Query("select p from Problem p where p.company.id = ?1")
	Collection<Problem> findAllByCompanyId(int companyId);
	
	@Query("select p from Problem p where p.isDraft = 0 AND p.company.id = ?1")
	Collection<Problem> findAllFinalByCompanyId(int companyId);

}
