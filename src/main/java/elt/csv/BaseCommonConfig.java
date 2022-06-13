package elt.csv;

public abstract class BaseCommonConfig<T>
        extends BaseValidate implements CommonConfig<T> {
    protected T type;

    public BaseCommonConfig() {

    }

    @Override
    public T getType() {
        return type;
    }

    @Override
    public void setType(T type) {
        this.type = type;
    }

}
