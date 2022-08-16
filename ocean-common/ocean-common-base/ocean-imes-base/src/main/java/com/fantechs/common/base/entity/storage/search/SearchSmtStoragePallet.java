package com.fantechs.common.base.entity.storage.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSmtStoragePallet extends BaseQuery implements Serializable {

    /**
     * 栈板号/箱号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    private Long storageId;

    /**
     * 是否绑定（0、否 1、是）
     */
    @ApiModelProperty(name="isBinding",value = "是否绑定（0、否 1、是）")
    private Byte isBinding;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

}
