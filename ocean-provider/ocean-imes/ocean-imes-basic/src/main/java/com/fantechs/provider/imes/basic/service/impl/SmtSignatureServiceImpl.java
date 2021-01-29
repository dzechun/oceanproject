package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
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
import javax.validation.constraints.NotBlank;
import java.util.*;

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
        smtSignature.setModifiedUserId(currentUser.getUserId());
        smtSignature.setModifiedTime(new Date());
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
        smtHtSignatureMapper.insertSelective(smtHtSignature);
        return i;
    }

    @Override
    public List<SmtSignature> findList(SearchSmtSignature searchSmtSignature) {
        return smtSignatureMapper.findList(searchSmtSignature);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtSignature> smtSignatures) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtSignature> list = new LinkedList<>();
        LinkedList<SmtHtSignature> htList = new LinkedList<>();
        for (int i = 0; i < smtSignatures.size(); i++) {
            SmtSignature smtSignature = smtSignatures.get(i);
            String materialCode = smtSignature.getMaterialCode();
            String signatureCode = smtSignature.getSignatureCode();
            if (StringUtils.isEmpty(
                    materialCode,signatureCode
            )){
                fail.add(i+3);
                continue;
            }

            //判断物料和物料的特征码是否已经存在
            Example example = new Example(SmtSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialCode",smtSignature.getMaterialCode())
                    .orEqualTo("signatureCode",smtSignature.getSignatureCode());
            if (StringUtils.isNotEmpty(smtSignatureMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            smtSignature.setCreateTime(new Date());
            smtSignature.setCreateUserId(currentUser.getUserId());
            smtSignature.setModifiedTime(new Date());
            smtSignature.setModifiedUserId(currentUser.getUserId());
            smtSignature.setStatus(1);
            list.add(smtSignature);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtSignatureMapper.insertList(list);
        }

        for (SmtSignature smtSignature : list) {
            SmtHtSignature smtHtSignature = new SmtHtSignature();
            BeanUtils.copyProperties(smtSignature,smtHtSignature);
            htList.add(smtHtSignature);
        }
        if (StringUtils.isNotEmpty(htList)){
            smtHtSignatureMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
