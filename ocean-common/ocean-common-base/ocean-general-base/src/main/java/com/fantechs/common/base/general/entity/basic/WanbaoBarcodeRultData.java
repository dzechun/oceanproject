package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 万宝条码规则数据
 * wanbao_barcode_rult_data
 * @author bgkun
 * @date 2022-02-22 16:33:32
 */
@Data
@Table(name = "wanbao_barcode_rult_data")
public class WanbaoBarcodeRultData extends ValidGroup implements Serializable {
    /**
     * 万宝条码规则数据ID
     */
    @ApiModelProperty(name="barcodeRultDataId",value = "万宝条码规则数据ID")
    @Id
    @Column(name = "barcode_rult_data_id")
    private Long barcodeRultDataId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialId",value = "物料编码")
    @Column(name = "material_id")
    private String materialId;

    /**
     * 电压
     */
    @ApiModelProperty(name="voltage",value = "电压")
    @Excel(name = "电压", height = 20, width = 30,orderNum="3")
    private String voltage;

    /**
     * 识别码
     */
    @ApiModelProperty(name="identificationCode",value = "识别码")
    @Excel(name = "识别码", height = 20, width = 30,orderNum="4")
    @Column(name = "identification_code")
    private String identificationCode;

    /**
     * 年
     */
    @ApiModelProperty(name="year",value = "年")
    private String year;

    /**
     * 月
     */
    @ApiModelProperty(name="month",value = "月")
    private String month;

    /**
     * 序列码
     */
    @ApiModelProperty(name="serialNumber",value = "序列码")
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * 占位
     */
    @ApiModelProperty(name="placeholder",value = "占位")
    private String placeholder;

    /**
     * 客户型号
     */
    @ApiModelProperty(name="customerModel",value = "客户型号")
    @Excel(name = "客户型号", height = 20, width = 30,orderNum="5")
    @Column(name = "customer_model")
    private String customerModel;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="productCode",value = "产品编码")
    @Column(name = "product_code")
    private String productCode;

    /**
     * 使用状态(0-未使用 1-已使用)
     */
    @ApiModelProperty(name="dataStatus",value = "使用状态(0-未使用 1-已使用)")
    @Excel(name = "使用状态", height = 20, width = 30,orderNum="6", replace = {})
    @Column(name = "data_status")
    private Byte dataStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}