package com.fantechs.provider.imes.basic.mapper;


import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.mybatis.MyMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SmtHtWorkShopMapper extends MyMapper<SmtHtWorkShop> {
    List<SmtHtWorkShop> findList(Map<String, Object> map);
}