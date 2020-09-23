package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouseArea;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWarehouseAreaMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWarehouseAreaMapper;
import com.fantechs.provider.imes.basic.service.SmtWarehouseAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@Service
public class SmtWarehouseAreaServiceImpl  extends BaseService<SmtWarehouseArea> implements SmtWarehouseAreaService {

    @Autowired
    private SmtWarehouseAreaMapper  smtWarehouseAreaMapper;
    @Autowired
    private SmtHtWarehouseAreaMapper smtHtWarehouseAreaMapper;


    @Override
    public List<SmtWarehouseArea> findList(Map<String, Object> map) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtWarehouseArea smtWarehouseArea) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        int i=0;
        Example example = new Example(SmtWarehouseArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseAreaCode",smtWarehouseArea.getWarehouseAreaCode());
        List<SmtWarehouseArea> smtWarehouseAreaList = smtWarehouseAreaMapper.selectByExample(example);

        if(StringUtils.isNotEmpty(smtWarehouseAreaList)){
             throw new BizErrorException (ErrorCodeEnum.OPT20012001);
        }
        smtWarehouseArea.setCreateUserId(currentUser.getUserId());
        smtWarehouseAreaMapper.insertUseGeneratedKeys(smtWarehouseArea);

        //新增物料历史信息
        SmtHtWarehouseArea smtHtWarehouseArea=new SmtHtWarehouseArea();
        BeanUtils.copyProperties(smtWarehouseArea,smtHtWarehouseArea);
        i=smtHtWarehouseAreaMapper.insertSelective(smtHtWarehouseArea);
        return i;
    }

//    @Transactional(rollbackFor = Exception.class)
//    public int updateById(SmtWarehouseArea smtWarehouseArea) {
//        SysUser currentUser = null;
//        try {
//            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        } catch (TokenValidationFailedException e) {
//            e.printStackTrace();
//        }
//        if(StringUtils.isEmpty(currentUser)){
//            return ErrorCodeEnum.UAC10011039.getCode();
//        }
//        smtWarehouseArea.setModifiedUserId(currentUser.getUserId());
//        smtWarehouseArea.setModifiedTime(new Date());
//        int i= smtWarehouseAreaMapper.updateByPrimaryKeySelective(smtWarehouseArea);
//
//        //新增物料历史信息
//        SmtHtMaterial smtHtMaterial=new SmtHtMaterial();
//        BeanUtils.copyProperties(smtMaterial,smtHtMaterial);
//        smtHtMaterial.setModifiedUserId(currentUser.getUserId());
//        smtHtMaterial.setModifiedTime(new Date());
//        smtHtMaterialMapper.insertSelective(smtHtMaterial);
//        return i;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public int deleteByIds(List<Long> materialIds) {
//        int i=0;
//        List<SmtHtMaterial> list=new ArrayList<>();
//        SysUser currentUser = null;
//        try {
//            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        } catch (TokenValidationFailedException e) {
//            e.printStackTrace();
//        }
//        if(StringUtils.isEmpty(currentUser)){
//            return ErrorCodeEnum.UAC10011039.getCode();
//        }
//
//        for (Long  materialId : materialIds) {
//            SmtMaterial smtMaterial = smtMaterialMapper.selectByPrimaryKey(materialId);
//            if(StringUtils.isEmpty(smtMaterial)){
//                //throw new BizErrorException("该物料已被删除。");
//                return ErrorCodeEnum.OPT20012003.getCode();
//            }
//            //新增物料历史信息
//            SmtHtMaterial smtHtMaterial=new SmtHtMaterial();
//            BeanUtils.copyProperties(smtMaterial,smtHtMaterial);
//            smtHtMaterial.setModifiedUserId(currentUser.getUserId());
//            smtHtMaterial.setModifiedTime(new Date());
//            list.add(smtHtMaterial);
//
//            smtMaterialMapper.deleteByPrimaryKey(materialId);
//        }
//
//        i=smtHtMaterialMapper.insertList(list);
//        return i;
//    }
}
