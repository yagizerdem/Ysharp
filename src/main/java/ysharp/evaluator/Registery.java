package ysharp.evaluator;

import ysharp.evaluator.Native.Collections.*;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.evaluator.Native.Collections.Queue.yQueue;
import ysharp.evaluator.Native.Collections.Set.ySet;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Native.Collections.Trie.yMapTrie;
import ysharp.evaluator.Native.Collections.Trie.ySortedMapTrie;
import ysharp.evaluator.Native.Collections.Trie.yT9Trie;
import ysharp.evaluator.Native.LINQ.Queryable;
import ysharp.evaluator.Native.Network.yHttp;
import ysharp.evaluator.Native.Range;
import ysharp.evaluator.Native.TUI.TUI;
import ysharp.evaluator.Native.Threading.ySemaphore;
import ysharp.evaluator.Native.Threading.yThread;
import ysharp.evaluator.Native.Util.*;
import ysharp.evaluator.Native.Util.Type.Type;
import ysharp.evaluator.Native.YPF.YPF;
import ysharp.evaluator.Native.function.core.Debug;

public class Registery {

    public static void register(Interpreter interpreter) throws Exception {

        // string
        yString.Register(interpreter);
        yStringBuilder.Register(interpreter);

        // collections
        yArray.Register(interpreter);
        yStack.Register(interpreter);
        yQueue.Register(interpreter);
        ySet.Register(interpreter);
        yHashTable.Register(interpreter);
        yLinkedList.Register(interpreter);
        yPriorityQueue.Register(interpreter);
        yArrayDeque.Register(interpreter);
        yTreeMap.Register(interpreter);
        yHashMap.Register(interpreter);
        yTreeSet.Register(interpreter);
        yWeakHashMap.Register(interpreter);
        yIdentityHashMap.Register(interpreter);
        yMapTrie.Register(interpreter);
        ySortedMapTrie.Register(interpreter);
        yT9Trie.Register(interpreter);

        //YPF


        //http
        yHttp.register(interpreter);

        // threading
        yThread.Register(interpreter);
        ySemaphore.Register(interpreter);


        // utils
        yMath.Register(interpreter);
        yUUID.Register(interpreter);
        yTime.Register(interpreter);
        yFile.Register(interpreter);
        yRandom.Register(interpreter);
        yDateTime.Register(interpreter);
        yCrypto.Register(interpreter);
        Type.Register(interpreter);

        // TUI
        TUI.Register(interpreter);


        // global functions
        Debug.Register(interpreter);

        // LINQ
        Queryable.Register(interpreter);

        //Path
        yPath.Register(interpreter);

        // range function
        Range.Register(interpreter);

        // YFP
        YPF.Register(interpreter);

    }
}
