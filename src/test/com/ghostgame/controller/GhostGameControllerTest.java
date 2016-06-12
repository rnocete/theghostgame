package com.ghostgame.controller;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import com.ghostgame.util.Constants;
import com.ghostgame.util.LoadDictionary;

/**
 * Test class for GhostGameController.
 * 
 * @author Rafa
 *
 */
@RunWith(JUnit4.class)
public class GhostGameControllerTest {

	private static final Logger logger = Logger.getLogger(GhostGameControllerTest.class);

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	MockServletContext servletContext;
	ServletContextEvent event;

	@InjectMocks
	GhostGameController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		servletContext = new MockServletContext();
		event = new ServletContextEvent(servletContext);

		LoadDictionary listener = new LoadDictionary();
		listener.contextInitialized(event);

		request.getSession().getServletContext().setAttribute(Constants.PARAM_CURRENT_NODE,
				event.getServletContext().getAttribute(Constants.DICTIONARY_CONTEXT_NAME));
		request.getSession().getServletContext().setAttribute(Constants.PARAM_WORD, "");
	}

	/**
	 * Testing if string matched a word on dictionary.
	 */	 
	@Test
	public void isWordTest() {
		// It is a word ('love')
		logger.info("isWordTest");
		Assert.assertNotNull(controller.processHumanLetter(request, 'l'));
		Assert.assertNotNull(controller.processHumanLetter(request, 'o'));
		Assert.assertNotNull(controller.processHumanLetter(request, 'v'));
		Assert.assertEquals(controller.processHumanLetter(request, 'e').getStatus().intValue(),
				Constants.STATUS_IS_WORD);
	}
	
	/**
	 * Testing if string does not matches a word on dictionary.
	 */
	@Test
	public void isNotWordTest() {
		// It is not a word ('xx')
		logger.info("isNotWordTest");
		Assert.assertNotNull(controller.processHumanLetter(request, 'x'));
		Assert.assertEquals(controller.processHumanLetter(request, 'x').getStatus().intValue(),
				Constants.STATUS_NO_WORD);
	}

	/**
	 * Testing if string matches a word on dictionary but its length is minor than 4 characters.
	 */
	@Test
	public void isInvalidWordTest() {
		// Is an invalid word ('dah'is an existing word but length < 4)
		logger.info("isInvalidWordTest");
		Assert.assertNotNull(controller.processHumanLetter(request, 'd'));
		Assert.assertNotNull(controller.processHumanLetter(request, 'a'));
		Assert.assertEquals(controller.processHumanLetter(request, 'h').getStatus().intValue(),
				Constants.STATUS_INVALID_WORD);
	}

	/**
	 * Testing if computer answers with a letter after human's turn.
	 */
	@Test
	public void computerControllerTest() {
		logger.info("computerControllerTest");
		controller.processHumanLetter(request, 'h');
		Assert.assertNotNull(controller.processComputerLetter(request).getLetter());
	}

}
