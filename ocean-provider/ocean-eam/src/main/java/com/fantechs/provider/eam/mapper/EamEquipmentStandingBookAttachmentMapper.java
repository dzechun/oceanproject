package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookAttachmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBookAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentStandingBookAttachmentMapper extends MyMapper<EamEquipmentStandingBookAttachment> {
    List<EamEquipmentStandingBookAttachmentDto> findList(Map<String,Object> map);
}