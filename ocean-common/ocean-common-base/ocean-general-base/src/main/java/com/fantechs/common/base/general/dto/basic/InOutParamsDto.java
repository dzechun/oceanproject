package com.fantechs.common.base.general.dto.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/30
 */
@Data
public class InOutParamsDto implements Serializable {
    private String parameterMode;

    private String parameterName;
}
