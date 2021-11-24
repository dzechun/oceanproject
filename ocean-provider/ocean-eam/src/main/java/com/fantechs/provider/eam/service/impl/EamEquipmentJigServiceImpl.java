package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentJigDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentJigListDto;
import com.fantechs.common.base.general.dto.eam.imports.EamEquipmentJigImport;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJig;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJigList;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJig;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquipmentJigService;
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
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentJigServiceImpl extends BaseService<EamEquipmentJig> implements EamEquipmentJigService {

    @Resource
    private EamEquipmentJigMapper eamEquipmentJigMapper;
    @Resource
    private EamHtEquipmentJigMapper eamHtEquipmentJigMapper;
    @Resource
    private EamEquipmentJigListMapper eamEquipmentJigListMapper;
    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    @Override
    public List<EamEquipmentJigDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentJigMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentJigDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamEquipmentJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", record.getEquipmentId());
        EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentJigMapper.insertUseGeneratedKeys(record);

        //新增明细
        List<EamEquipmentJigListDto> eamEquipmentJigListDtos = record.getList();
        if(StringUtils.isNotEmpty(eamEquipmentJigListDtos)){
            for (EamEquipmentJigListDto eamEquipmentJigListDto : eamEquipmentJigListDtos) {
                eamEquipmentJigListDto.setEquipmentJigId(record.getEquipmentJigId());
                eamEquipmentJigListDto.setCreateUserId(user.getUserId());
                eamEquipmentJigListDto.setCreateTime(new Date());
                eamEquipmentJigListDto.setModifiedUserId(user.getUserId());
                eamEquipmentJigListDto.setModifiedTime(new Date());
                eamEquipmentJigListDto.setStatus(StringUtils.isEmpty(eamEquipmentJigListDto.getStatus())?1:eamEquipmentJigListDto.getStatus());
                eamEquipmentJigListDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentJigListMapper.insertList(eamEquipmentJigListDtos);
        }

        //履历
        EamHtEquipmentJig eamHtEquipmentJig = new EamHtEquipmentJig();
        BeanUtils.copyProperties(record, eamHtEquipmentJig);
        int i = eamHtEquipmentJigMapper.insertSelective(eamHtEquipmentJig);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentJigDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamEquipmentJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", entity.getEquipmentId())
                .andNotEqualTo("equipmentJigId",entity.getEquipmentJigId());
        EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamEquipmentJigMapper.updateByPrimaryKeySelective(entity);

        //删除原明细
        Example example1 = new Example(EamEquipmentJigList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentJigId", entity.getEquipmentJigId());
        eamEquipmentJigListMapper.deleteByExample(example1);

        //新增明细
        List<EamEquipmentJigListDto> eamEquipmentJigListDtos = entity.getList();
        if(StringUtils.isNotEmpty(eamEquipmentJigListDtos)){
            for (EamEquipmentJigListDto eamEquipmentJigListDto : eamEquipmentJigListDtos) {
                eamEquipmentJigListDto.setEquipmentJigId(entity.getEquipmentJigId());
                eamEquipmentJigListDto.setCreateUserId(user.getUserId());
                eamEquipmentJigListDto.setCreateTime(new Date());
                eamEquipmentJigListDto.setModifiedUserId(user.getUserId());
                eamEquipmentJigListDto.setModifiedTime(new Date());
                eamEquipmentJigListDto.setStatus(StringUtils.isEmpty(eamEquipmentJigListDto.getStatus())?1:eamEquipmentJigListDto.getStatus());
                eamEquipmentJigListDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentJigListMapper.insertList(eamEquipmentJigListDtos);
        }

        //履历
        EamHtEquipmentJig eamHtEquipmentJig = new EamHtEquipmentJig();
        BeanUtils.copyProperties(entity, eamHtEquipmentJig);
        int i = eamHtEquipmentJigMapper.insertSelective(eamHtEquipmentJig);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtEquipmentJig> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentJig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentJig eamHtEquipmentJig = new EamHtEquipmentJig();
            BeanUtils.copyProperties(eamEquipmentJig, eamHtEquipmentJig);
            list.add(eamHtEquipmentJig);

            //删除明细
            Example example1 = new Example(EamEquipmentJigList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentJigId", id);
            eamEquipmentJigListMapper.deleteByExample(example1);
        }

        eamHtEquipmentJigMapper.insertList(list);

        return eamEquipmentJigMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EamEquipmentJigImport> eamEquipmentJigImportsTemp) throws ParseException {
        List<EamEquipmentJigImport> eamEquipmentJigImports=new ArrayList<>();
        for (EamEquipmentJigImport eamEquipmentJigImport : eamEquipmentJigImportsTemp) {
            if(StringUtils.isNotEmpty(eamEquipmentJigImport.getEquipmentCode())){
                eamEquipmentJigImports.add(eamEquipmentJigImport);
            }
        }
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<EamEquipmentJigImport> iterator = eamEquipmentJigImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            EamEquipmentJigImport eamEquipmentJigImport = iterator.next();
            String equipmentCode = eamEquipmentJigImport.getEquipmentCode();
            String equipmentName = eamEquipmentJigImport.getEquipmentName();
            String jigCode = eamEquipmentJigImport.getJigCode();
            String usageQty = eamEquipmentJigImport.getUsageQty().toString();

            //判断必传字段
            if (StringUtils.isEmpty(
                    equipmentCode,equipmentName,jigCode,usageQty
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断治具信息是否存在
            Example example1 = new Example(EamJig.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigCode",jigCode);
            EamJig eamJig = eamJigMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(eamJig)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }
            eamEquipmentJigImport.setJigId(eamJig.getJigId());
            i++;
        }

        //对合格数据进行分组
        Map<String, List<EamEquipmentJigImport>> map = eamEquipmentJigImports.stream().collect(Collectors.groupingBy(EamEquipmentJigImport::getEquipmentCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<EamEquipmentJigImport> eamEquipmentJigImports1 = map.get(code);

            //判断设备编码
            Long equipmentId=null;
            Long equipmentJigId=null;
            Example example = new Example(EamEquipment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("equipmentCode",code);
            EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(eamEquipment)){
                equipmentId=eamEquipment.getEquipmentId();
            }
            if(StringUtils.isNotEmpty(equipmentId)) {
                Example examplEamEJ = new Example(EamEquipmentJig.class);
                Example.Criteria criteriaEamEJ = examplEamEJ.createCriteria();
                criteriaEamEJ.andEqualTo("equipmentId", equipmentId);
                EamEquipmentJig eamEquipmentJig = eamEquipmentJigMapper.selectOneByExample(examplEamEJ);
                if (StringUtils.isNotEmpty(eamEquipmentJig)) {
                    equipmentJigId = eamEquipmentJig.getEquipmentJigId();
                }
            }
            //新增表头 EquipmentJigId
            if(StringUtils.isEmpty(equipmentJigId)){
                EamEquipmentJig eamEquipmentJigNew=new EamEquipmentJig();
                eamEquipmentJigNew.setEquipmentId(equipmentId);
                eamEquipmentJigNew.setStatus((byte)1);
                eamEquipmentJigNew.setOrgId(currentUser.getOrganizationId());
                eamEquipmentJigNew.setCreateUserId(currentUser.getUserId());
                eamEquipmentJigNew.setCreateTime(new Date());
                eamEquipmentJigMapper.insertUseGeneratedKeys(eamEquipmentJigNew);
                equipmentJigId=eamEquipmentJigNew.getEquipmentJigId();
            }
            //新增明细 EamEquipmentJigList
            if(StringUtils.isNotEmpty(equipmentJigId)) {
                List<EamEquipmentJigList> eamEquipmentJigLists=new ArrayList<>();
                for (EamEquipmentJigImport item : eamEquipmentJigImports1) {

                    Example examplEamEJL = new Example(EamEquipmentJigList.class);
                    Example.Criteria criteriaEamEJL = examplEamEJL.createCriteria();
                    criteriaEamEJL.andEqualTo("equipmentJigId", equipmentJigId);
                    criteriaEamEJL.andEqualTo("jigId",item.getJigId());
                    EamEquipmentJigList eamEquipmentJigList = eamEquipmentJigListMapper.selectOneByExample(examplEamEJL);
                    if(StringUtils.isEmpty(eamEquipmentJigList)) {
                        EamEquipmentJigList equipmentJigListNew=new EamEquipmentJigList();
                        equipmentJigListNew.setEquipmentJigId(equipmentJigId);
                        equipmentJigListNew.setJigId(item.getJigId());
                        equipmentJigListNew.setUsageQty(item.getUsageQty());
                        equipmentJigListNew.setOrgId(currentUser.getOrganizationId());
                        equipmentJigListNew.setCreateUserId(currentUser.getUserId());
                        equipmentJigListNew.setCreateTime(new Date());
                        eamEquipmentJigLists.add(equipmentJigListNew);
                    }

                }

                if(eamEquipmentJigLists.size()>0){
                    success += eamEquipmentJigListMapper.insertList(eamEquipmentJigLists);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

    @Override
    public List<EamEquipmentJigListDto> findExportList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentJigListMapper.findExportList(map);
    }
}
