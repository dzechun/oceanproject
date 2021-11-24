package com.fantechs.provider.srm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.general.dto.srm.SrmPoExpediteRecordDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmPoExpediteRecordMapper;
import com.fantechs.provider.srm.service.SrmPoExpediteRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */
@Service
public class SrmPoExpediteRecordServiceImpl extends BaseService<SrmPoExpediteRecord> implements SrmPoExpediteRecordService {

    @Resource
    private SrmPoExpediteRecordMapper srmPoExpediteRecordMapper;

    @Override
    public List<SrmPoExpediteRecordDto> findList(Map<String, Object> map) {
        List<SrmPoExpediteRecordDto> list = srmPoExpediteRecordMapper.findList(map);
        Map<Long, List<SrmPoExpediteRecordDto>> collect = list.stream().collect(Collectors.groupingBy(SrmPoExpediteRecordDto::getPoExpediteRecordId));

        list.clear();
        for (Long aLong : collect.keySet()) {
            List<SrmPoExpediteRecordDto> srmPoExpediteRecordDtos = collect.get(aLong);
            SrmPoExpediteRecordDto srmPoExpediteRecordDto = srmPoExpediteRecordDtos.get(0);
            SrmPoExpediteRecordDto srmPoExpediteRecord = new SrmPoExpediteRecordDto();

            BeanUtil.copyProperties(srmPoExpediteRecordDto,srmPoExpediteRecord);
            srmPoExpediteRecord.setFileList(srmPoExpediteRecordDtos);
            list.add(srmPoExpediteRecord);
        }

        return list;
    }

}
