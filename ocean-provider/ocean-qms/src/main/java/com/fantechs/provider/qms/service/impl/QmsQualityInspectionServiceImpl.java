package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
import com.fantechs.common.base.general.entity.qms.history.QmsHtQualityInspection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsHtQualityInspectionMapper;
import com.fantechs.provider.qms.mapper.QmsQualityInspectionMapper;
import com.fantechs.provider.qms.service.QmsQualityInspectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class QmsQualityInspectionServiceImpl extends BaseService<QmsQualityInspection> implements QmsQualityInspectionService {

    @Resource
    private QmsQualityInspectionMapper qmsQualityInspectionMapper;
    @Resource
    private QmsHtQualityInspectionMapper qmsHtQualityInspectionMapper;

    @Override
    public int save(QmsQualityInspection qmsQualityInspection) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsQualityInspection.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("qualityInspectionCode", qmsQualityInspection.getQualityInspectionCode());
        QmsQualityInspection qmsQualityInspection1 = qmsQualityInspectionMapper.selectOneByExample(example);
        //判断单号是否重复
        if (StringUtils.isNotEmpty(qmsQualityInspection1)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100,"质检单号已存在");
        }

        qmsQualityInspection.setCreateTime(new Date());
        qmsQualityInspection.setCreateUserId(user.getUserId());
        qmsQualityInspection.setModifiedTime(new Date());
        qmsQualityInspection.setModifiedUserId(user.getUserId());
        qmsQualityInspection.setStatus(StringUtils.isEmpty(qmsQualityInspection.getStatus())?1:qmsQualityInspection.getStatus());

        int i = qmsQualityInspectionMapper.insertUseGeneratedKeys(qmsQualityInspection);

        QmsHtQualityInspection baseHtProductFamily = new QmsHtQualityInspection();
        BeanUtils.copyProperties(qmsQualityInspection,baseHtProductFamily);
        qmsHtQualityInspectionMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    public int update(QmsQualityInspection qmsQualityInspection) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsQualityInspection.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("qualityInspectionCode", qmsQualityInspection.getQualityInspectionCode())
                .andNotEqualTo("qualityInspectionId",qmsQualityInspection.getQualityInspectionId());
        QmsQualityInspection qmsQualityInspection1 = qmsQualityInspectionMapper.selectOneByExample(example);
        //判断单号是否重复
        if (StringUtils.isNotEmpty(qmsQualityInspection1)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100,"质检单号已存在");
        }

        qmsQualityInspection.setModifiedTime(new Date());
        qmsQualityInspection.setModifiedUserId(user.getUserId());

        QmsHtQualityInspection baseHtProductFamily = new QmsHtQualityInspection();
        BeanUtils.copyProperties(qmsQualityInspection,baseHtProductFamily);
        qmsHtQualityInspectionMapper.insert(baseHtProductFamily);

        return qmsQualityInspectionMapper.updateByPrimaryKeySelective(qmsQualityInspection);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<QmsHtQualityInspection> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsQualityInspection qmsQualityInspection = qmsQualityInspectionMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsQualityInspection)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtQualityInspection qmsHtQualityInspection = new QmsHtQualityInspection();
            BeanUtils.copyProperties(qmsQualityInspection,qmsHtQualityInspection);
            qmsHtQualityInspections.add(qmsHtQualityInspection);
        }

        qmsHtQualityInspectionMapper.insertList(qmsHtQualityInspections);

        return qmsQualityInspectionMapper.deleteByIds(ids);
    }

    @Override
    public List<QmsQualityInspectionDto> findList(Map<String, Object> map) {
        return qmsQualityInspectionMapper.findList(map);
    }
}
