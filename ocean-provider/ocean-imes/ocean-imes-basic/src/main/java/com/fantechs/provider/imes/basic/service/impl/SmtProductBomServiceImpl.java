package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.dto.basic.SmtProductBomDto;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductBomMapper;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductBomDetMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductBomMapper;
import com.fantechs.provider.imes.basic.service.SmtProductBomDetService;
import com.fantechs.provider.imes.basic.service.SmtProductBomService;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.naming.NamingEnumeration;
import javax.validation.constraints.NotBlank;
import java.security.cert.X509Certificate;
import java.util.*;

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
    @Resource
    private SmtMaterialMapper smtMaterialMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProductBom smtProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(SmtProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomCode",smtProductBom.getProductBomCode());
        SmtProductBom smtProductBom1 = smtProductBomMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(smtProductBom1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();
        if (StringUtils.isEmpty(smtProductBom.getParentBomId())){
            //没有父BOM则为顶级BOM，判断同一产品的BOM是否已经存在
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId",smtProductBom.getMaterialId())
                    .andEqualTo("parentBomId",null);

            List<SmtProductBom> smtProductBoms = smtProductBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductBoms)){
                throw new BizErrorException("该物料的产品BOM已存在");
            }
        }else {
            materialRepeat(smtProductBom);
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

    //往上追溯，判断物料是否被上级引用
    public void materialRepeat(SmtProductBom smtProductBom){
        Example example = new Example(SmtProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomId",smtProductBom.getParentBomId());
        SmtProductBom smtProductBom1 = smtProductBomMapper.selectOneByExample(example);
        if (smtProductBom.getMaterialId().equals(smtProductBom1.getMaterialId())){
            throw new BizErrorException("该物料被父级引用");
        }
        if (StringUtils.isNotEmpty(smtProductBom1.getParentBomId())){
            materialRepeat(smtProductBom1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProductBom smtProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(SmtProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomCode",smtProductBom.getProductBomCode())
                .andNotEqualTo("productBomId",smtProductBom.getProductBomId());
        SmtProductBom smtProductBom1 = smtProductBomMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(smtProductBom1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();
        if (StringUtils.isEmpty(smtProductBom.getParentBomId())){
            //没有父BOM则为顶级BOM，判断同一产品的BOM是否已经存在
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId",smtProductBom.getMaterialId())
                    .andEqualTo("parentBomId",null)
                    .andNotEqualTo("productBomId",smtProductBom.getProductBomId());

            List<SmtProductBom> smtProductBoms = smtProductBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductBoms)){
                throw new BizErrorException("该物料的产品BOM已存在");
            }
        }else {
            materialRepeat(smtProductBom);
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
    public List<SmtProductBomDto> findList(Map<String,Object> map) {

        //查询指定层级的产品BOM
        List<SmtProductBomDto> smtProductBomDtos = smtProductBomMapper.findList(map);

        for (SmtProductBomDto smtProductBomDto : smtProductBomDtos) {
            map.put("productBomId",smtProductBomDto.getProductBomId());
            List<SmtProductBomDto> list = findList(map);
            smtProductBomDto.setSmtProductBomDtos(list);
        }
        /*if (StringUtils.isNotEmpty(smtProductBoms)){
            Example example = new Example(SmtProductBomDet.class);
            for (SmtProductBom smtProductBom : smtProductBoms) {
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("productBomId",smtProductBom.getProductBomId());
                //查询所有产品BOM明细
                List<SmtProductBomDet> smtProductBomDets = smtProductBomDetMapper.selectByExample(example);
                example.clear();
                if (StringUtils.isNotEmpty(smtProductBomDets)){
                    smtProductBom.setSmtProductBomDets(smtProductBomDets);
                    for (SmtProductBomDet smtProductBomDet : smtProductBomDets) {
                        findNextLevelProductBomDet(smtProductBomDet);
                    }
                }
            }
        }*/
        return smtProductBomDtos;
    }

    public void findNextLevelProductBomDet(SmtProductBomDet smtProductBomDet){
        Example example = new Example(SmtProductBomDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",smtProductBomDet.getProductBomDetId());
        //查询出所有的子级明细
        List<SmtProductBomDet> smtProductBomDets = smtProductBomDetMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProductBomDets)){
            //将子级明细放进父级实体中返回
            smtProductBomDet.setSmtProductBomDets(smtProductBomDets);
            for (SmtProductBomDet productBomDet : smtProductBomDets) {
                findNextLevelProductBomDet(productBomDet);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtProductBomDto> smtProductBomDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtProductBom> list = new LinkedList<>();
        for (int i = 0; i < smtProductBomDtos.size(); i++) {
            SmtProductBomDto smtProductBomDto = smtProductBomDtos.get(i);
            String productBomCode = smtProductBomDto.getProductBomCode();
            String materialCode = smtProductBomDto.getMaterialCode();
            if (StringUtils.isEmpty(
                    productBomCode,materialCode
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否存在
            Example example = new Example(SmtProductBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productBomCode",smtProductBomDto.getProductBomCode());
            if (StringUtils.isNotEmpty(smtProductBomDetMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            //判断物料是否存在
            Example example1 = new Example(SmtMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialCode",materialCode);
            SmtMaterial smtMaterial = smtMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtMaterial)){
                fail.add(i+3);
                continue;
            }

            SmtProductBom smtProductBom = new SmtProductBom();
            BeanUtils.copyProperties(smtProductBomDto,smtProductBom);
            smtProductBom.setCreateTime(new Date());
            smtProductBom.setCreateUserId(currentUser.getUserId());
            smtProductBom.setModifiedTime(new Date());
            smtProductBom.setModifiedUserId(currentUser.getUserId());
            smtProductBom.setStatus(1);
            list.add(smtProductBom);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtProductBomMapper.insertList(list);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}
