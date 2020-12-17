package com.fantechs.common.base.general.dto.bcm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.bcm.BcmLabelMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/17
 */
@Data
public class BcmLabelMaterialDto extends BcmLabelMaterial implements Serializable {
    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    @Excel(name = "产品料号",height = 20,width = 30,orderNum = "6")
    private String materialCode;
    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "产品版本")
    @Excel(name = "产品版本",height = 20,width = 30,orderNum = "6")
    private String materialVersion;
    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品版本",height = 20,width = 30,orderNum = "6")
    private String materialDesc;
    /**
     * 标签名称
     */
    @Transient
    @ApiModelProperty(name = "labelName",value = "标签名称")
    @Excel(name = "标签名称",height = 20,width = 30,orderNum = "6")
    private String labelName;
    /**
     * 标签版本
     */
    @Transient
    @ApiModelProperty(name = "labelVersion",value = "标签版本")
    @Excel(name = "标签版本",height = 20,width = 30,orderNum = "6")
    private String labelVersion;
    /**
     * 标签类别
     */
    @Transient
    @ApiModelProperty(name = "labelCategoryName",value = "标签类别")
    @Excel(name = "标签类别",height = 20,width = 30,orderNum = "6")
    private String labelCategoryName;
    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    @Excel(name = "工序名称",height = 20,width = 30,orderNum = "6")
    private String processName;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;
}
