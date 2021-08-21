package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBookAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentStandingBookAttachmentMapper extends MyMapper<EamHtEquipmentStandingBookAttachment> {
    List<EamHtEquipmentStandingBookAttachment> findHtList(Map<String,Object> map);
}