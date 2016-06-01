/**
 * @(#)HuffmanTree.java
 * This class is used to assign a code to each character
 * It creates a tree of nodes
 * Implements a comparator
 * Nodes contain information for each character
 * @author Jack Hughes
 * @version 1.00 2016/5/31
 */

import java.util.*;

public class HuffmanTree implements Comparable<HuffmanTree>{

	//Root node for the tree
	Node root;

	/**
	 * Initialises tree object
	 * Used to combine two trees
	 * @param treeOne treeTwo
	 */
	public HuffmanTree(HuffmanTree one, HuffmanTree two){
		//set root as null node
		root = new Node();
		/*have root left and right pointers point to
		 *roots of trees taken in as parameters*/
		root.left = one.root;
		root.right = two.root;
		//set frequency of root to be combined frequencies of trees one and two
		root.frequency = one.root.frequency+two.root.frequency;
	}

	/**
	 * Initialises tree object
	 * @param rootFrequency rootCharacter
	 */
	public HuffmanTree(int frequency, char character){
		/*set root node = new node using frequency and
		 *character taken in as parameters*/
		root = new Node(frequency, character);
	}

	/**
	 * Compares tree objects
	 * Used to sort trees by frequency
	 * @param tree
	 * @return -1 0 1
	 */
	public int compareTo(HuffmanTree object){
		if(root.frequency < object.root.frequency)
			return -1;
		else if(root.frequency > object.root.frequency)
			return 1;
		else
			return 0;
	}

	/**
	 * Nested node class
	 * Each node holds character, frequency, code,
	 * left pointer and right pointer
	 * @author Jack Hughes
	 * @version 1.00 2016/5/31
	 */
	public class Node{
		/*Initialise variables
		 *character-holds character which is in string to be compressed
		 *frequency-number of times character appears in string
		 *left and right-point to other nodes
		 *code-string of 1s and 0s that will represent character
		 */
		char character;
		int frequency;
		Node left;
		Node right;
		String code = "";

		/**
		 * Initialises empty node object
		 */
		public Node(){
		}

		/*
		 * Initialises node object
		 * @param frequency character
		 */
		public Node(int frequency, char character){
			//set nodes frequency and character
			this.frequency = frequency;
			this.character = character;
		}
	}
}