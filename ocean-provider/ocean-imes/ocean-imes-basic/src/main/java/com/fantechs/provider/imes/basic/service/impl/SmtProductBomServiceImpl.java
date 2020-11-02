package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtProductBomListDto;
import com.fantechs.common.base.entity.basic.SmtProductBom;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductBomMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductBomDetMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductBomMapper;
import com.fantechs.provider.imes.basic.service.SmtProductBomService;
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
public class SmtProductBomServiceImpl extends BaseService<SmtProductBom> implements SmtProductBomService {

    @Resource
    private SmtProductBomMapper smtProductBomMapper;
    @Resource
    private SmtHtProductBomMapper smtHtProductBomMapper;
    @Resource
    private SmtProductBomDetMapper smtProductBomDetMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProductBom smtProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",smtProductBom.getMaterialId());
        criteria.orEqualTo("productBomCode",smtProductBom.getProductBomCode());

        List<SmtProductBom> smtProductBoms = smtProductBomMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtProductBoms)){
            throw new BizErrorException("BOM ID或物料编码信息已存在");
        }

        smtProductBom.setCreateUserId(currentUser.getUserId());
        smtProductBom.setCreateTime(new Date());
        smtProductBom.setModifiedUserId(currentUser.getUserId());
        smtProductBom.setModifiedTime(new Date());
        smtProductBomMapper.insertUseGeneratedKeys(smtProductBom);

        //新增产品BOM历史信息
        SmtHtProductBom smtHtProductBom=new SmtHtProductBom();
        BeanUtils.copyProperties(smtProductBom,smtHtProductBom);
        int i = smtHtProductBomMapper.insertSelective(smtHtProductBom);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProductBom smtProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",smtProductBom.getMaterialId());
        criteria.orEqualTo("productBomCode",smtProductBom.getProductBomCode());

        List<SmtProductBom> smtProductBoms = smtProductBomMapper.selectByExample(example);

        for (SmtProductBom productBom : smtProductBoms) {
            if(StringUtils.isNotEmpty(productBom)&&!productBom.getProductBomId().equals(smtProductBom.getProductBomId())){
                throw new BizErrorException("BOM ID或物料编码信息已存在");
            }
        }


        smtProductBom.setModifiedUserId(currentUser.getUserId());
        smtProductBom.setModifiedTime(new Date());
        int i= smtProductBomMapper.updateByPrimaryKeySelective(smtProductBom);

        //新增产品BOM历史信息
        SmtHtProductBom smtHtProductBom=new SmtHtProductBom();
        BeanUtils.copyProperties(smtProductBom,smtHtProductBom);
        smtHtProductBomMapper.insertSelective(smtHtProductBom);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<SmtHtProductBom> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] productBomIds = ids.split(",");
        for (String productBomId : productBomIds) {
            SmtProductBom smtProductBom = smtProductBomMapper.selectByPrimaryKey(productBomId);
            if(StringUtils.isEmpty(smtProductBom)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增产品BOM历史信息
            SmtHtProductBom smtHtProductBom=new SmtHtProductBom();
            BeanUtils.copyProperties(smtProductBom,smtHtProductBom);
            smtHtProductBom.setModifiedUserId(currentUser.getUserId());
            smtHtProductBom.setModifiedTime(new Date());
            list.add(smtHtProductBom);

            Example example = new Example(SmtProductBomDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productBomId",productBomId);
            List<SmtProductBomDet> smtProductBomDets = smtProductBomDetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductBomDets)){
                throw new BizErrorException("产品BOM被引用，不能删除");
            }
        }
        smtHtProductBomMapper.insertList(list);

        return smtProductBomMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtProductBom> findList(SearchSmtProductBom searchSmtProductBom) {
        return smtProductBomMapper.findList(searchSmtProductBom);
    }

    @Override
    public List<SmtProductBomListDto> findProductBomList(SearchSmtProductBom searchSmtProductBom) {
        //查询所有的父BOM
        List<SmtProductBom> smtProductBomList = smtProductBomMapper.findList(searchSmtProductBom);
        List<SmtProductBomListDto> smtProductBomListDtoList = null;
        if (StringUtils.isNotEmpty(smtProductBomList)){
            smtProductBomListDtoList = new ArrayList<>();
            for (SmtProductBom smtProductBom : smtProductBomList) {
                SmtProductBomListDto smtProductBomListDto = new SmtProductBomListDto();
                smtProductBomListDto.setSmtProductBom(smtProductBom);
                smtProductBomListDtoList.add(smtProductBomListDto);
                searchSmtProductBom.setParentBomId(smtProductBom.getProductBomId());
                traverseFolder(searchSmtProductBom,smtProductBomListDto);
            }
        }
        return smtProductBomListDtoList;
    }

    private void traverseFolder(SearchSmtProductBom searchSmtProductBom,SmtProductBomListDto smtProductBomListDto){
        List<SmtProductBom> smtProductBomList = smtProductBomMapper.findList(searchSmtProductBom);
        if (StringUtils.isNotEmpty(smtProductBomList)){
            List<SmtProductBomListDto> smtProductBomListDtoList = new ArrayList<>();
            for (SmtProductBom smtProductBom : smtProductBomList) {
                SmtProductBomListDto smtProductBomListDto1 = new SmtProductBomListDto();
                smtProductBomListDto1.setSmtProductBom(smtProductBom);
                smtProductBomListDtoList.add(smtProductBomListDto1);
                smtProductBomListDto.setSmtProductBomList(smtProductBomListDtoList);
                searchSmtProductBom.setParentBomId(smtProductBom.getProductBomId());
                traverseFolder(searchSmtProductBom,smtProductBomListDto1);
            }
        }
    }
}
