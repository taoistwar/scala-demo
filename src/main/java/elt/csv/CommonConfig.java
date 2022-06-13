package elt.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface CommonConfig<T> {
    @JsonIgnore
    Result validate();

    T getType();

    void setType(T type);
}
