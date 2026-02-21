package lexer;

import java.util.*;

public class Cursor {


    public static final char END = '\0';


    public enum CharMask {
        End             (1 << 1),
        Blank           (1 << 2),
        Newline         (1 << 3),
        Comment         (1 << 4),
        Semi            (1 << 5),
        Equals          (1 << 6),
        Colon           (1 << 7),
        Percent         (1 << 8),
        Pipe            (1 << 9),
        Dot             (1 << 10),
        Comma           (1 << 11),
        Escape          (1 << 12),
        Plus            (1 << 13),
        Minus           (1 << 14),
        Asterisk        (1 << 15),
        Slash           (1 << 16),
        DoubleQuote     (1 << 17),
        SingleQuote     (1 << 18),
        LeftBrace       (1 << 19),
        RightBrace      (1 << 20),
        LeftParen       (1 << 21),
        RightParen      (1 << 22),
        LeftCurlyBrace  (1 << 23),
        RightCurlyBrace (1 << 24);

        public final int value;
        CharMask(int value) { this.value = value; }
    }


    private static final Map<Character, Integer> STOPCHAR_MAP = new HashMap<>();

    static {
        STOPCHAR_MAP.put('\0', CharMask.End.value);
        STOPCHAR_MAP.put(' ',  CharMask.Blank.value);
        STOPCHAR_MAP.put('\t', CharMask.Blank.value);
        STOPCHAR_MAP.put('\n', CharMask.Newline.value);
        STOPCHAR_MAP.put('#',  CharMask.Comment.value);
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
        STOPCHAR_MAP.put('[',  CharMask.LeftBrace.value);
        STOPCHAR_MAP.put(']',  CharMask.RightBrace.value);
        STOPCHAR_MAP.put('(',  CharMask.LeftParen.value);
        STOPCHAR_MAP.put(')',  CharMask.RightParen.value);
        STOPCHAR_MAP.put('{',  CharMask.LeftCurlyBrace.value);
        STOPCHAR_MAP.put('}',  CharMask.RightCurlyBrace.value);
    }

    public static int getStopcharMask(char key) {
        return STOPCHAR_MAP.getOrDefault(key, 0);
    }


    public static int maskOr(CharMask a, CharMask b) {
        return a.value | b.value;
    }

    public static boolean anySet(int v, CharMask m) {
        return (v & m.value) != 0;
    }

    public static boolean anySet(int v, int mask) {
        return (v & mask) != 0;
    }

    public static boolean noneSet(int v, CharMask m) {
        return !anySet(v, m);
    }

    public static boolean stopSet(char c, CharMask m) {
        return anySet(getStopcharMask(c), m);
    }

    public static boolean stopSet(char c, int mask) {
        return anySet(getStopcharMask(c), mask);
    }

    public static boolean isBlank(char c) {
        return stopSet(c, CharMask.Blank);
    }

    public static boolean isSpace(char c) {
        return stopSet(c, maskOr(CharMask.Newline, CharMask.Blank));
    }


    public static char peek(String text, int[] current) {
        if (current[0] >= text.length()) return END;
        return text.charAt(current[0]);
    }

    public static char advance(String text, int[] current) {
        if (current[0] >= text.length()) return END;
        return text.charAt(current[0]++);
    }

    public static char peekNext(String text, int[] current) {
        if (current[0] + 1 >= text.length()) return END;
        return text.charAt(current[0] + 1);
    }

    public static boolean match(String text, int[] current, char expected) {
        if (current[0] >= text.length()) return false;
        if (text.charAt(current[0]) == expected) {
            current[0]++;
            return true;
        }
        return false;
    }

    public static void consumeBlank(String text, int[] current) {
        while (isBlank(peek(text, current))) current[0]++;
    }

    public static void consumeSpace(String text, int[] current) {
        while (isSpace(peek(text, current))) current[0]++;
    }


    public static class Floc {
        public int line;
        public Floc() { this.line = 0; }
        public Floc(int line) { this.line = line; }
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

    public static Pchar advance(List<Pchar> buf, int[] current) {
        Pchar p = peek(buf, current[0]);
        if (current[0] < buf.size()) current[0]++;
        return p;
    }

    public static char peekChar(List<Pchar> buf, int current) {
        return peek(buf, current).c;
    }

    public static char peekNextChar(List<Pchar> buf, int current) {
        return peekNext(buf, current).c;
    }

    public static char advanceChar(List<Pchar> buf, int[] current) {
        return advance(buf, current).c;
    }

    public static boolean match(List<Pchar> buf, int[] current, char expected) {
        if (peek(buf, current[0]).c == expected) {
            current[0]++;
            return true;
        }
        return false;
    }

    public static void consumeBlank(List<Pchar> buf, int[] current) {
        while (isBlank(peek(buf, current[0]))) current[0]++;
    }

    public static void consumeSpace(List<Pchar> buf, int[] current) {
        while (isSpace(peek(buf, current[0]))) current[0]++;
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