package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessReMImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductMaterialReP;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProductProcessReMService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class BaseProductProcessReMServiceImpl extends BaseService<BaseProductProcessReM> implements BaseProductProcessReMService {

    @Resource
    private BaseProductProcessReMMapper baseProductProcessReMMapper;

    @Resource
    private BaseProductMaterialRePMapper baseProductMaterialRePMapper;

    @Resource
    private BaseHtProductProcessReMMapper baseHtProductProcessReMMapper;

    @Resource
    private BaseHtProductMaterialRePMapper baseHtProductMaterialRePMapper;

    @Resource
    private BaseMaterialMapper baseMaterialMapper;

    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Resource
    private BaseLabelCategoryMapper baseLabelCategoryMapper;


    @Override
    public List<BaseProductProcessReM> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        map.put("orgId", user.getOrganizationId());
        List<BaseProductProcessReM> baseProductProcessReMS = baseProductProcessReMMapper.findList(map);
        SearchBaseProductMaterialReP searchBaseProductMaterialReP = new SearchBaseProductMaterialReP();

        for (BaseProductProcessReM baseProductProcessReM : baseProductProcessReMS) {
            searchBaseProductMaterialReP.setProductProcessReMId(baseProductProcessReM.getProductProcessReMId());
            List<BaseProductMaterialReP> list = baseProductMaterialRePMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
            if (StringUtils.isNotEmpty(list)){
                baseProductProcessReM.setList(list);
            }
        }
        return baseProductProcessReMS;
    }

    @Override
    public List<BaseHtProductProcessReM> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        map.put("orgId", user.getOrganizationId());
        List<BaseHtProductProcessReM> baseHtProductProcessReMS = baseHtProductProcessReMMapper.findHtList(map);
        SearchBaseProductMaterialReP searchBaseProductMaterialReP = new SearchBaseProductMaterialReP();

        for (BaseHtProductProcessReM baseHtProductProcessReM : baseHtProductProcessReMS) {
            searchBaseProductMaterialReP.setProductProcessReMId(baseHtProductProcessReM.getProductProcessReMId());
            List<BaseHtProductMaterialReP> htList = baseHtProductMaterialRePMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
            if (StringUtils.isNotEmpty(htList)){
                baseHtProductProcessReM.setHtList(htList);
            }
        }
        return baseHtProductProcessReMS;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<BaseProductProcessReM> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if (StringUtils.isNotEmpty(list)){
            Example example = new Example(BaseProductProcessReM.class);
            if (StringUtils.isEmpty(list.get(0).getProcessId()) || list.get(0).getProcessId() == 0){
                String ids = "";
                example.createCriteria().andEqualTo("materialId",list.get(0).getMaterialId());
                List<BaseProductProcessReM> baseProductProcessReMS = baseProductProcessReMMapper.selectByExample(example);
                for (BaseProductProcessReM baseProductProcessReM : baseProductProcessReMS) {
                    ids += baseProductProcessReM.getProductProcessReMId() + ",";
                }
                this.batchDelete(ids.substring(0, ids.length() - 1));
            }else{
                List ids = new ArrayList();
                for (BaseProductProcessReM baseProductProcessReM : list) {
                    example.createCriteria().andEqualTo("productProcessReMId",baseProductProcessReM.getProductProcessReMId() == null ? -1 : baseProductProcessReM.getProductProcessReMId() );
                    BaseProductProcessReM baseProductProcessReM1 = baseProductProcessReMMapper.selectOneByExample(example);
                    if(StringUtils.isEmpty(baseProductProcessReM1)){
                        this.save(baseProductProcessReM);
                    }else {
                        this.update(baseProductProcessReM);
                    }
                    ids.add(baseProductProcessReM.getProductProcessReMId());
                    example.clear();
                }
                example.createCriteria().andEqualTo("materialId",list.get(0).getMaterialId()).andNotIn("productProcessReMId",ids);
                baseProductProcessReMMapper.deleteByExample(example);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseProductProcessReM baseProductProcessReM) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //物料工序关系表
        /*Example example = new Example(BaseProductProcessReM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductProcessReM.getMaterialId())
                .andEqualTo("processId", baseProductProcessReM.getProcessId());
        BaseProductProcessReM baseProductProcessReM1 = baseProductProcessReMMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductProcessReM1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/

        baseProductProcessReM.setCreateUserId(user.getUserId());
        baseProductProcessReM.setCreateTime(new Date());
        baseProductProcessReM.setModifiedUserId(user.getUserId());
        baseProductProcessReM.setModifiedTime(new Date());
        baseProductProcessReM.setStatus(StringUtils.isEmpty(baseProductProcessReM.getStatus())?1:baseProductProcessReM.getStatus());
        baseProductProcessReM.setOrgId(user.getOrganizationId());
        int i = baseProductProcessReMMapper.insertUseGeneratedKeys(baseProductProcessReM);

        //履历
        BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
        BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
        baseHtProductProcessReMMapper.insertSelective(baseHtProductProcessReM);

        //工序物料清单表
        List<BaseProductMaterialReP> baseProductMaterialRePS = baseProductProcessReM.getList();
        if (StringUtils.isNotEmpty(baseProductMaterialRePS)) {
            for (BaseProductMaterialReP baseProductMaterialReP : baseProductMaterialRePS) {
                baseProductMaterialReP.setCreateUserId(user.getUserId());
                baseProductMaterialReP.setCreateTime(new Date());
                baseProductMaterialReP.setModifiedUserId(user.getUserId());
                baseProductMaterialReP.setModifiedTime(new Date());
                baseProductMaterialReP.setStatus(StringUtils.isEmpty(baseProductMaterialReP.getStatus())?1:baseProductMaterialReP.getStatus());
                baseProductMaterialReP.setOrgId(user.getOrganizationId());
                baseProductMaterialReP.setProductProcessReMId(baseProductProcessReM.getProductProcessReMId());
            }
            baseProductMaterialRePMapper.insertList(baseProductMaterialRePS);
            //履历
            BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
            List<BaseHtProductMaterialReP> htList = new ArrayList<>();
            for (BaseProductMaterialReP baseProductMaterialReP:baseProductMaterialRePS) {
                BeanUtils.copyProperties(baseProductMaterialReP,baseHtProductMaterialReP);
                htList.add(baseHtProductMaterialReP);
            }
            baseHtProductMaterialRePMapper.insertSelective(baseHtProductMaterialReP);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseProductProcessReM baseProductProcessReM) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //物料工序关系表
       /* Example example = new Example(BaseProductProcessReM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductProcessReM.getMaterialId())
                .andEqualTo("processId", baseProductProcessReM.getProcessId())
                .andNotEqualTo("productProcessReMId",baseProductProcessReM.getProductProcessReMId());
        BaseProductProcessReM baseProductProcessReM1 = baseProductProcessReMMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductProcessReM1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/

        baseProductProcessReM.setModifiedUserId(user.getUserId());
        baseProductProcessReM.setModifiedTime(new Date());
        baseProductProcessReM.setOrgId(user.getOrganizationId());
        int i=baseProductProcessReMMapper.updateByPrimaryKeySelective(baseProductProcessReM);

        BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
        BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
        baseHtProductProcessReMMapper.insertSelective(baseHtProductProcessReM);

        //工序物料清单表
        Example example1 = new Example(BaseProductMaterialReP.class);
        example1.createCriteria().andEqualTo("productProcessReMId",baseProductProcessReM.getProductProcessReMId());
        baseProductMaterialRePMapper.deleteByExample(example1);

        List<BaseProductMaterialReP> baseProductMaterialRePS = baseProductProcessReM.getList();
        if(StringUtils.isNotEmpty(baseProductMaterialRePS)){
            for (BaseProductMaterialReP baseProductMaterialReP : baseProductMaterialRePS) {
                baseProductMaterialReP.setModifiedUserId(user.getUserId());
                baseProductMaterialReP.setModifiedTime(new Date());
                baseProductMaterialReP.setProductProcessReMId(baseProductProcessReM.getProductProcessReMId());
            }
            baseProductMaterialRePMapper.insertList(baseProductMaterialRePS);
            //履历
            BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
            List<BaseHtProductMaterialReP> htList = new ArrayList<>();
            for (BaseProductMaterialReP baseProductMaterialReP:baseProductMaterialRePS) {
                BeanUtils.copyProperties(baseProductMaterialReP,baseHtProductMaterialReP);
                htList.add(baseHtProductMaterialReP);
            }
            baseHtProductMaterialRePMapper.insertSelective(baseHtProductMaterialReP);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtProductProcessReM> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseProductProcessReM baseProductProcessReM = baseProductProcessReMMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseProductProcessReM)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example1 = new Example(BaseProductMaterialReP.class);
            example1.createCriteria().andEqualTo("productProcessReMId",baseProductProcessReM.getProductProcessReMId());
            baseProductMaterialRePMapper.deleteByExample(example1);

            BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
            BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
            list.add(baseHtProductProcessReM);
        }

        baseHtProductProcessReMMapper.insertList(list);

        return baseProductProcessReMMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProductProcessReMImport> baseProductProcessReMImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseProductProcessReM> list = new LinkedList<>();
        LinkedList<BaseHtProductProcessReM> htList = new LinkedList<>();
        LinkedList<BaseProductProcessReMImport> productProcessReMImports = new LinkedList<>();

        for (int i = 0; i < baseProductProcessReMImports.size(); i++) {
            BaseProductProcessReMImport baseProductProcessReMImport = baseProductProcessReMImports.get(i);
            String productMaterialCode = baseProductProcessReMImport.getProductMaterialCode();
            String processCode = baseProductProcessReMImport.getProcessCode();

            if (StringUtils.isEmpty(
                    productMaterialCode,processCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("materialCode",productMaterialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            baseProductProcessReMImport.setProductMaterialId(baseMaterial.getMaterialId());

            //判断工序信息是否存在
            Example example2 = new Example(BaseProcess.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria2.andEqualTo("processCode",processCode);
            BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseProcess)){
                fail.add(i+4);
                continue;
            }
            baseProductProcessReMImport.setProcessId(baseProcess.getProcessId());

            // 物料/条码
            String materialCode = baseProductProcessReMImport.getMaterialCode();
            String labelCategoryCode = baseProductProcessReMImport.getLabelCategoryCode();
            Integer scanType = baseProductProcessReMImport.getScanType();
            if(StringUtils.isNotEmpty(scanType)) {
                if (scanType == 1) {
                    if (StringUtils.isEmpty(materialCode) || StringUtils.isNotEmpty(labelCategoryCode)) {
                        fail.add(i + 4);
                        continue;
                    }
                    example1.clear();
                    Example.Criteria criteria = example1.createCriteria();
                    criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
                    criteria.andEqualTo("materialCode", materialCode);
                    BaseMaterial material = baseMaterialMapper.selectOneByExample(example1);
                    if (StringUtils.isEmpty(material)) {
                        fail.add(i + 4);
                        continue;
                    }
                    baseProductProcessReMImport.setMaterialId(material.getMaterialId());

                    //如果零件替代料编码不为空，则判断物料信息是否存在
                    String subMaterialCode = baseProductProcessReMImport.getSubMaterialCode();
                    if (StringUtils.isNotEmpty(subMaterialCode)){
                        example1.clear();
                        Example.Criteria criteria4 = example1.createCriteria();
                        criteria4.andEqualTo("organizationId", currentUser.getOrganizationId());
                        criteria4.andEqualTo("materialCode",subMaterialCode);
                        BaseMaterial subMaterial = baseMaterialMapper.selectOneByExample(example1);
                        if (StringUtils.isEmpty(subMaterial)){
                            fail.add(i+4);
                            continue;
                        }
                        baseProductProcessReMImport.setSubMaterialId(subMaterial.getMaterialId());
                    }

                } else if (scanType == 2) {
                    if (StringUtils.isNotEmpty(materialCode) || StringUtils.isEmpty(labelCategoryCode)) {
                        fail.add(i + 4);
                        continue;
                    }
                    Example example3 = new Example(BaseLabelCategory.class);
                    Example.Criteria criteria3 = example3.createCriteria();
                    criteria3.andEqualTo("orgId", currentUser.getOrganizationId());
                    criteria3.andEqualTo("labelCategoryCode", labelCategoryCode);
                    BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectOneByExample(example3);
                    if (StringUtils.isEmpty(baseLabelCategory)) {
                        fail.add(i + 4);
                        continue;
                    }
                    baseProductProcessReMImport.setLabelCategoryId(baseLabelCategory.getLabelCategoryId());
                }
            }


            productProcessReMImports.add(baseProductProcessReMImport);
        }

        if (StringUtils.isNotEmpty(productProcessReMImports)){
            //对合格数据进行分组
            HashMap<String, List<BaseProductProcessReMImport>> collect = productProcessReMImports.stream().collect(Collectors.groupingBy(BaseProductProcessReMImport::getProductMaterialCode, HashMap::new, Collectors.toList()));
            Set<String> materialCodeSet = collect.keySet();
            for (String materialCode : materialCodeSet) {
                List<BaseProductProcessReMImport> baseProductProcessReMImports1 = collect.get(materialCode);
                HashMap<String, List<BaseProductProcessReMImport>> map = baseProductProcessReMImports1.stream().collect(Collectors.groupingBy(BaseProductProcessReMImport::getProcessCode, HashMap::new, Collectors.toList()));
                Set<String> codeList = map.keySet();
                for (String code : codeList) {
                    List<BaseProductProcessReMImport> baseProductProcessReMImports2 = map.get(code);
                    //新增物料工序关系数据
                    BaseProductProcessReM baseProductProcessReM = new BaseProductProcessReM();
                    baseProductProcessReM.setMaterialId(baseProductProcessReMImports1.get(0).getProductMaterialId());
                    baseProductProcessReM.setProcessId(baseProductProcessReMImports2.get(0).getProcessId());
                    baseProductProcessReM.setCreateTime(new Date());
                    baseProductProcessReM.setCreateUserId(currentUser.getUserId());
                    baseProductProcessReM.setModifiedUserId(currentUser.getUserId());
                    baseProductProcessReM.setModifiedTime(new Date());
                    baseProductProcessReM.setOrgId(currentUser.getOrganizationId());
                    baseProductProcessReM.setStatus((byte) 1);
                    success += baseProductProcessReMMapper.insertUseGeneratedKeys(baseProductProcessReM);

                    BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
                    BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
                    htList.add(baseHtProductProcessReM);

                    //新增工序物料清单数据
                    LinkedList<BaseProductMaterialReP> baseProductMaterialRePList = new LinkedList<>();
                    for (BaseProductProcessReMImport baseProductProcessReMImport : baseProductProcessReMImports2) {
                        if(StringUtils.isNotEmpty(baseProductProcessReMImport.getMaterialCode())
                            && baseProductProcessReMImport.getScanType()==1) {
                            BaseProductMaterialReP baseProductMaterialReP = new BaseProductMaterialReP();
                            BeanUtils.copyProperties(baseProductProcessReMImport, baseProductMaterialReP);
                            baseProductMaterialReP.setProductProcessReMId(baseProductProcessReM.getProductProcessReMId());
                            baseProductMaterialReP.setScanType(StringUtils.isEmpty(baseProductProcessReMImport.getScanType()) ? null : baseProductProcessReMImport.getScanType().byteValue());
                            baseProductMaterialReP.setStatus((byte) 1);
                            baseProductMaterialRePList.add(baseProductMaterialReP);
                        }
                        else if(StringUtils.isEmpty(baseProductProcessReMImport.getMaterialCode())
                                && baseProductProcessReMImport.getScanType()==2){
                            BaseProductMaterialReP baseProductMaterialReP = new BaseProductMaterialReP();
                            BeanUtils.copyProperties(baseProductProcessReMImport, baseProductMaterialReP);
                            baseProductMaterialReP.setProductProcessReMId(baseProductProcessReM.getProductProcessReMId());
                            baseProductMaterialReP.setScanType(StringUtils.isEmpty(baseProductProcessReMImport.getScanType()) ? null : baseProductProcessReMImport.getScanType().byteValue());
                            baseProductMaterialReP.setStatus((byte) 1);
                            baseProductMaterialRePList.add(baseProductMaterialReP);
                        }
                    }
                    if(baseProductMaterialRePList.size()>0) {
                        baseProductMaterialRePMapper.insertList(baseProductMaterialRePList);
                    }
                }
            }
            if(htList.size()>0) {
                baseHtProductProcessReMMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
