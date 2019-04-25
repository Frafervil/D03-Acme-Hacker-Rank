package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml", })
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private AdministratorService administratorService;

	/*
	 * Percentage of service tested: AdministratorService 29,8%
	 */

	// --------------------------------------------------

	/*
	 * Requirement Tested: 11.1- Create user accounts for new administrators.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un administrator puede crear otra
	 * cuenta de administrator
	 */

	@Test
	public void createTestFail() {
		final Object createTest[][] = { {
		/*
		 * Test negativo: Alguien sin loguear intenta crear una cuenta de
		 * administrador
		 */
		null, "Lorenzo", "Rondán Domínguez", "http://mifoto.com",
				"+34912123123", "C/ A nº1 Sevilla", 1.0, "loren@", "admin",
				"admin", "Lorenzo Domínguez", "MasterCard",
				"5220 2777 7103 1876", 7, 19, 701,
				IllegalArgumentException.class } };
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0],
					(String) createTest[i][1], (String) createTest[i][2],
					(String) createTest[i][3], (String) createTest[i][4],
					(String) createTest[i][5], (double) createTest[i][6],
					(String) createTest[i][7], (String) createTest[i][8],
					(String) createTest[i][9], (String) createTest[i][10],
					(String) createTest[i][11], (String) createTest[i][12],
					(int) createTest[i][13], (int) createTest[i][14],
					(int) createTest[i][15], (Class<?>) createTest[i][16]);
	}

	@Test
	public void createTestSuccess() {
		final Object createTest[][] = { {
		/*
		 * Test positivo: Un administrador crea otra cuenta de administrador
		 */
		"admin", "Nueva", "Nueva Nueva", "http://mifoto2.com", "+34910323123",
				"C/ B nº2 Sevilla", 2.0, "nueva@", "admin20", "admin20",
				"Nueva Nueva", "MasterCard", "5169 8379 5973 7918", 8, 21, 703,
				null } };
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0],
					(String) createTest[i][1], (String) createTest[i][2],
					(String) createTest[i][3], (String) createTest[i][4],
					(String) createTest[i][5], (double) createTest[i][6],
					(String) createTest[i][7], (String) createTest[i][8],
					(String) createTest[i][9], (String) createTest[i][10],
					(String) createTest[i][11], (String) createTest[i][12],
					(int) createTest[i][13], (int) createTest[i][14],
					(int) createTest[i][15], (Class<?>) createTest[i][16]);
	}

	/*
	 * Requirement Tested: 8.2- Edit his or her personal data.
	 */

	/*
	 * En este test se va a probar que sólo un administrator puede modificar su
	 * propio perfil
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
				{
				/*
				 * Test negativo: Un hacker intenta modificar el perfil del
				 * administrador
				 */
				"hacker1", "administrator1", "other name",
						IllegalArgumentException.class }, {
				/*
				 * Test positivo: Poner otro nombre
				 */
				"admin", "administrator1", "other name", null } };
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0],
					(String) updateTest[i][1], (String) updateTest[i][2],
					(Class<?>) updateTest[i][3]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String name,
			final String surname, final String photo, final String phone,
			final String address, final double vatNumber, final String email,
			final String username, final String password,
			final String holderName, final String brandName,
			final String number, final int month, final int year,
			final int cvv, final Class<?> class1) {
		Class<?> caught;
		Administrator admin;

		caught = null;
		try {
			this.authenticate(actor);
			admin = this.administratorService.create();
			admin.setName(name);
			admin.setSurname(surname);
			admin.setPhoto(photo);
			admin.setPhone(phone);
			admin.setAddress(address);
			admin.setVatNumber(vatNumber);
			admin.setEmail(email);

			admin.getUserAccount().setUsername(username);
			admin.getUserAccount().setPassword(password);

			admin.getCreditCard().setHolderName(holderName);
			admin.getCreditCard().setBrandName(brandName);
			admin.getCreditCard().setCVV(cvv);
			admin.getCreditCard().setExpirationMonth(month);
			admin.getCreditCard().setExpirationYear(year);
			admin.getCreditCard().setNumber(number);

			final Administrator saved = this.administratorService.save(admin);
			this.unauthenticate();
			this.administratorService.flush();
			Assert.notNull(this.administratorService.findOne(saved.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing,
			final String name, final Class<?> class1) {
		Class<?> caught;
		Administrator admin;

		caught = null;
		try {
			this.authenticate(actor);
			admin = this.administratorService.findOne(super.getEntityId(thing));
			admin.setName(name);
			this.administratorService.save(admin);
			this.unauthenticate();
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
