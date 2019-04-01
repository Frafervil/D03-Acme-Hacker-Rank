
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity {

	private String				title;
	private String				description;
	private Date				deadline;
	private String				profileRequired;
	private Collection<String>	skillRequired;
	private Collection<String>	technologiesRequired;
	private Double				salaryOffered;
	private String				ticker;
	private String				status;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@NotBlank
	public String getProfileRequired() {
		return this.profileRequired;
	}

	public void setProfileRequired(final String profileRequired) {
		this.profileRequired = profileRequired;
	}

	@ElementCollection
	@EachNotBlank
	public Collection<String> getSkillRequired() {
		return this.skillRequired;
	}

	public void setSkillRequired(final Collection<String> skillRequired) {
		this.skillRequired = skillRequired;
	}

	@ElementCollection
	@EachNotBlank
	public Collection<String> getTechnologiesRequired() {
		return this.technologiesRequired;
	}

	public void setTechnologiesRequired(final Collection<String> technologiesRequired) {
		this.technologiesRequired = technologiesRequired;
	}

	@NotNull
	public Double getSalaryOffered() {
		return this.salaryOffered;
	}

	public void setSalaryOffered(final Double salaryOffered) {
		this.salaryOffered = salaryOffered;
	}

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|31(?!(?: 0[2469]|11))|30(?!02))-[A-Z0-9]{6}$")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	@Pattern(regexp = "^DRAFT|FINAL|CANCELLED$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}
}
