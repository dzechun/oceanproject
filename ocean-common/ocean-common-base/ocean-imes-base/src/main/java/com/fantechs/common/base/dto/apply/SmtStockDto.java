package com.fantechs.common.base.dto.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.apply.SmtStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/11/24
 */
@Data
public class SmtStockDto extends SmtStock implements Serializable {
    private static final long serialVersionUID = 3711283845924279295L;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="")
    private String workOrderCode;

    /**
     * 物料料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="")
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "生产线", height = 20, width = 30,orderNum = "9")
    private String proName;

    /**
     * 创建账号名称
     */
    @Transient
    @Excel(name = "创建账号", height = 20, width = 30)
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;
}
