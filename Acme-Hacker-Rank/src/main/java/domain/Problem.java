
package domain;

import java.util.Collection;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Problem extends DomainEntity {

	private String				title;
	private String				statement;
	private String				hint;
	private Collection<String>	attachment;
	private boolean	isDraft;

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	@NotBlank
	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	
	@ElementCollection
	@EachNotBlank
	public Collection<String> getAttachment() {
		return attachment;
	}

	public void setAttachment(Collection<String> attachment) {
		this.attachment = attachment;
	}

	public boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}




	// Relationships----------------------------------------------


	private Company	company;
	private Collection<Position> positions;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}
	
	@NotNull
	@Valid
	@ManyToMany 
	public Collection<Position> getPositions() {
		return this.positions;
	}

	public void setPositions(final Collection<Position> positions) {
		this.positions = positions;
	}


}
