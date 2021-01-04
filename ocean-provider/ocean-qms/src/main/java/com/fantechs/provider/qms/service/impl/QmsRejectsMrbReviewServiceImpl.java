package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsRejectsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.general.entity.qms.QmsRejectsMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtRejectsMrbReview;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsHtRejectsMrbReviewMapper;
import com.fantechs.provider.qms.mapper.QmsRejectsMrbReviewMapper;
import com.fantechs.provider.qms.service.QmsRejectsMrbReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/28.
 */
@Service
public class QmsRejectsMrbReviewServiceImpl extends BaseService<QmsRejectsMrbReview> implements QmsRejectsMrbReviewService {

    @Resource
    private QmsRejectsMrbReviewMapper qmsRejectsMrbReviewMapper;
    @Resource
    private QmsHtRejectsMrbReviewMapper qmsHtRejectsMrbReviewMapper;

    @Override
    public int save(QmsRejectsMrbReview qmsRejectsMrbReview) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsRejectsMrbReview.setCreateTime(new Date());
        qmsRejectsMrbReview.setCreateUserId(user.getUserId());
        qmsRejectsMrbReview.setModifiedTime(new Date());
        qmsRejectsMrbReview.setModifiedUserId(user.getUserId());
        qmsRejectsMrbReview.setStatus(StringUtils.isEmpty(qmsRejectsMrbReview.getStatus())?1:qmsRejectsMrbReview.getStatus());
        qmsRejectsMrbReview.setRejectsMrbReviewCode(getOdd());

        int i = qmsRejectsMrbReviewMapper.insertUseGeneratedKeys(qmsRejectsMrbReview);

        QmsHtRejectsMrbReview qmsHtRejectsMrbReview = new QmsHtRejectsMrbReview();
        BeanUtils.copyProperties(qmsRejectsMrbReview,qmsHtRejectsMrbReview);
        qmsHtRejectsMrbReviewMapper.insert(qmsHtRejectsMrbReview);

        return i;
    }



    @Override
    public int update(QmsRejectsMrbReview qmsRejectsMrbReview) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsRejectsMrbReview.setModifiedTime(new Date());
        qmsRejectsMrbReview.setModifiedUserId(user.getUserId());

        QmsHtRejectsMrbReview qmsHtRejectsMrbReview = new QmsHtRejectsMrbReview();
        BeanUtils.copyProperties(qmsRejectsMrbReview,qmsHtRejectsMrbReview);
        qmsHtRejectsMrbReviewMapper.insert(qmsHtRejectsMrbReview);
        return qmsRejectsMrbReviewMapper.updateByPrimaryKeySelective(qmsRejectsMrbReview);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<QmsHtRejectsMrbReview> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsRejectsMrbReview qmsMrbReview = qmsRejectsMrbReviewMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsMrbReview)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtRejectsMrbReview qmsHtRejectsMrbReview = new QmsHtRejectsMrbReview();
            BeanUtils.copyProperties(qmsMrbReview,qmsHtRejectsMrbReview);
            qmsHtQualityInspections.add(qmsHtRejectsMrbReview);
        }

        qmsHtRejectsMrbReviewMapper.insertList(qmsHtQualityInspections);

        return qmsRejectsMrbReviewMapper.deleteByIds(ids);
    }

    @Override
    public List<QmsRejectsMrbReviewDto> findList(Map<String, Object> map) {
        return qmsRejectsMrbReviewMapper.findList(map);
    }

    /**
     * 生成不良品评审单号
     * @return
     */
    public String getOdd(){
        String before = "BLD";
        String amongst = new SimpleDateFormat("yyMMdd").format(new Date());
        QmsRejectsMrbReview qmsMrbReview = qmsRejectsMrbReviewMapper.getMax();
        String qmsInspectionTypeCode = before+amongst+"0000";
        if (StringUtils.isNotEmpty(qmsMrbReview)){
            qmsInspectionTypeCode = qmsMrbReview.getRejectsMrbReviewCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionTypeCode.substring(9, qmsInspectionTypeCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }
}
