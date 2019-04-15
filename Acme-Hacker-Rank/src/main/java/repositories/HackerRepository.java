package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Hacker;

@Repository
public interface HackerRepository extends JpaRepository<Hacker, Integer> {

	@Query("select h from Hacker h where h.userAccount.id = ?1")
	Hacker findByUserAccountId(int userAccountId);

	@Query("select a.hacker from Application a group by a.hacker order by count(a.hacker) desc")
	Collection<Hacker> hackersWithMoreApplications();
}
