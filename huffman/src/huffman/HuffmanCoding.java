import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the entire
 * Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte and NOT
     * as characters of 1 and 0 which take up 8 bits each
     * 
     * @param filename  The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding - 1; i++)
            pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                System.exit(1);
            }

            if (c == '1')
                currentByte += 1 << (7 - byteIndex);
            byteIndex++;

            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }

        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        } catch (Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";

        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();

            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1')
                    return bitString.substring(i + 1);
            }

            return bitString.substring(8);
        } catch (Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /**
     * Reads a given text file character by character, and returns an arraylist of
     * CharFreq objects with frequency > 0, sorted by frequency
     * 
     * @param filename The text file to read from
     * @return Arraylist of CharFreq objects, sorted by frequency
     */
    public static ArrayList<CharFreq> makeSortedList(String filename) {
        double totalChars = 0;
        int[] charArr = new int[128];
        ArrayList<CharFreq> resultList = new ArrayList<>();
        StdIn.setFile(filename);
        while (StdIn.hasNextChar()) {
            charArr[StdIn.readChar()] += 1;
            totalChars++;
        }
        if (totalChars == 0)
            return null;
        else {
            for (int i = 0; i < charArr.length; i++) {
                if (charArr[i] > 0)
                    resultList.add(new CharFreq((char) i, charArr[i] / totalChars));
            }
        }
        if (resultList.size() == 1)
            if ((int) resultList.get(0).getCharacter() == 127)
                resultList.add(new CharFreq((char) 0, 0));
            else
                resultList.add(new CharFreq((char) (1 + resultList.get(0).getCharacter()), 0));
        Collections.sort(resultList);
        System.out.println(resultList.size());
        return resultList;
    }

    /**
     * Uses a given sorted arraylist of CharFreq objects to build a huffman coding
     * tree
     * 
     * @param sortedList The arraylist of CharFreq objects to build the tree from
     * @return A TreeNode representing the root of the huffman coding tree
     */
    public static TreeNode makeTree(ArrayList<CharFreq> sortedList) {
        if (sortedList == null)
            return null;
        Queue<TreeNode> treeQueue = new Queue<>();
        while (treeQueue.size() > 1 || sortedList.size() > 0) {
            TreeNode node1 = getAptNode(sortedList, treeQueue);
            TreeNode node2 = getAptNode(sortedList, treeQueue);
            double newProbOcc = node1.getData().getProbOccurrence() + node2.getData().getProbOccurrence();
            if (node1.getData().getProbOccurrence() <= node2.getData().getProbOccurrence())
                treeQueue.enqueue(new TreeNode(new CharFreq(null, newProbOcc), node1, node2));
            else
                treeQueue.enqueue(new TreeNode(new CharFreq(null, newProbOcc), node2, node1));
        }
        return treeQueue.dequeue();
    }

    private static TreeNode getAptNode(ArrayList<CharFreq> sortedList, Queue<TreeNode> treeQueue) {
        if (treeQueue.isEmpty() && sortedList.isEmpty())
            return null;
        if (treeQueue.isEmpty() && !sortedList.isEmpty())
            return new TreeNode(sortedList.remove(0), null, null);
        if (sortedList.isEmpty() && !treeQueue.isEmpty())
            return treeQueue.dequeue();
        if (sortedList.get(0).getProbOccurrence() <= treeQueue.peek().getData().getProbOccurrence())
            return new TreeNode(sortedList.remove(0), null, null);
        return treeQueue.dequeue();
    }

    /**
     * Uses a given huffman coding tree to create a string array of size 128, where
     * each index in the array contains that ASCII character's bitstring encoding.
     * Characters not present in the huffman coding tree should have their spots in
     * the array left null
     * 
     * @param root The root of the given huffman coding tree
     * @return Array of strings containing only 1's and 0's representing character
     *         encodings
     */
    public static String[] makeEncodings(TreeNode root) {
        String[] result = new String[128];
        ArrayList<String> encodes = new ArrayList<>();
        ArrayList<Character> charVals = new ArrayList<>();
        encode(encodes, charVals, root, "");
        for (int i = 0; i < encodes.size(); i++)
            result[charVals.get(i)] = encodes.get(i);
        return result;
    }

    private static void encode(ArrayList<String> encodes, ArrayList<Character> charVals, TreeNode root, String str) {
        if (root.getLeft() == null && root.getRight() == null) {
            encodes.add(str);
            charVals.add(root.getData().getCharacter());
            return;
        }
        encode(encodes, charVals, root.getLeft(), str + "0");
        encode(encodes, charVals, root.getRight(), str + "1");
    }

    /**
     * Using a given string array of encodings, a given text file, and a file name
     * to encode into, this method makes use of the writeBitString method to write
     * the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodings   The array containing binary string encodings for each
     *                    ASCII character
     * @param textFile    The text file which is to be encoded
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public static void encodeFromArray(String[] encodings, String textFile, String encodedFile) {
        StdIn.setFile(textFile);
        String bitString = "";
        while (StdIn.hasNextChar())
            bitString += "" + encodings[StdIn.readChar()];
        writeBitString(encodedFile, bitString);
    }

    /**
     * Using a given encoded file name and a huffman coding tree, this method makes
     * use of the readBitString method to convert the file into a bit string, then
     * decodes the bit string using the tree, and writes it to a file.
     * 
     * @param encodedFile The file which contains the encoded text we want to decode
     * @param root        The root of your Huffman Coding tree
     * @param decodedFile The file which you want to decode into
     */
    public static void decode(String encodedFile, TreeNode root, String decodedFile) {
        StdOut.setFile(decodedFile);
        String str = readBitString(encodedFile);
        TreeNode tempNode = root;
        while (0 < str.length() || tempNode != root) {
            if (tempNode.getLeft() == null && tempNode.getRight() == null && tempNode.getData().getCharacter() != 0) {
                StdOut.print(tempNode.getData().getCharacter());
                tempNode = root;
                continue;
            } else if (str.substring(0, 1).equals("0"))
                tempNode = tempNode.getLeft();
            else if (str.substring(0, 1).equals("1"))
                tempNode = tempNode.getRight();
            str = str.substring(1);
        }
    }
}