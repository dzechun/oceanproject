package com.fantechs.common.base.general.dto.restapi;

import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

@Data
public class RestapiWorkOrderApiDto implements Serializable {

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

    //工序
    protected String VORNR;  //工序号 唯一值
    protected String ARBPL;   //工作中心
    protected String LTXA1; //工序名称

    //物料
    protected String RSPOS;  //预留相目号  唯一值
    protected String ZJMATNR;   //组件物料号
    protected String ZJMATXT; //组件物料描述
    protected String ZJCHARG; //组件批次
    protected String MENGE;   //组件数量
    protected String EINHEIT;   //组件单位
    protected String XLOEK;   //组件状态

    //新增
    protected String AUART;   //工单类型
    protected String WERKS;   //工厂
    protected String LGORT;   //收货仓库



//    protected List<RestapiWorkOrderprocessApiDto> workOrderProcess;  //工序
//    protected List<RestapiWorkOrderBomApiDto> workOrderBom;    //工单bom
}
