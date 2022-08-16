package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.qms.QmsBadItem;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


@Data
public class QmsBadItemDto extends QmsBadItem implements Serializable {

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name = "processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30,orderNum="3")
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="4")
    private String processName;

    /**
     * 工段ID
     */
    @Transient
    @ApiModelProperty(name = "sectionId",value = "工段ID")
    private Long sectionId;

    /**
     * 工段编码
     */
    @Transient
    @ApiModelProperty(name = "sectionCode",value = "工段编码")
    @Excel(name = "工段编码", height = 20, width = 30,orderNum="5")
    private String sectionCode;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30,orderNum="6")
    private String sectionName;


    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
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
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
