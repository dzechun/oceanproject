package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPackageSpecification;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPackageSpecificationMapper;
import com.fantechs.provider.base.mapper.BaseMaterialPackageMapper;
import com.fantechs.provider.base.service.BaseHtPackageSpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/04.
 */
@Service
public class BaseHtPackageSpecificationServiceImpl extends BaseService<BaseHtPackageSpecification> implements BaseHtPackageSpecificationService {

    @Resource
    private BaseHtPackageSpecificationMapper baseHtPackageSpecificationMapper;
    @Resource
    private BaseMaterialPackageMapper baseMaterialPackageMapper;

    @Override
    public List<BaseHtPackageSpecification> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseHtPackageSpecification> baseHtPackageSpecifications = baseHtPackageSpecificationMapper.findHtList(map);

        /*SearchSmtMaterialPackage searchSmtMaterialPackage = new SearchSmtMaterialPackage();

        for (SmtHtPackageSpecification smtHtPackageSpecification : smtHtPackageSpecifications) {
            searchSmtMaterialPackage.setPackageSpecificationId(smtHtPackageSpecification.getPackageSpecificationId());
            List<SmtMaterialPackageDto> smtMaterialPackageDtos = smtMaterialPackageMapper.findList(searchSmtMaterialPackage);
            if (StringUtils.isNotEmpty(smtMaterialPackageDtos)){
                smtHtPackageSpecification.setSmtMaterialPackages(smtMaterialPackageDtos);
            }
        }*/

        return baseHtPackageSpecifications;
    }
}
