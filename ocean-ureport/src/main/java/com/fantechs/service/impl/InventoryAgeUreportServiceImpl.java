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
import java.util.*;

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

        String range = "";
        List<Map<String, Object>> ranges = new LinkedList<>();
        for (int i = 0; i < rangeList.size(); i++){
            range = rangeList.get(i).get("range");
            String[] split = range.split("-");
            Map<String, Object> rangeMap = new HashMap<>();
            if(split.length > 1) {
                rangeMap.put("rangeStart", split[0]);
                rangeMap.put("rangeEnd", split[1]);
                rangeMap.put("detCount", "detCount"+(i+1));
            }else {
                rangeMap.put("rangeStart", split[0]);
                rangeMap.put("detCount", "detCount"+(i+1));
            }
            ranges.add(rangeMap);
        }
        map.put("ranges",ranges);

        return inventoryAgeUreportMapper.findList(map);
    }

    @Override
    public List<InventoryAgeDet> findDetList(Map<String, Object> map) {
        return inventoryAgeUreportMapper.findDetList(map);
    }


}
