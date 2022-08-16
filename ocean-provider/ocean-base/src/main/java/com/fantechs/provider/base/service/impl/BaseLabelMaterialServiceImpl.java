package com.fantechs.provider.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseLabelMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.BaseLabelMaterial;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseLabelMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseLabelMaterialServiceImpl extends BaseService<BaseLabelMaterial> implements BaseLabelMaterialService {

    @Resource
    private BaseLabelMaterialMapper baseLabelMaterialMapper;
    @Resource
    private BaseHtLabelMaterialMapper baseHtLabelMaterialMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseLabelMapper baseLabelMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Override
    public List<BaseLabelMaterialDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseLabelMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseLabelMaterial record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(record.getIsProcess()) && record.getIsProcess()==(byte)1 && StringUtils.isEmpty(record.getProcessId())){
            throw new BizErrorException("请绑定工序");
        }

        Example example = new Example(BaseLabelMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelId",record.getLabelId());
        criteria.andEqualTo("materialId",record.getMaterialId());

        BaseLabelMaterial baseLabelMaterial = baseLabelMaterialMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabelMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //获取标签类别
        BaseLabel baseLabel = baseLabelMapper.selectByPrimaryKey(record.getLabelId());
        List<BaseLabelMaterial> list = baseLabelMaterialMapper.findEqualLabel(ControllerUtil.dynamicCondition("materialId",record.getMaterialId(),"categoryId",baseLabel.getLabelCategoryId(),"orgId",currentUserInfo.getOrganizationId()));
        if(StringUtils.isNotEmpty(list) || list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"同物料不能绑定相同产品类型标签");
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        record.setOrgId(currentUserInfo.getOrganizationId());

        return baseLabelMaterialMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseLabelMaterial entity) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseLabelMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",entity.getMaterialId());
        criteria.andEqualTo("labelId",entity.getLabelId());
        criteria.andNotEqualTo("labelMaterialId",entity.getLabelMaterialId());
        BaseLabelMaterial baseLabelMaterial = baseLabelMaterialMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabelMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //获取标签类别
        /*BaseLabel baseLabel = baseLabelMapper.selectByPrimaryKey(entity.getLabelId());
        List<BaseLabelMaterial> list = baseLabelMaterialMapper.findEqualLabel(ControllerUtil.dynamicCondition("materialId",entity.getMaterialId(),"categoryId",baseLabel.getLabelCategoryId(),"orgId",currentUserInfo.getOrganizationId()));
        if(StringUtils.isNotEmpty(list) || list.size()>0){
            if(list.stream().filter(x->x.getLabelMaterialId()!=entity.getLabelMaterialId()).collect(Collectors.toList()).size()>0) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "同物料不能绑定相同产品类型标签");
            }
        }*/

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setOrgId(currentUserInfo.getOrganizationId());

        BaseHtLabelMaterial baseHtLabelMaterial = new BaseHtLabelMaterial();
        BeanUtils.copyProperties(entity, baseHtLabelMaterial);
        baseHtLabelMaterialMapper.insertSelective(baseHtLabelMaterial);

        int i=baseLabelMaterialMapper.updateByPrimaryKeySelective(entity);

        BaseLabel baseLabel = baseLabelMapper.selectByPrimaryKey(entity.getLabelId());
        List<BaseLabelMaterial> list = baseLabelMaterialMapper.findEqualLabel(ControllerUtil.dynamicCondition("materialId",entity.getMaterialId(),"categoryId",baseLabel.getLabelCategoryId(),"orgId",currentUserInfo.getOrganizationId()));
        if(StringUtils.isNotEmpty(list) && list.size()>1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "同物料不能绑定相同产品类型标签");
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtLabelMaterial> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseLabelMaterial baseLabelMaterial = baseLabelMaterialMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseLabelMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtLabelMaterial baseHtLabelMaterial = new BaseHtLabelMaterial();
            BeanUtils.copyProperties(baseLabelMaterial, baseHtLabelMaterial);
            list.add(baseHtLabelMaterial);
        }
        baseHtLabelMaterialMapper.insertList(list);

        return baseLabelMaterialMapper.deleteByIds(ids);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseLabelMaterialImport> baseLabelMaterialImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseLabelMaterial> list = new LinkedList<>();
        LinkedList<BaseHtLabelMaterial> htList = new LinkedList<>();
        LinkedList<BaseLabelMaterialImport> labelMaterialImports = new LinkedList<>();
        LinkedList<BaseLabelMaterial> updateList = new LinkedList<>();

        for (int i = 0; i < baseLabelMaterialImports.size(); i++) {
            BaseLabelMaterialImport baseLabelMaterialImport = baseLabelMaterialImports.get(i);
            String materialCode = baseLabelMaterialImport.getMaterialCode();
            String labelCode = baseLabelMaterialImport.getLabelCode();

            if (StringUtils.isEmpty(
                    materialCode,labelCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId())
                    .andEqualTo("materialCode", materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseMaterial)) {
                fail.add(i + 4);
                continue;
            }
            baseLabelMaterialImport.setMaterialId(baseMaterial.getMaterialId());

            //判断标签信息是否存在
            Example example2 = new Example(BaseLabel.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("orgId", currentUser.getOrganizationId())
                     .andEqualTo("labelCode", labelCode);
            BaseLabel baseLabel = baseLabelMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseLabel)) {
                fail.add(i + 4);
                continue;
            }
            baseLabelMaterialImport.setLabelId(baseLabel.getLabelId());

            //判断工序信息是否存在
            if(StringUtils.isNotEmpty(baseLabelMaterialImport.getProcessCode())){
                Example example3 = new Example(BaseProcess.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("organizationId", currentUser.getOrganizationId())
                        .andEqualTo("processCode", baseLabelMaterialImport.getProcessCode());
                BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseProcess)) {
                    fail.add(i + 4);
                    continue;
                }
                baseLabelMaterialImport.setProcessId(baseProcess.getProcessId());
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(labelMaterialImports)){
                for (BaseLabelMaterialImport labelMaterialImport: labelMaterialImports) {
                    if (materialCode.equals(labelMaterialImport.getMaterialCode())
                            &&labelCode.equals(labelMaterialImport.getLabelCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseLabelMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId())
                    .andEqualTo("materialId",baseLabelMaterialImport.getMaterialId())
                    .andEqualTo("labelId",baseLabelMaterialImport.getLabelId());
            BaseLabelMaterial labelMaterial = baseLabelMaterialMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(labelMaterial)){
                labelMaterial.setOncePrintCount(baseLabelMaterialImport.getOncePrintCount());
                labelMaterial.setModifiedTime(new Date());
                labelMaterial.setModifiedUserId(currentUser.getUserId());
                updateList.add(labelMaterial);
            }else {
                labelMaterialImports.add(baseLabelMaterialImport);
            }
        }

        if (StringUtils.isNotEmpty(labelMaterialImports)){

            for (BaseLabelMaterialImport baseLabelMaterialImport : labelMaterialImports) {
                BaseLabelMaterial baseLabelMaterial = new BaseLabelMaterial();
                BeanUtils.copyProperties(baseLabelMaterialImport,baseLabelMaterial);
                baseLabelMaterial.setIsProcess(baseLabelMaterialImport.getIsProcess().byteValue());
                baseLabelMaterial.setCreateTime(new Date());
                baseLabelMaterial.setCreateUserId(currentUser.getUserId());
                baseLabelMaterial.setModifiedTime(new Date());
                baseLabelMaterial.setModifiedUserId(currentUser.getUserId());
                baseLabelMaterial.setOrgId(currentUser.getOrganizationId());
                baseLabelMaterial.setStatus((byte)1);
                list.add(baseLabelMaterial);
            }
            success = baseLabelMaterialMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseLabelMaterial baseLabelMaterial : list) {
                    BaseHtLabelMaterial baseHtLabelMaterial = new BaseHtLabelMaterial();
                    BeanUtils.copyProperties(baseLabelMaterial, baseHtLabelMaterial);
                    htList.add(baseHtLabelMaterial);
                }
                baseHtLabelMaterialMapper.insertList(htList);
            }
        }
        if (StringUtils.isNotEmpty(updateList)){
            baseLabelMaterialMapper.batchUpdate(updateList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
