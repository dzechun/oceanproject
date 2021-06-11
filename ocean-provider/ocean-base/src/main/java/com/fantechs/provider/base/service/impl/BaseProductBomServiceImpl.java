package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBom;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductBomImport;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProductBomService;
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
public class BaseProductBomServiceImpl extends BaseService<BaseProductBom> implements BaseProductBomService {

    @Resource
    private BaseProductBomMapper baseProductBomMapper;
    @Resource
    private BaseHtProductBomMapper baseHtProductBomMapper;
    @Resource
    private BaseProductBomDetMapper baseProductBomDetMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseProLineMapper baseProLineMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductBom baseProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }


        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomCode", baseProductBom.getProductBomCode());
        criteria.andEqualTo("productBomVersion", baseProductBom.getProductBomVersion());
        BaseProductBom baseProductBom1 = baseProductBomMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductBom1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();

        //子物料保存变更到det表，无需判断父bom ID
        /*if (StringUtils.isEmpty(baseProductBom.getParentBomId())){
            //没有父BOM则为顶级BOM，判断同一产品的BOM是否已经存在
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId", baseProductBom.getMaterialId())
                    .andEqualTo("parentBomId",null);

            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProductBoms)){
                throw new BizErrorException("该物料的产品BOM已存在");
            }
        }else {
            materialRepeat(baseProductBom);
        }*/

        baseProductBom.setCreateUserId(currentUser.getUserId());
        baseProductBom.setCreateTime(new Date());
        baseProductBom.setModifiedUserId(currentUser.getUserId());
        baseProductBom.setModifiedTime(new Date());
        baseProductBom.setOrgId(currentUser.getOrganizationId());
        baseProductBomMapper.insertUseGeneratedKeys(baseProductBom);

        //新增产品BOM历史信息
        BaseHtProductBom baseHtProductBom =new BaseHtProductBom();
        BeanUtils.copyProperties(baseProductBom, baseHtProductBom);
        int i = baseHtProductBomMapper.insertSelective(baseHtProductBom);
        return i;
    }

    //往上追溯，判断物料是否被上级引用
/*    public void materialRepeat(BaseProductBom baseProductBom){
        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomId", baseProductBom.getParentBomId());
        BaseProductBom baseProductBom1 = baseProductBomMapper.selectOneByExample(example);
        if (baseProductBom.getMaterialId().equals(baseProductBom1.getMaterialId())){
            throw new BizErrorException("该物料被父级引用");
        }
        if (StringUtils.isNotEmpty(baseProductBom1.getParentBomId())){
            materialRepeat(baseProductBom1);
        }
    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductBom baseProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomCode", baseProductBom.getProductBomCode())
                .andEqualTo("productBomVersion", baseProductBom.getProductBomVersion());
        BaseProductBom baseProductBom1 = baseProductBomMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductBom1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();
        /*if (StringUtils.isEmpty(baseProductBom.getParentBomId())){
            //没有父BOM则为顶级BOM，判断同一产品的BOM是否已经存在
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId", baseProductBom.getMaterialId())
                    .andEqualTo("parentBomId",null)
                    .andNotEqualTo("productBomId", baseProductBom.getProductBomId());

            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProductBoms)){
                throw new BizErrorException("该物料的产品BOM已存在");
            }
        }else {
            materialRepeat(baseProductBom);
        }*/

        baseProductBom.setModifiedUserId(currentUser.getUserId());
        baseProductBom.setModifiedTime(new Date());
        baseProductBom.setOrgId(currentUser.getOrganizationId());
        int i= baseProductBomMapper.updateByPrimaryKeySelective(baseProductBom);

        //新增产品BOM历史信息
        BaseHtProductBom baseHtProductBom =new BaseHtProductBom();
        BeanUtils.copyProperties(baseProductBom, baseHtProductBom);
        baseHtProductBomMapper.insertSelective(baseHtProductBom);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<BaseHtProductBom> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] productBomIds = ids.split(",");
        for (String productBomId : productBomIds) {
            BaseProductBom baseProductBom = baseProductBomMapper.selectByPrimaryKey(productBomId);
            if(StringUtils.isEmpty(baseProductBom)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增产品BOM历史信息
            BaseHtProductBom baseHtProductBom =new BaseHtProductBom();
            BeanUtils.copyProperties(baseProductBom, baseHtProductBom);
            baseHtProductBom.setModifiedUserId(currentUser.getUserId());
            baseHtProductBom.setModifiedTime(new Date());
            list.add(baseHtProductBom);

            Example example = new Example(BaseProductBomDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productBomId",productBomId);
            List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProductBomDets)){
                throw new BizErrorException("产品BOM被引用，不能删除");
            }
        }
        baseHtProductBomMapper.insertList(list);

        return baseProductBomMapper.deleteByIds(ids);
    }

    public void find(List<BaseProductBomDet> productBomDets){
        //查询指定层级的产品BOM
   //     List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findList(searchBaseProductBomDet);
   //     productBomDet.setBaseProductBomDets(baseProductBomDets);
        if (StringUtils.isNotEmpty(productBomDets)){
            for (BaseProductBomDet baseProductBomDet : productBomDets) {
                SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
                searchBaseProductBom.setMaterialId(baseProductBomDet.getMaterialId());
                searchBaseProductBom.setProductBomVersion("0");
                List<BaseProductBomDto> list = baseProductBomMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBom));
                if(StringUtils.isNotEmpty(list)){
                    SearchBaseProductBomDet searchProductBomDet = new SearchBaseProductBomDet();
                    searchProductBomDet.setProductBomId(list.get(0).getProductBomId());
                    List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findList(searchProductBomDet);
                    baseProductBomDet.setBaseProductBomDets(baseProductBomDets);
                    find(baseProductBomDets);
                }
            }
        }
    }

    @Override
    public List<BaseProductBomDto> findList(Map<String,Object> map) {

        //查询指定层级的产品BOM
        List<BaseProductBomDto> smtProductBomDtos = baseProductBomMapper.findList(map);

        /*BaseProductBomDet baseProductBomDet = new BaseProductBomDet();
        if (StringUtils.isNotEmpty(smtProductBomDtos)){
            for (BaseProductBomDto smtProductBomDto : smtProductBomDtos) {
                //先查出顶级产品的bom
                SearchBaseProductBomDet searchProductBomDet = new SearchBaseProductBomDet();
                searchProductBomDet.setProductBomId(smtProductBomDto.getProductBomId());
                List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findList(searchProductBomDet);
                find(baseProductBomDets);
                smtProductBomDto.setBaseProductBomDets(baseProductBomDets);
            }
        }*/
        return smtProductBomDtos;
    }

    public BaseProductBomDto findNextLevelProductBomDet(BaseProductBomDto baseProductBomDto){
        Example example = new Example(BaseProductBomDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("product_bom_id", baseProductBomDto.getProductBomId());
        //查询出所有的子级明细
        List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseProductBomDets)){
            //将子级明细放进父级实体中返回
            baseProductBomDto.setBaseProductBomDets(baseProductBomDets);
            //一次只展示一级
            /*for (BaseProductBomDet productBomDet : baseProductBomDets) {
                findNextLevelProductBomDet(productBomDet);
            }*/
        }
        return baseProductBomDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProductBomImport> baseProductBomImports) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        List<BaseProductBomDto> baseProductBomDtos = new ArrayList<>();//保存合格数据

        for (int i = 0; i < baseProductBomImports.size(); i++) {
            BaseProductBomImport baseProductBomImport = baseProductBomImports.get(i);
            String productBomCode = baseProductBomImport.getProductBomCode();
            String materialCode = baseProductBomImport.getMaterialCode();
            String proCode = baseProductBomImport.getProCode();
            String subMaterialCode = baseProductBomImport.getSubMaterialCode();
            String processCode = baseProductBomImport.getProcessCode();

            //判断必传字段
            if (StringUtils.isEmpty(
                    productBomCode,materialCode
            )) {
                fail.add(i + 4);
                continue;
            }

            //判断产品BOM编码是否重复
            Example example = new Example(BaseProductBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productBomCode",productBomCode);
            BaseProductBom baseProductBom = baseProductBomMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseProductBom)){
                fail.add(i + 4);
                continue;
            }

            //判断物料信息是否存在
            Example example2 = new Example(BaseMaterial.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i + 4);
                continue;
            }
            baseProductBomImport.setMaterialId(baseMaterial.getMaterialId());

            if (StringUtils.isNotEmpty(subMaterialCode)){
                example2.clear();
                Example.Criteria criteria4 = example2.createCriteria();
                criteria4.andEqualTo("subMaterialCode",materialCode);
                BaseMaterial subMaterial = baseMaterialMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(subMaterial)){
                    fail.add(i + 4);
                    continue;
                }
                baseProductBomImport.setSubMaterialId(subMaterial.getMaterialId());
            }

            //判断产线信息是否存在
            if (StringUtils.isNotEmpty(proCode)){
                Example example3 = new Example(BaseProLine.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("proCode",proCode);
                BaseProLine baseProLine = baseProLineMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseProLine)){
                    fail.add(i + 4);
                    continue;
                }
                baseProductBomImport.setProLineId(baseProLine.getProLineId());
            }

            //判断工序信息是否存在
            if (StringUtils.isNotEmpty(processCode)){
                Example example1 = new Example(BaseProcess.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("processCode",processCode);
                BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseProcess)){
                    fail.add(i + 4);
                    continue;
                }
                baseProductBomImport.setProcessId(baseProcess.getProcessId());
            }

            //判断子BOM物料是否已被上级BOM引用
            boolean repeat = false;
            if (findRepeatMaterialCode(repeat, baseProductBomImports, baseProductBomImport,materialCode)){
                fail.add(i + 4);
                continue;
            }

            //判断父BOM是否在合格数据中
            if (StringUtils.isNotEmpty(baseProductBomDtos)){
                boolean qualified = true;
                for (BaseProductBomDto baseProductBomDto : baseProductBomDtos) {
                    if (baseProductBomImport.getParentProductBomCode().equals(baseProductBomDto.getProductBomCode())){
                        qualified = false;
                        continue;
                    }
                }
                if (qualified){
                    fail.add(i + 4);
                    continue;
                }

            }

            BaseProductBomDto baseProductBomDto = new BaseProductBomDto();
            BeanUtils.copyProperties(baseProductBomImport,baseProductBomDto);
            baseProductBomDtos.add(baseProductBomDto);
        }

        //新增产品BOM
        if (StringUtils.isNotEmpty(baseProductBomDtos)){
            success = baseProductBomMapper.insertList(baseProductBomDtos);
        }

        //获取父ID
        for (BaseProductBomDto baseProductBomDto : baseProductBomDtos) {
            if (StringUtils.isNotEmpty(baseProductBomDto.getParentProductBomCode())){
                Example example = new Example(BaseProductBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("productBomCode",baseProductBomDto.getParentProductBomCode());
                BaseProductBom baseProductBom = baseProductBomMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(baseProductBom)){
                    //baseProductBomDto.setParentBomId(baseProductBom.getProductBomId());
                }
                baseProductBomMapper.updateByPrimaryKey(baseProductBomDto);
            }
        }

        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }


    boolean findRepeatMaterialCode(boolean repeat, List<BaseProductBomImport> baseProductBomImports, BaseProductBomImport baseProductBomImport, String materialCode){
        if (StringUtils.isNotEmpty(baseProductBomImport.getParentProductBomCode())){
            for (BaseProductBomImport productBomImport : baseProductBomImports) {
                if (baseProductBomImport.getParentProductBomCode().equals(productBomImport.getProductBomCode())){
                    repeat = findRepeatMaterialCode(repeat, baseProductBomImports,productBomImport,materialCode);
                    if (materialCode.equals(productBomImport.getMaterialCode())){
                        return true;
                    }
                }
            }
        }
        return repeat;
    }

    @Override
    public BaseProductBom addOrUpdate(BaseProductBom baseProductBom) {

        if(StringUtils.isEmpty(baseProductBom.getMaterialId()))
            throw new BizErrorException("物料id不能为空");

        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductBom.getMaterialId());
        if(StringUtils.isNotEmpty(baseProductBom.getProductBomVersion()))
            criteria.andEqualTo("productBomVersion", baseProductBom.getProductBomVersion());
        else
            criteria.andEqualTo("productBomVersion", "0");
        BaseProductBom baseProductBomOld = baseProductBomMapper.selectOneByExample(example);
        //添加组织，后续根据实际情况添加
        baseProductBom.setOrgId((long)1000);
        if (StringUtils.isNotEmpty(baseProductBomOld)){
            baseProductBom.setProductBomId(baseProductBomOld.getProductBomId());
            baseProductBomMapper.updateByPrimaryKey(baseProductBom);

        }else{
            baseProductBom.setCreateTime(new Date());
            baseProductBom.setModifiedTime(new Date());
            baseProductBomMapper.insertUseGeneratedKeys(baseProductBom);
        }

        //新增产品BOM历史信息
        /*BaseHtProductBom baseHtProductBom =new BaseHtProductBom();
        BeanUtils.copyProperties(baseProductBom, baseHtProductBom);
        int i = baseHtProductBomMapper.insertSelective(baseHtProductBom);*/

        return baseProductBom;
    }

    @Override
    public BaseProductBomDto findNextLevelProductBomDet(SearchBaseProductBom searchBaseProductBom) {
        if(StringUtils.isEmpty(searchBaseProductBom.getProductBomId()))  throw new BizErrorException("产品bomId 不能为空");
        List<BaseProductBomDto> list = baseProductBomMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBom));
        if(StringUtils.isEmpty(list))  throw new BizErrorException("未查询到对应的产品bom");
        BaseProductBomDto dto = list.get(0);
        List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findNextLevelProductBomDet(searchBaseProductBom.getProductBomId());
        dto.setBaseProductBomDets(baseProductBomDets);
        return dto;
    }
}
