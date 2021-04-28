package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseConsignee extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 4724079505651924663L;

    /**
     * 收货人信息ID
     */
    @ApiModelProperty(name="consigneeId" ,value="收货人信息ID")
    private Long consigneeId;

    /**
     * 收货人编码
     */
    @ApiModelProperty(name="consigneeCode" ,value="收货人编码")
    private String consigneeCode;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name="consigneeName" ,value="收货人名称")
    private String consigneeName;


}
