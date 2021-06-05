package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetSampleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@Service
public class QmsIpqcInspectionOrderDetSampleServiceImpl extends BaseService<QmsIpqcInspectionOrderDetSample> implements QmsIpqcInspectionOrderDetSampleService {

    @Resource
    private QmsIpqcInspectionOrderDetSampleMapper qmsIpqcInspectionOrderDetSampleMapper;

    @Override
    public List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map) {
        return qmsIpqcInspectionOrderDetSampleMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsIpqcInspectionOrderDetSample record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", record.getBarcode());
        List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增IPQC检验单明细样本
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1:record.getStatus());
        record.setOrgId(user.getOrganizationId());
        return qmsIpqcInspectionOrderDetSampleMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(QmsIpqcInspectionOrderDetSample entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", entity.getBarcode())
                .andNotEqualTo("inspectionOrderDetSampleId",entity.getInspectionOrderDetSampleId());
        List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改IPQC检验单明细样本
        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        return qmsIpqcInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(entity);
    }
}
