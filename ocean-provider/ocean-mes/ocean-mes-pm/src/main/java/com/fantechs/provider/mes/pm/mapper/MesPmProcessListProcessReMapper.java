package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessListProcessRe;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListProcessReDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmProcessListProcessReMapper extends MyMapper<MesPmProcessListProcessRe> {
   //以特定过滤条件查询
   List<MesPmProcessListProcessReDTO> selectFilterAll(Map<String,Object> map);

}