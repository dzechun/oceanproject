package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.dto.eam.EamDataCollectModel;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroup;
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
public class DaqDataCollectServiceImpl extends BaseService<EamDataCollect> implements DaqDataCollectService {

    @Resource
    private DaqDataCollectMapper daqDataCollectMapper;
    @Resource
    private DaqEquipmentMapper daqEquipmentMapper;
    @Resource
    private DaqEquipmentDataGroupMapper daqEquipmentDataGroupMapper;

    @Override
    public List<EamDataCollectDto> findList(Map<String, Object> map) {
        return daqDataCollectMapper.findList(map);
    }

    @Override
    public List<EamDataCollectDto> findByEquipmentId(Long equipmentId) {
        return daqDataCollectMapper.findByEquipmentId(equipmentId);
    }

    @Override
    public EamDataCollectModel findByGroup( SearchEamEquipmentDataGroup searchEamEquipmentDataGroup) {
        Map map = new HashMap();
        map.put("equipmentDataGroupId",searchEamEquipmentDataGroup.getEquipmentDataGroupId());
        List<EamEquipment> EamEquipmentList = daqEquipmentMapper.findList(map);
        if(StringUtils.isEmpty(EamEquipmentList)) throw new BizErrorException("未查询到对应设备参数");
        List collectDate = new ArrayList();
        for(EamEquipment eamEquipment : EamEquipmentList){
            List<EamDataCollectDto> collectDtos = daqDataCollectMapper.findByEquipmentId(eamEquipment.getEquipmentId());
            if(StringUtils.isNotEmpty(collectDtos))
                collectDate.add(collectDtos.get(0).getCollectData());
        }
        List<EamEquipmentDataGroupDto> eamEquipmentDataGroups = daqEquipmentDataGroupMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroup));
        if(StringUtils.isEmpty(eamEquipmentDataGroups)) throw new BizErrorException("未查询到对应组别");
        if(StringUtils.isEmpty(eamEquipmentDataGroups.get(0).getEamEquipmentDataGroupParamDtos())) throw new BizErrorException("该组别未配置对应参数");
      //  String tableName = "[";
        List tableNames = new ArrayList();
        int i = 1;
        for(EamEquipmentDataGroupParamDto param : eamEquipmentDataGroups.get(0).getEamEquipmentDataGroupParamDtos()){
            /*tableName = tableName + "{\"name\":\""+param.getParamName()+"\",\"value\":\""+param.getFieldName()+"\",\"address\":\""+param.getAddressLoca()+"\"," +
                    "\"min\":\""+param.getMinValue()+"\",\"max\":\""+param.getMaxValue()+"\"}";
            if (i < eamEquipmentDataGroups.get(0).getEamEquipmentDataGroupParamDtos().size())
                tableName = tableName + ",";
            i++;*/
            String tableName ="{\"name\":\""+param.getParamName()+"\",\"value\":\""+param.getFieldName()+"\",\"address\":\""+param.getAddressLoca()+"\"," +
                    "\"min\":\""+param.getMinValue()+"\",\"max\":\""+param.getMaxValue()+"\"}";
            tableNames.add(tableName);
        }
     //   tableName += "]";
        EamDataCollectModel model = new EamDataCollectModel();
        model.setTableName(tableNames);
        model.setCollectDate(collectDate);
        return model;
    }
}
