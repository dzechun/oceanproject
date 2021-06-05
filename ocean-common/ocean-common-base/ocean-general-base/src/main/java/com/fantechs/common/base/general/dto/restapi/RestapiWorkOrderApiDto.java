package com.fantechs.common.base.general.dto.restapi;

import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

@Data
public class RestapiWorkOrderApiDto implements Serializable {

    /**
     * 工单
     */
  //  @Transient
 //   private MesPmWorkOrder mesPmWorkOrder;

    /**
     * 工单BOOM
     */
  //  private List<MesPmWorkOrderBom> MesPmWorkOrderBoms;

    protected String AUFNR;  //工单号
    protected String ODST;   //工单状态
    protected String MATNR; //物料号
    protected String MATXT; //物料描述
    protected String REVLV;   //版次
    protected String CHARG;   //批次
    protected String FEVOR;   //产线
    protected String GAMNG;   //数量
    protected String GSTRP;   //开始日期
    protected String GLTRP;   //结束日期
    protected String FTRMI;   //下达日期
    protected String ERDAT;   //创建日期
    protected String ERNAM;    //创建人

    protected List<RestapiWorkOrderprocessApiDto> workOrderProcess;  //工序
    protected List<RestapiWorkOrderBomApiDto> workOrderBom;    //工单bom
}
