package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class DashboardServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private PositionService	positionService;
	/*
	 *  Percentage of service tested:
	 * 
	 */

	// --------------------------------------------------

	// Tests

	/*
	 * Requirement Tested:
	 * 11. An actor who is authenticated as an administrator must be able to:
	 * 2. Display a dashboard.
	 * 
	 * En este test probamos que unicamente un usuario logueado como admin puede hacer uso
	 * de los servicios del dashboard.
	 * Creamos un driver con distintos tipos de usuarios y lo probamos.
	 */

	@Test
	public void authorityTest() {
		final Object authorityTest[][] = {
			{
				"admin", null
			}, {
				"hacker1", IllegalArgumentException.class
			}, {
				"company1", IllegalArgumentException.class
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < authorityTest.length; i++)
			this.AuthorityTemplate((String) authorityTest[i][0], (Class<?>) authorityTest[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	private void AuthorityTemplate(final String string, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(string);
			this.positionService.avgPositionsPerCompany();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	/*
	 * Requirement Tested:
	 * Probamos que los resultados devueltos por los test del calculo de estadisticas
	 * sean los esperados en base a nuestro populate.
	 */

	@Test
	public void valueTest() {
		final Object valueTest[][] = {
			{
				"admin", "avg", 1.33333, null
			}, {
				"admin", "min", 0.0, null
			}, {
				"admin", "max", 3.0, null
			}, {
				"admin", "stddev", 1.24722, null
			}, {
				"hacker1", "avg", 1.33333, IllegalArgumentException.class
			}, {
				"hacker1", "min", 0.0, IllegalArgumentException.class
			}, {
				"hacker1", "max", 3.0, IllegalArgumentException.class
			}, {
				"hacker1", "stddev", 1.24722, IllegalArgumentException.class
			}, {
				"company1", "avg", 1.33333, IllegalArgumentException.class
			}, {
				"company1", "min", 0.0, IllegalArgumentException.class
			}, {
				"company1", "max", 3.0, IllegalArgumentException.class
			}, {
				"company1", "stddev", 1.24722, IllegalArgumentException.class
		}
		};
		for (int i = 0; i < valueTest.length; i++)
			this.ValueTemplate((String) valueTest[i][0], (String) valueTest[i][1], (Double) valueTest[i][2], (Class<?>) valueTest[i][3]);

	}

	private void ValueTemplate(final String actor, final String string, final Double double1, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		Double value = 0.0;
		try {
			this.authenticate(actor);
			if (string == "avg") {
				value = this.positionService.avgPositionsPerCompany();
				Assert.isTrue(value.equals(double1));
			} else if (string == "min") {
				value = this.positionService.minPositionsPerCompany();
				Assert.isTrue(value.equals(double1));
			} else if (string == "max") {
				value = this.positionService.maxPositionsPerCompany();
				Assert.isTrue(value.equals(double1));
			} else if (string == "stddev") {
				value = this.positionService.stddevPositionsPerCompany();
				Assert.isTrue(value.equals(double1));
			}
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
