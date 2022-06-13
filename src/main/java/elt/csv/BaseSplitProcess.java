package elt.csv;



public abstract class BaseSplitProcess<T extends BaseSplitConfig, R>
        extends BaseCommonProcess<T>
        implements SplitProcess<R> {

    public BaseSplitProcess(T config) {
        super(config);
    }
}
