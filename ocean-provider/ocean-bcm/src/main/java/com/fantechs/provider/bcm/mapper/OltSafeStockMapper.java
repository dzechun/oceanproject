package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.OltSafeStockDto;
import com.fantechs.common.base.general.entity.bcm.OltSafeStock;
import com.fantechs.common.base.general.entity.bcm.search.SearchOltSafeStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OltSafeStockMapper extends MyMapper<OltSafeStock> {
    List<OltSafeStockDto> findList(SearchOltSafeStock searchOltSafeStock);

    /**
     * 根据仓库查询总数
     * @return
     */
    BigDecimal selectCountByWare(@Param("warehouseId")Long warehouseId,@Param("materialCategoryId") Long materialCategoryId,@Param("materialId") Long materialId);

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