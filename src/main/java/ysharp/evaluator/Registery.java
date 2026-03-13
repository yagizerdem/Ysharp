package ysharp.evaluator;

import ysharp.evaluator.Native.Collections.*;
import ysharp.evaluator.Native.Collections.Trie.yMapTrie;
import ysharp.evaluator.Native.Collections.Trie.ySortedMapTrie;
import ysharp.evaluator.Native.Collections.Trie.yT9Trie;
import ysharp.evaluator.Native.Form.Y_Button;
import ysharp.evaluator.Native.Form.Y_Frame;
import ysharp.evaluator.Native.Network.yHttp;
import ysharp.evaluator.Native.TUI.Terminal.yDefaultTerminal;
import ysharp.evaluator.Native.TUI.Terminal.ySwingTerminal;
import ysharp.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.evaluator.Native.TUI.Util.ySGR;
import ysharp.evaluator.Native.Threading.ySemaphore;
import ysharp.evaluator.Native.Threading.yThread;
import ysharp.evaluator.Native.Util.*;

public class Registery {

    public static void register(Interpreter interpreter) throws Exception {

        yString.Register(interpreter);

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

        //forms
        Y_Frame.Register(interpreter);
        Y_Button.Register(interpreter);


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

        // TUI
        yDefaultTerminal.Register(interpreter);
        ySwingTerminal.Register(interpreter);
        ySGR.Register(interpreter);
        yTextColor.Register(interpreter);

    }
}
