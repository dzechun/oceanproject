package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtPackageSpecification;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtPackageSpecificationMapper;
import com.fantechs.provider.imes.basic.mapper.SmtPackageSpecificationMapper;
import com.fantechs.provider.imes.basic.service.SmtPackageSpecificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/04.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmtPackageSpecificationServiceImpl extends BaseService<SmtPackageSpecification> implements SmtPackageSpecificationService {

    @Resource
    private SmtPackageSpecificationMapper smtPackageSpecificationMapper;
    @Resource
    private SmtHtPackageSpecificationMapper smtHtPackageSpecificationMapper;

    @Override
    public int save(SmtPackageSpecification smtPackageSpecification) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPackageSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packageSpecificationCode", smtPackageSpecification.getPackageSpecificationCode())
                .orEqualTo("materialId", smtPackageSpecification.getMaterialId());
        List<SmtPackageSpecification> smtPackageSpecifications = smtPackageSpecificationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPackageSpecifications)) {
            throw new BizErrorException("包装规格编码或物料ID重复");
        }

        smtPackageSpecification.setCreateUserId(user.getUserId());
        smtPackageSpecification.setCreateTime(new Date());
        smtPackageSpecification.setModifiedUserId(user.getUserId());
        smtPackageSpecification.setModifiedTime(new Date());

        smtPackageSpecificationMapper.insertUseGeneratedKeys(smtPackageSpecification);
        SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
        BeanUtils.copyProperties(smtPackageSpecification, smtHtPackageSpecification);

        return  smtHtPackageSpecificationMapper.insert(smtHtPackageSpecification);
    }

    @Override
    public int update(SmtPackageSpecification smtPackageSpecification) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPackageSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packageSpecificationCode", smtPackageSpecification.getPackageSpecificationCode())
                .orEqualTo("materialId", smtPackageSpecification.getMaterialId())
                .andNotEqualTo("packageSpecificationId", smtPackageSpecification.getPackageSpecificationId());
        List<SmtPackageSpecification> smtPackageSpecifications = smtPackageSpecificationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPackageSpecifications)) {
            throw new BizErrorException("包装规格编码或物料ID重复");
        }

        smtPackageSpecification.setModifiedUserId(user.getUserId());
        smtPackageSpecification.setModifiedTime(new Date());

        SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
        BeanUtils.copyProperties(smtPackageSpecification, smtHtPackageSpecification);
        smtHtPackageSpecificationMapper.insert(smtHtPackageSpecification);

        return smtPackageSpecificationMapper.updateByPrimaryKeySelective(smtPackageSpecification);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtHtPackageSpecification> smtPackageSpecifications = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtPackageSpecification smtPackageSpecification = smtPackageSpecificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtPackageSpecification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
            BeanUtils.copyProperties(smtPackageSpecification, smtHtPackageSpecification);
            smtHtPackageSpecification.setModifiedTime(new Date());
            smtHtPackageSpecification.setModifiedUserId(user.getUserId());
            smtPackageSpecifications.add(smtHtPackageSpecification);
        }
        smtHtPackageSpecificationMapper.insertList(smtPackageSpecifications);
        return smtPackageSpecificationMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtPackageSpecificationDto> findList(Map<String, Object> map) {
        return smtPackageSpecificationMapper.findList(map);
    }
}
