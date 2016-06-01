/**
 * @(#)HuffmanMain.java
 * This class compresses a string via huffman encoding
 * It has 11 methods, including the main method
 * @author Jack Hughes
 * @version 1.02 2016/5/31
 */

import java.util.*;

public class HuffmanMain{

	/**
	 * Main method asks the user to enter a string
	 * then displays to console the binary form of the string,
	 * how maany times each character occurs,
	 * the code assigned to each character,
	 * the huffman encoded version of the string,
	 * and the compression ratio
	 */
	public static void main(String[] args){
		//Take in input string from the user
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter string to be compressed:");
		String input = scan.nextLine();

		//Print binary form of the input string
		System.out.println();
		printAsBinary(input);

		//Get and print frequencies of the characters
		int[] ascii = characterFrequency(input);
		System.out.println();
		printFrequencies(input,ascii);

		//Generate huffman tree and codes
		HuffmanTree tree = getHuffman(ascii);
		String[] codes = getCodes(tree.root);

		//Print codes
		System.out.println();
		printCodes(ascii,codes);

		//Put characters and their codes in hash map
		HashMap<Character,String> map = setHashMap(ascii,codes);

		//Compress input string and print output
		String output = compressedString(input,map);
		System.out.println();
		System.out.println(output);

		//Calculate new number of bits
		int bits = output.replace(" ","").length();

		/* Print original number of bits, new number of bits,
		 * compression ratio, and decompressed string
		 */
		System.out.println("\nOriginal number of bits: "+input.length()*7);
		System.out.println("New number of bits: "+bits);
		double ratio=(bits*100.0)/(input.length()*7.0);
		System.out.printf("\nThe text has been compressed to %.2f percent of it's original size.",ratio);
		System.out.println("\nDecompressed string(original text) : "+decompress(map,output));
	}

	/**
	 * Prints the binary form of a given string
	 * @param string
	 */
	public static void printAsBinary(String input){
		for(int i = 0; i < input.length(); i++){
			//print binary of current character
			int decimal = (int)input.charAt(i);
			String binary = Integer.toBinaryString(decimal);
			for(int j = 7; j > binary.length(); j--)
				binary += "0";
			System.out.print(binary+" ");
		}
	}

	/**
	 * Prints the characters and their frequencies
	 * @param string intArray
	 */
	public static void printFrequencies(String input, int[] ascii){
		//prints characters in order of frequency (most to least)
		for(int i = input.length(); i > 0; i--){
			for(int j = 0; j < ascii.length; j++){
				//if character is in string, print character and frequency
				if(ascii[j] == i)
					System.out.println("'"+(char)j+"' appeared "+ascii[j]+((ascii[j] == 1)?" time":" times"));
			}
		}
	}

	/**
	 * Prints the characters and their codes
	 * @param intArray stringArray
	 */
	public static void printCodes(int[] ascii, String[] codes){
		for(int i = 0; i < ascii.length; i++){
			//if character is in input string, print character and code
			if(ascii[i] != 0)
				System.out.println((char)i+" : "+codes[i]);
		}
	}

	/**
	 * Puts frequencies of characters of given string in an array
	 * @param string
	 * @return intArray
	 */
	public static int[] characterFrequency(String input){
		int[] ascii = new int[256];
		for(int i = 0; i < input.length(); i++)
			//increment frequency of current character
			ascii[(int)input.charAt(i)]++;
		return ascii;
	}

	/**
	 * Compresses input string with given codes
	 * @param string hashmap
	 * @return string
	 */
	public static String compressedString(String input,HashMap<Character,String> map){
		String output="";
		for(int i = 0; i < input.length(); i++)
			//add code of current character to string
			output += map.get(input.charAt(i))+" ";
		return output;
	}

	/**
	 * Puts characters and their codes in hash map
	 * @param intArray stringArray
	 * @return hashmap
	 */
	public static HashMap<Character,String> setHashMap(int[] ascii, String[] codes){
		//create hash map
		HashMap<Character,String> map = new HashMap<Character,String>();
		//put characters and their codes in hash map
		for(int i = 0; i < ascii.length; i++){
			if(ascii[i] != 0)
				map.put((char)i,codes[i]);
		}
		return map;
	}

	/**
	 * Generates a huffman tree from given frequencies
	 * @param intArray
	 * @return huffmanTree
	 */
	public static HuffmanTree getHuffman(int[] ascii){
		//create priority queue of huffman trees, prioritises by size
		PriorityQueue<HuffmanTree> PQ = new PriorityQueue<HuffmanTree>();
		//add huffman tree to priority queue for each character
		for(int i = 0; i < ascii.length; i++){
			if(ascii[i] > 0)
				PQ.add(new HuffmanTree(ascii[i],(char)i));
		}

		//combine trees until there is only one left
		while(PQ.size() > 1){
			HuffmanTree t1 = PQ.remove();
			HuffmanTree t2 = PQ.remove();
			PQ.add(new HuffmanTree(t1,t2));
		}

		//return final huffman tree
		return PQ.remove();
	}

	/**
	 * Gets character codes from huffman tree
	 * @param rootNode
	 * @return stringArray
	 */
	public static String[] getCodes(HuffmanTree.Node root){
		//if empty tree return null
		if(root == null)
			return null;
		String[] codes = new String[256];
		//assign codes to nodes
		assignCode(root,codes);
		int charnum = 0;
		//calculates number of characters
		for(int i = 0; i < codes.length; i++){
			if(codes[i] != null)
				charnum++;
		}
		//if only 1 character, give it the code "0"
		if(charnum == 1)
			codes[(int)root.character] = "0";
		return codes;
	}

	/**
	 * Assigns codes to nodes in tree
	 * @param rootNode stringArray
	 */
	public static void assignCode(HuffmanTree.Node root, String[] codes){
		if(root.left == null){
			//if leaf node, put code in array
			codes[(int)root.character] = root.code;
		}else{
			//append 0 to the node on the left
			root.left.code = root.code+"0";
			assignCode(root.left,codes);
			//append 1 to the node on the right
			root.right.code = root.code+"1";
			assignCode(root.right,codes);
		}
	}

	/**
	 * Decompresses string using codes
	 * @param hashmap string
	 * @return string
	 */
	public static String decompress(HashMap<Character,String> map,String in){
		//split string into array
		String[] chars=in.split(" ");
		String out="";
		//loop through array
		for(int i = 0; i < chars.length; i++){
			for(Character key : map.keySet()){
				//add character to output string
				if(map.get(key).equals(chars[i]))
					out += key;
			}
		}
		return out;
	}
}