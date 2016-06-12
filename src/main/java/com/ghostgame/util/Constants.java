package com.ghostgame.util;

public class Constants {

	// GAME STATUS
	public static final int STATUS_CONTINUE_GAME = 1;
	public static final int STATUS_NO_WORD = 2;
	public static final int STATUS_IS_WORD = 3;
	public static final int STATUS_WIN = 4;
	public static final int STATUS_INVALID_WORD = 5;
	
	// LETTER STATUS
	// string is not a word, but part of a word
	public static final int STATUS_LETTER_PART = 0;
	// string is a word
	public static final int STATUS_LETTER_WORD = 1;
	// string is neither a word or part of a word
	public static final int STATUS_LETTER_NOWORD = 2;

	// WORD MINIMAL LENGTH
	public static final int MIN_WORD_LENGTH = 4;

	// NAMES FOR REQUEST PARAMETERS
	public static final String DICTIONARY_CONTEXT_NAME = "dictionaryCtx";
	public static final String DICTIONARY_FILE_NAME = "word.lst";
	public static final String PARAM_WORD = "word";
	public static final String PARAM_NEXT_LETTER = "nextLetter";
	public static final String PARAM_CURRENT_NODE = "currentNode";
	
	// TURNS
	public static final String HUMAN_TURN = "H";
	public static final String COMPUTER_TURN = "C";
	
	

}
