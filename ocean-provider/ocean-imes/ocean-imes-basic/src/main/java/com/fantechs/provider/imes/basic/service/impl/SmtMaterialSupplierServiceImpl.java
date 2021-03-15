package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtMaterialSupplierDto;
import com.fantechs.common.base.dto.basic.imports.SmtMaterialSupplierImport;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtMaterialSupplier;
import com.fantechs.common.base.entity.basic.SmtSupplier;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialSupplier;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialMapper;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialSupplierMapper;
import com.fantechs.provider.imes.basic.mapper.SmtSupplierMapper;
import com.fantechs.provider.imes.basic.service.SmtMaterialSupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 *
 * Created by wcz on 2020/11/03.
 */
@Service
public class SmtMaterialSupplierServiceImpl extends BaseService<SmtMaterialSupplier> implements SmtMaterialSupplierService {

        @Resource
        private SmtMaterialSupplierMapper smtMaterialSupplierMapper;
        @Resource
        private SmtMaterialMapper smtMaterialMapper;
        @Resource
        private SmtSupplierMapper smtSupplierMapper;


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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtMaterialSupplierImport> smtMaterialSupplierImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtMaterialSupplier> list = new LinkedList<>();
        LinkedList<SmtMaterialSupplierImport> materialSupplierImports = new LinkedList<>();

        for (int i = 0; i < smtMaterialSupplierImports.size(); i++) {
            SmtMaterialSupplierImport smtMaterialSupplierImport = smtMaterialSupplierImports.get(i);
            String materialSupplierCode = smtMaterialSupplierImport.getMaterialSupplierCode();
            String materialCode = smtMaterialSupplierImport.getMaterialCode();
            String supplierCode = smtMaterialSupplierImport.getSupplierCode();
            if (StringUtils.isEmpty(
                    materialSupplierCode,materialCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            Example example1 = new Example(SmtMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialCode",materialCode);
            SmtMaterial smtMaterial = smtMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtMaterial)){
                fail.add(i+4);
                continue;
            }
            Long materialId = smtMaterial.getMaterialId();
            smtMaterialSupplierImport.setMaterialId(materialId);

            //判断数据是否重复
            Example example = new Example(SmtMaterialSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialSupplierCode",materialSupplierCode)
                    .andEqualTo("materialId",materialId);
            if (StringUtils.isNotEmpty(smtMaterialSupplierMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //客户编码不为空则判断客户信息是否存在
            if (StringUtils.isNotEmpty(supplierCode)){
                Example example2 = new Example(SmtSupplier.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("supplierCode",supplierCode);
                SmtSupplier smtSupplier = smtSupplierMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(smtSupplier)){
                    fail.add(i+4);
                    continue;
                }
                smtMaterialSupplierImport.setSupplierId(smtSupplier.getSupplierId());
            }

            //判断集合中是否存在重复数据
            boolean tag = true;
            if (StringUtils.isNotEmpty(materialSupplierImports)){
                for (SmtMaterialSupplierImport materialSupplierImport :materialSupplierImports){
                    if (materialSupplierImport.getMaterialCode().equals(materialCode)
                            && materialSupplierImport.getMaterialSupplierCode().equals(materialSupplierCode)){
                        tag = false;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }
            materialSupplierImports.add(smtMaterialSupplierImport);
        }

        if (StringUtils.isNotEmpty(materialSupplierImports)){
            for (SmtMaterialSupplierImport materialSupplierImport : materialSupplierImports) {
                SmtMaterialSupplier smtMaterialSupplier = new SmtMaterialSupplier();
                BeanUtils.copyProperties(materialSupplierImport,smtMaterialSupplier);
                smtMaterialSupplier.setCreateTime(new Date());
                smtMaterialSupplier.setCreateUserId(currentUser.getUserId());
                smtMaterialSupplier.setModifiedTime(new Date());
                smtMaterialSupplier.setModifiedUserId(currentUser.getUserId());
                if (StringUtils.isEmpty(smtMaterialSupplier.getStatus())){
                    smtMaterialSupplier.setStatus(1);
                }
                list.add(smtMaterialSupplier);
            }

            success = smtMaterialSupplierMapper.insertList(list);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
