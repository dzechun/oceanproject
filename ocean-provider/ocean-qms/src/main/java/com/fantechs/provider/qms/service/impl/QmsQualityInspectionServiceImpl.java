package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
    @Transactional(rollbackFor = Exception.class)
    public synchronized int save(QmsQualityInspection qmsQualityInspection) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsQualityInspection.setCreateTime(new Date());
        qmsQualityInspection.setCreateUserId(user.getUserId());
        qmsQualityInspection.setModifiedTime(new Date());
        qmsQualityInspection.setModifiedUserId(user.getUserId());
        qmsQualityInspection.setStatus(StringUtils.isEmpty(qmsQualityInspection.getStatus())?1:qmsQualityInspection.getStatus());

        qmsQualityInspection.setQualityInspectionCode(getOdd());

        int i = qmsQualityInspectionMapper.insertUseGeneratedKeys(qmsQualityInspection);

        QmsHtQualityInspection baseHtProductFamily = new QmsHtQualityInspection();
        BeanUtils.copyProperties(qmsQualityInspection,baseHtProductFamily);
        qmsHtQualityInspectionMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsQualityInspection qmsQualityInspection) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsQualityInspection.setModifiedTime(new Date());
        qmsQualityInspection.setModifiedUserId(user.getUserId());

        QmsHtQualityInspection baseHtProductFamily = new QmsHtQualityInspection();
        BeanUtils.copyProperties(qmsQualityInspection,baseHtProductFamily);
        qmsHtQualityInspectionMapper.insert(baseHtProductFamily);

        return qmsQualityInspectionMapper.updateByPrimaryKeySelective(qmsQualityInspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 生成质检单号
     * @return
     */
    public String getOdd(){
        String before = "DH";
        String amongst = new SimpleDateFormat("YYMMdd").format(new Date());
        QmsQualityInspection qmsQualityInspection = qmsQualityInspectionMapper.getMax();
        String qualityInspectionCode = before+amongst+"0001";
        if (StringUtils.isNotEmpty(qmsQualityInspection)){
            qualityInspectionCode = qmsQualityInspection.getQualityInspectionCode();
        }
        Integer maxCode = Integer.parseInt(qualityInspectionCode.substring(8, qualityInspectionCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
