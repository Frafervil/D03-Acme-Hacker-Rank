package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class PositionServiceTest extends AbstractTest {
	// Service under test

	@Autowired
	private PositionService	positionService;
	

	/*
	 *  Percentage of service tested: 56,3 %
	 * 
	 */

	// --------------------------------------------------

	/*
	 *	9. An actor who is authenticated as a company must be able to:
	 *		1. Manage their positions, which includes listing, showing, creating, updating, and deleting them. Positions can be saved in draft mode; they are not available publicly
	 *		until they are saved in final mode. Once a position is saved in final mode, it cannot
	 *		be further edited, but it can be cancelled. A position cannot be saved in final mode
	 *		unless there are at least two problems associated with it.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como company
	 * puede crear una position
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como administrador
				 */
				"admin", IllegalArgumentException.class
			}, {
				/*
				 * Test negativo:
				 * Probamos como hacker
				 */
				"hacker1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como company
				 */
				"company1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (Class<?>) createTest[i][1]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como company
	 * puede actualizar una position
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Dejar el campo de descripción vacío
				 */
				"company1", "position1", null,"DRAFT", ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Introducir otra descripción
				 */
				"company1", "position1", "other description","DRAFT", null
			}, {
				/*
				 * Test positivo:
				 * Guardar una position DRAFT como FINAL
				 */
				"company1", "position4", "other description","FINAL", null
			}, {
				/*
				 * Test positivo:
				 * Guardar una position FINAL como CANCELLED
				 */
				"company1", "position2", "other description","CANCELLED", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (String) updateTest[i][3], (Class<?>) updateTest[i][4]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como company
	 * puede eliminar una position
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Alguien sin autentificar intenta eliminar una position
				 */
				null, "position1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * La company al que le pertenece el  lo elimina
				 */
				"company1", "position1", null
			}, {
				/*
				 * Test negativo:
				 * Una company a la que no pertenece lo intenta eliminar
				 */
				"company2", "position1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], super.getEntityId((String) deleteTest[i][1]), (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.positionService.create();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String description,final String saveMode, final Class<?> class1) {
		Class<?> caught;
		Position position;
		caught = null;
		try {
			this.authenticate(actor);
			position = this.positionService.findOne(super.getEntityId(thing));
			position.setDescription(description);
			this.positionService.save(position, saveMode);
			this.unauthenticate();
			this.positionService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final int thing, final Class<?> class1) {
		Class<?> caught;
		Position position;

		caught = null;
		try {
			this.authenticate(actor);
			position = this.positionService.findOne(thing);
			this.positionService.delete(position);
			this.positionService.flush();
			this.unauthenticate();
			Assert.isTrue(!this.positionService.exist(position.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}


}