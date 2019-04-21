
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
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
	private Collection<String>	skillsRequired;
	private Collection<String>	technologiesRequired;
	private double				salaryOffered;
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
	public Collection<String> getSkillsRequired() {
		return this.skillsRequired;
	}

	public void setSkillsRequired(final Collection<String> skillsRequired) {
		this.skillsRequired = skillsRequired;
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
	public double getSalaryOffered() {
		return this.salaryOffered;
	}

	public void setSalaryOffered(final Double salaryOffered) {
		this.salaryOffered = salaryOffered;
	}

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{4}-([0-9]{4})$")
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


	// Relationships----------------------------------------------

	private Company	company;
	private Collection<Problem> problems;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	@Valid
	@ManyToMany
	public Collection<Problem> getProblems() {
		return problems;
	}

	public void setProblems(Collection<Problem> problems) {
		this.problems = problems;
	}

}
