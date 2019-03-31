
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/terms")
public class TermsController extends AbstractController {

	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	public ModelAndView show() {
		ModelAndView result;
		result = new ModelAndView("terms/terms");
		return result;
	}

	@RequestMapping(value = "/englishTerms", method = RequestMethod.GET)
	public ModelAndView showEn() {
		ModelAndView result;
		result = new ModelAndView("terms/englishTerms");
		return result;
	}
}
