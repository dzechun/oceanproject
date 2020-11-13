package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.entity.apply.SmtOrder;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/10/13.
 */
@Service
public class SmtOrderServiceImpl extends BaseService<SmtOrder> implements SmtOrderService {

    @Resource
    private SmtOrderMapper smtOrderMapper;

    @Override
    public int save(SmtOrder smtOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderCode",smtOrder.getOrderCode());
        SmtOrder order = smtOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(order)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtOrder.setCreateTime(new Date());
        smtOrder.setCreateUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());
        smtOrder.setModifiedUserId(currentUserInfo.getUserId());

        return smtOrderMapper.insertSelective(smtOrder);
    }

    @Override
    public int batchDelete(String ids) {
        return smtOrderMapper.deleteByIds(ids);
    }

    @Override
    public int update(SmtOrder smtOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderCode",smtOrder.getOrderCode());
        SmtOrder order = smtOrderMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(order) && !order.getOrderId().equals(smtOrder.getOrderId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtOrder.setModifiedUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());

        int i = smtOrderMapper.updateByPrimaryKey(smtOrder);
        return i;
    }

    @Override
    public List<SmtOrderDto> findList(Map<String,Object> parm) {
        return smtOrderMapper.findList(parm);
    }
}
