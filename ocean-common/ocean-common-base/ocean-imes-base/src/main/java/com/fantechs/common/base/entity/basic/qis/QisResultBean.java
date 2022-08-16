package com.fantechs.common.base.entity.basic.qis;

import lombok.Data;

@Data
public class QisResultBean<T>  {
    private int code;
    private String message;
    private int recordCount;
    private T result;
}
