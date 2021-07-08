package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiBom;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiFTAndInspectionTool;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiQualityStandards;
import com.fantechs.common.base.general.entity.eam.history.EamHtWorkInstruction;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWorkInstruction;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamWorkInstructionService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
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
    private EamHtWorkInstructionMapper eamHtWorkInstructionMapper;

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
    private EamHtWiFTAndInspectionToolMapper eamHtWiFTAndInspectionToolMapper;
    @Resource
    private EamHtWiQualityStandardsMapper eamHtWiQualityStandardsMapper;

    @Resource
    private BaseFeignApi baseFeignApi;




    @Override
    public List<EamWorkInstructionDto> findList(SearchEamWorkInstruction searchEamWorkInstruction) {
        if(StringUtils.isEmpty(searchEamWorkInstruction.getOrgId())){
            SysUser sysUser = currentUser();
            searchEamWorkInstruction.setOrgId(sysUser.getOrganizationId());
        }
        return eamWorkInstructionMapper.findList(searchEamWorkInstruction);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamWorkInstructionDto eamWorkInstructionDto) {
        SysUser user = currentUser();

        Example example = new Example(EamWorkInstruction.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workInstructionCode", eamWorkInstructionDto.getWorkInstructionCode());
        EamWorkInstruction eamWorkInstruction = eamWorkInstructionMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamWorkInstruction)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //保存主表
        eamWorkInstructionDto.setCreateUserId(user.getUserId());
        eamWorkInstructionDto.setCreateTime(new Date());
        eamWorkInstructionDto.setModifiedUserId(user.getUserId());
        eamWorkInstructionDto.setModifiedTime(new Date());
        eamWorkInstructionDto.setStatus(StringUtils.isEmpty(eamWorkInstructionDto.getStatus())?1: eamWorkInstructionDto.getStatus());
        eamWorkInstructionDto.setOrgId(user.getOrganizationId());
        eamWorkInstructionMapper.insertUseGeneratedKeys(eamWorkInstructionDto);

        EamHtWorkInstruction eamHtWorkInstruction = new EamHtWorkInstruction();
        BeanUtils.copyProperties(eamWorkInstructionDto, eamHtWorkInstruction);
        int i = eamHtWorkInstructionMapper.insertUseGeneratedKeys(eamHtWorkInstruction);


        if (StringUtils.isNotEmpty(eamWorkInstructionDto.getEamWiBoms())){
            List<EamWiBom> wiBom = new ArrayList<>();
            List<EamHtWiBom> eamHtWiBoms = new ArrayList<>();
            for (EamWiBom eamWiBom : eamWorkInstructionDto.getEamWiBoms()) {
                EamHtWiBom eamHtWiBom = new EamHtWiBom();
                eamWiBom.setWorkInstructionId(eamWorkInstruction.getWorkInstructionId());
                BeanUtils.copyProperties(eamWiBom,eamHtWiBom);
                wiBom.add(eamWiBom);
                eamHtWiBoms.add(eamHtWiBom);
            }
            eamWiBomMapper.insertList(wiBom);
            if (StringUtils.isNotEmpty(eamHtWiBoms)){
                eamHtWiBomMapper.insertList(eamHtWiBoms);
            }
        }

        if (StringUtils.isNotEmpty(eamWorkInstructionDto.getEamWiFTAndInspectionTools())){
            List<EamWiFTAndInspectionTool> wiFTAndInspectionTools = new ArrayList<>();
            List<EamHtWiFTAndInspectionTool> eamHtWiFTAndInspectionTools = new ArrayList<>();
            for (EamWiFTAndInspectionTool tool : eamWorkInstructionDto.getEamWiFTAndInspectionTools()) {
                EamHtWiFTAndInspectionTool eamHtWiFTAndInspectionTool = new EamHtWiFTAndInspectionTool();
                tool.setWorkInstructionId(eamWorkInstruction.getWorkInstructionId());
                BeanUtils.copyProperties(tool,eamHtWiFTAndInspectionTool);
                wiFTAndInspectionTools.add(tool);
                eamHtWiFTAndInspectionTools.add(eamHtWiFTAndInspectionTool);
            }
            eamWiFTAndInspectionToolMapper.insertList(wiFTAndInspectionTools);
            if (StringUtils.isNotEmpty(eamHtWiFTAndInspectionTools)){
                eamHtWiFTAndInspectionToolMapper.insertList(eamHtWiFTAndInspectionTools);
            }
        }

        if (StringUtils.isNotEmpty(eamWorkInstructionDto.getEamWiQualityStandardss())){
            List<EamWiQualityStandards> wiQualityStandardss = new ArrayList<>();
            List<EamHtWiQualityStandards> eamHtWiQualityStandardss = new ArrayList<>();
            for (EamWiQualityStandards standards : eamWorkInstructionDto.getEamWiQualityStandardss()) {
                EamHtWiQualityStandards eamHtWiQualityStandards = new EamHtWiQualityStandards();
                standards.setWorkInstructionId(eamWorkInstruction.getWorkInstructionId());
                BeanUtils.copyProperties(standards,eamHtWiQualityStandards);
                wiQualityStandardss.add(standards);
                eamHtWiQualityStandardss.add(eamHtWiQualityStandards);
            }
            eamWiQualityStandardsMapper.insertList(wiQualityStandardss);
            if (StringUtils.isNotEmpty(eamHtWiQualityStandardss)){
                eamHtWiQualityStandardsMapper.insertList(eamHtWiQualityStandardss);
            }
        }

        if (StringUtils.isNotEmpty(eamWorkInstructionDto.getEamWiFile())) {
            EamWiFile file = eamWorkInstructionDto.getEamWiFile();
            file.setWorkInstructionId(eamWorkInstruction.getWorkInstructionId());
            eamWiFileMapper.insertUseGeneratedKeys(file);
        }

        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public EamWorkInstructionDto importExcel(MultipartFile file) throws IOException {
        SysUser user = currentUser();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        EamWorkInstructionDto eamWorkInstructionDto = new EamWorkInstructionDto();
        //导入物料清单
        List<EamWiBom> eamWiBoms = importEamWiBomExcel(file, user);
        eamWorkInstructionDto.setEamWiBoms(eamWiBoms);
        //导入设备清单
        List<EamWiFTAndInspectionTool> eamWiFTAndInspectionTools = importEamWiFTAndInspectionToolExcel(file, user);
        eamWorkInstructionDto.setEamWiFTAndInspectionTools(eamWiFTAndInspectionTools);
        //导入品质清单
        List<EamWiQualityStandards> eamWiQualityStandardss = importEamWiQualityStandardsExcel(file, user);
        eamWorkInstructionDto.setEamWiQualityStandardss(eamWiQualityStandardss);


        return eamWorkInstructionDto;
    }

    /**
    *  导入物料清单
    **/
    private List<EamWiBom> importEamWiBomExcel(MultipartFile file,SysUser user) throws IOException {
        //获取物料清单
        List<EamWiBom> eamWiBoms = importWiBomData(file.getInputStream());
        LinkedList<EamWiBom> list = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < eamWiBoms.size(); i++) {

            EamWiBom eamWiBom = eamWiBoms.get(i);
            eamWiBom.setWorkInstructionId((long)11);
            if (StringUtils.isEmpty(eamWiBom) || StringUtils.isEmpty(eamWiBom.getWorkInstructionId()) || StringUtils.isEmpty(eamWiBom.getMaterialCode())){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(eamWiBom.getMaterialCode());
            ResponseEntity<List<BaseMaterial>> baseMaterials = baseFeignApi.findList(searchBaseMaterial);
            if(StringUtils.isNotEmpty(baseMaterials.getData())) {
                Example example = new Example(EamWiBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", eamWiBom.getMaterialId()).andEqualTo("workInstructionId", eamWiBom.getWorkInstructionId());
                if (StringUtils.isNotEmpty(eamWiBomMapper.selectOneByExample(example))) {
                    fail.add(i + 4);
                    example.clear();
                    continue;
                }
            }else{
                fail.add(i + 4);
                continue;
            }

            EamWiBom wiBom = new EamWiBom();
            BeanUtils.copyProperties(eamWiBom,wiBom);
            wiBom.setMaterialId(baseMaterials.getData().get(0).getMaterialId());
            wiBom.setCreateTime(new Date());
            wiBom.setCreateUserId(user.getUserId());
            wiBom.setModifiedTime(new Date());
            wiBom.setModifiedUserId(user.getUserId());
            wiBom.setStatus(StringUtils.isEmpty(wiBom.getStatus())?1:wiBom.getStatus());
            wiBom.setOrgId(user.getOrganizationId());
            list.add(wiBom);
        }
        return list;
    }

    //FIXME
    public List<EamWiBom> importWiBomData(InputStream in ) throws IOException {
        //xls用HSSFWorkbook ，xlsx用XSSFWorkbook
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sht = workbook.getSheetAt(2);
        List<EamWiBom> list = new ArrayList<>();
        for (int i = 3;i<= sht.getLastRowNum(); i++) {
            Row r = sht.getRow(i);
            if(r != null && r.getCell(0)!= null && !r.getCell(0).getCellType().equals(CellType.BLANK)) {
                EamWiBom eamWiBom = new EamWiBom();
                String strValue = r.getCell(0).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue))
                    eamWiBom.setMaterialCode(strValue);

                String strValue1 = r.getCell(1).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue1))
                    eamWiBom.setMaterialName(strValue1);

                if(r.getCell(2).getCellType().equals(CellType.STRING)){
                    String strValue2 = r.getCell(2).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue2))
                        eamWiBom.setMaterialVersion(strValue2);
                }else if(r.getCell(2).getCellType().equals(CellType.NUMERIC)){
                    Double strValue2 = r.getCell(2).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue2))
                        eamWiBom.setMaterialVersion(strValue2.toString());
                }

                if(r.getCell(3).getCellType().equals(CellType.STRING)){
                    String strValue3 = r.getCell(2).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        eamWiBom.setUsageQty(strValue3);
                }else if(r.getCell(3).getCellType().equals(CellType.NUMERIC)){
                    Double strValue3 = r.getCell(3).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        eamWiBom.setUsageQty(strValue3.toString());
                }


                list.add(eamWiBom);
            }
        }
        return list;
    }



    /**
     *  导入工装设备及检具要求
     **/
    private List<EamWiFTAndInspectionTool> importEamWiFTAndInspectionToolExcel(MultipartFile file, SysUser user) throws IOException {
        //工装设备及检具要求
        List<EamWiFTAndInspectionTool> eamWiFTAndInspectionTools = importWiFTAndInspectionToolData(file.getInputStream());
        LinkedList<EamWiFTAndInspectionTool> list = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < eamWiFTAndInspectionTools.size(); i++) {
            EamWiFTAndInspectionTool eamWiFTAndInspectionTool = eamWiFTAndInspectionTools.get(i);
            eamWiFTAndInspectionTool.setWorkInstructionId((long)11);
            if (StringUtils.isEmpty(eamWiFTAndInspectionTool) || StringUtils.isEmpty(eamWiFTAndInspectionTool.getWorkInstructionId()) || eamWiFTAndInspectionTool.getWorkInstructionId() ==0 ){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EamWiFTAndInspectionTool.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("facilityTooling",eamWiFTAndInspectionTool.getFacilityTooling()).andEqualTo("workInstructionId",eamWiFTAndInspectionTool.getWorkInstructionId());
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
        return list;
    }



    public List<EamWiFTAndInspectionTool> importWiFTAndInspectionToolData(InputStream in ) throws IOException {
        //xls用HSSFWorkbook ，xlsx用XSSFWorkbook
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sht = workbook.getSheetAt(1);
        List<EamWiFTAndInspectionTool> list = new ArrayList<>();
        for (int i = 3;i<= sht.getLastRowNum(); i++) {
            Row r = sht.getRow(i);
            if(r != null && r.getCell(0)!= null && !r.getCell(0).getCellType().equals(CellType.BLANK)) {
                EamWiFTAndInspectionTool eamWiFTAndInspectionTool = new EamWiFTAndInspectionTool();
                String strValue = r.getCell(0).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue))
                    eamWiFTAndInspectionTool.setFacilityTooling(strValue);
                if(r.getCell(1).getCellType().equals(CellType.STRING)){
                    String strValue1 = r.getCell(1).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue1))
                        eamWiFTAndInspectionTool.setQty(strValue1);
                }else if(r.getCell(1).getCellType().equals(CellType.NUMERIC)){
                    Double strValue1 = r.getCell(1).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue1))
                        eamWiFTAndInspectionTool.setQty(strValue1.toString());
                }

                String strValue2 = r.getCell(2).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue2))
                    eamWiFTAndInspectionTool.setRemark(strValue2);

                list.add(eamWiFTAndInspectionTool);
            }
        }
        return list;
    }



    /**
     *  品质标准
     **/
    private  List<EamWiQualityStandards> importEamWiQualityStandardsExcel(MultipartFile file, SysUser user) throws IOException {
        //获取品质标准数据
        List<EamWiQualityStandards> eamWiQualityStandardss = importWiQualityStandardsData(file.getInputStream());
        LinkedList<EamWiQualityStandards> list = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < eamWiQualityStandardss.size(); i++) {
            EamWiQualityStandards eamWiQualityStandards = eamWiQualityStandardss.get(i);
            eamWiQualityStandards.setWorkInstructionId((long)11);
            if (StringUtils.isEmpty(eamWiQualityStandards) || StringUtils.isEmpty(eamWiQualityStandards.getWorkInstructionId())|| eamWiQualityStandards.getWorkInstructionId() ==0){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EamWiQualityStandards.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("wiQualityStandardsIssuesName",eamWiQualityStandards.getWiQualityStandardsIssuesName())
                    .andEqualTo("workInstructionId",eamWiQualityStandards.getWorkInstructionId());
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
        return list;
    }


    public List<EamWiQualityStandards> importWiQualityStandardsData(InputStream in ) throws IOException {
        //xls用HSSFWorkbook ，xlsx用XSSFWorkbook
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sht = workbook.getSheetAt(0);
        List<EamWiQualityStandards> list = new ArrayList<>();
        for (int i = 3;i<= sht.getLastRowNum(); i++) {
            Row r = sht.getRow(i);
            if(r != null && r.getCell(0)!= null && !r.getCell(0).getCellType().equals(CellType.BLANK)) {
                EamWiQualityStandards eamWiQualityStandards = new EamWiQualityStandards();
                String strValue = r.getCell(0).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue))
                    eamWiQualityStandards.setWiQualityStandardsIssuesName(strValue);

                String strValue1 = r.getCell(1).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue1))
                    eamWiQualityStandards.setWiQualityStandardsContent(strValue1);

                String strValue2 = r.getCell(2).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue2))
                    eamWiQualityStandards.setInspectionType(strValue2);

                if(r.getCell(3).getCellType().equals(CellType.STRING)){
                    String strValue3 = r.getCell(3).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        eamWiQualityStandards.setInspectionFrequency(strValue3);
                }else if(r.getCell(3).getCellType().equals(CellType.NUMERIC)){
                    Double strValue3 = r.getCell(3).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        eamWiQualityStandards.setInspectionFrequency(strValue3.toString());
                }


                String strValue4 = r.getCell(4).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue4))
                    eamWiQualityStandards.setRecord(strValue4);

                list.add(eamWiQualityStandards);
            }
        }
        return list;
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