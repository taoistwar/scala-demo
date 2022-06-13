package elt.csv;


import org.slf4j.Logger;

import java.util.WeakHashMap;

public abstract class BaseCommonProcess<T extends BaseCommonConfig>
        extends BaseValidate implements CommonProcess {
    protected WeakHashMap<String, Integer> errorCaches = new WeakHashMap<>();
    protected T config;

    public BaseCommonProcess(T config) {
        this.config = config;
    }

    @Override
    public Result setup() {
        if (this.config == null) {
            return Result.failure("config is null");
        }
        Result result = this.config.validate();
        if (!result.isSuccess()) {
            return result;
        }
        return Result.success;
    }

    public T getConfig() {
        return config;
    }

    public void setConfig(T config) {
        this.config = config;
    }

    protected void log(Logger LOG, Exception e, String msg, Object... params) {
        String message = e.getMessage();
        Integer count = errorCaches.get(message);
        if (count == null) {
            count = 1;
            errorCaches.put(message, count);
            LOG.warn(msg, params);
        } else if (count % 1000 == 1) {
            LOG.warn(msg, params);
        }
    }
}
