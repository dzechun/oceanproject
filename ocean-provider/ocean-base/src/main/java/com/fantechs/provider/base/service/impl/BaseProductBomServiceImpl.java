package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDetDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductBomImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
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
    @Resource
    private BaseOrganizationMapper baseOrganizationMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductBom baseProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("productBomCode", baseProductBom.getProductBomCode());
        criteria.andEqualTo("productBomVersion", baseProductBom.getProductBomVersion());
        BaseProductBom baseProductBom1 = baseProductBomMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductBom1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();

        //????????????????????????det?????????????????????bom ID
        /*if (StringUtils.isEmpty(baseProductBom.getParentBomId())){
            //?????????BOM????????????BOM????????????????????????BOM??????????????????
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId", baseProductBom.getMaterialId())
                    .andEqualTo("parentBomId",null);

            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProductBoms)){
                throw new BizErrorException("??????????????????BOM?????????");
            }
        }else {
            materialRepeat(baseProductBom);
        }*/

        baseProductBom.setCreateUserId(currentUser.getUserId());
        baseProductBom.setCreateTime(new Date());
        baseProductBom.setModifiedUserId(currentUser.getUserId());
        baseProductBom.setModifiedTime(new Date());
        baseProductBom.setOrgId(currentUser.getOrganizationId());
        if(StringUtils.isEmpty(baseProductBom.getProductBomVersion()))
            baseProductBom.setProductBomVersion(baseProductBom.getProductBomCode());
        baseProductBomMapper.insertUseGeneratedKeys(baseProductBom);

        //????????????BOM????????????
        BaseHtProductBom baseHtProductBom =new BaseHtProductBom();
        BeanUtils.copyProperties(baseProductBom, baseHtProductBom);
        int i = baseHtProductBomMapper.insertSelective(baseHtProductBom);
        return i;
    }

    //????????????????????????????????????????????????
/*    public void materialRepeat(BaseProductBom baseProductBom){
        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomId", baseProductBom.getParentBomId());
        BaseProductBom baseProductBom1 = baseProductBomMapper.selectOneByExample(example);
        if (baseProductBom.getMaterialId().equals(baseProductBom1.getMaterialId())){
            throw new BizErrorException("????????????????????????");
        }
        if (StringUtils.isNotEmpty(baseProductBom1.getParentBomId())){
            materialRepeat(baseProductBom1);
        }
    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductBom baseProductBom) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId())
                .andEqualTo("productBomCode", baseProductBom.getProductBomCode())
                .andEqualTo("productBomVersion", baseProductBom.getProductBomVersion())
                .andNotEqualTo("productBomId",baseProductBom.getProductBomId());
        BaseProductBom baseProductBom1 = baseProductBomMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductBom1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();
        /*if (StringUtils.isEmpty(baseProductBom.getParentBomId())){
            //?????????BOM????????????BOM????????????????????????BOM??????????????????
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId", baseProductBom.getMaterialId())
                    .andEqualTo("parentBomId",null)
                    .andNotEqualTo("productBomId", baseProductBom.getProductBomId());

            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProductBoms)){
                throw new BizErrorException("??????????????????BOM?????????");
            }
        }else {
            materialRepeat(baseProductBom);
        }*/

        baseProductBom.setModifiedUserId(currentUser.getUserId());
        baseProductBom.setModifiedTime(new Date());
        baseProductBom.setOrgId(currentUser.getOrganizationId());
        int i= baseProductBomMapper.updateByPrimaryKeySelective(baseProductBom);

        //????????????BOM????????????
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

        String[] productBomIds = ids.split(",");
        for (String productBomId : productBomIds) {
            BaseProductBom baseProductBom = baseProductBomMapper.selectByPrimaryKey(productBomId);
            if(StringUtils.isEmpty(baseProductBom)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //????????????BOM????????????
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
                throw new BizErrorException("??????BOM????????????????????????");
            }
        }
        baseHtProductBomMapper.insertList(list);

        return baseProductBomMapper.deleteByIds(ids);
    }

    public void find(List<BaseProductBomDet> productBomDets){
        //???????????????????????????BOM
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
                    List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchProductBomDet));
                    baseProductBomDet.setBaseProductBomDets(baseProductBomDets);
                    find(baseProductBomDets);
                }
            }
        }
    }

    @Override
    public List<BaseProductBomDto> findList(Map<String,Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        //???????????????????????????BOM
        List<BaseProductBomDto> smtProductBomDtos = baseProductBomMapper.findList(map);

        /*BaseProductBomDet baseProductBomDet = new BaseProductBomDet();
        if (StringUtils.isNotEmpty(smtProductBomDtos)){
            for (BaseProductBomDto smtProductBomDto : smtProductBomDtos) {
                //????????????????????????bom
                SearchBaseProductBomDet searchProductBomDet = new SearchBaseProductBomDet();
                searchProductBomDet.setProductBomId(smtProductBomDto.getProductBomId());
                List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findList(searchProductBomDet);
                find(baseProductBomDets);
                smtProductBomDto.setBaseProductBomDets(baseProductBomDets);
            }
        }*/
        return smtProductBomDtos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProductBomImport> baseProductBomImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????

        List<BaseProductBomDto> baseProductBomDtos = new ArrayList<>();//??????????????????

        for (int i = 0; i < baseProductBomImports.size(); i++) {
            BaseProductBomImport baseProductBomImport = baseProductBomImports.get(i);
            String productBomCode = baseProductBomImport.getProductBomCode();
            String materialCode = baseProductBomImport.getMaterialCode();
            String proCode = baseProductBomImport.getProCode();
            String subMaterialCode = baseProductBomImport.getSubMaterialCode();
            String processCode = baseProductBomImport.getProcessCode();

            //??????????????????
            if (StringUtils.isEmpty(
                    productBomCode,materialCode
            )) {
                fail.add(i + 4);
                continue;
            }

            //????????????BOM??????????????????
            Example example = new Example(BaseProductBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("productBomCode",productBomCode);
            BaseProductBom baseProductBom = baseProductBomMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseProductBom)){
                fail.add(i + 4);
                continue;
            }

            //??????????????????????????????
            Example example2 = new Example(BaseMaterial.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
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
                criteria4.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria4.andEqualTo("subMaterialCode",materialCode);
                BaseMaterial subMaterial = baseMaterialMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(subMaterial)){
                    fail.add(i + 4);
                    continue;
                }
                baseProductBomImport.setSubMaterialId(subMaterial.getMaterialId());
            }

            //??????????????????????????????
            if (StringUtils.isNotEmpty(proCode)){
                Example example3 = new Example(BaseProLine.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria3.andEqualTo("proCode",proCode);
                BaseProLine baseProLine = baseProLineMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseProLine)){
                    fail.add(i + 4);
                    continue;
                }
                baseProductBomImport.setProLineId(baseProLine.getProLineId());
            }

            //??????????????????????????????
            if (StringUtils.isNotEmpty(processCode)){
                Example example1 = new Example(BaseProcess.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria1.andEqualTo("processCode",processCode);
                BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseProcess)){
                    fail.add(i + 4);
                    continue;
                }
                baseProductBomImport.setProcessId(baseProcess.getProcessId());
            }

            //?????????BOM????????????????????????BOM??????
            boolean repeat = false;
            if (findRepeatMaterialCode(repeat, baseProductBomImports, baseProductBomImport,materialCode)){
                fail.add(i + 4);
                continue;
            }

            //?????????BOM????????????????????????
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

        //????????????BOM
        if (StringUtils.isNotEmpty(baseProductBomDtos)){
            success = baseProductBomMapper.insertList(baseProductBomDtos);
        }

        //?????????ID
        for (BaseProductBomDto baseProductBomDto : baseProductBomDtos) {
            if (StringUtils.isNotEmpty(baseProductBomDto.getParentProductBomCode())){
                Example example = new Example(BaseProductBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("orgId", currentUser.getOrganizationId());
                criteria.andEqualTo("productBomCode",baseProductBomDto.getParentProductBomCode());
                BaseProductBom baseProductBom = baseProductBomMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(baseProductBom)){
                    //baseProductBomDto.setParentBomId(baseProductBom.getProductBomId());
                }
                baseProductBomMapper.updateByPrimaryKey(baseProductBomDto);
            }
        }

        resultMap.put("??????????????????", success);
        resultMap.put("???????????????", fail);
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
            throw new BizErrorException("??????id????????????");

        Example example = new Example(BaseProductBom.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductBom.getMaterialId());
        if(StringUtils.isNotEmpty(baseProductBom.getProductBomVersion()))
            criteria.andEqualTo("productBomVersion", baseProductBom.getProductBomVersion());
        else
            criteria.andEqualTo("productBomVersion", "0");
        BaseProductBom baseProductBomOld = baseProductBomMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(baseProductBomOld)){
            baseProductBom.setProductBomId(baseProductBomOld.getProductBomId());
            baseProductBomMapper.updateByPrimaryKey(baseProductBom);
        }else{
            baseProductBom.setCreateTime(new Date());
            baseProductBomMapper.insertUseGeneratedKeys(baseProductBom);
        }

        return baseProductBom;
    }

    @Override
    public BaseProductBomDto findNextLevelProductBomDet(SearchBaseProductBom searchBaseProductBom) {
        if(StringUtils.isEmpty(searchBaseProductBom.getProductBomId()) && StringUtils.isEmpty(searchBaseProductBom.getProductBomDetId()))
            throw new BizErrorException("??????bomId?????????detId??????????????????");

        List<BaseProductBomDetDto> baseProductBomDetDtos = null;
        List<BaseProductBomDto> list = null;
        SearchBaseProductBom searchProductBom = new SearchBaseProductBom();
        if(StringUtils.isNotEmpty(searchBaseProductBom.getProductBomId())){
            baseProductBomDetDtos = baseProductBomDetMapper.findNextLevelProductBomDet(searchBaseProductBom.getProductBomId());
            searchProductBom.setProductBomId(searchBaseProductBom.getProductBomId());
            list = baseProductBomMapper.findList(ControllerUtil.dynamicConditionByEntity(searchProductBom));

        }else if(StringUtils.isNotEmpty(searchBaseProductBom.getProductBomDetId())){
            BaseProductBomDet baseProductBomDet = baseProductBomDetMapper.selectByPrimaryKey(searchBaseProductBom.getProductBomDetId());
            Example example = new Example(BaseProductBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId", baseProductBomDet.getMaterialId());
            criteria.andEqualTo("orgId", getOrId());
            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example);
            for(BaseProductBom baseProductBom : baseProductBoms){
                if(StringUtils.isEmpty(baseProductBom.getProductBomVersion()) || "0".equals(baseProductBom.getProductBomVersion())) {
                    searchProductBom.setProductBomId(baseProductBom.getProductBomId());
                    list = baseProductBomMapper.findList(ControllerUtil.dynamicConditionByEntity(searchProductBom));
                    baseProductBomDetDtos = baseProductBomDetMapper.findNextLevelProductBomDet(baseProductBom.getProductBomId());
                }
            }
        }
        BaseProductBomDto dto = null;
        if(StringUtils.isEmpty(list)) {
            throw new BizErrorException("???????????????????????????bom");
        }else{
            dto = list.get(0);
        }
        dto.setBaseProductBomDetDtos(baseProductBomDetDtos);
        return dto;
    }

    public Long getOrId() {
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("??????");
        List<BaseOrganizationDto> organizationList = baseOrganizationMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrganization));
        if(StringUtils.isEmpty(organizationList))  throw new BizErrorException("????????????????????????");
        return organizationList.get(0).getOrganizationId();
    }
}
