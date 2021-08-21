package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqDataCollectDto;
import com.fantechs.common.base.general.dto.daq.DaqDataCollectModel;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.daq.DaqDataCollect;
import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipmentDataGroup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqDataCollectMapper;
import com.fantechs.provider.daq.mapper.DaqEquipmentDataGroupMapper;
import com.fantechs.provider.daq.mapper.DaqEquipmentMapper;
import com.fantechs.provider.daq.service.DaqDataCollectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */
@Service
public class DaqDataCollectServiceImpl extends BaseService<DaqDataCollect> implements DaqDataCollectService {

    @Resource
    private DaqDataCollectMapper daqDataCollectMapper;
    @Resource
    private DaqEquipmentMapper daqEquipmentMapper;
    @Resource
    private DaqEquipmentDataGroupMapper daqEquipmentDataGroupMapper;

    @Override
    public List<DaqDataCollectDto> findList(Map<String, Object> map) {
        return daqDataCollectMapper.findList(map);
    }

    @Override
    public List<DaqDataCollectDto> findByEquipmentId(Long equipmentId) {
        return daqDataCollectMapper.findByEquipmentId(equipmentId);
    }

    @Override
    public DaqDataCollectModel findByGroup(SearchDaqEquipmentDataGroup searchDaqEquipmentDataGroup) {
        DaqDataCollectModel model = new DaqDataCollectModel();
        Map map = new HashMap();
        map.put("equipmentDataGroupId",searchDaqEquipmentDataGroup.getEquipmentDataGroupId());
        List<DaqEquipment> EamEquipmentList = daqEquipmentMapper.findList(map);
        if(StringUtils.isEmpty(EamEquipmentList)) throw new BizErrorException("未查询到对应设备参数");
        for(DaqEquipment eamEquipment : EamEquipmentList){
            List<DaqDataCollectDto> collectDtos = daqDataCollectMapper.findByEquipmentId(eamEquipment.getEquipmentId());
            if(StringUtils.isNotEmpty(collectDtos)) {
                model.setDaqDataCollectDtos(collectDtos);
            }
        }
        List<DaqEquipmentDataGroupDto> daqEquipmentDataGroupDtos = daqEquipmentDataGroupMapper.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentDataGroup));
        if(StringUtils.isEmpty(daqEquipmentDataGroupDtos)) throw new BizErrorException("未查询到对应组别");
        if(StringUtils.isEmpty(daqEquipmentDataGroupDtos.get(0).getDaqEquipmentDataGroupParamDtos())) throw new BizErrorException("该组别未配置对应参数");
        List tableNames = new ArrayList();
        for(DaqEquipmentDataGroupParamDto param : daqEquipmentDataGroupDtos.get(0).getDaqEquipmentDataGroupParamDtos()){
            String tableName ="{\"name\":\""+param.getParamName()+"\",\"value\":\""+param.getFieldName()+"\",\"address\":\""+param.getAddressLoca()+"\"," +
                    "\"min\":\""+param.getMinValue()+"\",\"max\":\""+param.getMaxValue()+"\"}";
            tableNames.add(tableName);
        }

        model.setTableName(tableNames);
        return model;
    }
}
