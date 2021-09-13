package com.fantechs.provider.materialapi.imes.utils;

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
    public List<BaseOrganizationDto> getOrId() {
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("雷赛");
        ResponseEntity<List<BaseOrganizationDto>> organizationList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        return organizationList.getData();
    }

    /**
     * 获取物料信息
     */
    public List<BaseMaterial> getBaseMaterial(String materialCode,Long orgId){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(removeZero(materialCode));
        searchBaseMaterial.setOrganizationId(orgId);
        ResponseEntity<List<BaseMaterial>> parentMaterialList = baseFeignApi.findList(searchBaseMaterial);
        return parentMaterialList.getData();
    }

    public String removeZero(String str){
        String newStr = str.replaceAll("^(0+)", "");
        return newStr;
    }
}
