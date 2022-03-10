package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchBaseSignature extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -2677190668550208596L;
    /**
     * 特征码
     */
    @ApiModelProperty(name="signatureCode" ,value="特征码")
    private String signatureCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName" ,value="供应商名称")
    private String supplierName;

    /**
     * 物料ID集合
     */
    @ApiModelProperty(name="materialIds" ,value="物料ID集合")
    private List<Long> materialIds;

    // 20220104 bgkun

    @ApiModelProperty(name="fixedSignature" ,value="固定特征码")
    private String fixedSignature;

    @ApiModelProperty(name="modelSignature" ,value="型号特征码")
    private String modelSignature;
}
