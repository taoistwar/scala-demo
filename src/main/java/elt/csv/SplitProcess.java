package elt.csv;


public interface SplitProcess<R> extends CommonProcess {
    R process(String raw) throws Exception;
}
