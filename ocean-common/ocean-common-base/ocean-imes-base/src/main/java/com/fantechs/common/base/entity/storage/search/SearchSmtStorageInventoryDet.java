package com.fantechs.common.base.entity.storage.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * @date 2020-12-04 14:39:37
 */
@Data
public class SearchSmtStorageInventoryDet extends BaseQuery implements Serializable {

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    private String storageName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;


}
