package com.fantechs.common.base.general.dto.mes.sfc;

import lombok.Data;

import java.io.Serializable;

@Data
public class WanbaoStackingMQDto implements Serializable {
    private int code;
    private String stackingCode;
    private String stackingLine;
}
