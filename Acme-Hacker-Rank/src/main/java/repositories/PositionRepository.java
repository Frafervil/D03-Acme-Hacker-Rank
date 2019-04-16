
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.company.id = ?1")
	Collection<Position> findByCompanyId(int companyId);

	@Query("select p from Position p where p.company.id = ?1 AND p.status='FINAL'")
	Collection<Position> findAvailableByCompanyId(int companyId);

	@Query("select distinct p from Position p join p.company c join p.skillsRequired s join p.technologiesRequired t where (p.title like %:keyword% or p.description like %:keyword% or p.profileRequired like %:keyword% or s like %:keyword% or t like %:keyword% or c.name like %:keyword%)")
	Collection<Position> findByKeyword(@Param("keyword") String keyword);
	
	@Query("select count(p) from Position p where p.company.id = ?1 AND p.ticker = ?2")
	Integer findRepeatedTickers (int companyId, String ticker);

	@Query("select avg(1.0*(select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double avgPositionsPerCompany();
	
	@Query("select min(1.0*(select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double minPositionsPerCompany();
	
	@Query("select max(1.0*(select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double maxPositionsPerCompany();
	
	@Query("select stddev(1.0*(select count(p) from Position p where p.company.id = c.id)) from Company c")
	Double stddevPositionsPerCompany();
	
	@Query("select avg(p.salaryOffered) from Position p")
	Double avgSalariesOffered();
	
	@Query("select min(p.salaryOffered) from Position p")
	Double minSalariesOffered();
	
	@Query("select max(p.salaryOffered) from Position p")
	Double maxSalariesOffered();
	
	@Query("select stddev(p.salaryOffered) from Position p")
	Double stddevSalariesOffered();
	
	@Query("select p from Position p where p.salaryOffered = (select max(p.salaryOffered) from Position p)")
	Position bestSalaryPosition();
	
	@Query("select p from Position p where p.salaryOffered = (select min(p.salaryOffered) from Position p)")
	Position worstSalaryPosition();
}
