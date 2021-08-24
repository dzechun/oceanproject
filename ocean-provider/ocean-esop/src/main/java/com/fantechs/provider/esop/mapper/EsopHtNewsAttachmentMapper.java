package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.history.EsopHtNewsAttachment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtNewsAttachmentMapper extends MyMapper<EsopHtNewsAttachment> {
    List<EsopHtNewsAttachment> findHtList(Map<String,Object> map);
}