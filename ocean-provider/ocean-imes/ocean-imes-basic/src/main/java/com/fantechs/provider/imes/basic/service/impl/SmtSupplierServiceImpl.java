package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtAddressDto;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.SmtSupplier;
import com.fantechs.common.base.entity.basic.SmtSupplierAddress;
import com.fantechs.common.base.entity.basic.search.SearchSmtSupplier;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtSignatureMapper;
import com.fantechs.provider.imes.basic.mapper.SmtSupplierAddressMapper;
import com.fantechs.provider.imes.basic.mapper.SmtSupplierMapper;
import com.fantechs.provider.imes.basic.service.SmtSupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2020/09/27.
 */
@Service
public class SmtSupplierServiceImpl  extends BaseService<SmtSupplier> implements SmtSupplierService {

    @Resource
    private SmtSupplierMapper smtSupplierMapper;
    @Resource
    private SmtSignatureMapper smtSignatureMapper;
    @Resource
    private SmtSupplierAddressMapper smtSupplierAddressMapper;

    @Override
    public List<SmtSupplier> findList(SearchSmtSupplier searchSmtSupplier) {
        return smtSupplierMapper.findList(searchSmtSupplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtSupplier record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtSupplier.class);
        example.createCriteria().andEqualTo("supplierCode",record.getSupplierCode())
                                .andNotEqualTo("supplierId",record.getSupplierId());
        List<SmtSupplier> list = smtSupplierMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(list)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUser.getUserId());
        record.setModifiedUserId(currentUser.getUserId());
        record.setModifiedTime(new Date());
        record.setIsDelete((byte) 1);
        int i = smtSupplierMapper.insertUseGeneratedKeys(record);

        List<SmtAddressDto> address = record.getList();

        bindAddress(record.getSupplierId(),address);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtSupplier entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtSupplier.class);
        example.createCriteria().andEqualTo("supplierCode",entity.getSupplierCode());
        SmtSupplier smtSupplier = smtSupplierMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(smtSupplier)&&!smtSupplier.getSupplierId().equals(entity.getSupplierId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        int i = smtSupplierMapper.updateByPrimaryKeySelective(entity);

        Example supplierAddressExample = new Example(SmtSupplierAddress.class);
        supplierAddressExample.createCriteria().andEqualTo("supplierId",entity.getSupplierId());
        smtSupplierAddressMapper.deleteByExample(supplierAddressExample);

        //获取当前供应商的地址集合对象
        List<SmtAddressDto> address = entity.getList();

        bindAddress(entity.getSupplierId(),address);

        return i;
    }

    //供应商绑定地址
    public void bindAddress(Long supplier,List<SmtAddressDto> list){
        if (StringUtils.isNotEmpty(list)){
            List<SmtSupplierAddress> supplierAddresses = new ArrayList<>();
            //将新增的地址与供应商进行绑定
            for (SmtAddressDto smtAddressDto : list) {
                SmtSupplierAddress smtSupplierAddress = new SmtSupplierAddress();
                smtSupplierAddress.setIfDefault(smtAddressDto.getIfDefault());
                smtSupplierAddress.setSupplierId(supplier);
                smtSupplierAddress.setAddressId(smtAddressDto.getAddressId());
                supplierAddresses.add(smtSupplierAddress);
            }
            if(StringUtils.isNotEmpty(supplierAddresses)){
                smtSupplierAddressMapper.insertList(supplierAddresses);
            }
        }
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
            SmtSupplier smtSupplier = smtSupplierMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(smtSupplier)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被物料特征码引用
            Example example = new Example(SmtSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("supplierId",smtSupplier.getSupplierId());
            List<SmtSignature> smtSignatures = smtSignatureMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtSignatures)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }
        }
        i = smtSupplierMapper.deleteByIds(ids);
        return i;
    }
}
