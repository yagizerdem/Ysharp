package ysharp.treewalk.evaluator.Native.Collections.Trie.Base;


import java.util.Collection;

public interface TrieNode<V> {

    V getValue();

    void setValue(V value);

    char getChar();

    TrieNode<V> addChild(final char character);

    TrieNode<V> getChild(final char character);

    void removeChild(final char character);

    Collection<TrieNode<V>> getChildren();

    boolean isEnd();

    boolean isKey();

    void setKey(boolean isKey);

    void print();

    boolean containsChild(char c);

    void clear();
}