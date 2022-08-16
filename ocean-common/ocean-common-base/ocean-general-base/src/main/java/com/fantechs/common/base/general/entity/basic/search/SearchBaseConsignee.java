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
     * 收货人编码
     */
    @ApiModelProperty(name="consigneeCode" ,value="收货人编码")
    private String consigneeCode;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name="consigneeName" ,value="收货人名称")
    private String consigneeName;

    /**
     * 公司名称
     */
    @ApiModelProperty(name="companyName" ,value="公司名称")
    private String companyName;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName" ,value="联系人名称")
    private String linkManName;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName" ,value="货主名称")
    private String materialOwnerName;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId" ,value="货主ID")
    private Long materialOwnerId;

}
