package elt.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Result extends BaseValidate {
    private static final Logger LOG = LoggerFactory.getLogger(Result.class);

    private Boolean result;
    private Exception exception;


    public Result(Boolean result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public Boolean isSuccess() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

    public static final Result success = new Result(true, null);

    public static Result failure(Exception exception) {
        return new Result(false, exception);
    }

    public static Result failure(String message, String... params) {
        if (notEmpty(params)) {
            String msg = message + StringUtils.join(params, ",");
        }
        return new Result(false, new Exception(message));
    }

    public static Result failure(String message, Object... params) {
        if (notEmpty(params)) {
            ObjectMapper mapper = MapperUtils.getInstance();
            try {
                String msg = message + mapper.writeValueAsString(params);
                return new Result(false, new Exception(msg));
            } catch (JsonProcessingException e) {
                LOG.error("object to json fail", params);
            }
        }
        return new Result(false, new Exception(message));
    }

    public static Result failure(String message, Result result) {
        if (result == null || result.getException() == null) {
            return failure(message);
        }
        return new Result(false, new Exception(message, result.getException()));
    }
}
