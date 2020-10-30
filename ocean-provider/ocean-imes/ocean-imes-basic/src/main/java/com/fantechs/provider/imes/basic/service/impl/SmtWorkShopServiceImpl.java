package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkShopMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProLineMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtWorkShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
public class SmtWorkShopServiceImpl extends BaseService<SmtWorkShop> implements SmtWorkShopService {

    @Autowired
    private SmtWorkShopMapper smtWorkShopMapper;
    @Autowired
    private SmtHtWorkShopMapper smtHtWorkShopMapper;
    @Autowired
    private SmtProLineMapper smtProLineMapper;

    @Override
    public List<SmtWorkShopDto> findList(Map<String, Object> map) {
        return smtWorkShopMapper.findList(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkShop smtWorkShop) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShopCode", smtWorkShop.getWorkShopCode());
        SmtWorkShop odlSmtWorkShop = smtWorkShopMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(odlSmtWorkShop)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtWorkShop.setCreateUserId(user.getUserId());
        smtWorkShop.setCreateTime(new Date());
        smtWorkShop.setModifiedUserId(user.getUserId());
        smtWorkShop.setModifiedTime(new Date());
        smtWorkShop.setStatus(StringUtils.isEmpty(smtWorkShop.getStatus())?1:smtWorkShop.getStatus());
        smtWorkShopMapper.insertUseGeneratedKeys(smtWorkShop);

        SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
        BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
        int i = smtHtWorkShopMapper.insert(smtHtWorkShop);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtHtWorkShop> list=new LinkedList<>();
        String[] idsArr =  ids.split(",");
        for(String id : idsArr){
            SmtWorkShop smtWorkShop = smtWorkShopMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtWorkShop)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被生产线引用
            Example example = new Example(SmtProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workShopId",smtWorkShop.getWorkShopId());
            List<SmtProLine> smtProLines = smtProLineMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProLines)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            SmtHtWorkShop smtHtWorkShop = new SmtHtWorkShop();
            BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
            smtHtWorkShop.setModifiedUserId(currentUser.getUserId());
            smtHtWorkShop.setModifiedTime(new Date());
            list.add(smtHtWorkShop);
        }
         smtHtWorkShopMapper.insertList(list);
        return smtWorkShopMapper.deleteByIds(ids);
    }
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWorkShop smtWorkShop) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShopCode", smtWorkShop.getWorkShopCode());

        SmtWorkShop odlsmtWorkShop = smtWorkShopMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtWorkShop)&&!odlsmtWorkShop.getWorkShopId().equals(smtWorkShop.getWorkShopId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtWorkShop.setModifiedTime(new Date());
        smtWorkShop.setModifiedUserId(user.getUserId());

        SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
        BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);

        smtHtWorkShopMapper.insert(smtHtWorkShop);

        return smtWorkShopMapper.updateByPrimaryKeySelective(smtWorkShop);
    }
}
