package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDetDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspectionDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsQualityInspectionDetMapper;
import com.fantechs.provider.qms.mapper.QmsQualityInspectionMapper;
import com.fantechs.provider.qms.service.QmsQualityInspectionDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class QmsQualityInspectionDetServiceImpl extends BaseService<QmsQualityInspectionDet> implements QmsQualityInspectionDetService {

    @Resource
    private QmsQualityInspectionDetMapper qmsQualityInspectionDetMapper;
    @Resource
    private QmsQualityInspectionMapper qmsQualityInspectionMapper;

    @Override
    public List<QmsQualityInspectionDetDto> findList(Map<String, Object> map) {
        return qmsQualityInspectionDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsQualityInspectionDet qmsQualityInspectionDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsQualityInspectionDet.setModifiedTime(new Date());
        qmsQualityInspectionDet.setModifiedUserId(user.getUserId());
        int i = qmsQualityInspectionDetMapper.updateByPrimaryKeySelective(qmsQualityInspectionDet);

        Example qmsQualityInspectionDetExample = new Example(QmsQualityInspectionDet.class);
        Example.Criteria qmsQualityInspectionDetCriteria = qmsQualityInspectionDetExample.createCriteria();
        qmsQualityInspectionDetCriteria.andEqualTo("qualityInspectionId", qmsQualityInspectionDet.getQualityInspectionId());
        List<QmsQualityInspectionDet> list1 = qmsQualityInspectionDetMapper.selectByExample(qmsQualityInspectionDetExample);

        qmsQualityInspectionDetCriteria.andNotEqualTo("checkoutStatus","0");
        List<QmsQualityInspectionDet> list2 = qmsQualityInspectionDetMapper.selectByExample(qmsQualityInspectionDetExample);

        if (StringUtils.isNotEmpty(list1) && StringUtils.isNotEmpty(list2)){
            QmsQualityInspection qmsQualityInspection = qmsQualityInspectionMapper.selectByPrimaryKey(qmsQualityInspectionDet.getQualityInspectionId());
            if (StringUtils.isNotEmpty(qmsQualityInspection)){
                if (list1.size() == list2.size()){
                    qmsQualityInspection.setBillsStatus(Byte.valueOf("2"));
                }else if (qmsQualityInspection.getBillsStatus() == Byte.valueOf("0")){
                    qmsQualityInspection.setBillsStatus(Byte.valueOf("1"));
                }
            }
            qmsQualityInspectionMapper.updateByPrimaryKeySelective(qmsQualityInspection);
        }

        return i;
    }
}
