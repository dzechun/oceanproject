package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessCardDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListCardDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessCard;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessListCard;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessCard;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmProcessCardMapper extends MyMapper<MesPmProcessCard> {
    MesPmProcessCardDto findList(SearchMesPmProcessCard searchMesPmProcessCard);

    List<MesPmProcessListCardDto> processList(SearchMesPmProcessListCard searchMesPmProcessListCard);
}