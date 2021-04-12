package com.fantechs.provider.base.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@Data
public class PrintEntity {
    private String printName;
    private String tempName;
    private String size;
    private Object object;
}
