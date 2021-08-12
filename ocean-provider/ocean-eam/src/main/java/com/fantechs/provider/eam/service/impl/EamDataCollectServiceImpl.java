package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.dto.eam.EamDataCollectModel;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamDataCollectMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentDataGroupMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.service.EamDataCollectService;
import com.fantechs.provider.eam.service.EamEquipmentDataGroupService;
import io.micrometer.core.instrument.search.Search;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
public class EamDataCollectServiceImpl extends BaseService<EamDataCollect> implements EamDataCollectService {

    @Resource
    private EamDataCollectMapper eamDataCollectMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private EamEquipmentDataGroupMapper eamEquipmentDataGroupMapper;

    @Override
    public List<EamDataCollectDto> findList(Map<String, Object> map) {
        return eamDataCollectMapper.findList(map);
    }

    @Override
    public List<EamDataCollectDto> findByEquipmentId(Long equipmentId) {
        return eamDataCollectMapper.findByEquipmentId(equipmentId);
    }

    @Override
    public EamDataCollectModel findByGroup( SearchEamEquipmentDataGroup searchEamEquipmentDataGroup) {
        Map map = new HashMap();
        map.put("equipmentDataGroupId",searchEamEquipmentDataGroup.getEquipmentDataGroupId());
        List<EamEquipment> EamEquipmentList = eamEquipmentMapper.findList(map);
        if(StringUtils.isEmpty(EamEquipmentList)) throw new BizErrorException("未查询到对应设备参数");
        List collectDate = new ArrayList();
        for(EamEquipment eamEquipment : EamEquipmentList){
            List<EamDataCollectDto> collectDtos = eamDataCollectMapper.findByEquipmentId(eamEquipment.getEquipmentId());
            if(StringUtils.isNotEmpty(collectDtos))
                collectDate.add(collectDtos.get(0).getCollectData());
        }
        List<EamEquipmentDataGroupDto> eamEquipmentDataGroups = eamEquipmentDataGroupMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentDataGroup));
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
