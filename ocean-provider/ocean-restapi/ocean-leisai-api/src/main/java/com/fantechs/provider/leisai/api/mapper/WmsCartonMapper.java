package com.fantechs.provider.leisai.api.mapper;

import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCarton;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WmsCartonMapper extends MyMapper<LeisaiWmsCarton> {
    /**
     * 插入WMS库
     * @param
     * @return
     */
    int save(LeisaiWmsCarton leisaiWmsCarton);
}
