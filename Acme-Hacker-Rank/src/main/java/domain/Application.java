
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Application extends DomainEntity {

	private Date	moment;
	private String	status;


	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	@Pattern(regexp = "^PENDING|APPROVED|SUBMITTED|REJECTED$")
	public String getStatus() {
		return this.status;
	}
	public void setStatus(final String status) {
		this.status = status;
	}


	// Relationships----------------------------------------------

	private Answer		answer;
	private Problem		problem;
	private Hacker		hacker;
	private Position	position;


	@Valid
	@OneToOne(optional = true, cascade = CascadeType.ALL)
	public Answer getAnswer() {
		return this.answer;
	}

	public void setAnswer(final Answer answer) {
		this.answer = answer;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Problem getProblem() {
		return this.problem;
	}

	public void setProblem(final Problem problem) {
		this.problem = problem;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Hacker getHacker() {
		return this.hacker;
	}

	public void setHacker(final Hacker hacker) {
		this.hacker = hacker;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

}
