package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/1/19
 */
@Data
public class SearchMesPmProcessCard extends BaseQuery implements Serializable {

    private String workOrderCode;
    /**
     * 流转卡编码
     */
    @ApiModelProperty(name = "workOrderCardId",value = "流转卡编码")
    private String workOrderCardId;

    /**
     * 流转卡类型：1：工序过站、2:工序过站物料明细、3:完工上报、4:不良品、5:绑定关键物料、6:拆批作业、7:合批作业
     */
    @ApiModelProperty(name="typeCard",value = "流转卡类型：1：工序过站、2:工序过站物料明细、3:完工上报、4:不良品、5:绑定关键物料、6:拆批作业、7:合批作业")
    private Byte typeCard;
}
