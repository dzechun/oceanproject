package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


/**
 * 导入模板
 * sys_import_template
 * @author admin
 * @date 2021-10-13 09:49:04
 */
@Data
@Table(name = "sys_import_template")
public class SysImportTemplate extends ValidGroup implements Serializable {
    /**
     * 导入模板ID
     */
    @ApiModelProperty(name="importTemplateId",value = "导入模板ID")
    @Id
    @Column(name = "import_template_id")
    private Long importTemplateId;

    /**
     * 模板所属菜单
     */
    @ApiModelProperty(name="menuId",value = "模板所属菜单")
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 模板编码
     */
    @ApiModelProperty(name="importTemplateCode",value = "模板编码")
    @Excel(name = "模板编码", height = 20, width = 30,orderNum="1")
    @Column(name = "import_template_code")
    private String importTemplateCode;

    /**
     * 模板名称
     */
    @ApiModelProperty(name="importTemplateName",value = "模板名称")
    @Excel(name = "模板名称", height = 20, width = 30,orderNum="2")
    @Column(name = "import_template_name")
    private String importTemplateName;

    /**
     * 模板文件名
     */
    @ApiModelProperty(name="importTemplateFileName",value = "模板文件名")
    @Excel(name = "模板文件名", height = 20, width = 30,orderNum="3")
    @Column(name = "import_template_file_name")
    private String importTemplateFileName;

    /**
     * 模板下载地址
     */
    @ApiModelProperty(name="importTemplateUrl",value = "模板下载地址")
    @Excel(name = "模板下载地址", height = 20, width = 30,orderNum="4")
    @Column(name = "import_template_url")
    private String importTemplateUrl;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
    private String remark;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 创建账号名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Transient
    @Excel(name = "创建账号名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改账号名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Transient
    @Excel(name = "修改账号名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}