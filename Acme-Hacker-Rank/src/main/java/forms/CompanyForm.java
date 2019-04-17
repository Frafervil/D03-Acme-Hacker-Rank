package forms;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

public class CompanyForm {

	// --- CompanyAtributes ---------------------------------------------

	private String name;
	private String surname;
	private String photo;
	private String email;
	private String phone;
	private String address;
	private double vatNumber;

	private String commercialName;

	private String username;
	private String password;
	private int id;

	// --- Getters y Setters ---------------------------------------

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9 ]*[<]?\\w+[@][a-zA-Z0-9.]+[>]?$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@NotNull
	public double getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final double vatNumber) {
		this.vatNumber = vatNumber;
	}

	@NotBlank
	public String getCommercialName() {
		return this.commercialName;
	}

	public void setCommercialName(final String commercialName) {
		this.commercialName = commercialName;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	// --- CreditCardAtributes ------------------------------------

	private String holderName;
	private String brandName;
	private String number;
	private int expirationMonth;
	private int expirationYear;
	private int CVV;

	@NotBlank
	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}

	@NotBlank
	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(final String brandName) {
		this.brandName = brandName;
	}

	@NotBlank
	@CreditCardNumber
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@NotNull
	@Range(min = 1, max = 12)
	public int getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	@Range(min = 10, max = 99)
	public int getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final int expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 100, max = 999)
	public int getCVV() {
		return this.CVV;
	}

	public void setCVV(final int cVV) {
		this.CVV = cVV;
	}

	// --- Others -------------------------------------------------

	private String passwordChecker;
	private boolean checkBox;

	public boolean getCheckBox() {
		return this.checkBox;
	}

	public void setCheckBox(final boolean checkBox) {
		this.checkBox = checkBox;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	public String getPasswordChecker() {
		return this.passwordChecker;
	}

	public void setPasswordChecker(final String passwordChecker) {
		this.passwordChecker = passwordChecker;
	}
}
