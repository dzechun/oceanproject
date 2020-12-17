package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BcmLabelCategoryMapper extends MyMapper<BcmLabelCategory> {
    List<BcmLabelCategoryDto> findList(SearchBcmLabelCategory searchBcmLabelCategory);
}