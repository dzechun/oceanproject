package com.fantechs.provider.baseapi.esop.util;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
@Component
public class BaseUtils {

    @Resource
    private BaseFeignApi baseFeignApi;


    /**
     * 获取组织信息
     */
    public Long getOrId() {
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("新宝");
        ResponseEntity<List<BaseOrganizationDto>> organizationList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        if (StringUtils.isEmpty(organizationList.getData())) throw new BizErrorException("未查询到对应组织");
        return organizationList.getData().get(0).getOrganizationId();
    }

}
