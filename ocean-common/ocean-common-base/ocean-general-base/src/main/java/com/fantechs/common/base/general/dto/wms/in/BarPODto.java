package com.fantechs.common.base.general.dto.wms.in;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
@Data
public class BarPODto implements Serializable {
    private String barCode;

    private String POCode;
}
