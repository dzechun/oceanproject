package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchSrmInAsnOrderDet extends BaseQuery implements Serializable {
    /**
     * ANS单ID
     */
    @ApiModelProperty(name="id",value = "ANS单ID")
    private Long asnOrderId;

    private Long asnOrderDetId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * ANS单ID集合
     */
    @ApiModelProperty(name="asnOrderIdList",value = "ANS单ID集合")
    private List<Long> asnOrderIdList;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    private Byte ifAllIssued;

    @ApiModelProperty(name="ifFiltrate",value = "是否筛选（0，否  1，是）")
    private byte ifFiltrate;

}
