package com.fantechs.provider.base.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BaseProductProcessReMVo implements Serializable {

    /**
     * 产品ID(即物料ID)
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="产品ID(即物料ID)")
    @Excel(name = "产品ID(即物料ID)", height = 20, width = 30)
    private Long materialId;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="厂别名称")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="产品版本")
    @Excel(name = "产品版本", height = 20, width = 30)
    private String version;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 物料工序关系集合
     */
    @ApiModelProperty(name="baseProductProcessReMS",value = "物料工序关系集合")
    private List<BaseProductProcessReM> baseProductProcessReMS = new ArrayList<>();

}
