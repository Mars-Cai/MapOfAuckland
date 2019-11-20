import java.util.ArrayList;

public class Trie {
	TrieNode root = new TrieNode();

	public void add(String word) {
		TrieNode tn = root;
		for(int i = 0; i <word.length();i++) {
			char c = word.charAt(i);
			if(!tn.children.containsKey(c))
				tn.addChildren(c, new TrieNode());
			tn = tn.children.get(c);
		}
		tn.value=word;
	}

	public String get(String word) {
		TrieNode tn = root;
		for(int i = 0; i <word.length();i++) {
			char c = word.charAt(i);
			if(!tn.children.containsKey(c))
				return null;
			tn = tn.children.get(c);
		}
		return tn.value;
	}

	public ArrayList<String> search(String prefix) {
		TrieNode tn = root;
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			if (!tn.children.containsKey(c))
				return null;
			tn = tn.children.get(c);
		}
		results = getAllFrom(tn,results);
		return results;
	}

	private ArrayList<String> getAllFrom(TrieNode tn, ArrayList<String> results) {
		if(tn.value!=null)
			results.add(tn.value);
		for(char c : tn.children.keySet()) {
			getAllFrom(tn.children.get(c),results);
		}
		return results;
	}

}
