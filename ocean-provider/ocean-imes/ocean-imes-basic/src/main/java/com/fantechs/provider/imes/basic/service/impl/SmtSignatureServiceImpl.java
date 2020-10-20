package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.history.SmtHtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtSignatureMapper;
import com.fantechs.provider.imes.basic.mapper.SmtSignatureMapper;
import com.fantechs.provider.imes.basic.service.SmtSignatureService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class SmtSignatureServiceImpl  extends BaseService<SmtSignature> implements SmtSignatureService {

    @Resource
    private SmtSignatureMapper smtSignatureMapper;

    @Resource
    private SmtHtSignatureMapper smtHtSignatureMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtSignature smtSignature) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("signatureCode",smtSignature.getSignatureCode());
        List<SmtSignature> smtSignatures = smtSignatureMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtSignatures)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtSignature.setCreateUserId(currentUser.getUserId());
        smtSignature.setCreateTime(new Date());
        smtSignature.setStatus(1);
        smtSignatureMapper.insertUseGeneratedKeys(smtSignature);

        //新增物料特征码历史信息
        SmtHtSignature smtHtSignature=new SmtHtSignature();
        BeanUtils.copyProperties(smtSignature,smtHtSignature);
        int i = smtHtSignatureMapper.insertSelective(smtHtSignature);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtSignature> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] signatureIds = ids.split(",");
        for (String signatureId : signatureIds) {
            SmtSignature smtSignature = smtSignatureMapper.selectByPrimaryKey(Long.parseLong(signatureId));
            if(StringUtils.isEmpty(smtSignature)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增物料特征码历史信息
            SmtHtSignature smtHtSignature=new SmtHtSignature();
            BeanUtils.copyProperties(smtSignature,smtHtSignature);
            smtHtSignature.setModifiedUserId(currentUser.getUserId());
            smtHtSignature.setModifiedTime(new Date());
            list.add(smtHtSignature);
        }
        smtHtSignatureMapper.insertList(list);

        return smtSignatureMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtSignature smtSignature) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("signatureCode",smtSignature.getSignatureCode());

        SmtSignature signature = smtSignatureMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(signature)&&!signature.getSignatureId().equals(smtSignature.getSignatureId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtSignature.setModifiedUserId(currentUser.getUserId());
        smtSignature.setModifiedTime(new Date());
        int i= smtSignatureMapper.updateByPrimaryKeySelective(smtSignature);

        //新增物料特征码历史信息
        SmtHtSignature smtHtSignature=new SmtHtSignature();
        BeanUtils.copyProperties(smtSignature,smtHtSignature);
        smtHtSignature.setCreateUserId(smtSignature.getCreateUserId());
        smtHtSignature.setCreateTime(smtSignature.getCreateTime());
        smtHtSignature.setModifiedUserId(currentUser.getUserId());
        smtHtSignature.setModifiedTime(new Date());
        smtHtSignatureMapper.insertSelective(smtHtSignature);
        return i;
    }

    @Override
    public List<SmtSignature> findList(SearchSmtSignature searchSmtSignature) {
        return smtSignatureMapper.findList(searchSmtSignature);
    }
}
