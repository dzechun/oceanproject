package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPalletDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcProductPalletDetMapper extends MyMapper<MesSfcProductPalletDet> {

    List<MesSfcProductPalletDetDto> findList(Map<String, Object> map);

    /**
     * 根据栈板查询栈板条码明细
     * 按工单，栈板分组
     * @param map
     * @return
     */
    List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map);
}