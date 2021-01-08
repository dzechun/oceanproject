package com.fantechs.provider.wms.storageBills.mapper;

import com.fantechs.common.base.entity.storage.MesPackageManager;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPackageManagerMapper extends MyMapper<MesPackageManager> {
    //通过ID查找用户名称
   String selectUserName(Object id);
   //以特定过滤条件查询
   List<MesPackageManagerDTO> selectFilterAll(Map<String,Object> map);
   //通过包装规格ID找到条码规则
    String findBarcodeRule(Long packageSpecificationId);
}