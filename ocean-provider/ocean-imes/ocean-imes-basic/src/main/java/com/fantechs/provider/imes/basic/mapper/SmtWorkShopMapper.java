package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtWorkShopMapper extends MyMapper<SmtWorkShop> {

    List<SmtWorkShopDto> findList(Map<String, Object> map);
}