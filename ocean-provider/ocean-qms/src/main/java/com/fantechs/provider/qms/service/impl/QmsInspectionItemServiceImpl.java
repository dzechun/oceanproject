package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionItem;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsHtInspectionItemMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionItemMapper;
import com.fantechs.provider.qms.service.QmsInspectionItemService;
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
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class QmsInspectionItemServiceImpl extends BaseService<QmsInspectionItem> implements QmsInspectionItemService {

    @Resource
    private QmsInspectionItemMapper qmsInspectionItemMapper;
    @Resource
    private QmsHtInspectionItemMapper qmsHtInspectionItemMapper;

    @Override
    public List<QmsInspectionItemDto> findList(Map<String, Object> map) {
        return qmsInspectionItemMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsInspectionItem qmsInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionItem.setCreateTime(new Date());
        qmsInspectionItem.setCreateUserId(user.getUserId());
        qmsInspectionItem.setModifiedTime(new Date());
        qmsInspectionItem.setModifiedUserId(user.getUserId());
        qmsInspectionItem.setStatus(StringUtils.isEmpty(qmsInspectionItem.getStatus())?1:qmsInspectionItem.getStatus());
        qmsInspectionItem.setInspectionItemCode(getOdd());

        int i = qmsInspectionItemMapper.insertUseGeneratedKeys(qmsInspectionItem);

        QmsHtInspectionItem baseHtProductFamily = new QmsHtInspectionItem();
        BeanUtils.copyProperties(qmsInspectionItem,baseHtProductFamily);
        qmsHtInspectionItemMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsInspectionItem qmsInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        qmsInspectionItem.setModifiedTime(new Date());
        qmsInspectionItem.setModifiedUserId(user.getUserId());

        QmsHtInspectionItem baseHtProductFamily = new QmsHtInspectionItem();
        BeanUtils.copyProperties(qmsInspectionItem,baseHtProductFamily);
        qmsHtInspectionItemMapper.insert(baseHtProductFamily);

        return qmsInspectionItemMapper.updateByPrimaryKeySelective(qmsInspectionItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<QmsHtInspectionItem> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsInspectionItem qmsInspectionItem = qmsInspectionItemMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsInspectionItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtInspectionItem qmsHtInspectionItem = new QmsHtInspectionItem();
            BeanUtils.copyProperties(qmsInspectionItem,qmsHtInspectionItem);
            qmsHtQualityInspections.add(qmsHtInspectionItem);
        }

        qmsHtInspectionItemMapper.insertList(qmsHtQualityInspections);

        return qmsInspectionItemMapper.deleteByIds(ids);
    }


    /**
     * 生成检验项目单号
     * @return
     */
    public String getOdd(){
        String before = "JYXM";
        String amongst = new SimpleDateFormat("YYMMdd").format(new Date());
        QmsInspectionItem qmsInspectionItem = qmsInspectionItemMapper.getMax();
        String qmsInspectionItemCode = before+amongst+"0000";
        if (StringUtils.isNotEmpty(qmsInspectionItem)){
            qmsInspectionItemCode = qmsInspectionItem.getInspectionItemCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionItemCode.substring(8, qmsInspectionItemCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
