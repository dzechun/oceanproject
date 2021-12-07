package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSrmAppointDeliveryReAsn extends BaseQuery implements Serializable {

    /**
     * asn编码
     */
    @ApiModelProperty(name="asnCode",value = "asn编码")
    private String asnCode;






    private static final long serialVersionUID = 1L;
}
