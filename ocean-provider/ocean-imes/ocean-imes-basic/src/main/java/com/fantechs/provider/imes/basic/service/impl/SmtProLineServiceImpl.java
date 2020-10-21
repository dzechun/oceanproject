package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProLineMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProLineMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtProLineService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmtProLineServiceImpl  extends BaseService<SmtProLine> implements SmtProLineService {

    @Resource
    private SmtProLineMapper smtProLineMapper;

    @Resource
    private SmtHtProLineMapper smtHtProLineMapper;

    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;

    @Override
    public List<SmtProLine> findList(SearchSmtProLine searchSmtProLine) {
        return smtProLineMapper.findList(searchSmtProLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProLine smtProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i=0;
        Example example = new Example(SmtProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode",smtProLine.getProCode());
        List<SmtProLine> smtProLines = smtProLineMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtProLines)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProLine.setCreateUserId(currentUser.getUserId());
        smtProLine.setCreateTime(new Date());
        smtProLineMapper.insertUseGeneratedKeys(smtProLine);

        //新增生产线历史信息
        SmtHtProLine smtHtProLine=new SmtHtProLine();
        BeanUtils.copyProperties(smtProLine,smtHtProLine);
        i = smtHtProLineMapper.insertSelective(smtHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProLine smtProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode",smtProLine.getProCode());
        SmtProLine proLine = smtProLineMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(proLine)&&!proLine.getProLineId().equals(smtProLine.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProLine.setModifiedUserId(currentUser.getUserId());
        smtProLine.setModifiedTime(new Date());
        int i= smtProLineMapper.updateByPrimaryKeySelective(smtProLine);

        //新增生产线历史信息
        SmtHtProLine smtHtProLine=new SmtHtProLine();
        BeanUtils.copyProperties(smtProLine,smtHtProLine);
        smtHtProLine.setModifiedUserId(currentUser.getUserId());
        smtHtProLine.setModifiedTime(new Date());
        smtHtProLineMapper.insertSelective(smtHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtProLine> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String proLineId : idsArr) {
            SmtProLine smtProLine = smtProLineMapper.selectByPrimaryKey(proLineId);
            if(StringUtils.isEmpty(smtProLine)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }


            //被产品工艺路线引用
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proLineId",proLineId);
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductProcessRoutes)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增生产线历史信息
            SmtHtProLine smtHtProLine=new SmtHtProLine();
            BeanUtils.copyProperties(smtProLine,smtHtProLine);
            smtHtProLine.setModifiedUserId(currentUser.getUserId());
            smtHtProLine.setModifiedTime(new Date());
            list.add(smtHtProLine);

        }
         smtHtProLineMapper.insertList(list);
         i=smtProLineMapper.deleteByIds(ids);
        return i;
    }
}
