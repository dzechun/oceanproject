package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.dto.basic.SmtMaterialPackageDto;
import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialPackage;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtPackageSpecificationMapper;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialPackageMapper;
import com.fantechs.provider.imes.basic.service.SmtHtPackageSpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/04.
 */
@Service
public class SmtHtPackageSpecificationServiceImpl extends BaseService<SmtHtPackageSpecification> implements SmtHtPackageSpecificationService {

    @Resource
    private SmtHtPackageSpecificationMapper smtHtPackageSpecificationMapper;
    @Resource
    private SmtMaterialPackageMapper smtMaterialPackageMapper;

    @Override
    public List<SmtHtPackageSpecification> findHtList(Map<String, Object> map) {

        List<SmtHtPackageSpecification> smtHtPackageSpecifications = smtHtPackageSpecificationMapper.findHtList(map);

        SearchSmtMaterialPackage searchSmtMaterialPackage = new SearchSmtMaterialPackage();

        for (SmtHtPackageSpecification smtHtPackageSpecification : smtHtPackageSpecifications) {
            searchSmtMaterialPackage.setPackageSpecificationId(smtHtPackageSpecification.getPackageSpecificationId());
            List<SmtMaterialPackageDto> smtMaterialPackageDtos = smtMaterialPackageMapper.findList(searchSmtMaterialPackage);
            if (StringUtils.isNotEmpty(smtMaterialPackageDtos)){
                smtHtPackageSpecification.setSmtMaterialPackages(smtMaterialPackageDtos);
            }
        }

        return smtHtPackageSpecifications;
    }
}
