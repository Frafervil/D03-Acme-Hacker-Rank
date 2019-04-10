package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

@Entity
@Access(AccessType.PROPERTY)
public class Administrator extends Actor {

	private String email;

	@Pattern(regexp = "^[a-zA-Z0-9 ]*[<]?\\w+[@]")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
}
