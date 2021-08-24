package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopNewsAttachmentDto;
import com.fantechs.common.base.general.entity.esop.EsopNewsAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopNewsAttachmentMapper extends MyMapper<EsopNewsAttachment> {
    List<EsopNewsAttachmentDto> findList(Map<String,Object> map);
}