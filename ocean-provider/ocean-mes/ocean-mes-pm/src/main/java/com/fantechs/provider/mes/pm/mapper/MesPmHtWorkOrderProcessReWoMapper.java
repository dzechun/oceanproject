package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderProcessReWo;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.mes.pm.vo.MesPmHtWorkOrderProcessReWoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmHtWorkOrderProcessReWoMapper extends MyMapper<MesPmHtWorkOrderProcessReWo> {
    List<MesPmHtWorkOrderProcessReWo> findList(Map<String, Object> map);

    List<MesPmHtWorkOrderProcessReWoVo> findMaterialList(Map<String, Object> map);
}
