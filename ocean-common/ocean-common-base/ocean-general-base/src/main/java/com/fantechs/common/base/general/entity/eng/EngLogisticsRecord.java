package com.fantechs.common.base.general.entity.eng;

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

;
;

/**
 * 物流跟踪记录
 * eng_logistics_record
 * @author admin
 * @date 2021-11-03 19:15:46
 */
@Data
@Table(name = "eng_logistics_record")
public class EngLogisticsRecord extends ValidGroup implements Serializable {
    /**
     * 物流跟踪记录ID
     */
    @ApiModelProperty(name="logisticsRecordId",value = "物流跟踪记录ID")
    @Excel(name = "物流跟踪记录ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "logistics_record_id")
    private Long logisticsRecordId;

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name="contractQtyOrderId",value = "合同量单ID")
    @Excel(name = "合同量单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "contract_qty_order_id")
    private Long contractQtyOrderId;

    /**
     * 物流变更节点(1-出厂、2-离港、3-到港、4-到场、5-入库、6-出库、7-发运)
     */
    @ApiModelProperty(name="materialLogisticsNode",value = "物流变更节点(1-出厂、2-离港、3-到港、4-到场、5-入库、6-出库、7-发运)")
    @Excel(name = "物流变更节点(1-出厂、2-离港、3-到港、4-到场、5-入库、6-出库、7-发运)", height = 20, width = 30,orderNum="") 
    @Column(name = "material_logistics_node")
    private Byte materialLogisticsNode;

    /**
     * 标题
     */
    @ApiModelProperty(name="title",value = "标题")
    @Excel(name = "标题", height = 20, width = 30,orderNum="") 
    private String title;

    /**
     * 接收用户ID
     */
    @ApiModelProperty(name="receiveUserId",value = "接收用户ID")
    @Excel(name = "接收用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "receive_user_id")
    private Long receiveUserId;

    /**
     * 阅读状态(0-未读 1-已读)
     */
    @ApiModelProperty(name="readStatus",value = "阅读状态(0-未读 1-已读)")
    @Excel(name = "阅读状态(0-未读 1-已读)", height = 20, width = 30,orderNum="") 
    @Column(name = "read_status")
    private Byte readStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * option1
     */
    @ApiModelProperty(name="option1",value = "option1")
    @Excel(name = "option1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * option2
     */
    @ApiModelProperty(name="option2",value = "option2")
    @Excel(name = "option2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * option3
     */
    @ApiModelProperty(name="option3",value = "option3")
    @Excel(name = "option3", height = 20, width = 30,orderNum="") 
    private String option3;

    /**
     * 消息内容
     */
    @ApiModelProperty(name="messageContent",value = "消息内容")
    @Excel(name = "消息内容", height = 20, width = 30,orderNum="") 
    @Column(name = "message_content")
    private String messageContent;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 接收用户名称
     */
    @Transient
    @ApiModelProperty(name = "receiveUserName",value = "接收用户名称")
    @Excel(name = "接收用户名称", height = 20, width = 30,orderNum="14")
    private String receiveUserName;

    /**
     * 消息内容实体
     */
    @Transient
    @ApiModelProperty(name = "Message",value = "消息内容实体")
    private EngLogisticsRecordMessage Message;

    private static final long serialVersionUID = 1L;
}