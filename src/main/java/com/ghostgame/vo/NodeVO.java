package com.ghostgame.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * NodeVO class: is a node of this tree structure.
 */
public class NodeVO {

	// Node children
	Map<Character, NodeVO> children;
	// letter is the letter the node represents
	private char letter;
	// word is a boolean of whether the path to this node represents a full word
	private boolean word;
	// height is the number of nodes away from the closest full word
	private int height;

	/**
	 * Default constructor.
	 */
	public NodeVO() {
		children = new HashMap<Character, NodeVO>();
	}

	/**
	 * Constructor.
	 * 
	 * @param letter current letter
	 * @param word word in process
	 * @param height the distance from either a leaf node or a letter node that ends a word.
	 */
	public NodeVO(char letter, boolean word, int height) {
		this();
		this.letter = letter;
		this.word = word;
		this.height = height;
	}

	/**
	 * Chooses a letter node based on the height of the node. 
	 * That is, the distance from either a leaf node or a letter node that ends a word.
	 * 
	 * @param curr_length current number of letters in the game.
	 * @return The computer's chosen node
	 */
	public NodeVO chooseLetter(int curr_length) {

		Character key;
		NodeVO node = null;
		int max = 0;

		// boolean that determines whether winning is a possibility
		boolean winning = false;
		ArrayList<NodeVO> winning_nodes = new ArrayList<NodeVO>();
		ArrayList<NodeVO> loosing_nodes = new ArrayList<NodeVO>();
		
		Iterator<Character> keys = children.keySet().iterator();

		while (keys.hasNext()) {

			key = (Character) keys.next();
			node = children.get(key);

			int child_height = node.getHeight();

			// computer wants to pick a child node
			// of height 1. That is, choose a node that
			// is one node away from a word so human loses.
			// choose nodes that satisfies this condition.
			boolean choose = chooseNode(child_height);

			if (choose) {
				winning_nodes.add(node);
				winning = true;
			} else if (child_height > max) {

				// if there are no nodes with height of 1,
				// choose the node with the largest height to
				// prolong the game as much as possible.
				max = child_height;
				if(loosing_nodes.isEmpty()) {
					loosing_nodes.add(node);
				} else {
					loosing_nodes.clear();
					loosing_nodes.add(node);
				} 
			}
		}

		// Randomly selecting a winner node
		Random r = new Random();
		int random_index = 0; 
		if (winning) {
			random_index = r.nextInt(winning_nodes.size());
			node = winning_nodes.get(random_index);
		} else {
			// If not winning nodes selecting loser node
			if (!loosing_nodes.isEmpty()) {
				random_index = r.nextInt(loosing_nodes.size());
				node = loosing_nodes.get(random_index);
			}
		}
		return node;
	}

	/**
	 * Chooses the right node: computer wants to pick a child node of height 1. 
	 * That is, choose a node that is one node away from a word so human loses.
	 * 
	 * @param height height of the node we're checking for
	 * @return a boolean of whether the node should be picked or not based on given height
	 */
	private boolean chooseNode(int height) {
		if (height % 2 == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the children.
	 * 
	 * @return Map<Character,NodeVO>
	 */
	public Map<Character, NodeVO> getChildren() {
		return children;
	}

	/**
	 * Sets the children.
	 * 
	 * @param children
	 */
	public void setChildren(HashMap<Character, NodeVO> children) {
		this.children = children;
	}

	/**
	 * Adds a child
	 * 
	 * @param n
	 *            NodeVO
	 */
	public void addChild(NodeVO n) {
		this.children.put(n.getLetter(), n);
	}

	/**
	 * Gets a child.
	 * 
	 * @param letter
	 * @return NodeVO
	 */
	public NodeVO getChild(char letter) {
		return children.get(letter);
	}

	/**
	 * Gets a letter.
	 * 
	 * @return char
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * Sets a letter.
	 * 
	 * @param letter
	 */
	public void setLetter(char letter) {
		this.letter = letter;
	}

	/**
	 * Gets is a word.
	 * 
	 * @return boolean
	 */
	public boolean isWord() {
		return word;
	}

	/**
	 * Sets a word.
	 * 
	 * @param word
	 */
	public void setWord(boolean word) {
		this.word = word;
	}

	/**
	 * Gets the height.
	 * 
	 * @return int
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Sets the height.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
