package elt.csv;


import static elt.csv.Result.failure;
import static elt.csv.Result.success;

public class CsvSplitConfig extends DelimiterSplitConfig {
    private String delimiter;

    public CsvSplitConfig() {
        super(EtlConstants.CSV);
    }

    @Override
    public Result validate() {
        if (isEmpty(this.delimiter)) {
            return failure("delimiter csv delimiter empty");
        }
        if (this.delimiter.length() > 1) {
            return failure("delimiter csv delimiter length not 1");
        }
        return success;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
