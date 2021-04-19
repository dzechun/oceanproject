package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.entity.storage.MesPackageManager;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPackageManagerMapper extends MyMapper<MesPackageManager> {
    //通过ID查找用户名称
   String selectUserName(Object id);
   //以特定过滤条件查询
   List<MesPackageManagerDTO> selectFilterAll(Map<String,Object> map);
   //通过包装规格ID找到条码规则
    List<BaseBarcodeRuleSpec> findBarcodeRule(@Param("packageSpecificationId") Long packageSpecificationId,
                                              @Param("materialId") Long materialId);
    //查找条码打印总次数
    int findPrintBarcodeCount();
    //查询还剩余可打印数量
    int remainQty(Long workOrderId,int type);

    //查询打印需要的数据
    PrintModel findPrintModel(@Param("packageManageId")Long packageManageId);

    int updWorkOrderStatus(@Param("workOrderId") Long workOrderId);

    BigDecimal findWorkOrderQty(@Param("workOrderId") long workOrderId);

    List<BaseBarcodeRuleSpec> findNoCode(@Param("barcodeRuleCategoryId") Byte barcodeRuleCategoryId);
}