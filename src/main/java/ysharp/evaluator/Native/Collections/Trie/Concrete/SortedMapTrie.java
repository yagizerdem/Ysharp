package ysharp.evaluator.Native.Collections.Trie.Concrete;

import ysharp.evaluator.Native.Collections.Trie.Base.TrieNode;
import ysharp.evaluator.Native.Collections.Trie.Node.TreeMapNode;

public class SortedMapTrie<V> extends MapTrie<V> {

    @Override
    protected TrieNode<V> onCreateRootNode() {
        return new TreeMapNode<>(ROOT_KEY);
    }
}