package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamNews extends BaseQuery implements Serializable {

    /**
     * 新闻编码
     */
    @ApiModelProperty(name="newsCode",value = "新闻编码")
    private String newsCode;

    /**
     * 新闻标题
     */
    @ApiModelProperty(name="newsTitle",value = "新闻标题")
    private String newsTitle;

    /**
     * 工厂名称
     */
    @ApiModelProperty(name="factoryName",value = "工厂名称")
    private String factoryName;

    /**
     * 车间名称
     */
    @ApiModelProperty(name="workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName",value = "产线名称")
    private String proName;

    /**
     * 新闻状态
     */
    @ApiModelProperty(name="newStatus",value = "新闻状态")
    private Byte newStatus;


}
