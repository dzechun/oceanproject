package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiBom;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiFTAndInspectionTool;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiQualityStandards;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWorkInstruction;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.FileCheckUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamWorkInstructionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@Service
public class EamWorkInstructionServiceImpl extends BaseService<EamWorkInstruction> implements EamWorkInstructionService {

    @Resource
    private EamWorkInstructionMapper eamWorkInstructionMapper;
    @Resource
    private EamWiBomMapper eamWiBomMapper;
    @Resource
    private EamWiFileMapper eamWiFileMapper;
    @Resource
    private EamWiFTAndInspectionToolMapper eamWiFTAndInspectionToolMapper;
    @Resource
    private EamWiQualityStandardsMapper eamWiQualityStandardsMapper;

    @Resource
    private EamHtWiBomMapper eamHtWiBomMapper;
    @Resource
    private EamHtWiFileMapper eamHtWiFileMapper;
    @Resource
    private EamHtWiFTAndInspectionToolMapper eamHtWiFTAndInspectionToolMapper;
    @Resource
    private EamHtWiQualityStandardsMapper eamHtWiQualityStandardsMapper;

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private FileFeignApi fileFeignApi;



    @Override
    public List<EamWorkInstructionDto> findList(SearchEamWorkInstruction searchEamWorkInstruction) {
        /*if(StringUtils.isEmpty(searchEamWorkInstruction.getOrgId())){
            SysUser sysUser = currentUser();
            searchEamWorkInstruction.setOrgId(sysUser.getOrganizationId());
        }*/
        return eamWorkInstructionMapper.findList(searchEamWorkInstruction);
    }

   /* @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamWorkInstructionDto eamWorkInstructionDto) {
        SysUser sysUser = currentUser();

        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", record.getEquipmentCode());
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentMapper.insertUseGeneratedKeys(record);

        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(record, eamHtEquipment);
        int i = eamHtEquipmentMapper.insert(eamHtEquipment);

        return i;
    }*/


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(EamWorkInstructionDto baseMaterialOwnerDto) {
        SysUser user = currentUser();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果

        //导入物料清单
        importEamWiBomExcel(baseMaterialOwnerDto.getEamWiBoms(),user,resultMap);

        //导入设备清单
        importEamWiFTAndInspectionToolExcel(baseMaterialOwnerDto.getEamWiFTAndInspectionTool(),user,resultMap);

        //导入设备清单
        importEamWiQualityStandardsExcel(baseMaterialOwnerDto.getEamWiQualityStandards(),user,resultMap);

        //导入文件
        EamWiFile eamWiFile = new EamWiFile();
        String fileName = baseMaterialOwnerDto.getEamWiFile().getOriginalFilename();
        if(!FileCheckUtil.checkFileType(fileName)){
            resultMap.put("附件上传失败总数",1);
        }
        Map<String, Object> data = (Map<String, Object>)fileFeignApi.fileUpload(baseMaterialOwnerDto.getEamWiFile());
        String path = data.get("url").toString();

        eamWiFile.setWiFileName(fileName);
        eamWiFile.setStorePath(path);
        eamWiFile.setCreateTime(new Date());
        eamWiFile.setCreateUserId(user.getUserId());
        eamWiFile.setModifiedTime(new Date());
        eamWiFile.setModifiedUserId(user.getUserId());
        eamWiFile.setOrgId(user.getOrganizationId());
        if(eamWiFileMapper.insertUseGeneratedKeys(eamWiFile)>0)
            resultMap.put("附件上传成功总数",1);
        else
            resultMap.put("附件上传失败总数",1);

        return resultMap;
    }

    /**
    *  导入物料清单
    **/
    private Map<String, Object> importEamWiBomExcel(List<EamWiBom> eamWiBoms,SysUser user,Map<String, Object> resultMap) {
        LinkedList<EamWiBom> list = new LinkedList<>();
        LinkedList<EamHtWiBom> htList = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        for (int i = 0; i < eamWiBoms.size(); i++) {
            EamWiBom eamWiBom = eamWiBoms.get(i);
            String materialCode = eamWiBom.getMaterialCode();
            Long workInstructionId = eamWiBom.getWorkInstructionId();
            if (StringUtils.isEmpty(materialCode) || workInstructionId ==0 ){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialCode",materialCode).andEqualTo("workInstructionId",workInstructionId);
            if (StringUtils.isNotEmpty(eamWiBomMapper.selectOneByExample(example))){
                fail.add(i+4);
                example.clear();
                continue;
            }
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            ResponseEntity<List<BaseMaterial>> baseMaterialList = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isEmpty(baseMaterialList.getData())){
                fail.add(i+4);
                continue;
            }

            EamWiBom wiBom = new EamWiBom();
            BeanUtils.copyProperties(eamWiBom,wiBom);
            wiBom.setMaterialId(baseMaterialList.getData().get(0).getMaterialId());
            wiBom.setCreateTime(new Date());
            wiBom.setCreateUserId(user.getUserId());
            wiBom.setModifiedTime(new Date());
            wiBom.setModifiedUserId(user.getUserId());
            wiBom.setStatus(StringUtils.isEmpty(wiBom.getStatus())?1:wiBom.getStatus());
            wiBom.setOrgId(user.getOrganizationId());
            list.add(wiBom);
        }

        if (StringUtils.isNotEmpty(list)){
            success = eamWiBomMapper.insertList(list);
        }
        for (EamWiBom eamWiBom : list) {
            EamHtWiBom eamHtWiBom = new EamHtWiBom();
            BeanUtils.copyProperties(eamWiBom,eamHtWiBom);
            htList.add(eamHtWiBom);
        }

        if (StringUtils.isNotEmpty(htList)){
            eamHtWiBomMapper.insertList(htList);
        }
        resultMap.put("物料清单成功总数",success);
        resultMap.put("物料清单失败行数",fail);
        return resultMap;
    }

    /**
     *  导入工装设备及检具要求
     **/
    private Map<String, Object> importEamWiFTAndInspectionToolExcel(List<EamWiFTAndInspectionTool> eamWiFTAndInspectionTools, SysUser user, Map<String, Object> resultMap) {
        LinkedList<EamWiFTAndInspectionTool> list = new LinkedList<>();
        LinkedList<EamHtWiFTAndInspectionTool> htList = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        for (int i = 0; i < eamWiFTAndInspectionTools.size(); i++) {
            EamWiFTAndInspectionTool eamWiFTAndInspectionTool = eamWiFTAndInspectionTools.get(i);
            Long workInstructionId = eamWiFTAndInspectionTool.getWorkInstructionId();
            if (workInstructionId == 0){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("facilityTooling",eamWiFTAndInspectionTool.getFacilityTooling()).andEqualTo("workInstructionId",workInstructionId);
            if (StringUtils.isNotEmpty(eamWiFTAndInspectionToolMapper.selectOneByExample(example))){
                fail.add(i+4);
                example.clear();
                continue;
            }

            EamWiFTAndInspectionTool wiFTAndInspectionTool = new EamWiFTAndInspectionTool();
            BeanUtils.copyProperties(eamWiFTAndInspectionTool,wiFTAndInspectionTool);
            wiFTAndInspectionTool.setCreateTime(new Date());
            wiFTAndInspectionTool.setCreateUserId(user.getUserId());
            wiFTAndInspectionTool.setModifiedTime(new Date());
            wiFTAndInspectionTool.setModifiedUserId(user.getUserId());
            wiFTAndInspectionTool.setStatus(StringUtils.isEmpty(wiFTAndInspectionTool.getStatus())?1:wiFTAndInspectionTool.getStatus());
            wiFTAndInspectionTool.setOrgId(user.getOrganizationId());
            list.add(wiFTAndInspectionTool);
            example.clear();
        }

        if (StringUtils.isNotEmpty(list)){
            success = eamWiFTAndInspectionToolMapper.insertList(list);
        }
        for (EamWiFTAndInspectionTool tool : list) {
            EamHtWiFTAndInspectionTool eamHtWiFTAndInspectionTool = new EamHtWiFTAndInspectionTool();
            BeanUtils.copyProperties(tool,eamHtWiFTAndInspectionTool);
            htList.add(eamHtWiFTAndInspectionTool);
        }

        if (StringUtils.isNotEmpty(htList)){
            eamHtWiFTAndInspectionToolMapper.insertList(htList);
        }
        resultMap.put("物料清单成功总数",success);
        resultMap.put("物料清单失败行数",fail);

        return resultMap;
    }


    /**
     *  品质标准
     **/
    private Map<String, Object> importEamWiQualityStandardsExcel(List<EamWiQualityStandards> eamWiQualityStandardss, SysUser user, Map<String, Object> resultMap) {
        LinkedList<EamWiQualityStandards> list = new LinkedList<>();
        LinkedList<EamHtWiQualityStandards> htList = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < eamWiQualityStandardss.size(); i++) {
            EamWiQualityStandards eamWiQualityStandards = eamWiQualityStandardss.get(i);
            Long workInstructionId = eamWiQualityStandards.getWorkInstructionId();
            if (workInstructionId == 0){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("wiQualityStandardsIssuesName",eamWiQualityStandards.getWiQualityStandardsIssuesName())
                    .andEqualTo("workInstructionId",workInstructionId);
            if (StringUtils.isNotEmpty(eamWiQualityStandardsMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            EamWiQualityStandards wiQualityStandards = new EamWiQualityStandards();
            wiQualityStandards.setCreateTime(new Date());
            wiQualityStandards.setCreateUserId(user.getUserId());
            wiQualityStandards.setModifiedTime(new Date());
            wiQualityStandards.setModifiedUserId(user.getUserId());
            wiQualityStandards.setStatus(StringUtils.isEmpty(wiQualityStandards.getStatus())?1:wiQualityStandards.getStatus());
            wiQualityStandards.setOrgId(user.getOrganizationId());
            list.add(wiQualityStandards);
        }

        if (StringUtils.isNotEmpty(list)){
            success = eamWiQualityStandardsMapper.insertList(list);
        }
        for (EamWiQualityStandards standards : list) {
            EamHtWiQualityStandards eamHtWiQualityStandards = new EamHtWiQualityStandards();
            BeanUtils.copyProperties(standards,eamHtWiQualityStandards);
            htList.add(eamHtWiQualityStandards);
        }

        if (StringUtils.isNotEmpty(htList)){
            eamHtWiQualityStandardsMapper.insertList(htList);
        }
        resultMap.put("品质清单成功总数",success);
        resultMap.put("品质清单失败行数",fail);
        return resultMap;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
