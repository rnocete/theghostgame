package com.ghostgame.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ghostgame.vo.NodeVO;

/**
 * Utility class to load provided dictionary.
 * Implements the ServletContextListener interface.
 * @see ServletContextListener
 */
public class LoadDictionary implements ServletContextListener {

	// Logger
	private static final Logger logger = Logger.getLogger(LoadDictionary.class);
	
	/**
	 * Initializes servlet context.
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent scEvent) {
		logger.info("Initializing context...");
		NodeVO root = new NodeVO();	
		try {
			// Loading dictionary
			root = loadDictionary();	
			
			// Setting dictionary as attribute in servlet context
			scEvent.getServletContext().setAttribute(Constants.DICTIONARY_CONTEXT_NAME, root);
		} catch (IOException ioException) {
			logger.error("Error reading file:  " + ioException.toString());
		} catch (Exception e) {
			logger.error("Error initializing context: " + e);
		}
	}
	
	/**
	 * Destroys servlet context.
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Destroying context.");
		try {
			// Removing attribute from context
			event.getServletContext().removeAttribute(Constants.DICTIONARY_CONTEXT_NAME);
		} catch (Exception e) {
			logger.error("Error destroying context: " + e);
		}
	}

	/**
	 * Loads the dictionary and maps it into a NodeVO tree structure.
	 * 
	 * @return NodeVO Dictionary as tree structure
	 * @throws IOException In case of IO exception
	 */
	private NodeVO loadDictionary() throws IOException {
		// Reading and mapping dictionary
		NodeVO rootNode = new NodeVO();	
		NodeVO parentNode;
		NodeVO prevNode;
		BufferedReader textreader = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/" + Constants.DICTIONARY_FILE_NAME)));
		
		String line;
		while( (line = textreader.readLine()) != null) {
			
			boolean word = false;
			int length = line.length();
			char c = line.charAt(0);
			
			// establish root node for word, has a height of length - 1
			parentNode = addNode(rootNode, c, word, length-1);
			prevNode = parentNode;
			
			// Iterating all letters of the word and building the tree structure
			for(int i = 1; i < length; i++) {
				if (i == length - 1) {
					word =  true;
				}
				prevNode = addNode(prevNode, line.charAt(i), word, length - i - 1);
			}
		}
		return rootNode;
	}
	
	/**
	 * Checks whether the parent node contains the searched child with the given character as key
	 * 
	 * @param parentNode Parent node.
	 * @param currentLetter current letter.
	 * @param word boolean that indicates if the letter completes a word.
	 * @param height
	 * @return
	 */
	private static NodeVO addNode(NodeVO parentNode, char currentLetter, boolean word, int height) {
		NodeVO child = null;
		if ((child = parentNode.getChild(currentLetter)) == null) {
			NodeVO node = new NodeVO(currentLetter, word, height);
			parentNode.addChild(node);
			return node;
		} else {
			
			// Replace the height of child if the currently added word has a smaller length.
			if (height < child.getHeight()){ 
				child.setHeight(height);
			}
			return child;
		}
	}
	

}
