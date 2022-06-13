package elt.csv;


import java.util.ArrayList;
import java.util.List;

public class CsvSplitProcess
        extends DelimiterSplitProcess<CsvSplitConfig> {

    private final CsvLexer lexer = new CsvLexer();
    private final List<String> recordList = new ArrayList<>();

    public CsvSplitProcess(CsvSplitConfig config) {
        super(config);
    }


    @Override
    public Result setup() {
        Result result = super.setup();
        if (!result.isSuccess()) {
            return result;
        }
        this.lexer.setDelimiter(config.getDelimiter().charAt(0));
        return Result.success;
    }

    public List<String> process(String raw) {
        this.lexer.reset(raw);
        this.recordList.clear();
        LOOP:
        while (true) {
            CsvToken token = this.lexer.nextToken();
            switch (token.getStatus()) {
                case EOF:
                    if (token.isReady()) {
                        this.addRecord(token);
                    } else {
                        this.recordList.add("");
                    }
                    break LOOP;
                case TOKEN:
                    this.addRecord(token);
            }
        }
        return recordList;
    }

    private void addRecord(CsvToken token) {
        this.recordList.add(token.getContent().toString());
    }

}
