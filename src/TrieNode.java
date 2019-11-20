import java.util.HashMap;

public class TrieNode {
	String value;
	HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();

	public void addChildren(char c, TrieNode tn) {
		this.children.put(c, tn);
	}
}
