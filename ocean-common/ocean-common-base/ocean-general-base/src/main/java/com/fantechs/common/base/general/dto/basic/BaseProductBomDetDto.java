package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseProductBomDetDto extends BaseProductBomDet implements Serializable {

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 替代物料编码
     */
    @Transient
    @ApiModelProperty(name="submaterialCode" ,value="替代物料编码")
    @Excel(name = "替代物料编码", height = 20, width = 30)
    private String subMaterialCode;

    /**
     * 替代物料名称
     */
    @Transient
    @ApiModelProperty(name="submaterialName" ,value="替代物料名称")
    private String subMaterialName;

    /**
     * 替代物料版本
     */
    @Transient
    @ApiModelProperty(name="subversion" ,value="替代物料版本")
    @Excel(name = "替代物料版本", height = 20, width = 30)
    private String subVersion;

    /**
     * 替代物料描述
     */
    @Transient
    @ApiModelProperty(name="submaterialDesc" ,value="替代物料描述")
    @Excel(name = "替代物料描述", height = 20, width = 30)
    private String subMaterialDesc;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @Excel(name = "线别代码", height = 20, width = 30)
    @Transient
    private String proCode;

    /**
     * 线别名称
     */
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "线别名称", height = 20, width = 30)
    @Transient
    private String proName;

    /**
     * 线别描述
     */
    @ApiModelProperty(name="proDesc" ,value="线别描述")
    @Excel(name = "线别描述", height = 20, width = 30)
    @Transient
    private String proDesc;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 工序代码
     */
    @ApiModelProperty(name="processCode" ,value="线别代码")
    @Excel(name = "工序代码", height = 20, width = 30)
    @Transient
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    @Transient
    private String processName;

    /**
     * 工序描述
     */
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    @Excel(name = "工序描述", height = 20, width = 30)
    @Transient
    private String processDesc;


}
