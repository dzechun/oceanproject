package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseWorkShopMapper extends MyMapper<BaseWorkShop> {

    List<BaseWorkShopDto> findList(Map<String, Object> map);
}