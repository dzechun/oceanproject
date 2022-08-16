package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSafeStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSafeStockMapper extends MyMapper<BaseSafeStock> {
    List<BaseSafeStockDto> findList(Map<String, Object> map);

    /**
     * 根据仓库查询总数
     * @return
     */
    BigDecimal selectCountByWare(@Param("warehouseId")Long warehouseId,@Param("materialId") Long materialId);

    /**
     * 根据物料类别查询总数
     * @return
     */
    BigDecimal selectCountByCate(@Param("materialCategoryId")Long materialCategoryId);

    /**
     * 根据物料编码查询总数
     * @return
     */
    BigDecimal selectCountByCode(@Param("materialId")Long materialId);
}