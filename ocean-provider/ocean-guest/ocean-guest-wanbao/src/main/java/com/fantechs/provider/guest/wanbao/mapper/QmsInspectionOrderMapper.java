package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionOrderMapper extends MyMapper<QmsInspectionOrder> {
    List<QmsInspectionOrder> findList(Map<String, Object> map);

    List<WmsInnerInventoryDetDto> findInventoryDetList(Map<String, Object> map);
}