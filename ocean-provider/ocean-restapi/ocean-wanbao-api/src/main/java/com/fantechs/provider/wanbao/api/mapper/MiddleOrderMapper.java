package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.wanbao.api.entity.MiddleOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MiddleOrderMapper extends MyMapper<MiddleOrder> {

    /**
     * 万宝-工单信息查询
     * @return
     */
    List<MiddleOrder> findOrderData(Map<String, Object> map);

    /**
     * 插入中间库
     * @param order
     * @return
     */
    int save(MiddleOrder order);
}