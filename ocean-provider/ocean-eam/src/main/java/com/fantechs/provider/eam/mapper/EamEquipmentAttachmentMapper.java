package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamEquipmentAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentAttachmentMapper extends MyMapper<EamEquipmentAttachment> {
    List<EamEquipmentAttachment> findList(Map<String,Object> map);
}