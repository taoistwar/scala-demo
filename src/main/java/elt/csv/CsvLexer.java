package elt.csv;

import static elt.csv.CsvConstants.*;

public class CsvLexer {
    //
    final static int ENF_OF_STREAM = -1;
    // 配置
    private char delimiter;

    private final CsvToken token;
    // 每次拆分中间结果
    private char[] chars;
    private int maxIndex;
    private int index;

    public CsvLexer() {
        this.token = new CsvToken();
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public CsvToken nextToken() {
        this.token.reset();
        int ch = this.read();
        if (isEndOfStream(ch)) {
            // 没有数据，ready=false
            return this.token.eof();
        }
        if (isDelimiter(ch)) {
            token.token();
        } else if (isEndOfStream(ch)) {
            token.eof().ready();
        } else if (isQuoteChar(ch)) {
            parseEncapsulatedToken(token, ch);
        } else {
            token.append((char) ch);
            parseSimpleToken(token);
        }
        return this.token;
    }

    private void parseSimpleToken(CsvToken token) {
        int ch;
        while (true) {
            ch = this.read();
            if (isEndOfStream(ch)) {
                token.eof();
                break;
            } else if (isDelimiter(ch)) {
                token.token();
                break;
            } else {
                token.append((char) ch);
            }
        }
    }

    private void parseEncapsulatedToken(CsvToken token, int quoteChar) {
        int c;
        while (true) {
            c = this.read();

            if (isEscape(c)) {
                final int unescaped = readEscape();
                if (unescaped == END_OF_STREAM) { // unexpected char after escape
                    token.append((char) c);
                    break;
                } else {
                    token.append((char) unescaped);
                }
            } else {
                int next = this.lookAhead();
                if (isEndOfStream(next)) {
                    token.append((char) c).eof();
                    break;
                }
                if (c == quoteChar && isDelimiter(next)) {
                    token.token();
                    break;
                }
                token.append((char) c);
            }
        }
    }

    private int lookAhead() {
        if (this.index > this.maxIndex) {
            return ENF_OF_STREAM;
        }
        return chars[this.index];
    }

    boolean isEscape(final int ch) {
        return ch == '\\';
    }

    boolean isQuoteChar(final int ch) {
        return ch == '\'' || ch == '"';
    }

    boolean isDelimiter(final int ch) {
        return ch == delimiter;
    }

    private boolean isEndOfStream(int ch) {
        return ch == ENF_OF_STREAM;
    }

    private boolean isMetaChar(final int ch) {
        return ch == delimiter ||
                ch == '\\' ||
                ch == '\'' ||
                ch == '\"' ||
                ch == '#';
    }

    private int read() {
        if (this.index > this.maxIndex) {
            return ENF_OF_STREAM;
        }
        char ch = chars[this.index];
        this.index += 1;
        return ch;
    }

    int readEscape() {
        final int ch = this.read();
        switch (ch) {
            case 'r':
                return CR;
            case 'n':
                return LF;
            case 't':
                return TAB;
            case 'b':
                return BACKSPACE;
            case 'f':
                return FF;
            default:
                return ch;
        }
    }

    public void reset(String raw) {
        this.chars = raw.toCharArray();
        this.maxIndex = this.chars.length - 1;
        this.index = 0;
    }
}
