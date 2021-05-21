package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcProductPalletMapper extends MyMapper<MesSfcProductPallet> {

    List<MesSfcProductPalletDto> findList(Map<String, Object> map);
}