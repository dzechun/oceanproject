package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearcBaseAddress extends BaseQuery implements Serializable {

    /**
     * 省编码
     */
    @ApiModelProperty(name="provinceCode",value = "省编码")
    private String provinceCode;

    /**
     * 市编码
     */
    @ApiModelProperty(name="cityCode",value = "市编码")
    private String cityCode;

    /**
     * 区/县编码
     */
    @ApiModelProperty(name="classifyCode",value = "区/县编码")
    private String classifyCode;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="addressDetail",value = "详细地址")
    private String addressDetail;

    /**
     * 完整地址
     */
    @ApiModelProperty(name="completeDetail",value = "完整地址")
    private String completeDetail;

    /**
     * 邮政编码
     */
    @ApiModelProperty(name="postCode",value = "邮政编码")
    private String postCode;
}
