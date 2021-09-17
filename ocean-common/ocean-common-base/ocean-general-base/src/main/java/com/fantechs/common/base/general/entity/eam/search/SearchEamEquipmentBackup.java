package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentBackup extends BaseQuery implements Serializable {
    /**
     * 备用件编码
     */
    @ApiModelProperty(name="equipmentBackupCode",value = "备用件编码")
    private String equipmentBackupCode;

    /**
     * 备用件名稱
     */
    @ApiModelProperty(name="equipmentBackupName",value = "备用件名稱")
    private String equipmentBackupName;

    /**
     * 备用件描述
     */
    @ApiModelProperty(name="equipmentBackupDesc",value = "备用件描述")
    private String equipmentBackupDesc;

    /**
     * 备用件型號
     */
    @ApiModelProperty(name="equipmentBackupModel",value = "备用件型號")
    private String equipmentBackupModel;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    private String status;

}
