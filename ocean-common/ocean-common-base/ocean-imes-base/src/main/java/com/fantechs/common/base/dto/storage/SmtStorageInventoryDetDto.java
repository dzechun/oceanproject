package com.fantechs.common.base.dto.storage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Date 2020/12/4 14:43
 */
@Data
public class SmtStorageInventoryDetDto extends SmtStorageInventoryDet implements Serializable {
    /**
     * 储位编码
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30)
    private String storageCode;

    /**
     * 储位名称
     */
    @Transient
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30)
    private String storageName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Column(name = "organization_name")
    private String organizationName;
}
