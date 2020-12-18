package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.BcmLabelMaterialDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BcmLabelMaterialMapper extends MyMapper<BcmLabelMaterial> {
    List<BcmLabelMaterialDto> findList(SearchBcmLabelMaterial searchBcmLabelMaterial);
}