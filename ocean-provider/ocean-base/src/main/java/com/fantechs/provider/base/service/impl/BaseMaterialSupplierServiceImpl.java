package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseMaterialMapper;
import com.fantechs.provider.base.mapper.BaseMaterialSupplierMapper;
import com.fantechs.provider.base.mapper.BaseSupplierMapper;
import com.fantechs.provider.base.service.BaseMaterialSupplierService;
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
public class BaseMaterialSupplierServiceImpl extends BaseService<BaseMaterialSupplier> implements BaseMaterialSupplierService {

        @Resource
        private BaseMaterialSupplierMapper baseMaterialSupplierMapper;
        @Resource
        private BaseMaterialMapper baseMaterialMapper;
        @Resource
        private BaseSupplierMapper baseSupplierMapper;


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(BaseMaterialSupplier baseMaterialSupplier) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            Example example = new Example(BaseMaterialSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",baseMaterialSupplier.getMaterialId());
            criteria.andEqualTo("materialSupplierCode",baseMaterialSupplier.getMaterialSupplierCode());
            List<BaseMaterialSupplier> baseMaterialSuppliers = baseMaterialSupplierMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseMaterialSuppliers)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
            baseMaterialSupplier.setCreateTime(new Date());
            baseMaterialSupplier.setCreateUserId(currentUser.getUserId());
            baseMaterialSupplier.setModifiedUserId(currentUser.getUserId());
            baseMaterialSupplier.setModifiedTime(new Date());
            int i = baseMaterialSupplierMapper.insertSelective(baseMaterialSupplier);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(BaseMaterialSupplier baseMaterialSupplier) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            Example example = new Example(BaseMaterialSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",baseMaterialSupplier.getMaterialId());
            criteria.andEqualTo("materialSupplierCode",baseMaterialSupplier.getMaterialSupplierCode());
            BaseMaterialSupplier materialSupplier = baseMaterialSupplierMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(materialSupplier)&&!materialSupplier.getMaterialSupplierId().equals(baseMaterialSupplier.getMaterialSupplierId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
            baseMaterialSupplier.setModifiedTime(new Date());
            baseMaterialSupplier.setModifiedUserId(currentUser.getUserId());
            int i = baseMaterialSupplierMapper.updateByPrimaryKeySelective(baseMaterialSupplier);
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
                BaseMaterialSupplier baseMaterialSupplier = baseMaterialSupplierMapper.selectByPrimaryKey(item);
                if(StringUtils.isEmpty(baseMaterialSupplier)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
            }
            i = baseMaterialSupplierMapper.deleteByIds(ids);
            return i;
        }

        @Override
        public List<BaseMaterialSupplierDto> findList(SearchBaseMaterialSupplier searchBaseMaterialSupplier) {
            return baseMaterialSupplierMapper.findList(searchBaseMaterialSupplier);
        }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseMaterialSupplierImport> baseMaterialSupplierImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseMaterialSupplier> list = new LinkedList<>();
        LinkedList<BaseMaterialSupplierImport> materialSupplierImports = new LinkedList<>();

        for (int i = 0; i < baseMaterialSupplierImports.size(); i++) {
            BaseMaterialSupplierImport baseMaterialSupplierImport = baseMaterialSupplierImports.get(i);
            String materialSupplierCode = baseMaterialSupplierImport.getMaterialSupplierCode();
            String materialCode = baseMaterialSupplierImport.getMaterialCode();
            String supplierCode = baseMaterialSupplierImport.getSupplierCode();
            if (StringUtils.isEmpty(
                    materialSupplierCode,materialCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            Long materialId = baseMaterial.getMaterialId();
            baseMaterialSupplierImport.setMaterialId(materialId);

            //判断数据是否重复
            Example example = new Example(BaseMaterialSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialSupplierCode",materialSupplierCode)
                    .andEqualTo("materialId",materialId);
            if (StringUtils.isNotEmpty(baseMaterialSupplierMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //客户编码不为空则判断客户信息是否存在
            if (StringUtils.isNotEmpty(supplierCode)){
                Example example2 = new Example(BaseSupplier.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("supplierCode",supplierCode);
                BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseSupplier)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialSupplierImport.setSupplierId(baseSupplier.getSupplierId());
            }

            //判断集合中是否存在重复数据
            boolean tag = true;
            if (StringUtils.isNotEmpty(materialSupplierImports)){
                for (BaseMaterialSupplierImport materialSupplierImport :materialSupplierImports){
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
            materialSupplierImports.add(baseMaterialSupplierImport);
        }

        if (StringUtils.isNotEmpty(materialSupplierImports)){
            for (BaseMaterialSupplierImport materialSupplierImport : materialSupplierImports) {
                BaseMaterialSupplier baseMaterialSupplier = new BaseMaterialSupplier();
                BeanUtils.copyProperties(materialSupplierImport,baseMaterialSupplier);
                baseMaterialSupplier.setCreateTime(new Date());
                baseMaterialSupplier.setCreateUserId(currentUser.getUserId());
                baseMaterialSupplier.setModifiedTime(new Date());
                baseMaterialSupplier.setModifiedUserId(currentUser.getUserId());
                if (StringUtils.isEmpty(baseMaterialSupplier.getStatus())){
                    baseMaterialSupplier.setStatus(1);
                }
                list.add(baseMaterialSupplier);
            }

            success = baseMaterialSupplierMapper.insertList(list);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
