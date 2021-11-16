package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialListDto;
import com.fantechs.common.base.general.dto.eam.imports.EamJigMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigMaterial;
import com.fantechs.common.base.general.entity.eam.EamJigMaterialList;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.eam.mapper.EamHtJigMaterialMapper;
import com.fantechs.provider.eam.mapper.EamJigMapper;
import com.fantechs.provider.eam.mapper.EamJigMaterialListMapper;
import com.fantechs.provider.eam.mapper.EamJigMaterialMapper;
import com.fantechs.provider.eam.service.EamJigMaterialService;
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
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamJigMaterialServiceImpl extends BaseService<EamJigMaterial> implements EamJigMaterialService {

    @Resource
    private EamJigMaterialMapper eamJigMaterialMapper;
    @Resource
    private EamJigMaterialListMapper eamJigMaterialListMapper;
    @Resource
    private EamHtJigMaterialMapper eamHtJigMaterialMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EamJigMapper eamJigMapper;

    @Override
    public List<EamJigMaterialDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }

        return eamJigMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigMaterialDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.ifRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamJigMaterialMapper.insertUseGeneratedKeys(record);

        //治具绑定产品明细
        List<EamJigMaterialListDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaterialListDto eamJigMaterialListDto : list){
                eamJigMaterialListDto.setJigMaterialId(record.getJigMaterialId());
                eamJigMaterialListDto.setCreateUserId(user.getUserId());
                eamJigMaterialListDto.setCreateTime(new Date());
                eamJigMaterialListDto.setModifiedUserId(user.getUserId());
                eamJigMaterialListDto.setModifiedTime(new Date());
                eamJigMaterialListDto.setStatus(StringUtils.isEmpty(eamJigMaterialListDto.getStatus())?1: eamJigMaterialListDto.getStatus());
                eamJigMaterialListDto.setOrgId(user.getOrganizationId());
            }
            eamJigMaterialListMapper.insertList(list);
        }

        EamHtJigMaterial eamHtJigMaterial = new EamHtJigMaterial();
        BeanUtils.copyProperties(record,eamHtJigMaterial);
        int i = eamHtJigMaterialMapper.insert(eamHtJigMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigMaterialDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.ifRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigMaterialMapper.updateByPrimaryKeySelective(entity);

        //删除原治具绑定产品明细
        Example example1 = new Example(EamJigMaterialList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigMaterialId", entity.getJigMaterialId());
        eamJigMaterialListMapper.deleteByExample(example1);

        //治具绑定产品明细
        List<EamJigMaterialListDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaterialListDto eamJigMaterialListDto : list){
                eamJigMaterialListDto.setJigMaterialId(entity.getJigMaterialId());
                eamJigMaterialListDto.setCreateUserId(user.getUserId());
                eamJigMaterialListDto.setCreateTime(new Date());
                eamJigMaterialListDto.setModifiedUserId(user.getUserId());
                eamJigMaterialListDto.setModifiedTime(new Date());
                eamJigMaterialListDto.setStatus(StringUtils.isEmpty(eamJigMaterialListDto.getStatus())?1: eamJigMaterialListDto.getStatus());
                eamJigMaterialListDto.setOrgId(user.getOrganizationId());
            }
            eamJigMaterialListMapper.insertList(list);
        }

        EamHtJigMaterial eamHtJigMaterial = new EamHtJigMaterial();
        BeanUtils.copyProperties(entity,eamHtJigMaterial);
        int i = eamHtJigMaterialMapper.insert(eamHtJigMaterial);

        return i;
    }

    private void ifRepeat(EamJigMaterialDto eamJigMaterialDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        //判断是否重复
        criteria.andEqualTo("jigId",eamJigMaterialDto.getJigId());
        if (StringUtils.isNotEmpty(eamJigMaterialDto.getJigMaterialId())){
            criteria.andNotEqualTo("jigMaterialId",eamJigMaterialDto.getJigMaterialId());
        }
        EamJigMaterial eamJigMaterial = eamJigMaterialMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(eamJigMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigMaterial> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigMaterial eamJigMaterial = eamJigMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigMaterial eamHtJigMaterial = new EamHtJigMaterial();
            BeanUtils.copyProperties(eamJigMaterial,eamHtJigMaterial);
            htList.add(eamHtJigMaterial);

            //删除治具绑定产品明细
            Example example1 = new Example(EamJigMaterialList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigMaterialId", id);
            eamJigMaterialListMapper.deleteByExample(example1);
        }

        eamHtJigMaterialMapper.insertList(htList);

        return eamJigMaterialMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EamJigMaterialImport> eamJigMaterialImports) throws ParseException {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<EamJigMaterialImport> iterator = eamJigMaterialImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            EamJigMaterialImport eamJigMaterialImport = iterator.next();
            String jigCode = eamJigMaterialImport.getJigCode();
            String jigName = eamJigMaterialImport.getJigName();
            String materialCode = eamJigMaterialImport.getMaterialCode();
            String usageQty = eamJigMaterialImport.getUsageQty().toString();

            //判断必传字段
            if (StringUtils.isEmpty(
                    jigCode,jigName,materialCode,usageQty
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
                eamJigMaterialImport.setMaterialId(baseMaterial.getMaterialId());
                i++;
            }

        }

        //对合格数据进行分组
        Map<String, List<EamJigMaterialImport>> map = eamJigMaterialImports.stream().collect(Collectors.groupingBy(EamJigMaterialImport::getJigCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<EamJigMaterialImport> eamJigMaterialImports1 = map.get(code);

            //判断设备编码
            Long jigId=null;
            Long jigMaterialId=null;
            Example example = new Example(EamJig.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("jigCode",code);
            EamJig eamJig = eamJigMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(eamJig)){
                jigId=eamJig.getJigId();
            }
            if(StringUtils.isNotEmpty(jigId)) {
                Example examplEamMaterial = new Example(EamJigMaterial.class);
                Example.Criteria criteriaEamMaterial = examplEamMaterial.createCriteria();
                criteriaEamMaterial.andEqualTo("jigId", jigId);
                EamJigMaterial EamJigMaterial = eamJigMaterialMapper.selectOneByExample(examplEamMaterial);
                if (StringUtils.isNotEmpty(EamJigMaterial)) {
                    jigMaterialId = EamJigMaterial.getJigMaterialId();
                }
            }
            //新增表头 EamEquipmentMaterial
            if(StringUtils.isEmpty(jigMaterialId)){
                EamJigMaterial eamJigMaterialNew=new EamJigMaterial();
                eamJigMaterialNew.setJigId(jigId);
                eamJigMaterialNew.setStatus((byte)1);
                eamJigMaterialNew.setOrgId(currentUser.getOrganizationId());
                eamJigMaterialNew.setCreateUserId(currentUser.getUserId());
                eamJigMaterialNew.setCreateTime(new Date());
                eamJigMaterialMapper.insertUseGeneratedKeys(eamJigMaterialNew);
                jigMaterialId=eamJigMaterialNew.getJigMaterialId();
            }
            //新增明细 EamEquipmentMaterialList
            if(StringUtils.isNotEmpty(jigMaterialId)) {
                List<EamJigMaterialList> eamJigMaterialLists=new ArrayList<>();
                for (EamJigMaterialImport item : eamJigMaterialImports1) {

                    Example examplEamEJL = new Example(EamJigMaterialList.class);
                    Example.Criteria criteriaEamML = examplEamEJL.createCriteria();
                    criteriaEamML.andEqualTo("jigMaterialId", jigMaterialId);
                    criteriaEamML.andEqualTo("materialId",item.getMaterialId());
                    EamJigMaterialList eamJigMaterialList = eamJigMaterialListMapper.selectOneByExample(examplEamEJL);
                    if(StringUtils.isEmpty(eamJigMaterialList)) {
                        EamJigMaterialList eamJigMaterialListNew=new EamJigMaterialList();
                        eamJigMaterialListNew.setJigMaterialId(jigMaterialId);
                        eamJigMaterialListNew.setMaterialId(item.getMaterialId());
                        eamJigMaterialListNew.setUsageQty(item.getUsageQty());
                        eamJigMaterialListNew.setOrgId(currentUser.getOrganizationId());
                        eamJigMaterialListNew.setCreateUserId(currentUser.getUserId());
                        eamJigMaterialListNew.setCreateTime(new Date());
                        eamJigMaterialLists.add(eamJigMaterialListNew);
                    }

                }

                if(eamJigMaterialLists.size()>0){
                    success += eamJigMaterialListMapper.insertList(eamJigMaterialLists);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

}
