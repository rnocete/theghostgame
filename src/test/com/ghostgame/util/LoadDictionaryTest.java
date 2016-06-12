package com.ghostgame.util;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockServletContext;

/**
 * Test class for LoadDictionary.
 * 
 * @author Rafa Nocete
 *
 */
@RunWith(JUnit4.class)
public class LoadDictionaryTest {

	private static final Logger logger = Logger.getLogger(LoadDictionaryTest.class);

	MockServletContext servletContext;
	ServletContextEvent event;

	@Before
	public void setUp() {
		servletContext = new MockServletContext();
		event = new ServletContextEvent(servletContext);
	}

	@Test
	public void loadDictionaryTest() {
		logger.info("loadDictionaryTest");
		LoadDictionary dictionary = new LoadDictionary();

		// Testing if DICTIONARY_CONTEXT_NAME attribute is not null after context
		// initialization
		dictionary.contextInitialized(event);
		Assert.assertNotNull(event.getServletContext());
		Assert.assertNotNull(event.getServletContext().getAttribute(Constants.DICTIONARY_CONTEXT_NAME));

		// Testing if DICTIONARY_CONTEXT_NAME attribute is null after context is destroyed
		dictionary.contextDestroyed(event);
		Assert.assertNull(event.getServletContext().getAttribute(Constants.DICTIONARY_CONTEXT_NAME));
	}

}
