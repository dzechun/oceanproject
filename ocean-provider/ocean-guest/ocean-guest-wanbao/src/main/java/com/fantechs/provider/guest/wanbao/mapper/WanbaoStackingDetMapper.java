package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoStackingDetMapper extends MyMapper<WanbaoStackingDet> {
    List<WanbaoStackingDet> findList(Map<String, Object> map);

    /**
     * 查找空闲并且有条码的堆垛
     * @param proLineId
     * @return
     */
    List<WanbaoAutoStackingDto> findStackingByAuto(Long proLineId);
}