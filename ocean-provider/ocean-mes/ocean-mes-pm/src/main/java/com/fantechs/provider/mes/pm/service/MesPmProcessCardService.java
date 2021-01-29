package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessCardDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListCardDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessCard;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessListCard;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessCard;
import com.fantechs.common.base.support.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/01/19.
 */

@Service
public interface MesPmProcessCardService extends IService<MesPmProcessCard> {

    MesPmProcessCardDto detial(SearchMesPmProcessCard searchMesPmProcessCard);

    List<MesPmProcessListCardDto> processList(SearchMesPmProcessListCard searchMesPmProcessListCard);
}
