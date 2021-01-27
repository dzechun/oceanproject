package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDetDto;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.entity.qms.*;
import com.fantechs.common.base.general.entity.qms.history.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsBadItemDetMapper;
import com.fantechs.provider.qms.mapper.QmsBadItemMapper;
import com.fantechs.provider.qms.mapper.QmsHtBadItemMapper;
import com.fantechs.provider.qms.service.QmsBadItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/01/16.
 */
@Service
public class QmsBadItemServiceImpl extends BaseService<QmsBadItem> implements QmsBadItemService {

    @Resource
    private QmsBadItemMapper qmsBadItemMapper;
    @Resource
    private QmsBadItemDetMapper qmsBadItemDetMapper;
    @Resource
    private QmsHtBadItemMapper qmsHtBadItemMapper;

    @Override
    public List<QmsBadItemDto> findList(Map<String, Object> map) {
        return qmsBadItemMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsBadItem qmsBadItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsBadItem.class);
        example.createCriteria().andEqualTo("badTypeCode",qmsBadItem.getBadTypeCode());
        List<QmsBadItem> qmsBadItems = qmsBadItemMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(qmsBadItems)){
            throw new BizErrorException("不良项目类型编码重复");
        }

        qmsBadItem.setCreateTime(new Date());
        qmsBadItem.setCreateUserId(user.getUserId());
        qmsBadItem.setModifiedTime(new Date());
        qmsBadItem.setModifiedUserId(user.getUserId());
        qmsBadItem.setStatus(StringUtils.isEmpty(qmsBadItem.getStatus())?1:qmsBadItem.getStatus());
        qmsBadItem.setOrganizationId(user.getOrganizationId());

        int i = qmsBadItemMapper.insertUseGeneratedKeys(qmsBadItem);

        QmsHtBadItem qmsHtBadItem = new QmsHtBadItem();
        BeanUtils.copyProperties(qmsBadItem,qmsHtBadItem);
        qmsHtBadItemMapper.insert(qmsHtBadItem);

        List<QmsBadItemDetDto> list = qmsBadItem.getList();
        if (StringUtils.isNotEmpty(list)){
            for (QmsBadItemDetDto qmsBadItemDetDto : list) {
                qmsBadItemDetDto.setBadPhenomenonCode(CodeUtils.getId("BPC"));
                qmsBadItemDetDto.setBadItemId(qmsBadItem.getBadItemId());
            }
            qmsBadItemDetMapper.insertList(list);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<QmsHtBadItem> qmsHtBadItems = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsBadItem qmsBadItem = qmsBadItemMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsBadItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtBadItem qmsHtBadItem = new QmsHtBadItem();
            BeanUtils.copyProperties(qmsBadItem,qmsHtBadItem);
            qmsHtBadItems.add(qmsHtBadItem);
        }

        qmsHtBadItemMapper.insertList(qmsHtBadItems);

        Example example = new Example(QmsBadItemDet.class);
        Example.Criteria criteria = example.createCriteria();
        String[] split = ids.split(",");
        criteria.andIn("badItemId", Arrays.asList(split));
        qmsBadItemDetMapper.deleteByExample(example);

        return qmsBadItemMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsBadItem qmsBadItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        qmsBadItem.setModifiedTime(new Date());
        qmsBadItem.setModifiedUserId(user.getUserId());
        qmsBadItem.setOrganizationId(user.getOrganizationId());

        Example example = new Example(QmsBadItemDet.class);
        example.createCriteria().andEqualTo("badItemId",qmsBadItem.getBadItemId());
        qmsBadItemDetMapper.deleteByExample(example);
        List<QmsBadItemDetDto> list = qmsBadItem.getList();
        if (StringUtils.isNotEmpty(list)){
            for (QmsBadItemDetDto qmsBadItemDetDto : list) {
                qmsBadItemDetDto.setBadPhenomenonCode(CodeUtils.getId("BPC"));
                qmsBadItemDetDto.setBadItemId(qmsBadItem.getBadItemId());
            }
            qmsBadItemDetMapper.insertList(list);
        }

        int i = qmsBadItemMapper.updateByPrimaryKeySelective(qmsBadItem);

        QmsHtBadItem qmsHtBadItem = new QmsHtBadItem();
        BeanUtils.copyProperties(qmsBadItem,qmsHtBadItem);
        qmsHtBadItemMapper.insert(qmsHtBadItem);

        return i;
    }
}
