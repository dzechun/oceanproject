package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamIssueDto;
import com.fantechs.common.base.general.dto.eam.EamJigStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBook;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigStandingBookMapper extends MyMapper<EamJigStandingBook> {
        List<EamJigStandingBookDto> findList(Map<String,Object> map);
        }