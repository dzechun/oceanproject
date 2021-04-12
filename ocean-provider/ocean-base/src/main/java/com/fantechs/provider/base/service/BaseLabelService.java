package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BaseLabelService extends IService<BaseLabel> {
    List<BaseLabelDto> findList(SearchBaseLabel searchBaseLabel);

    int add(BaseLabel baseLabel, MultipartFile file);

    int update(BaseLabel baseLabel, MultipartFile file);
}
