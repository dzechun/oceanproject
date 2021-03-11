package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtProductBomDto;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.dto.basic.imports.SmtProductBomImport;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtProductBomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    @Resource
    private SmtProLineMapper smtProLineMapper;
    @Resource
    private SmtProcessMapper smtProcessMapper;
    @Resource
    private BaseFeignApi baseFeignApi;


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
    public Map<String, Object> importExcel(List<SmtProductBomImport> smtProductBomImports) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        List<SmtProductBomDto> smtProductBomDtos = new ArrayList<>();//保存合格数据

        for (int i = 0; i < smtProductBomImports.size(); i++) {
            SmtProductBomImport smtProductBomImport = smtProductBomImports.get(i);
            String productBomCode = smtProductBomImport.getProductBomCode();
            String materialCode = smtProductBomImport.getMaterialCode();
            String proCode = smtProductBomImport.getProCode();
            String subMaterialCode = smtProductBomImport.getSubMaterialCode();
            String processCode = smtProductBomImport.getProcessCode();

            //判断必传字段
            if (StringUtils.isEmpty(
                    productBomCode,materialCode
            )) {
                fail.add(i + 4);
                continue;
            }

            //判断产品BOM编码是否重复
            Example example = new Example(SmtProductBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productBomCode",productBomCode);
            SmtProductBom smtProductBom = smtProductBomMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(smtProductBom)){
                fail.add(i + 4);
                continue;
            }

            //判断物料信息是否存在
            Example example2 = new Example(SmtMaterial.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("materialCode",materialCode);
            SmtMaterial smtMaterial = smtMaterialMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(smtMaterial)){
                fail.add(i + 4);
                continue;
            }
            smtProductBomImport.setMaterialId(smtMaterial.getMaterialId());

            if (StringUtils.isNotEmpty(subMaterialCode)){
                example2.clear();
                Example.Criteria criteria4 = example2.createCriteria();
                criteria4.andEqualTo("subMaterialCode",materialCode);
                SmtMaterial subMaterial = smtMaterialMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(subMaterial)){
                    fail.add(i + 4);
                    continue;
                }
                smtProductBomImport.setSubMaterialId(subMaterial.getMaterialId());
            }

            //判断产线信息是否存在
            if (StringUtils.isNotEmpty(proCode)){
                Example example3 = new Example(SmtProLine.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("proCode",proCode);
                SmtProLine smtProLine = smtProLineMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(smtProLine)){
                    fail.add(i + 4);
                    continue;
                }
                smtProductBomImport.setProLineId(smtProLine.getProLineId());
            }

            //判断工序信息是否存在
            if (StringUtils.isNotEmpty(processCode)){
                Example example1 = new Example(SmtProcess.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("processCode",processCode);
                SmtProcess smtProcess = smtProcessMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(smtProcess)){
                    fail.add(i + 4);
                    continue;
                }
                smtProductBomImport.setProcessId(smtProcess.getProcessId());
            }

            //判断子BOM物料是否已被上级BOM引用
            boolean repeat = false;
            if (findRepeatMaterialCode(repeat,smtProductBomImports,smtProductBomImport,materialCode)){
                fail.add(i + 4);
                continue;
            }

            //判断父BOM是否在合格数据中
            if (StringUtils.isNotEmpty(smtProductBomDtos)){
                boolean qualified = true;
                for (SmtProductBomDto smtProductBomDto : smtProductBomDtos) {
                    if (smtProductBomImport.getParentProductBomCode().equals(smtProductBomDto.getProductBomCode())){
                        qualified = false;
                        continue;
                    }
                }
                if (qualified){
                    fail.add(i + 4);
                    continue;
                }

            }

            SmtProductBomDto smtProductBomDto = new SmtProductBomDto();
            BeanUtils.copyProperties(smtProductBomImport,smtProductBomDto);
            smtProductBomDtos.add(smtProductBomDto);
        }

        //新增产品BOM
        if (StringUtils.isNotEmpty(smtProductBomDtos)){
            success = smtProductBomMapper.insertList(smtProductBomDtos);
        }

        //获取父ID
        for (SmtProductBomDto smtProductBomDto : smtProductBomDtos) {
            if (StringUtils.isNotEmpty(smtProductBomDto.getParentProductBomCode())){
                Example example = new Example(SmtProductBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("productBomCode",smtProductBomDto.getParentProductBomCode());
                SmtProductBom smtProductBom = smtProductBomMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(smtProductBom)){
                    smtProductBomDto.setParentBomId(smtProductBom.getProductBomId());
                }
                smtProductBomMapper.updateByPrimaryKey(smtProductBomDto);
            }
        }

        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

    boolean findRepeatMaterialCode(boolean repeat,List<SmtProductBomImport> smtProductBomImports,SmtProductBomImport smtProductBomImport,String materialCode){
        if (StringUtils.isNotEmpty(smtProductBomImport.getParentProductBomCode())){
            for (SmtProductBomImport productBomImport : smtProductBomImports) {
                if (smtProductBomImport.getParentProductBomCode().equals(productBomImport.getProductBomCode())){
                    repeat = findRepeatMaterialCode(repeat,smtProductBomImports,productBomImport,materialCode);
                    if (materialCode.equals(productBomImport.getMaterialCode())){
                        return true;
                    }
                }
            }
        }
        return repeat;
    }

}
