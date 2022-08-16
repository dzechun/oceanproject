package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.wanbao.api.entity.MiddleMaterial;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MiddleMaterialMapper extends MyMapper<MiddleMaterial> {

    /**
     * 万宝-物料基础信息查询
     * @return
     */
    List<MiddleMaterial> findMaterialData(Map<String, Object> map);

    /**
     * 插入中间库
     * @param material
     * @return
     */
    int save(MiddleMaterial material);
}