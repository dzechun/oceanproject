package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialSupplierDto;
import com.fantechs.common.base.entity.basic.SmtMaterialSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialSupplier;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialSupplierMapper;
import com.fantechs.provider.imes.basic.service.SmtMaterialSupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/03.
 */
@Service
public class SmtMaterialSupplierServiceImpl extends BaseService<SmtMaterialSupplier> implements SmtMaterialSupplierService {

        @Resource
        private SmtMaterialSupplierMapper smtMaterialSupplierMapper;


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtMaterialSupplier smtMaterialSupplier) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            Example example = new Example(SmtMaterialSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",smtMaterialSupplier.getMaterialId());
            criteria.andEqualTo("materialSupplierCode",smtMaterialSupplier.getMaterialSupplierCode());
            List<SmtMaterialSupplier> smtMaterialSuppliers = smtMaterialSupplierMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtMaterialSuppliers)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
            smtMaterialSupplier.setCreateTime(new Date());
            smtMaterialSupplier.setCreateUserId(currentUser.getUserId());
            smtMaterialSupplier.setModifiedUserId(currentUser.getUserId());
            smtMaterialSupplier.setModifiedTime(new Date());
            int i = smtMaterialSupplierMapper.insertSelective(smtMaterialSupplier);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtMaterialSupplier smtMaterialSupplier) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            Example example = new Example(SmtMaterialSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",smtMaterialSupplier.getMaterialId());
            criteria.andEqualTo("materialSupplierCode",smtMaterialSupplier.getMaterialSupplierCode());
            SmtMaterialSupplier materialSupplier = smtMaterialSupplierMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(materialSupplier)&&!materialSupplier.getMaterialSupplierId().equals(smtMaterialSupplier.getMaterialSupplierId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
            smtMaterialSupplier.setModifiedTime(new Date());
            smtMaterialSupplier.setModifiedUserId(currentUser.getUserId());
            int i = smtMaterialSupplierMapper.updateByPrimaryKeySelective(smtMaterialSupplier);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            int i = 0;
            String[] idsArr = ids.split(",");
            for (String item:idsArr) {
                SmtMaterialSupplier smtMaterialSupplier = smtMaterialSupplierMapper.selectByPrimaryKey(item);
                if(StringUtils.isEmpty(smtMaterialSupplier)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
            }
            i = smtMaterialSupplierMapper.deleteByIds(ids);
            return i;
        }

        @Override
        public List<SmtMaterialSupplierDto> findList(SearchSmtMaterialSupplier searchSmtMaterialSupplier) {
            return smtMaterialSupplierMapper.findList(searchSmtMaterialSupplier);
        }
}
