package ysharp.treewalk.evaluator.Native.Collections.Trie.Node;

import ysharp.treewalk.evaluator.Native.Collections.Trie.Base.TrieNode;

import java.util.Map;
import java.util.TreeMap;


public class TreeMapNode<V> extends AbstractMapNode<V> {

    public TreeMapNode(char character) {
        super(character);
    }

    @Override
    protected Map<Character, TrieNode<V>> onCreateMap() {
        return new TreeMap<>();
    }

    @Override
    protected AbstractMapNode<V> onCreateNewNode(char character) {
        return new TreeMapNode<>(character);
    }
}