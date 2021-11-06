package com.fantechs.provider.leisai.api.mapper;

import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCartonDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WmsCartonDetMapper extends MyMapper<LeisaiWmsCartonDet> {
    /**
     * 插入WMS库
     * @param leisaiWmsCartonDet
     * @return
     */
    int save(LeisaiWmsCartonDet leisaiWmsCartonDet);
}
