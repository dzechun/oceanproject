package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductBomDetMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductBomDetMapper;
import com.fantechs.provider.imes.basic.service.SmtProductBomDetService;
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
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtProductBomDetServiceImpl extends BaseService<SmtProductBomDet> implements SmtProductBomDetService {

         @Resource
         private SmtProductBomDetMapper smtProductBomDetMapper;
         @Resource
         private SmtHtProductBomDetMapper smtHtProductBomDetMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtProductBomDet smtProductBomDet) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtProductBomDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",smtProductBomDet.getPartMaterialId());

            List<SmtProductBomDet> smtProductBomDets = smtProductBomDetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductBomDets)){
                throw new BizErrorException("零件料号已存在");
            }

            smtProductBomDet.setCreateUserId(currentUser.getUserId());
            smtProductBomDet.setCreateTime(new Date());
            smtProductBomDetMapper.insertUseGeneratedKeys(smtProductBomDet);

            //新增产品BOM详细历史信息
            SmtHtProductBomDet smtHtProductBomDet=new SmtHtProductBomDet();
            BeanUtils.copyProperties(smtProductBomDet,smtHtProductBomDet);
            int i = smtHtProductBomDetMapper.insertSelective(smtHtProductBomDet);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtProductBomDet smtProductBomDet) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtProductBomDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",smtProductBomDet.getPartMaterialId());

            SmtProductBomDet productBomDet = smtProductBomDetMapper.selectOneByExample(example);

            if(StringUtils.isNotEmpty(productBomDet)&&!productBomDet.getProductBomDetId().equals(productBomDet.getProductBomDetId())){
                throw new BizErrorException("BOM ID或物料编码信息已存在");
            }

            smtProductBomDet.setModifiedUserId(currentUser.getUserId());
            smtProductBomDet.setModifiedTime(new Date());
            int i= smtProductBomDetMapper.updateByPrimaryKeySelective(smtProductBomDet);

            //新增产品BOM详细历史信息
            SmtHtProductBomDet smtHtProductBomDet=new SmtHtProductBomDet();
            BeanUtils.copyProperties(smtProductBomDet,smtHtProductBomDet);
            smtHtProductBomDet.setModifiedUserId(currentUser.getUserId());
            smtHtProductBomDet.setModifiedTime(new Date());
            smtHtProductBomDetMapper.insertSelective(smtHtProductBomDet);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<SmtHtProductBomDet> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            String[] productBomDetIds = ids.split(",");
            for (String productBomDetId : productBomDetIds) {
                SmtProductBomDet smtProductBomDet = smtProductBomDetMapper.selectByPrimaryKey(productBomDetId);
                if(StringUtils.isEmpty(smtProductBomDet)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //新增产品BOM详细历史信息
                SmtHtProductBomDet smtHtProductBomDet=new SmtHtProductBomDet();
                BeanUtils.copyProperties(smtProductBomDet,smtHtProductBomDet);
                smtHtProductBomDet.setModifiedUserId(currentUser.getUserId());
                smtHtProductBomDet.setModifiedTime(new Date());
                list.add(smtHtProductBomDet);
            }
            smtHtProductBomDetMapper.insertList(list);

            return smtProductBomDetMapper.deleteByIds(ids);
        }

        @Override
        public List<SmtProductBomDet> findList(SearchSmtProductBomDet searchSmtProductBomDet) {
            return smtProductBomDetMapper.findList(searchSmtProductBomDet);
        }
}
