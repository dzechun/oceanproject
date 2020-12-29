package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsHtMrbReviewMapper;
import com.fantechs.provider.qms.mapper.QmsMrbReviewMapper;
import com.fantechs.provider.qms.service.QmsMrbReviewService;
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
 * Created by leifengzhi on 2020/12/24.
 */
@Service
public class QmsMrbReviewServiceImpl extends BaseService<QmsMrbReview> implements QmsMrbReviewService {

    @Resource
    private QmsMrbReviewMapper qmsMrbReviewMapper;
    @Resource
    private QmsHtMrbReviewMapper qmsHtMrbReviewMapper;

    @Override
    public List<QmsMrbReviewDto> findList(Map<String, Object> map) {
        return qmsMrbReviewMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsMrbReview qmsMrbReview) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsMrbReview.setCreateTime(new Date());
        qmsMrbReview.setCreateUserId(user.getUserId());
        qmsMrbReview.setModifiedTime(new Date());
        qmsMrbReview.setModifiedUserId(user.getUserId());
        qmsMrbReview.setStatus(StringUtils.isEmpty(qmsMrbReview.getStatus())?1:qmsMrbReview.getStatus());
        qmsMrbReview.setMrbReviewCode(getOdd());

        int i = qmsMrbReviewMapper.insertUseGeneratedKeys(qmsMrbReview);

        QmsHtMrbReview baseHtProductFamily = new QmsHtMrbReview();
        BeanUtils.copyProperties(qmsMrbReview,baseHtProductFamily);
        qmsHtMrbReviewMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsMrbReview qmsMrbReview) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        qmsMrbReview.setModifiedTime(new Date());
        qmsMrbReview.setModifiedUserId(user.getUserId());

        QmsHtMrbReview baseHtProductFamily = new QmsHtMrbReview();
        BeanUtils.copyProperties(qmsMrbReview,baseHtProductFamily);
        qmsHtMrbReviewMapper.insert(baseHtProductFamily);

        return qmsMrbReviewMapper.updateByPrimaryKeySelective(qmsMrbReview);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<QmsHtMrbReview> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsMrbReview qmsMrbReview = qmsMrbReviewMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsMrbReview)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtMrbReview qmsHtInspectionType = new QmsHtMrbReview();
            BeanUtils.copyProperties(qmsMrbReview,qmsHtInspectionType);
            qmsHtQualityInspections.add(qmsHtInspectionType);
        }

        qmsHtMrbReviewMapper.insertList(qmsHtQualityInspections);

        return qmsMrbReviewMapper.deleteByIds(ids);
    }

    /**
     * 生成评审单号
     * @return
     */
    public String getOdd(){
        String before = "MRB";
        String amongst = new SimpleDateFormat("YYMMdd").format(new Date());
        QmsMrbReview qmsMrbReview = qmsMrbReviewMapper.getMax();
        String qmsInspectionTypeCode = before+amongst+"0000";
        if (StringUtils.isNotEmpty(qmsMrbReview)){
            qmsInspectionTypeCode = qmsMrbReview.getMrbReviewCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionTypeCode.substring(9, qmsInspectionTypeCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }
}
