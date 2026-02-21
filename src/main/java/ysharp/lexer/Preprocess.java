package ysharp.lexer;

import ysharp.YsharpError;

import java.util.ArrayList;
import java.util.List;


/**
 * Preprocessor: clears escapes, merges continuation lines, removes comments.
 */
public class Preprocess {


    private enum BlankType {
        Tab,   // \t
        Blank  // ' '
    }


    private static char next(String input, int current) {
        if (current >= input.length() - 1) return (char) -1;
        return input.charAt(current + 1);
    }

    // PBuffer overload
    private static char next(List<Cursor.Pchar> buf, int current) {
        if (current >= buf.size() - 1) return (char) -1;
        return buf.get(current + 1).c;
    }


    private static String collectString(String input, Cursor.CursorState cursor, int lineNo) throws YsharpError {
        if (cursor.current < 0 || cursor.current >= input.length())
            throw new IllegalStateException(
                    "[Programmatic error] current should be in range of input string");
        if (!Cursor.stopSet(input.charAt(cursor.current), Cursor.CharMask.DoubleQuote))
            throw new IllegalStateException(
                    "[Programmatic error] input should start with double quote");

        StringBuilder result = new StringBuilder();
        result.append(Cursor.advance(input, cursor)); // consume "

        while (
                (!Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.DoubleQuote) ||
                        (Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.DoubleQuote)
                                && Cursor.isEscapedBackslash(input, cursor.current)))
                        && !Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.End)
        ) {
            result.append(Cursor.advance(input, cursor));
        }

        if (!Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.DoubleQuote)) {

            throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX, lineNo, "Unclosed double quote");
        }

        result.append(Cursor.advance(input, cursor)); // consume "
        return result.toString();
    }


    private static boolean isNextWordString(String input, int startCurrent) {
        Cursor.CursorState cursor = new Cursor.CursorState();
        cursor.current = startCurrent;

        while (Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.Blank)
                || Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.Escape)) {

            if (Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.Escape)
                    && !Cursor.stopSet(next(input, cursor.current), Cursor.CharMask.Blank)) {
                return false;
            }
            Cursor.advance(input, cursor);
        }

        return Cursor.stopSet(Cursor.peek(input, cursor), Cursor.CharMask.DoubleQuote)
                && !Cursor.isEscapedBackslash(input, cursor.current);
    }


    private static String expandBlankVector(List<BlankType> vec) {
        StringBuilder sb = new StringBuilder();
        for (BlankType bt : vec) {
            sb.append(bt == BlankType.Blank ? ' ' : '\t');
        }
        return sb.toString();
    }


    private static String mergeStringRecursive(String program, Cursor.CursorState cursor, int origin) throws YsharpError {
        String sub = collectString(program, cursor, origin);

        List<BlankType> blanks = new ArrayList<>();

        while (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Blank)
                || Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Escape)) {

            if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Escape)
                    && Cursor.stopSet(next(program, cursor.current), Cursor.CharMask.Newline)) {

                Cursor.advance(program, cursor); // consume '\'
                Cursor.advance(program, cursor); // consume '\n'

                if (isNextWordString(program, cursor.current)) {
                    Cursor.consumeSpace(program, cursor);
                    String sub2 = mergeStringRecursive(program, cursor, origin);
                    sub = sub.substring(0, sub.length() - 1) + sub2.substring(1);
                    return sub;
                }
                return sub + expandBlankVector(blanks);
            }

            if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Escape)
                    && !Cursor.stopSet(next(program, cursor.current), Cursor.CharMask.Blank)) {
                return sub + expandBlankVector(blanks);
            }

            char ch = Cursor.peek(program, cursor);
            if (ch == '\t') blanks.add(BlankType.Tab);
            if (ch == ' ')  blanks.add(BlankType.Blank);

            Cursor.advance(program, cursor);
        }

        return sub + expandBlankVector(blanks);
    }


    private static String clearEscapedBlanks(String program) {
        StringBuilder result = new StringBuilder();
        Cursor.CursorState cursor = new Cursor.CursorState();
        cursor.current = 0;

        while (Cursor.peek(program, cursor) != Cursor.END) {
            if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Escape)
                    && !Cursor.isEscapedBackslash(program, cursor.current)
                    && Cursor.stopSet(next(program, cursor.current), Cursor.CharMask.Blank)) {
                Cursor.advance(program, cursor); // eat '\ ' (escaped blank)
            } else {
                result.append(Cursor.advance(program, cursor));
            }
        }
        return result.toString();
    }


    public static List<Cursor.Pchar> mergeContinuation(String program) throws YsharpError {
        program = clearEscapedBlanks(program);

        List<Cursor.Pchar> programResult = new ArrayList<>();
        int lineNo = 1;
        Cursor.CursorState cursor = new Cursor.CursorState();
        cursor.current = 0;

        while (Cursor.peek(program, cursor) != Cursor.END) {
            Cursor.Floc floc = new Cursor.Floc(lineNo);

            if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.DoubleQuote)
                    && !Cursor.isEscapedBackslash(program, cursor.current)) {

                String sub = mergeStringRecursive(program, cursor, lineNo);
                programResult = Cursor.mergePBuffer(
                        programResult,
                        Cursor.toPBuffer(sub, lineNo)
                );

            } else if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Escape)
                    && !Cursor.isEscapedBackslash(program, cursor.current)) {

                long nextMask = Cursor.getStopcharMask(next(program, cursor.current));
                boolean nextIsNewlineOrBlank =
                        Cursor.anySet(nextMask, Cursor.CharMask.Newline)
                                || Cursor.anySet(nextMask, Cursor.CharMask.Blank);

                if (nextIsNewlineOrBlank) {
                    Cursor.advance(program, cursor);
                } else {
                    Cursor.Pchar pchar = new Cursor.Pchar(Cursor.peek(program, cursor), floc);
                    programResult.add(pchar);
                    if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Newline))
                        lineNo++;
                    Cursor.advance(program, cursor);
                }

            } else if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Newline)
                    && Cursor.isEscapedBackslash(program, cursor.current)) {

                Cursor.advance(program, cursor); // remove escaped \n

            } else {
                Cursor.Pchar pchar = new Cursor.Pchar(Cursor.peek(program, cursor), floc);
                programResult.add(pchar);
                if (Cursor.stopSet(Cursor.peek(program, cursor), Cursor.CharMask.Newline))
                    lineNo++;
                Cursor.advance(program, cursor);
            }
        }

        return programResult;
    }


    public static List<Cursor.Pchar> removeComments(List<Cursor.Pchar> program) {
        List<Cursor.Pchar> programResult = new ArrayList<>();
        Cursor.CursorState cursor = new Cursor.CursorState();
        cursor.current = 0;
        boolean inQuotes = false;

        while (Cursor.peekChar(program, cursor.current) != Cursor.END) {

            if (Cursor.stopSet(Cursor.peek(program, cursor.current), Cursor.CharMask.DoubleQuote)
                    && !Cursor.isEscapedBackslash(program, cursor.current)) {
                inQuotes = !inQuotes;
            }

            if (Cursor.stopSet(Cursor.peekChar(program, cursor.current), Cursor.CharMask.Slash)
                    && !inQuotes) {

                if (Cursor.stopSet(Cursor.peekNextChar(program, cursor.current), Cursor.CharMask.Slash)) {
                    while (!Cursor.stopSet(Cursor.peekChar(program, cursor.current), Cursor.CharMask.Newline)
                            && Cursor.peekChar(program, cursor.current) != Cursor.END) {
                        Cursor.advance(program, cursor);
                    }

                } else if (Cursor.stopSet(Cursor.peekNextChar(program, cursor.current), Cursor.CharMask.Asterisk)) {
                    Cursor.advance(program, cursor); // consume '/'
                    Cursor.advance(program, cursor); // consume '*'

                    while (Cursor.peekChar(program, cursor.current) != Cursor.END) {
                        if (Cursor.stopSet(Cursor.peekChar(program, cursor.current), Cursor.CharMask.Asterisk)
                                && Cursor.stopSet(Cursor.peekNextChar(program, cursor.current), Cursor.CharMask.Slash)) {
                            Cursor.advance(program, cursor); // consume '*'
                            Cursor.advance(program, cursor); // consume '/'
                            break;
                        }
                        Cursor.advance(program, cursor);
                    }

                } else {
                    programResult.add(Cursor.advance(program, cursor));
                }

            } else {
                programResult.add(Cursor.advance(program, cursor));
            }
        }

        return programResult;
    }
}