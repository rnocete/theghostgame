package com.ghostgame.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ghostgame.util.Constants;
import com.ghostgame.vo.NodeVO;
import com.ghostgame.vo.ResponseVO;

/**
 * Main controller class that handles both human and computer requests.
 */
@Controller
public class GhostGameController {
	
	// Logger
	private static final Logger logger = Logger.getLogger(GhostGameController.class);
	
	// Message source
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Sets message source.
	 * 
	 * @param messageSource
	 */
	public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
	// Messages
    private String INVALID_MESSAGE = "";
    private String LOSE_WORD_MESSAGE = "";
    private String LOSE_DICT_MESSAGE = "";
    private String WIN_MESSAGE = "";
    private String HUMAN_TURN_MESSAGE = "";
	
    /**
     * Initializes the attributes and returns the ModelAndView.
     * 
     * @param request
     * @return ModelAndView
     * @throws IOException
     */
	@RequestMapping(value = "ghostgame.htm")
	public ModelAndView goGhostGame(HttpServletRequest request) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Processing request...");
		}

		// Setting attributes
		setAttribute(request, Constants.PARAM_WORD, "");
		setAttribute(request, Constants.PARAM_CURRENT_NODE,
				getAttribute(request, Constants.DICTIONARY_CONTEXT_NAME));
		
		// Setting resource messages
		INVALID_MESSAGE    = getResourceMessage("message.invalid", new Object [] {Constants.MIN_WORD_LENGTH});
		LOSE_WORD_MESSAGE  = getResourceMessage("message.lose.word", null);
		LOSE_DICT_MESSAGE  = getResourceMessage("message.lose.dictionary", null); 
		WIN_MESSAGE        = getResourceMessage("message.win", null);
		HUMAN_TURN_MESSAGE = getResourceMessage("message.humanturn", null);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Returnning ModelAndView object");
		}
		return new ModelAndView("ghostgame");
	}
	
	/**
	 * Handles the human requests and returns the response.
	 * 
	 * @param request
	 * @param nextLetter Retrieved by request in case of human.
	 * @return ResponseVO the response.
	 */
	@RequestMapping(value = "/processHumanLetter.htm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseVO processHumanLetter(HttpServletRequest request, @RequestParam(Constants.PARAM_NEXT_LETTER) Character nextLetter) {

		return processLetter(request, nextLetter, Constants.HUMAN_TURN);
	}
	
	/**
	 * Handles the computer requests and returns the response.
	 * 
	 * @param request
	 * @return ResponseVO the response.
	 */
	@RequestMapping(value = "/processComputerLetter.htm", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseVO processComputerLetter(HttpServletRequest request) {
		
		return processLetter(request, null, Constants.COMPUTER_TURN);
	}

	/**
	 * Process the next letter added and calculates the game status.
	 * 
	 * @param request
	 * @param nextLetter Retrieved by request in case of human and calculated in case of computer.
	 * @param turn Constants.COMPUTER_TURN or Constants.HUMAN_TURN.
	 * @return ResponseVO the response.
	 */
	private ResponseVO processLetter(HttpServletRequest request, Character nextLetter, String turn) {
				
		String letter;
		int result = 0;
		int length = 0;
		String word = getAttribute(request, Constants.PARAM_WORD).toString();
		NodeVO curr_node = (NodeVO) getAttribute(request, Constants.PARAM_CURRENT_NODE);
		
		if (Constants.COMPUTER_TURN.equals(turn)) {
			// Computer turn: next letter is calculated.
			NodeVO node = curr_node.chooseLetter(length);
			letter = Character.toString(node.getLetter());
		} else {
			// Human turn: letter retrieved from request.
			letter = nextLetter.toString();
		}
		setAttribute(request, Constants.PARAM_WORD, word + letter);
		
		// Checking next letter
		result = checkLetter(letter, curr_node, request);
				
		if (result != Constants.STATUS_LETTER_NOWORD) {
			word = word + letter;
		}
		length = word.length();

		// Setting the response according to game status.
		return gameStatus(result, length, turn, letter);
	}

	/** 
	 * Checks through tree for the game's letters. 
	 * 
	 * @param curr_letter Current letter
	 * @param curr_node Current node
	 * @param request
	 * @return int 3 possible integers:
	 * 	0 if the input string is not a word, but part of a word
	 * 	1 if the input string is a word
 	 * 	2 if the input string is neither a word or part of a word
	 **/
	private int checkLetter(String curr_letter, NodeVO curr_node, HttpServletRequest request) {
	
		char c = curr_letter.charAt(0);
		NodeVO node = curr_node.getChild(c);
		int result = Constants.STATUS_LETTER_PART;
		
		if (node == null) {
			// There is no word that finishes with the current letter
			result = Constants.STATUS_LETTER_NOWORD;
		} else {
			
			// Update currently checked node
			curr_node = node;
			setAttribute(request, Constants.PARAM_CURRENT_NODE, curr_node);
			
			if (node.isWord() == true) {
				// letter finishes a word
				result = Constants.STATUS_LETTER_WORD;
			} else {
				// letter is part of a word
				result = Constants.STATUS_LETTER_PART;
			}
		}
		return result;
	}
	
	/** 
	 * Determines whether the game should continue or end.
	 * 
	 * @param result integer result of checkLetter function.
	 * @param length the length of the input string so far.
	 * @param turn Constants.COMPUTER_TURN or Constants.HUMAN_TURN.
	 * @return a boolean determining whether game should end.
	 */
	private ResponseVO gameStatus(int result, int length, String turn, String letter) {
		
		// Default status
		int status = Constants.STATUS_CONTINUE_GAME;
		
		if (result == Constants.STATUS_LETTER_NOWORD) {
			// String is not a word
			logger.info("There's no word with that spelling! HUMAN LOSES!");
			status = Constants.STATUS_NO_WORD;
		}
		
		if (result == Constants.STATUS_LETTER_WORD && length > Constants.MIN_WORD_LENGTH-1) {
			// if input string is a word and has more than 3 letters..
			if (Constants.HUMAN_TURN.equals(turn)) {
				// Human spelled a word: HUMAN LOSES!
				logger.info("Human spelled a word: HUMAN LOSES!");
				status = Constants.STATUS_IS_WORD;
			} else {
				// Computer spelled a word: COMPUTER LOSES!
				logger.info("Computer spelled a word: COMPUTER LOSES!");
				status = Constants.STATUS_WIN;
			}
		} else if (result == Constants.STATUS_LETTER_WORD && length < Constants.MIN_WORD_LENGTH){
			// Input string is a word but is invalid because of its length (<4)
			logger.info("Invalid word");
			status = Constants.STATUS_INVALID_WORD;
		}
		
		return new ResponseVO(letter.charAt(0), status, setStatusMessage(status));
	}
	
		
	/**
	 * Gets an attribute.
	 * 
	 * @param request
	 * @param attribName The attribute name.
	 * @param AttribValue The attribute value to set.
	 */
	private void setAttribute(HttpServletRequest request, String attribName, Object AttribValue) {
		request.getSession().getServletContext().setAttribute(attribName, AttribValue);
	}
	
	/**
	 * Sets an attribute.
	 * 
	 * @param request
	 * @param attribName The attribute name.
	 * @return Object attribute The attribute value.
	 */
	private Object getAttribute(HttpServletRequest request, String attribName) {
		return request.getSession().getServletContext().getAttribute(attribName);
	}
	
	/**
	 * Gets the message from message.properties resources given a key.
	 * 
	 * @param key The key to retrieve from the messages.properties.
	 * @param params Optional params.
	 * @return String with the message.
	 */
	private String getResourceMessage(String key, Object [] params) {
		String message = "";
		message = this.messageSource.getMessage(key, params, null);
		return message;
	}
	
	/**
	 * Returns the appropriate message to show given the game status.
	 * 
	 * @param status current status.
	 * @return String message to show on page.
	 */
	private String setStatusMessage(int status) {
		
		String returnMessage = "";
		switch (status) {
		case 1:  returnMessage = HUMAN_TURN_MESSAGE;
				 break;
        case 2:  returnMessage = LOSE_DICT_MESSAGE;
                 break;
        case 3:  returnMessage = LOSE_WORD_MESSAGE;
                 break;
        case 4:  returnMessage = WIN_MESSAGE;
                 break;
        case 5:  returnMessage = INVALID_MESSAGE;
                 break;
        default: returnMessage = "";
                 break;
		}
		return returnMessage;
	}
	
}