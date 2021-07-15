package com.fantechs.common.base.general.entity.ureport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
public class MesSfcBarcodeProcessReport extends ValidGroup implements Serializable {

    /**
     * ID
     */
    @Id
    private Long id;

    /**
     * 成品条码
     */
    @ApiModelProperty(name="barcode",value = "成品条码")
    private String barcode;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    private String workOrderCode;

    /**
     *工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
     */
    @ApiModelProperty(name="workOrderStatus",value = "工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)")
    private Byte workOrderStatus;

    /**
     * 生产线
     */
    @ApiModelProperty(name="proLineName",value = "生产线")
    private String proLineName;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号")
    private String productModelCode;

    /**
     * 装配记录
     */
    @ApiModelProperty(name="assemblyList",value = "装配记录")
    private List<AssemblyRecordUreport> assemblyList;

    /**
     * 条码过站记录
     */
//    @ApiModelProperty(name="barCodeList",value = "条码过站记录")
//    private List<BarcodeRecordUreport> barcodeList;

    /**
     * 包箱记录
     */
    @ApiModelProperty(name="boxList",value = "包箱记录")
    private List<BoxRecordUreport> boxList;

    /**
     * 设备参数记录
     */
    @ApiModelProperty(name="equipmentParameterList",value = "设备参数记录")
    private List<EquipmentParameterRecordUreport> equipmentParameterList;

    /**
     * 检验记录
     */
    @ApiModelProperty(name="inspectionList",value = "检验记录")
    private List<InspectionRecordUreport> inspectionList;

    /**
     * 栈板记录
     */
    @ApiModelProperty(name="palletList",value = "栈板记录")
    private List<PalletRecordUreport> palletList;

    /**
     * 返修记录
     */
    @ApiModelProperty(name="reworkList",value = "返修记录")
    private List<ReworkRecordUreport> reworkList;

}
