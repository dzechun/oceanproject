package com.fantechs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.entity.InventoryAge;
import com.fantechs.entity.InventoryAgeDet;
import com.fantechs.mapper.InventoryAgeUreportMapper;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.service.InventoryAgeUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryAgeUreportServiceImpl extends BaseService<InventoryAge> implements InventoryAgeUreportService {

    @Resource
    private InventoryAgeUreportMapper inventoryAgeUreportMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<InventoryAge> findList(Map<String, Object> map) {
        //获取库龄范围
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("InventoryAgeRange");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        List<Map<String, String>> rangeList = JSONArray.parseObject(sysSpecItemList.get(0).getParaValue(), List.class);
        String range1 = rangeList.get(0).get("range");
        String range2 = rangeList.get(1).get("range");
        String range3 = rangeList.get(2).get("range");
        String range4 = rangeList.get(3).get("range");
        String range5 = rangeList.get(4).get("range");

        map.put("rangeStart1",range1.split("-")[0]);
        map.put("rangeEnd1",range1.split("-")[1]);
        map.put("rangeStart2",range2.split("-")[0]);
        map.put("rangeEnd2",range2.split("-")[1]);
        map.put("rangeStart3",range3.split("-")[0]);
        map.put("rangeEnd3",range3.split("-")[1]);
        map.put("rangeStart4",range4.split("-")[0]);
        map.put("rangeEnd4",range4.split("-")[1]);
        map.put("rangeStart5",range5);
        List<InventoryAge> list = inventoryAgeUreportMapper.findList(map);

        return list;
    }

    @Override
    public List<InventoryAgeDet> findDetList(Map<String, Object> map) {
        return inventoryAgeUreportMapper.findDetList(map);
    }


}
