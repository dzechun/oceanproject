package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.history.SmtHtOrder;
import com.fantechs.common.base.dto.apply.history.SmtHtOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtOrderMapper extends MyMapper<SmtHtOrder> {
   //以特定过滤条件查询
   List<SmtHtOrderDTO> selectFilterAll(Map<String,Object> map);

}