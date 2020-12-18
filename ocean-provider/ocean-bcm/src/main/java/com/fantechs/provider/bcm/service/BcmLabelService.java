package com.fantechs.provider.bcm.service;

import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BcmLabelService extends IService<BcmLabel> {
    List<BcmLabelDto> findList(SearchBcmLabel searchBcmLabel);

    int add(BcmLabel bcmLabel, MultipartFile file);

    int update(BcmLabel bcmLabel,MultipartFile file);
}
