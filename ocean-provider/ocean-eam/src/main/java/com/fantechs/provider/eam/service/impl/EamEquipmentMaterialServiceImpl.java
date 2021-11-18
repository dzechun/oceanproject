package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialListDto;
import com.fantechs.common.base.general.dto.eam.imports.EamEquipmentMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentMaterialListMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentMaterialMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaterialMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/06/28.
 */
@Service
public class EamEquipmentMaterialServiceImpl extends BaseService<EamEquipmentMaterial> implements EamEquipmentMaterialService {

    @Resource
    private EamEquipmentMaterialMapper eamEquipmentMaterialMapper;
    @Resource
    private EamEquipmentMaterialListMapper eamEquipmentMaterialListMapper;
    @Resource
    private EamHtEquipmentMaterialMapper eamHtEquipmentMaterialMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    @Override
    public List<EamEquipmentMaterialDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentMaterial record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", record.getEquipmentId());
        EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentMaterialMapper.insertUseGeneratedKeys(record);

        //新增明细
        List<EamEquipmentMaterialList> eamEquipmentMaterialLists = record.getList();
        if(StringUtils.isNotEmpty(eamEquipmentMaterialLists)){
            for (EamEquipmentMaterialList eamEquipmentMaterialList : eamEquipmentMaterialLists) {
                eamEquipmentMaterialList.setEquipmentMaterialId(record.getEquipmentMaterialId());
                eamEquipmentMaterialList.setCreateUserId(user.getUserId());
                eamEquipmentMaterialList.setCreateTime(new Date());
                eamEquipmentMaterialList.setModifiedUserId(user.getUserId());
                eamEquipmentMaterialList.setModifiedTime(new Date());
                eamEquipmentMaterialList.setStatus(StringUtils.isEmpty(eamEquipmentMaterialList.getStatus())?1:eamEquipmentMaterialList.getStatus());
                eamEquipmentMaterialList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentMaterialListMapper.insertList(eamEquipmentMaterialLists);
        }

        //履历
        EamHtEquipmentMaterial eamHtEquipmentMaterial = new EamHtEquipmentMaterial();
        BeanUtils.copyProperties(record, eamHtEquipmentMaterial);
        int i = eamHtEquipmentMaterialMapper.insert(eamHtEquipmentMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentMaterial entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", entity.getEquipmentId())
                .andNotEqualTo("equipmentMaterialId",entity.getEquipmentMaterialId());
        EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamEquipmentMaterialMapper.updateByPrimaryKeySelective(entity);

        //删除原明细
        Example example1 = new Example(EamEquipmentMaterialList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentMaterialId", entity.getEquipmentMaterialId());
        eamEquipmentMaterialListMapper.deleteByExample(example1);

        //新增明细
        List<EamEquipmentMaterialList> eamEquipmentMaterialLists = entity.getList();
        if(StringUtils.isNotEmpty(eamEquipmentMaterialLists)){
            for (EamEquipmentMaterialList eamEquipmentMaterialList : eamEquipmentMaterialLists) {
                eamEquipmentMaterialList.setEquipmentMaterialId(entity.getEquipmentMaterialId());
                eamEquipmentMaterialList.setCreateUserId(user.getUserId());
                eamEquipmentMaterialList.setCreateTime(new Date());
                eamEquipmentMaterialList.setModifiedUserId(user.getUserId());
                eamEquipmentMaterialList.setModifiedTime(new Date());
                eamEquipmentMaterialList.setStatus(StringUtils.isEmpty(eamEquipmentMaterialList.getStatus())?1:eamEquipmentMaterialList.getStatus());
                eamEquipmentMaterialList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentMaterialListMapper.insertList(eamEquipmentMaterialLists);
        }

        //履历
        EamHtEquipmentMaterial eamHtEquipmentMaterial = new EamHtEquipmentMaterial();
        BeanUtils.copyProperties(entity, eamHtEquipmentMaterial);
        int i = eamHtEquipmentMaterialMapper.insert(eamHtEquipmentMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtEquipmentMaterial> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtEquipmentMaterial eamHtEquipmentMaterial = new EamHtEquipmentMaterial();
            BeanUtils.copyProperties(eamEquipmentMaterial, eamHtEquipmentMaterial);
            list.add(eamHtEquipmentMaterial);

            //删除明细
            Example example1 = new Example(EamEquipmentMaterialList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentMaterialId", eamEquipmentMaterial.getEquipmentMaterialId());
            eamEquipmentMaterialListMapper.deleteByExample(example1);
        }

        eamHtEquipmentMaterialMapper.insertList(list);

        return eamEquipmentMaterialMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EamEquipmentMaterialImport> eamEquipmentMaterialImportsTemp) throws ParseException {
        List<EamEquipmentMaterialImport> eamEquipmentMaterialImports=new ArrayList<>();
        for (EamEquipmentMaterialImport eamEquipmentMaterialImport : eamEquipmentMaterialImportsTemp) {
            if(StringUtils.isNotEmpty(eamEquipmentMaterialImport.getEquipmentCode())){
                eamEquipmentMaterialImports.add(eamEquipmentMaterialImport);
            }
        }
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<EamEquipmentMaterialImport> iterator = eamEquipmentMaterialImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            EamEquipmentMaterialImport eamEquipmentMaterialImport = iterator.next();
            String equipmentCode = eamEquipmentMaterialImport.getEquipmentCode();
            String equipmentName = eamEquipmentMaterialImport.getEquipmentName();
            String materialCode = eamEquipmentMaterialImport.getMaterialCode();
            String usageQty = eamEquipmentMaterialImport.getUsageQty().toString();

            //判断必传字段
            if (StringUtils.isEmpty(
                    equipmentCode,equipmentName,materialCode,usageQty
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断物料信息是否存在
            SearchBaseMaterial searchBaseMaterial=new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            ResponseEntity<List<BaseMaterial>> baseMaterialList=baseFeignApi.findList(searchBaseMaterial);
            if(StringUtils.isNotEmpty(baseMaterialList.getData())){
                BaseMaterial baseMaterial=baseMaterialList.getData().get(0);
                if (StringUtils.isEmpty(baseMaterial)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                eamEquipmentMaterialImport.setMaterialId(baseMaterial.getMaterialId());
                i++;
            }

        }

        //对合格数据进行分组
        Map<String, List<EamEquipmentMaterialImport>> map = eamEquipmentMaterialImports.stream().collect(Collectors.groupingBy(EamEquipmentMaterialImport::getEquipmentCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<EamEquipmentMaterialImport> eamEquipmentMaterialImports1 = map.get(code);

            //判断设备编码
            Long equipmentId=null;
            Long equipmentMaterialId=null;
            Example example = new Example(EamEquipment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("equipmentCode",code);
            EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(eamEquipment)){
                equipmentId=eamEquipment.getEquipmentId();
            }
            if(StringUtils.isNotEmpty(equipmentId)) {
                Example examplEamMaterial = new Example(EamEquipmentMaterial.class);
                Example.Criteria criteriaEamMaterial = examplEamMaterial.createCriteria();
                criteriaEamMaterial.andEqualTo("equipmentId", equipmentId);
                EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectOneByExample(examplEamMaterial);
                if (StringUtils.isNotEmpty(eamEquipmentMaterial)) {
                    equipmentMaterialId = eamEquipmentMaterial.getEquipmentMaterialId();
                }
            }
            //新增表头 EamEquipmentMaterial
            if(StringUtils.isEmpty(equipmentMaterialId)){
                EamEquipmentMaterial eamEquipmentMaterialNew=new EamEquipmentMaterial();
                eamEquipmentMaterialNew.setEquipmentId(equipmentId);
                eamEquipmentMaterialNew.setStatus((byte)1);
                eamEquipmentMaterialNew.setOrgId(currentUser.getOrganizationId());
                eamEquipmentMaterialNew.setCreateUserId(currentUser.getUserId());
                eamEquipmentMaterialNew.setCreateTime(new Date());
                eamEquipmentMaterialMapper.insertUseGeneratedKeys(eamEquipmentMaterialNew);
                equipmentMaterialId=eamEquipmentMaterialNew.getEquipmentMaterialId();
            }
            //新增明细 EamEquipmentMaterialList
            if(StringUtils.isNotEmpty(equipmentMaterialId)) {
                List<EamEquipmentMaterialList> eamEquipmentMaterialLists=new ArrayList<>();
                for (EamEquipmentMaterialImport item : eamEquipmentMaterialImports1) {

                    Example examplEamEJL = new Example(EamEquipmentMaterialList.class);
                    Example.Criteria criteriaEamML = examplEamEJL.createCriteria();
                    criteriaEamML.andEqualTo("equipmentMaterialId", equipmentMaterialId);
                    criteriaEamML.andEqualTo("materialId",item.getMaterialId());
                    EamEquipmentMaterialList eamEquipmentMaterialList = eamEquipmentMaterialListMapper.selectOneByExample(examplEamEJL);
                    if(StringUtils.isEmpty(eamEquipmentMaterialList)) {
                        EamEquipmentMaterialList eamEquipmentMaterialListNew=new EamEquipmentMaterialList();
                        eamEquipmentMaterialListNew.setEquipmentMaterialId(equipmentMaterialId);
                        eamEquipmentMaterialListNew.setMaterialId(item.getMaterialId());
                        eamEquipmentMaterialListNew.setUsageQty(item.getUsageQty());
                        eamEquipmentMaterialListNew.setOrgId(currentUser.getOrganizationId());
                        eamEquipmentMaterialListNew.setCreateUserId(currentUser.getUserId());
                        eamEquipmentMaterialListNew.setCreateTime(new Date());
                        eamEquipmentMaterialLists.add(eamEquipmentMaterialListNew);
                    }

                }

                if(eamEquipmentMaterialLists.size()>0){
                    success += eamEquipmentMaterialListMapper.insertList(eamEquipmentMaterialLists);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

    @Override
    public List<EamEquipmentMaterialListDto> findExportList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentMaterialListMapper.findExportList(map);
    }
}
