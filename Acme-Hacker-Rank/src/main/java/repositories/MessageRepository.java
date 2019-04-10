package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where m.sender.id = ?1")
	Collection<Message> findBySenderId(int senderId);
	
	@Query("select m from Message m join m.recipients r where r.id = ?1")
	Collection<Message> findByRecipientId(int recipientId);
}