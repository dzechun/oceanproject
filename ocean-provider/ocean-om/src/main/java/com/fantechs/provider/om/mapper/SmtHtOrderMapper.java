package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtOrder;
import com.fantechs.common.base.general.dto.mes.pm.history.SmtHtOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtOrderMapper extends MyMapper<SmtHtOrder> {
   //以特定过滤条件查询
   List<SmtHtOrderDTO> selectFilterAll(Map<String,Object> map);

}