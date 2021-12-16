package com.fantechs.provider.restapi.mulinsen.mapper;

import com.fantechs.common.base.general.entity.mulinsen.NccBdStordoc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NccBdStordocMapper extends MyMapper<NccBdStordoc> {

    int batchUpdate(List<NccBdStordoc> nccBdStordocList);
}