package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessCard;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmBreakBulkDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmBreakBulkMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmProcessCardMapper;
import com.fantechs.provider.mes.pm.mapper.SmtProcessListProcessMapper;
import com.fantechs.provider.mes.pm.service.MesPmProcessCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/01/19.
 */
@Service
public class MesPmProcessCardServiceImpl extends BaseService<MesPmProcessCard> implements MesPmProcessCardService {

    @Resource
    private MesPmProcessCardMapper mesPmProcessCardMapper;
    @Resource
    private SmtProcessListProcessMapper smtProcessListProcessMapper;
    @Resource
    private MesPmBreakBulkMapper mesPmBreakBulkMapper;
    @Resource
    private MesPmBreakBulkDetMapper mesPmBreakBulkDetMapper;

    @Override
    public MesPmProcessCardDto detial(SearchMesPmProcessCard searchMesPmProcessCard) {
        if(StringUtils.isEmpty(searchMesPmProcessCard.getWorkOrderCardId()) && StringUtils.isEmpty(searchMesPmProcessCard.getWorkOrderId())){
            throw new BizErrorException(ErrorCodeEnum.valueOf("流转卡编码不能空"));
        }
        MesPmProcessCardDto mesPmProcessCardDto = mesPmProcessCardMapper.findList(searchMesPmProcessCard);
//        if(!StringUtils.isEmpty(searchMesPmProcessCard.getTypeCard())){
//            switch (searchMesPmProcessCard.getTypeCard()){
//                case 1:
//                    //工序过站记录
//                    SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
//                    searchSmtProcessListProcess.setWorkOrderCode(mesPmProcessCardDto.getWorkOrderCode());
//
//                    List<SmtProcessListProcessDto> smtProcessListProcessDtos = smtProcessListProcessMapper.findList(searchSmtProcessListProcess);
//                    mesPmProcessCardDto.setSmtProcessListProcessDtos(smtProcessListProcessDtos);
//                    break;
//                case 6:
//                    SearchMesPmBreakBulk searchMesPmBreakBulk = new SearchMesPmBreakBulk();
//                    searchMesPmBreakBulk.setWorkOrderId(mesPmProcessCardDto.getWorkOrderId());
//                    List<MesPmBreakBulkDto> mesPmBreakBulkDto = mesPmBreakBulkMapper.findList(searchMesPmBreakBulk);
//                    mesPmBreakBulkDto.forEach(msd->{
//                        SearchMesPmBreakBulkDet searchMesPmBreakBulkDet = new SearchMesPmBreakBulkDet();
//                        searchMesPmBreakBulkDet.setBreakBulkId(msd.getBreakBulkId());
//                        List<MesPmBreakBulkDetDto> mesPmBreakBulkDetDtos = mesPmBreakBulkDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmBreakBulkDet));
//                        msd.setMesPmBreakBulkDets(mesPmBreakBulkDetDtos);
//                    });
//
//                    break;
//                case 7:
//                    break;
//            }
//        }
        return mesPmProcessCardDto;
    }

    @Override
    public List<MesPmProcessListCardDto> processList(SearchMesPmProcessListCard searchMesPmProcessListCard) {
        return mesPmProcessCardMapper.processList(searchMesPmProcessListCard);
    }
}
