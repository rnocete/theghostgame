package com.ghostgame.vo;

/**
 * Class that holds the response of the controller.
 */
public class ResponseVO {

	private Character letter;
	private Integer status;
	private String message;

	/**
	 * Default constructor.
	 */
	public ResponseVO() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param letter
	 * @param status
	 * @param message
	 */
	public ResponseVO(char letter, Integer status, String message) {
		this();
		this.letter = letter;
		this.status = status;
		this.message = message;
	}

	/**
	 * Gets the letter.
	 * 
	 * @return Character
	 */
	public Character getLetter() {
		return letter;
	}

	/**
	 * Sets the letter.
	 * 
	 * @param letter
	 */
	public void setLetter(Character letter) {
		this.letter = letter;
	}

	/**
	 * Get the status.
	 * 
	 * @return Integer
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Get the message.
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
