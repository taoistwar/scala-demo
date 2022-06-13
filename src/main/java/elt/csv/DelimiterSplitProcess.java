package elt.csv;


import java.util.List;

public abstract class DelimiterSplitProcess<T extends DelimiterSplitConfig>
        extends BaseSplitProcess<T, List<String>> {
    public DelimiterSplitProcess(T config) {
        super(config);
    }
}
