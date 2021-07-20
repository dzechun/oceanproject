package com.fantechs.common.base.general.entity.eam.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchEamWiRelease extends BaseQuery implements Serializable {

    /**
     * 发布编码
     */
    @ApiModelProperty(name="wiReleaseCode",value = "发布编码")
    private String wiReleaseCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "设备ip")
    private String equipmentIp;

    /**
     * 发布状态(1-待发布 2-已发布)
     */
    @ApiModelProperty(name="release_status",value = "发布状态(1-待发布 2-已发布)")
    private Byte releaseStatus;
}
