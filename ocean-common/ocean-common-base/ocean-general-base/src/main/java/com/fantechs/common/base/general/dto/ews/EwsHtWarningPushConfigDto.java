package com.fantechs.common.base.general.dto.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningPushConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/30
 */
@Data
public class EwsHtWarningPushConfigDto extends EwsHtWarningPushConfig implements Serializable {
    /**
     * 预警事件ID编码
     */
    @ApiModelProperty(name="warningEventIdCode",value = "预警事件ID编码")
    private String warningEventIdCode;

    /**
     * 事件名称
     */
    @ApiModelProperty(name = "warningEventName",value = "事件名称")
    private String warningEventName;
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
