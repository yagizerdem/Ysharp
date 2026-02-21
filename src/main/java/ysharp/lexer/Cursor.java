package ysharp.lexer;

import java.util.*;

public class Cursor {

    public static class CursorState {
        int current;
    }

    public static class Floc {
        public int line;
        public Floc() { this.line = 0; }
        public Floc(int line) { this.line = line; }
    }



    public static final char END = '\0';


    public enum CharMask {
        End             (1L << 1), // \0
        Blank           (1L << 2), //
        Newline         (1L << 3), // \n
        Semi            (1L << 5), // ;
        Equals          (1L << 6), // =
        Colon           (1L << 7), // :
        Percent         (1L << 8), // %
        Pipe            (1L << 9), // |
        Dot             (1L << 10), // .
        Comma           (1L << 11), // ,
        Escape          (1L << 12), //
        Plus            (1L << 13), // +
        Minus           (1L << 14), // -
        Asterisk        (1L << 15), // *
        Slash           (1L << 16), // /
        DoubleQuote     (1L << 17), // ;
        SingleQuote     (1L << 18), // '
        LeftBracket      (1L << 19), // [
        RightBracket     (1L << 20), // ]
        LeftParen       (1L << 21), //(
        RightParen      (1L << 22), // )
        LeftCurlyBrace  (1L << 23), // {
        RightCurlyBrace (1L << 24), // }
        UnderScore      (1L << 25), // _
        Hash            (1L << 26), // #
        Greater         (1L << 27), // >
        Less            (1L << 28), // <
        Bang            (1L << 29), // !
        Ampersand       (1L << 30), // &
        Caret           (1L << 31), // ^
        Question        (1L << 32), // ?
        Tilde           (1L << 33), // ~
        BackTick        (1L << 34); // `

        public final long value;
        CharMask(long value) { this.value = value; }
    }


    private static final Map<Character, Long> STOPCHAR_MAP = new HashMap<>();

    static {
        STOPCHAR_MAP.put('\0', CharMask.End.value);
        STOPCHAR_MAP.put(' ',  CharMask.Blank.value);
        STOPCHAR_MAP.put('\t', CharMask.Blank.value);
        STOPCHAR_MAP.put('\n', CharMask.Newline.value);
        STOPCHAR_MAP.put(';',  CharMask.Semi.value);
        STOPCHAR_MAP.put('=',  CharMask.Equals.value);
        STOPCHAR_MAP.put(':',  CharMask.Colon.value);
        STOPCHAR_MAP.put('%',  CharMask.Percent.value);
        STOPCHAR_MAP.put('|',  CharMask.Pipe.value);
        STOPCHAR_MAP.put('.',  CharMask.Dot.value);
        STOPCHAR_MAP.put(',',  CharMask.Comma.value);
        STOPCHAR_MAP.put('\\', CharMask.Escape.value);
        STOPCHAR_MAP.put('+',  CharMask.Plus.value);
        STOPCHAR_MAP.put('-',  CharMask.Minus.value);
        STOPCHAR_MAP.put('*',  CharMask.Asterisk.value);
        STOPCHAR_MAP.put('/',  CharMask.Slash.value);
        STOPCHAR_MAP.put('"',  CharMask.DoubleQuote.value);
        STOPCHAR_MAP.put('\'', CharMask.SingleQuote.value);
        STOPCHAR_MAP.put('[',  CharMask.LeftBracket.value);
        STOPCHAR_MAP.put(']',  CharMask.RightBracket.value);
        STOPCHAR_MAP.put('(',  CharMask.LeftParen.value);
        STOPCHAR_MAP.put(')',  CharMask.RightParen.value);
        STOPCHAR_MAP.put('{',  CharMask.LeftCurlyBrace.value);
        STOPCHAR_MAP.put('}',  CharMask.RightCurlyBrace.value);
        STOPCHAR_MAP.put('_',  CharMask.UnderScore.value);
        STOPCHAR_MAP.put('#',  CharMask.Hash.value);
        STOPCHAR_MAP.put('>',  CharMask.Greater.value);
        STOPCHAR_MAP.put('<',  CharMask.Less.value);
        STOPCHAR_MAP.put('!',  CharMask.Bang.value);
        STOPCHAR_MAP.put('&',  CharMask.Ampersand.value);
        STOPCHAR_MAP.put('^',  CharMask.Caret.value);
        STOPCHAR_MAP.put('?',  CharMask.Question.value);
        STOPCHAR_MAP.put('~',  CharMask.Tilde.value);
        STOPCHAR_MAP.put('`',  CharMask.BackTick.value);

    }

    public static long getStopcharMask(char key) {
        return STOPCHAR_MAP.getOrDefault(key, (long)0);
    }


    public static long maskOr(CharMask a, CharMask b) {
        return a.value | b.value;
    }

    public static boolean anySet(long v, CharMask m) {
        return (v & m.value) != 0;
    }

    public static boolean anySet(long v, long mask) {
        return (v & mask) != 0;
    }

    public static boolean noneSet(int v, CharMask m) {
        return !anySet(v, m);
    }

    public static boolean stopSet(char c, CharMask m) {
        return anySet(getStopcharMask(c), m);
    }

    public static boolean stopSet(char c, long mask) {
        return anySet(getStopcharMask(c), mask);
    }

    public static boolean isBlank(char c) {
        return stopSet(c, CharMask.Blank);
    }

    public static boolean isSpace(char c) {
        return stopSet(c, maskOr(CharMask.Newline, CharMask.Blank));
    }


    public static char peek(String text, CursorState cursor) {
        if (cursor.current >= text.length()) return END;
        return text.charAt(cursor.current);
    }

    public static char advance(String text, CursorState cursor) {
        if (cursor.current >= text.length()) return END;
        return text.charAt(cursor.current++);
    }

    public static char peekNext(String text, CursorState cursor) {
        if (cursor.current + 1 >= text.length()) return END;
        return text.charAt(cursor.current + 1);
    }

    public static boolean match(String text, CursorState cursor, char expected) {
        if (cursor.current >= text.length()) return false;
        if (text.charAt(cursor.current) == expected) {
            cursor.current++;
            return true;
        }
        return false;
    }

    public static void consumeBlank(String text, CursorState cursor) {
        while (isBlank(peek(text, cursor))) cursor.current++;
    }

    public static void consumeSpace(String text, CursorState cursor) {
        while (isSpace(peek(text, cursor))) cursor.current++;
    }

    public static class Pchar {
        public char c;
        public Floc loc;

        public Pchar(char c, Floc loc) {
            this.c   = c;
            this.loc = loc;
        }
    }


    private static final Pchar EOF_PCHAR = new Pchar(END, new Floc());

    public static boolean stopSet(Pchar p, CharMask m) {
        return stopSet(p.c, m);
    }

    public static boolean isBlank(Pchar p) {
        return isBlank(p.c);
    }

    public static boolean isSpace(Pchar p) {
        return isSpace(p.c);
    }

    public static Pchar peek(List<Pchar> buf, int current) {
        if (current >= buf.size()) return EOF_PCHAR;
        return buf.get(current);
    }

    public static Pchar peekNext(List<Pchar> buf, int current) {
        if (current + 1 >= buf.size()) return EOF_PCHAR;
        return buf.get(current + 1);
    }

    public static Pchar advance(List<Pchar> buf, CursorState cursor) {
        Pchar p = peek(buf, cursor.current);
        if (cursor.current < buf.size()) cursor.current++;
        return p;
    }

    public static char peekChar(List<Pchar> buf, int current) {
        return peek(buf, current).c;
    }

    public static char peekNextChar(List<Pchar> buf, int current) {
        return peekNext(buf, current).c;
    }

    public static char advanceChar(List<Pchar> buf, CursorState cursor) {
        return advance(buf, cursor).c;
    }

    public static boolean match(List<Pchar> buf, CursorState cursor, char expected) {
        if (peek(buf, cursor.current).c == expected) {
            cursor.current++;
            return true;
        }
        return false;
    }

    public static void consumeBlank(List<Pchar> buf, CursorState cursor) {
        while (isBlank(peek(buf, cursor.current))) cursor.current++;
    }

    public static void consumeSpace(List<Pchar> buf, CursorState cursor) {
        while (isSpace(peek(buf, cursor.current))) cursor.current++;
    }


    public static List<Pchar> mergePBuffer(List<Pchar> buf1, List<Pchar> buf2) {
        if (buf1.isEmpty()) return new ArrayList<>(buf2);
        if (buf2.isEmpty()) return new ArrayList<>(buf1);
        List<Pchar> result = new ArrayList<>(buf1.size() + buf2.size());
        result.addAll(buf1);
        result.addAll(buf2);
        return result;
    }

    public static List<Pchar> toPBuffer(String str, int line) {
        List<Pchar> buf = new ArrayList<>(str.length());
        for (int i = 0; i < str.length(); i++) {
            buf.add(new Pchar(str.charAt(i), new Floc(line)));
        }
        return buf;
    }

    public static String pBufferToString(List<Pchar> buf) {
        StringBuilder sb = new StringBuilder(buf.size());
        for (Pchar p : buf) sb.append(p.c);
        return sb.toString();
    }

    public static void printPBuffer(List<Pchar> buf) {
        for (Pchar p : buf) System.out.print(p.c);
    }

    public static boolean isEscaped(String text, int charIndex, char escapeCharacter) {
        int i = charIndex - 1;
        int counter = 0;
        while (i >= 0 && text.charAt(i) == escapeCharacter) {
            i--;
            counter++;
        }
        return counter % 2 == 1;
    }

    public static boolean isEscapedBackslash(String text, int charIndex) {
        return isEscaped(text, charIndex, '\\');
    }

    public static boolean isEscaped(List<Cursor.Pchar> buf, int charIndex, char escapeCharacter) {
        int i = charIndex - 1;
        int counter = 0;
        while (i >= 0 && buf.get(i).c == escapeCharacter) {
            i--;
            counter++;
        }
        return counter % 2 == 1;
    }

    public static boolean isEscapedBackslash(List<Cursor.Pchar> buf, int charIndex) {
        return isEscaped(buf, charIndex, '\\');
    }

}