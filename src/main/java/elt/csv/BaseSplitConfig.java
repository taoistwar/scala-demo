package elt.csv;



public abstract class BaseSplitConfig
        extends BaseCommonConfig<String>
        implements SplitConfig {


    public BaseSplitConfig(String type) {
        this.type = type;
    }
}
