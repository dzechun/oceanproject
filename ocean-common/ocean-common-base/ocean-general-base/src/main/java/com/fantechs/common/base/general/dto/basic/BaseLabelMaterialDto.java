package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseLabelMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/17
 */
@Data
public class BaseLabelMaterialDto extends BaseLabelMaterial implements Serializable {
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码",height = 20,width = 30,orderNum = "1")
    private String materialCode;

    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名字")
    @Excel(name = "物料名字",height = 20,width = 30,orderNum = "1")
    private String materialName;
    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    @Excel(name = "物料版本",height = 20,width = 30,orderNum = "2")
    private String materialVersion;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    @Excel(name = "物料描述",height = 20,width = 30,orderNum = "3")
    private String materialDesc;
    /**
     * 标签编码
     */
    @Transient
    @ApiModelProperty(name = "labelCode",value = "标签编码")
    @Excel(name = "标签编码",height = 20,width = 30,orderNum = "4")
    private String labelCode;
    /**
     * 标签名称
     */
    @Transient
    @ApiModelProperty(name = "labelName",value = "标签名称")
    @Excel(name = "标签名称",height = 20,width = 30,orderNum = "5")
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
    @Excel(name = "标签类别",height = 20,width = 30,orderNum = "7")
    private String labelCategoryName;
    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    @Excel(name = "工序名称",height = 20,width = 30,orderNum = "8")
    private String processName;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
