package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsInnerDirectTransferOrderDet extends BaseQuery implements Serializable {

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;


    private static final long serialVersionUID = 1L;
}
