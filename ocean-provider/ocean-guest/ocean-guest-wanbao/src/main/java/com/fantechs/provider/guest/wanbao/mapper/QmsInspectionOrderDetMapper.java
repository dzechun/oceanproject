package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionOrderDetMapper extends MyMapper<QmsInspectionOrderDet> {
    List<QmsInspectionOrderDet> findDetList(Map<String, Object> map);

    /**
     * 批量修改部分字段
     * @param list
     * @return
     */
    int batchUpdate(List<QmsInspectionOrderDet> list);
}