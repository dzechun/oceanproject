package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDetDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
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

;
;

/**
 * 产品BOM父料
 * base_product_bom
 * @author 81947
 * @date 2021-06-09 20:37:07
 */
@Data
@Table(name = "base_product_bom")
public class BaseProductBom extends ValidGroup implements Serializable {
    /**
     * 产品BOM ID
     */
    @ApiModelProperty(name="productBomId",value = "产品BOM ID")
    @Id
    @Column(name = "product_bom_id")
    private Long productBomId;

    /**
     * BOM编码
     */
    @ApiModelProperty(name="productBomCode",value = "BOM编码")
    @Excel(name = "BOM编码", height = 20, width = 30,orderNum="1")
    @Column(name = "product_bom_code")
    private String productBomCode;

    /**
     * BOM版本号
     */
    @ApiModelProperty(name="productBomVersion",value = "BOM版本号")
    @Excel(name = "BOM版本号", height = 20, width = 30,orderNum="2")
    @Column(name = "product_bom_version")
    private String productBomVersion;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId",value = "产品料号ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 节拍(秒)
     */
    @ApiModelProperty(name="takt",value = "节拍(秒)")
    @Excel(name = "节拍(秒)", height = 20, width = 30,orderNum="3")
    private Integer takt;

    /**
     * BOM状态(1-未核准 2-已核准)
     */
    @ApiModelProperty(name="bomStatus",value = "BOM状态(1-未核准 2-已核准)")
    @Excel(name = "BOM状态(1-未核准 2-已核准)", height = 20, width = 30,orderNum="4",replace = {"未核准_1", "已核准_2"})
    @Column(name = "bom_status")
    private Byte bomStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="5",replace = {"无效_0", "有效_1"})
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="21",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="23",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "baseProductBomDets", value = "产品BOM集合")
    @Transient
    private List<BaseProductBomDetDto> baseProductBomDetDtos;
}