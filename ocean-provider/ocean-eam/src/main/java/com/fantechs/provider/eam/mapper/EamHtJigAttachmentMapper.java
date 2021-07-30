package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtJigAttachmentMapper extends MyMapper<EamHtJigAttachment> {
    List<EamHtJigAttachment> findHtList(Map<String,Object> map);
}