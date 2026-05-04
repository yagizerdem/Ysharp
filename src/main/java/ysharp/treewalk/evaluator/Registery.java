package ysharp.treewalk.evaluator;

import ysharp.treewalk.evaluator.Native.Assert.yAssert;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.treewalk.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.treewalk.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Native.Collections.Queue.yQueue;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Native.Collections.Stack.yStack;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie.yT9Trie;
import ysharp.treewalk.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.treewalk.evaluator.Native.IO.yIO;
import ysharp.treewalk.evaluator.Native.LINQ.Queryable;
import ysharp.treewalk.evaluator.Native.Network.yHttp;
import ysharp.treewalk.evaluator.Native.P5ys.yCanvas;
import ysharp.treewalk.evaluator.Native.Range;
import ysharp.treewalk.evaluator.Native.TUI.TUI;
import ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.ySemaphore;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Native.Util.*;
import ysharp.treewalk.evaluator.Native.Util.Math.yMath;
import ysharp.treewalk.evaluator.Native.Util.Path.yPath;
import ysharp.treewalk.evaluator.Native.Util.Random.yRandom;
import ysharp.treewalk.evaluator.Native.Util.Regex.yNeedle;
import ysharp.treewalk.evaluator.Native.Util.Type.Type;
import ysharp.treewalk.evaluator.Native.Util.UUID.yUUID;
import ysharp.treewalk.evaluator.Native.YPF.YPF;
import ysharp.treewalk.evaluator.Native.function.core.GlobalNatives;

public class Registery {

    public static void register(Interpreter interpreter) throws Exception {

        // string
        yString.Register(interpreter);
        yStringBuilder.Register(interpreter);
        yStringBuffer.Register(interpreter);

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
        yRandom.Register(interpreter);
        yDateTime.Register(interpreter);
        yCrypto.Register(interpreter);
        Type.Register(interpreter);

        // TUI
        TUI.Register(interpreter);


        // LINQ
        Queryable.Register(interpreter);

        //Path
        yPath.Register(interpreter);

        // range function
        Range.Register(interpreter);

        // YFP
        YPF.Register(interpreter);

        // Assert
        yAssert.Register(interpreter);


        // regex
        yNeedle.Register(interpreter);

        // global native functions
        GlobalNatives.Register(interpreter);

        // IO
        yIO.Register(interpreter);

        // Canvas
        yCanvas.Register(interpreter);
    }
}
