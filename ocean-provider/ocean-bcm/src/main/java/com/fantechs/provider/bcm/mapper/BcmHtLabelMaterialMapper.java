package com.fantechs.provider.bcm.mapper;

import com.fantechs.common.base.general.dto.bcm.BcmLabelMaterialDto;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BcmHtLabelMaterialMapper extends MyMapper<BcmHtLabelMaterial> {
    List<BcmHtLabelMaterial> findList(SearchBcmLabelMaterial searchBcmLabelMaterial);
}