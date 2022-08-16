package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletRePODto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcProductPalletRePOMapper extends MyMapper<MesSfcProductPalletRePODto> {
    List<MesSfcProductPalletRePODto> findList(Map<String, Object> map);

}