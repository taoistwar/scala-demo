package elt.csv;

public class CsvToken {
    private CsvTokenType status;
    private StringBuilder content;
    private boolean ready; // status=EOF && ready=true时，才有内容。

    public CsvToken() {
        this.content = new StringBuilder();
    }

    public void reset() {
        this.status = CsvTokenType.INIT;
        this.content.setLength(0);
        this.ready = false;
    }

    public CsvTokenType getStatus() {
        return status;
    }

    public void setStatus(CsvTokenType status) {
        this.status = status;
    }

    public StringBuilder getContent() {
        return content;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public CsvToken ready() {
        this.ready = true;
        return this;
    }

    public CsvToken append(char ch) {
        this.content.append(ch);
        return this;
    }

    public CsvToken eof() {
        this.status = CsvTokenType.EOF;
        return this;
    }

    public CsvToken token() {
        this.status = CsvTokenType.TOKEN;
        return this;
    }

}
