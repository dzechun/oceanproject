package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class SrmPoExpediteRecordDto extends SrmPoExpediteRecord implements Serializable {

    /**
     * 访问URL(可以访问的绝对地址)
     */
    @Transient
    @ApiModelProperty(name="accessUrl",value = "访问URL(可以访问的绝对地址)")
    private String accessUrl;

    /**
     * 用户名称
     */
    @Transient
    @ApiModelProperty(name="userName",value = "用户名称")
    private String userName;

    private static final long serialVersionUID = 1L;
}
