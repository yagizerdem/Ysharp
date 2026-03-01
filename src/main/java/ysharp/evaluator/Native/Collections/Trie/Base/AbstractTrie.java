package ysharp.evaluator.Native.Collections.Trie.Base;

import java.util.List;

public interface AbstractTrie<V> {
    char ROOT_KEY = ' ';

    void insert(String key, V value);

    void deleteKey(String key);

    boolean contains(String key);

    V get(String key);

    List<String> getKeySuggestions(String prefix);

    List<String> keys();

    List<V> getValueSuggestions(String prefix);

    List<V> values();

    void print();

    int size();

    void clear();

    void fastClear();
}