package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDetDto;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDto;
import com.fantechs.common.base.general.dto.esop.EsopWorkInstructionDto;
import com.fantechs.common.base.general.dto.esop.imports.EsopWorkInstructionImport;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.general.entity.esop.*;
import com.fantechs.common.base.general.entity.esop.history.*;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiRelease;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWorkInstruction;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.esop.mapper.*;
import com.fantechs.provider.esop.service.EsopWorkInstructionService;
import com.fantechs.provider.esop.service.socket.SocketService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@Service
public class EsopWorkInstructionServiceImpl extends BaseService<EsopWorkInstruction> implements EsopWorkInstructionService {

    @Resource
    private EsopWorkInstructionMapper esopWorkInstructionMapper;
    @Resource
    private EsopHtWorkInstructionMapper esopHtWorkInstructionMapper;

    @Resource
    private EsopWiBomMapper esopWiBomMapper;
    @Resource
    private EsopWiFileMapper esopWiFileMapper;
    @Resource
    private EsopWiFTAndInspectionToolMapper esopWiFTAndInspectionToolMapper;
    @Resource
    private EsopWiQualityStandardsMapper esopWiQualityStandardsMapper;

    @Resource
    private EsopHtWiBomMapper esopHtWiBomMapper;
    @Resource
    private EsopHtWiFileMapper esopHtWiFileMapper;
    @Resource
    private EsopHtWiFTAndInspectionToolMapper esopHtWiFTAndInspectionToolMapper;
    @Resource
    private EsopHtWiQualityStandardsMapper esopHtWiQualityStandardsMapper;

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private EsopWiReleaseMapper esopWiReleaseMapper;
    @Resource
    private EsopEquipmentMapper esopEquipmentMapper;

    @Resource
    private EsopWiReleaseDetMapper esopWiReleaseDetMapper;

    @Resource
    private SocketService socketService;

    @Override
    public List<EsopWorkInstructionDto> findList(SearchEsopWorkInstruction searchEsopWorkInstruction) {
        if(StringUtils.isEmpty(searchEsopWorkInstruction.getOrgId())){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            searchEsopWorkInstruction.setOrgId(user.getOrganizationId());
        }
        return esopWorkInstructionMapper.findList(searchEsopWorkInstruction);
    }

    @Override
    public EsopWorkInstructionDto findByEquipmentIp(SearchEsopWorkInstruction searchEsopWorkInstruction) {
        if(StringUtils.isEmpty(searchEsopWorkInstruction.getEquipmentIp()) && StringUtils.isEmpty(searchEsopWorkInstruction.getEquipmentMacAddress()))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"设备ip不能为空");
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(searchEsopWorkInstruction.getEquipmentMacAddress()))
            criteria.andEqualTo("equipmentMacAddress", searchEsopWorkInstruction.getEquipmentMacAddress());
        if(StringUtils.isNotEmpty(searchEsopWorkInstruction.getEquipmentIp()))
            criteria.andEqualTo("equipmentIp", searchEsopWorkInstruction.getEquipmentIp());
        criteria.andEqualTo("orgId", user.getOrganizationId());
        List<EsopEquipment> EsopEquipments = esopEquipmentMapper.selectByExample(example);
        if(EsopEquipments.size() >1) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "出现两个或两个以上的设备ip相同");
        example.clear();
        if(EsopEquipments.size() <1 ) return null;
        //查询到对应的wi
        SearchEsopWiRelease searchEsopWiRelease = new SearchEsopWiRelease();
    //    searchEsopWiRelease.setEquipmentMacAddress(searchEsopWorkInstruction.getEquipmentMacAddress());
        searchEsopWiRelease.setOrgId(user.getOrganizationId());
        searchEsopWiRelease.setReleaseStatus((byte)2);
        searchEsopWiRelease.setProLineId(EsopEquipments.get(0).getProLineId());

        List<EsopWiReleaseDto> list = esopWiReleaseMapper.findList(searchEsopWiRelease);
        //客户要求不再根据工序发布，根据设备序号发布
        if(StringUtils.isNotEmpty(list) ) {
            if (list.size()>1)  throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"查询到多条该设备对应产线发布的WI");
            for(EsopWiReleaseDetDto dto : list.get(0).getEsopWiReleaseDetDtos()){
                if(dto.getWiReleaseDetSeqNum().equals(EsopEquipments.get(0).getEquipmentSeqNum()) ){
                    searchEsopWorkInstruction.setWorkInstructionId(dto.getWorkInstructionId());
                }
            }
            if(StringUtils.isEmpty(searchEsopWorkInstruction.getWorkInstructionId())) return null;
            searchEsopWorkInstruction.setProLineId(list.get(0).getProLineId());
        }else{
            return null;
        }

        //searchEsopWorkInstruction.setProcessId(EsopEquipments.get(0).getProcessId());
        searchEsopWorkInstruction.setOrgId(EsopEquipments.get(0).getOrgId());
        EsopWorkInstructionDto EsopWorkInstructionDto = esopWorkInstructionMapper.findList(searchEsopWorkInstruction).get(0);
        EsopWorkInstructionDto.setUserName(user.getUserName());
        EsopWorkInstructionDto.setWorkShopName(list.get(0).getWorkShopName());

        return EsopWorkInstructionDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EsopWorkInstructionDto esopWorkInstructionDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(esopWorkInstructionDto.getWorkInstructionCode())) throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"Wi编码不能为空");
        Example example = new Example(EsopWorkInstruction.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workInstructionCode", esopWorkInstructionDto.getWorkInstructionCode());
        criteria.andEqualTo("orgId", user.getOrganizationId());
        EsopWorkInstruction EsopWorkInstruction = esopWorkInstructionMapper.selectOneByExample(example);
        example.clear();
        if (StringUtils.isNotEmpty(EsopWorkInstruction)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //保存主表
        esopWorkInstructionDto.setCreateUserId(user.getUserId());
        esopWorkInstructionDto.setCreateTime(new Date());
        esopWorkInstructionDto.setModifiedUserId(user.getUserId());
        esopWorkInstructionDto.setModifiedTime(new Date());
        esopWorkInstructionDto.setStatus((byte)1);
        esopWorkInstructionDto.setWiStatus((byte)1);
        esopWorkInstructionDto.setOrgId(user.getOrganizationId());
        esopWorkInstructionMapper.insertUseGeneratedKeys(esopWorkInstructionDto);

        EsopHtWorkInstruction esopHtWorkInstruction = new EsopHtWorkInstruction();
        BeanUtils.copyProperties(esopWorkInstructionDto, esopHtWorkInstruction);
        int i = esopHtWorkInstructionMapper.insertSelective(esopHtWorkInstruction);

        saveBom(esopWorkInstructionDto,esopWorkInstructionDto.getWorkInstructionId(),user);

        saveTool(esopWorkInstructionDto,esopWorkInstructionDto.getWorkInstructionId(),user);

        saveStandards(esopWorkInstructionDto,esopWorkInstructionDto.getWorkInstructionId(),user);

        saveFile(esopWorkInstructionDto,esopWorkInstructionDto.getWorkInstructionId(),user);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EsopWorkInstructionDto EsopWorkInstructionDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(EsopWorkInstructionDto.getWorkInstructionId()))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"id不能为空");
        EsopWorkInstructionDto.setWiStatus((byte)1);
        esopWorkInstructionMapper.updateByPrimaryKeySelective(EsopWorkInstructionDto);

        Example example = new Example(EsopWorkInstruction.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workInstructionId", EsopWorkInstructionDto.getWorkInstructionId());
        criteria.andEqualTo("orgId", user.getOrganizationId());
        EsopWorkInstruction EsopWorkInstruction = esopWorkInstructionMapper.selectOneByExample(example);

        //保存履历表
        EsopHtWorkInstruction EsopHtWorkInstruction = new EsopHtWorkInstruction();
        BeanUtils.copyProperties(EsopWorkInstruction, EsopHtWorkInstruction);
        int i = esopHtWorkInstructionMapper.insertSelective(EsopHtWorkInstruction);
        example.clear();

            Example bomExample = new Example(EsopWiBom.class);
            Example.Criteria bomCriteria = bomExample.createCriteria();
            bomCriteria.andEqualTo("workInstructionId", EsopWorkInstructionDto.getWorkInstructionId());
            esopWiBomMapper.deleteByExample(bomExample);
            saveBom(EsopWorkInstructionDto, EsopWorkInstruction.getWorkInstructionId(), user);
            bomExample.clear();

            Example toolExample = new Example(EsopWiFTAndInspectionTool.class);
            Example.Criteria toolCriteria = toolExample.createCriteria();
            toolCriteria.andEqualTo("workInstructionId", EsopWorkInstructionDto.getWorkInstructionId());
            esopWiFTAndInspectionToolMapper.deleteByExample(toolExample);
            saveTool(EsopWorkInstructionDto, EsopWorkInstruction.getWorkInstructionId(), user);
            toolExample.clear();

            Example standardsExample = new Example(EsopWiQualityStandards.class);
            Example.Criteria standardsCriteria = standardsExample.createCriteria();
            standardsCriteria.andEqualTo("workInstructionId", EsopWorkInstructionDto.getWorkInstructionId());
            esopWiQualityStandardsMapper.deleteByExample(standardsExample);
            saveStandards(EsopWorkInstructionDto, EsopWorkInstruction.getWorkInstructionId(), user);
            standardsExample.clear();

            Example fileExample = new Example(EsopWiFile.class);
            Example.Criteria fileCriteria = fileExample.createCriteria();
            fileCriteria.andEqualTo("workInstructionId", EsopWorkInstructionDto.getWorkInstructionId());
            esopWiFileMapper.deleteByExample(fileExample);
            saveFile(EsopWorkInstructionDto, EsopWorkInstruction.getWorkInstructionId(), user);
            fileExample.clear();

        return i;
    }


    public void saveBom(EsopWorkInstructionDto EsopWorkInstructionDto,Long id,SysUser user){
        if (StringUtils.isNotEmpty(EsopWorkInstructionDto.getEsopWiBoms())){
            List<EsopWiBom> wiBom = new ArrayList<>();
            List<EsopHtWiBom> EsopHtWiBoms = new ArrayList<>();
            for (EsopWiBom EsopWiBom : EsopWorkInstructionDto.getEsopWiBoms()) {
                EsopHtWiBom EsopHtWiBom = new EsopHtWiBom();
                EsopWiBom.setWorkInstructionId(id);
                EsopWiBom.setCreateUserId(user.getUserId());
                EsopWiBom.setCreateTime(new Date());
                EsopWiBom.setModifiedUserId(user.getUserId());
                EsopWiBom.setModifiedTime(new Date());
                EsopWiBom.setStatus(StringUtils.isEmpty(EsopWiBom.getStatus())?1: EsopWiBom.getStatus());
                EsopWiBom.setOrgId(user.getOrganizationId());
                BeanUtils.copyProperties(EsopWiBom,EsopHtWiBom);
                wiBom.add(EsopWiBom);
                EsopHtWiBoms.add(EsopHtWiBom);
            }
            esopWiBomMapper.insertList(wiBom);
            if (StringUtils.isNotEmpty(EsopHtWiBoms)){
                esopHtWiBomMapper.insertList(EsopHtWiBoms);
            }
        }
    }

    public void saveTool(EsopWorkInstructionDto EsopWorkInstructionDto,Long id,SysUser user){
        if (StringUtils.isNotEmpty(EsopWorkInstructionDto.getEsopWiFTAndInspectionTools())){
            List<EsopWiFTAndInspectionTool> wiFTAndInspectionTools = new ArrayList<>();
            List<EsopHtWiFTAndInspectionTool> EsopHtWiFTAndInspectionTools = new ArrayList<>();
            for (EsopWiFTAndInspectionTool tool : EsopWorkInstructionDto.getEsopWiFTAndInspectionTools()) {
                EsopHtWiFTAndInspectionTool EsopHtWiFTAndInspectionTool = new EsopHtWiFTAndInspectionTool();
                tool.setWorkInstructionId(id);
                tool.setCreateUserId(user.getUserId());
                tool.setCreateTime(new Date());
                tool.setModifiedUserId(user.getUserId());
                tool.setModifiedTime(new Date());
                tool.setStatus(StringUtils.isEmpty(tool.getStatus())?1: tool.getStatus());
                tool.setOrgId(user.getOrganizationId());
                BeanUtils.copyProperties(tool,EsopHtWiFTAndInspectionTool);
                wiFTAndInspectionTools.add(tool);
                EsopHtWiFTAndInspectionTools.add(EsopHtWiFTAndInspectionTool);
            }
            esopWiFTAndInspectionToolMapper.insertList(wiFTAndInspectionTools);
            if (StringUtils.isNotEmpty(EsopHtWiFTAndInspectionTools)){
                esopHtWiFTAndInspectionToolMapper.insertList(EsopHtWiFTAndInspectionTools);
            }
        }
    }

    public void saveStandards(EsopWorkInstructionDto EsopWorkInstructionDto,Long id,SysUser user){
        if (StringUtils.isNotEmpty(EsopWorkInstructionDto.getEsopWiQualityStandardss())){
            List<EsopWiQualityStandards> wiQualityStandardss = new ArrayList<>();
            List<EsopHtWiQualityStandards> EsopHtWiQualityStandardss = new ArrayList<>();
            for (EsopWiQualityStandards standards : EsopWorkInstructionDto.getEsopWiQualityStandardss()) {
                EsopHtWiQualityStandards EsopHtWiQualityStandards = new EsopHtWiQualityStandards();
                standards.setWorkInstructionId(id);
                standards.setCreateUserId(user.getUserId());
                standards.setCreateTime(new Date());
                standards.setModifiedUserId(user.getUserId());
                standards.setModifiedTime(new Date());
                standards.setStatus(StringUtils.isEmpty(standards.getStatus())?1: standards.getStatus());
                standards.setOrgId(user.getOrganizationId());
                BeanUtils.copyProperties(standards,EsopHtWiQualityStandards);
                wiQualityStandardss.add(standards);
                EsopHtWiQualityStandardss.add(EsopHtWiQualityStandards);
            }
            esopWiQualityStandardsMapper.insertList(wiQualityStandardss);
            if (StringUtils.isNotEmpty(EsopHtWiQualityStandardss)){
                esopHtWiQualityStandardsMapper.insertList(EsopHtWiQualityStandardss);
            }
        }
    }

    public void saveFile(EsopWorkInstructionDto EsopWorkInstructionDto,Long id,SysUser user){
        if (StringUtils.isNotEmpty(EsopWorkInstructionDto.getEsopWiFiles())) {
            for(EsopWiFile file : EsopWorkInstructionDto.getEsopWiFiles()) {
                file.setWorkInstructionId(id);
                file.setCreateUserId(user.getUserId());
                file.setCreateTime(new Date());
                file.setModifiedUserId(user.getUserId());
                file.setModifiedTime(new Date());
                file.setStatus(StringUtils.isEmpty(file.getStatus()) ? 1 : file.getStatus());
                file.setOrgId(user.getOrganizationId());
                esopWiFileMapper.insertSelective(file);
                EsopHtWiFile EsopHtWiFile = new EsopHtWiFile();
                BeanUtils.copyProperties(file, EsopHtWiFile);
                esopHtWiFileMapper.insertSelective(EsopHtWiFile);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {

            Example bomExample = new Example(EsopWiBom.class);
            Example.Criteria bomCriteria = bomExample.createCriteria();
            bomCriteria.andEqualTo("workInstructionId", id);
            esopWiBomMapper.deleteByExample(bomExample);
            bomExample.clear();

            Example toolExample = new Example(EsopWiFTAndInspectionTool.class);
            Example.Criteria toolCriteria = toolExample.createCriteria();
            toolCriteria.andEqualTo("workInstructionId",id);
            esopWiFTAndInspectionToolMapper.deleteByExample(toolExample);
            toolExample.clear();

            Example standardsExample = new Example(EsopWiQualityStandards.class);
            Example.Criteria standardsCriteria = standardsExample.createCriteria();
            standardsCriteria.andEqualTo("workInstructionId", id);
            esopWiQualityStandardsMapper.deleteByExample(standardsExample);
            standardsExample.clear();

            Example fileExample = new Example(EsopWiFile.class);
            Example.Criteria fileCriteria = fileExample.createCriteria();
            fileCriteria.andEqualTo("workInstructionId", id);
            esopWiFileMapper.deleteByExample(fileExample);
            fileExample.clear();
        }

        return esopWorkInstructionMapper.deleteByIds(ids);
    }


/*

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EsopWorkInstructionDto importExcel(MultipartFile file) throws IOException {
        SysUser user = currentUser();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        EsopWorkInstructionDto EsopWorkInstructionDto = new EsopWorkInstructionDto();
        //导入物料清单
        List<EsopWiBom> EsopWiBoms = importEsopWiBomExcel(file, user);
        EsopWorkInstructionDto.setEsopWiBoms(EsopWiBoms);
        //导入设备清单
        List<EsopWiFTAndInspectionTool> EsopWiFTAndInspectionTools = importEsopWiFTAndInspectionToolExcel(file, user);
        EsopWorkInstructionDto.setEsopWiFTAndInspectionTools(EsopWiFTAndInspectionTools);
        //导入品质清单
        List<EsopWiQualityStandards> EsopWiQualityStandardss = importEsopWiQualityStandardsExcel(file, user);
        EsopWorkInstructionDto.setEsopWiQualityStandardss(EsopWiQualityStandardss);
        
        return EsopWorkInstructionDto;
    }
*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EsopWorkInstructionImport> esopWorkInstructionImports) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

            Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
            int success = 0;  //记录操作成功数
            List<Integer> fail = new ArrayList<>();  //记录操作失败行数

            LinkedList<EsopWorkInstruction> list = new LinkedList<>();
            LinkedList<EsopHtWorkInstruction> htList = new LinkedList<>();
            for (int i = 0; i < esopWorkInstructionImports.size(); i++) {
                EsopWorkInstructionImport esopWorkInstructionImport = esopWorkInstructionImports.get(i);
                EsopWorkInstruction esopWorkInstruction = new EsopWorkInstruction();

                String workInstructionCode = esopWorkInstructionImport.getWorkInstructionCode();
                String workInstructionName = esopWorkInstructionImport.getWorkInstructionName();
                String processCode = esopWorkInstructionImport.getProcessCode();
                String productModelCode = esopWorkInstructionImport.getProductModelCode();
                String workInstructionSeqNum = esopWorkInstructionImport.getWorkInstructionSeqNum();
                if (StringUtils.isEmpty(
                        workInstructionCode,workInstructionName,processCode,productModelCode,workInstructionSeqNum
                )){
                    fail.add(i+4);
                    continue;
                }

                //判断编码是否重复
                Example example = new Example(EsopWorkInstruction.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("orgId", user.getOrganizationId());
                criteria.andEqualTo("workInstructionCode", workInstructionCode);
                if (StringUtils.isNotEmpty(esopWorkInstructionMapper.selectOneByExample(example))){
                    fail.add(i+4);
                    continue;
                }

                BeanUtils.copyProperties(esopWorkInstructionImport, esopWorkInstruction);
                //产品型号是否为空
                if(StringUtils.isNotEmpty(productModelCode)){
                    SearchBaseProductModel searchBaseProductModel = new SearchBaseProductModel();
                    searchBaseProductModel.setOrgId(user.getOrganizationId());
                    searchBaseProductModel.setProductModelCode(productModelCode);
                    List<BaseProductModel> baseProductModelList = baseFeignApi.findList(searchBaseProductModel).getData();
                    if (StringUtils.isEmpty(baseProductModelList)){
                        fail.add(i+4);
                        continue;
                    }
                    esopWorkInstruction.setProductModelId(baseProductModelList.get(0).getProductModelId());
                }

                //工序是否为空
                if(StringUtils.isNotEmpty(processCode)){
                    SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
                    searchBaseProcess.setOrgId(user.getOrganizationId());
                    searchBaseProcess.setProcessCode(processCode);
                    List<BaseProcess> processList = baseFeignApi.findProcessList(searchBaseProcess).getData();
                    if (StringUtils.isEmpty(processList)){
                        fail.add(i+4);
                        continue;
                    }
                    esopWorkInstruction.setProcessId(processList.get(0).getProcessId());
                }

                esopWorkInstruction.setCreateTime(new Date());
                esopWorkInstruction.setCreateUserId(user.getUserId());
                esopWorkInstruction.setModifiedTime(new Date());
                esopWorkInstruction.setModifiedUserId(user.getUserId());
                esopWorkInstruction.setStatus((byte)1);
                esopWorkInstruction.setOrgId(user.getOrganizationId());
                list.add(esopWorkInstruction);
            }

        if (StringUtils.isNotEmpty(list)){
            success = esopWorkInstructionMapper.insertList(list);
        }

        for (EsopWorkInstruction esopWorkInstruction : list) {
            EsopHtWorkInstruction esopHtWorkInstruction = new EsopHtWorkInstruction();
            BeanUtils.copyProperties(esopWorkInstruction, esopHtWorkInstruction);
            htList.add(esopHtWorkInstruction);
        }
        if (StringUtils.isNotEmpty(htList)){
            esopHtWorkInstructionMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
/*

    */
/**
    *  导入物料清单
    **//*

    private List<EsopWiBom> importEsopWiBomExcel(MultipartFile file,SysUser user) throws IOException {
        //获取物料清单
        List<EsopWiBom> EsopWiBoms = importWiBomData(file.getInputStream());
        LinkedList<EsopWiBom> list = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < EsopWiBoms.size(); i++) {

            EsopWiBom EsopWiBom = EsopWiBoms.get(i);
            EsopWiBom.setWorkInstructionId((long)11);
            if (StringUtils.isEmpty(EsopWiBom) || StringUtils.isEmpty(EsopWiBom.getWorkInstructionId()) || StringUtils.isEmpty(EsopWiBom.getMaterialCode())){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(EsopWiBom.getMaterialCode());
            searchBaseMaterial.setOrganizationId(user.getOrganizationId());
            ResponseEntity<List<BaseMaterial>> baseMaterials = baseFeignApi.findList(searchBaseMaterial);
            if(StringUtils.isNotEmpty(baseMaterials.getData())) {
                Example example = new Example(EsopWiBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("materialId", EsopWiBom.getMaterialId()).andEqualTo("workInstructionId", EsopWiBom.getWorkInstructionId());
                if (StringUtils.isNotEmpty(esopWiBomMapper.selectOneByExample(example))) {
                    fail.add(i + 4);
                    example.clear();
                    continue;
                }
            }else{
                fail.add(i + 4);
                continue;
            }

            EsopWiBom wiBom = new EsopWiBom();
            BeanUtils.copyProperties(EsopWiBom,wiBom);
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


    public List<EsopWiBom> importWiBomData(InputStream in ) throws IOException {
        //xls用HSSFWorkbook ，xlsx用XSSFWorkbook
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sht = workbook.getSheetAt(2);
        List<EsopWiBom> list = new ArrayList<>();
        for (int i = 3;i<= sht.getLastRowNum(); i++) {
            Row r = sht.getRow(i);
            if(r != null && r.getCell(0)!= null && !r.getCell(0).getCellType().equals(CellType.BLANK)) {
                EsopWiBom EsopWiBom = new EsopWiBom();

                if(r.getCell(0).getCellType().equals(CellType.STRING)){
                    String strValue = r.getCell(0).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue))
                        EsopWiBom.setMaterialCode(strValue);
                }else if(r.getCell(0).getCellType().equals(CellType.NUMERIC)){
                    Double strValue = r.getCell(0).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue))
                        EsopWiBom.setMaterialCode(strValue.toString());
                }

                String strValue1 = r.getCell(1).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue1))
                    EsopWiBom.setMaterialName(strValue1);

                if(r.getCell(2).getCellType().equals(CellType.STRING)){
                    String strValue2 = r.getCell(2).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue2))
                        EsopWiBom.setMaterialVersion(strValue2);
                }else if(r.getCell(2).getCellType().equals(CellType.NUMERIC)){
                    Double strValue2 = r.getCell(2).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue2))
                        EsopWiBom.setMaterialVersion(strValue2.toString());
                }

                if(r.getCell(3).getCellType().equals(CellType.STRING)){
                    String strValue3 = r.getCell(2).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        EsopWiBom.setUsageQty(strValue3);
                }else if(r.getCell(3).getCellType().equals(CellType.NUMERIC)){
                    Double strValue3 = r.getCell(3).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        EsopWiBom.setUsageQty(strValue3.toString());
                }


                list.add(EsopWiBom);
            }
        }
        return list;
    }



    */
/**
     *  导入工装设备及检具要求
     **//*

    private List<EsopWiFTAndInspectionTool> importEsopWiFTAndInspectionToolExcel(MultipartFile file, SysUser user) throws IOException {
        //工装设备及检具要求
        List<EsopWiFTAndInspectionTool> EsopWiFTAndInspectionTools = importWiFTAndInspectionToolData(file.getInputStream());
        LinkedList<EsopWiFTAndInspectionTool> list = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < EsopWiFTAndInspectionTools.size(); i++) {
            EsopWiFTAndInspectionTool EsopWiFTAndInspectionTool = EsopWiFTAndInspectionTools.get(i);
            EsopWiFTAndInspectionTool.setWorkInstructionId((long)11);
            if (StringUtils.isEmpty(EsopWiFTAndInspectionTool) || StringUtils.isEmpty(EsopWiFTAndInspectionTool.getWorkInstructionId()) || EsopWiFTAndInspectionTool.getWorkInstructionId() ==0 ){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EsopWiFTAndInspectionTool.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("facilityTooling",EsopWiFTAndInspectionTool.getFacilityTooling()).andEqualTo("workInstructionId",EsopWiFTAndInspectionTool.getWorkInstructionId());
            if (StringUtils.isNotEmpty(esopWiFTAndInspectionToolMapper.selectOneByExample(example))){
                fail.add(i+4);
                example.clear();
                continue;
            }

            EsopWiFTAndInspectionTool wiFTAndInspectionTool = new EsopWiFTAndInspectionTool();
            BeanUtils.copyProperties(EsopWiFTAndInspectionTool,wiFTAndInspectionTool);
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



    public List<EsopWiFTAndInspectionTool> importWiFTAndInspectionToolData(InputStream in ) throws IOException {
        //xls用HSSFWorkbook ，xlsx用XSSFWorkbook
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sht = workbook.getSheetAt(1);
        List<EsopWiFTAndInspectionTool> list = new ArrayList<>();
        for (int i = 3;i<= sht.getLastRowNum(); i++) {
            Row r = sht.getRow(i);
            if(r != null && r.getCell(0)!= null && !r.getCell(0).getCellType().equals(CellType.BLANK)) {
                EsopWiFTAndInspectionTool EsopWiFTAndInspectionTool = new EsopWiFTAndInspectionTool();
                String strValue = r.getCell(0).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue))
                    EsopWiFTAndInspectionTool.setFacilityTooling(strValue);
                if(r.getCell(1).getCellType().equals(CellType.STRING)){
                    String strValue1 = r.getCell(1).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue1))
                        EsopWiFTAndInspectionTool.setQty(strValue1);
                }else if(r.getCell(1).getCellType().equals(CellType.NUMERIC)){
                    Double strValue1 = r.getCell(1).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue1))
                        EsopWiFTAndInspectionTool.setQty(strValue1.toString());
                }

                String strValue2 = r.getCell(2).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue2))
                    EsopWiFTAndInspectionTool.setRemark(strValue2);

                list.add(EsopWiFTAndInspectionTool);
            }
        }
        return list;
    }



    */
/**
     *  品质标准
     **//*

    private  List<EsopWiQualityStandards> importEsopWiQualityStandardsExcel(MultipartFile file, SysUser user) throws IOException {
        //获取品质标准数据
        List<EsopWiQualityStandards> EsopWiQualityStandardss = importWiQualityStandardsData(file.getInputStream());
        LinkedList<EsopWiQualityStandards> list = new LinkedList<>();
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < EsopWiQualityStandardss.size(); i++) {
            EsopWiQualityStandards EsopWiQualityStandards = EsopWiQualityStandardss.get(i);
            EsopWiQualityStandards.setWorkInstructionId((long)11);
            if (StringUtils.isEmpty(EsopWiQualityStandards) || StringUtils.isEmpty(EsopWiQualityStandards.getWorkInstructionId())|| EsopWiQualityStandards.getWorkInstructionId() ==0){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EsopWiQualityStandards.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("wiQualityStandardsIssuesName",EsopWiQualityStandards.getWiQualityStandardsIssuesName())
                    .andEqualTo("workInstructionId",EsopWiQualityStandards.getWorkInstructionId());
            if (StringUtils.isNotEmpty(esopWiQualityStandardsMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            EsopWiQualityStandards wiQualityStandards = new EsopWiQualityStandards();
            BeanUtils.copyProperties(EsopWiQualityStandards,wiQualityStandards);
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


    public List<EsopWiQualityStandards> importWiQualityStandardsData(InputStream in ) throws IOException {
        //xls用HSSFWorkbook ，xlsx用XSSFWorkbook
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sht = workbook.getSheetAt(0);
        List<EsopWiQualityStandards> list = new ArrayList<>();
        for (int i = 3;i<= sht.getLastRowNum(); i++) {
            Row r = sht.getRow(i);
            if(r != null && r.getCell(0)!= null && !r.getCell(0).getCellType().equals(CellType.BLANK)) {
                EsopWiQualityStandards EsopWiQualityStandards = new EsopWiQualityStandards();
                String strValue = r.getCell(0).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue))
                    EsopWiQualityStandards.setWiQualityStandardsIssuesName(strValue);

                String strValue1 = r.getCell(1).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue1))
                    EsopWiQualityStandards.setWiQualityStandardsContent(strValue1);

                String strValue2 = r.getCell(2).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue2))
                    EsopWiQualityStandards.setInspectionType(strValue2);

                if(r.getCell(3).getCellType().equals(CellType.STRING)){
                    String strValue3 = r.getCell(3).getStringCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        EsopWiQualityStandards.setInspectionFrequency(strValue3);
                }else if(r.getCell(3).getCellType().equals(CellType.NUMERIC)){
                    Double strValue3 = r.getCell(3).getNumericCellValue();
                    if (StringUtils.isNotEmpty(strValue3))
                        EsopWiQualityStandards.setInspectionFrequency(strValue3.toString());
                }


                String strValue4 = r.getCell(4).getStringCellValue();
                if (StringUtils.isNotEmpty(strValue4))
                    EsopWiQualityStandards.setRecord(strValue4);

                list.add(EsopWiQualityStandards);
            }
        }
        return list;
    }
*/

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String download(HttpServletResponse response) throws IOException {

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("WiExcelDowload");
        ResponseEntity<List<SysSpecItem>> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        if(StringUtils.isEmpty(specItemList.getData())) throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未设置下载模板");
        String fileUrl = specItemList.getData().get(0).getParaValue();
        return fileUrl.substring(fileUrl.lastIndexOf("group"));
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    @Override
    public int censor(EsopWorkInstructionDto EsopWorkInstructionDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(EsopWorkInstructionDto.getWorkInstructionId()))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"id不能为空");
        esopWorkInstructionMapper.updateByPrimaryKeySelective(EsopWorkInstructionDto);

        Long workInstructionId=EsopWorkInstructionDto.getWorkInstructionId();
        Example exampleWiRDet = new Example(EsopWiReleaseDet.class);
        Example.Criteria criteriaWiRDet = exampleWiRDet.createCriteria();
        criteriaWiRDet.andEqualTo("workInstructionId", workInstructionId);
        criteriaWiRDet.andEqualTo("orgId", user.getOrganizationId());
        List<EsopWiReleaseDet> EsopWiReleaseDetList= esopWiReleaseDetMapper.selectByExample(exampleWiRDet);

        if(StringUtils.isNotEmpty(EsopWiReleaseDetList)) {
            for (EsopWiReleaseDet EsopWiReleaseDet : EsopWiReleaseDetList) {
                EsopWiRelease EsopWiRelease = esopWiReleaseMapper.selectByPrimaryKey(EsopWiReleaseDet.getWiReleaseId());
                if (EsopWiRelease.getReleaseStatus() == (byte) 2) {
                    socketService.BatchInstructions(EsopWiRelease.getProLineId(), "1202", "/#/YunZhiESOP?mac=","0");
                }
            }
        }
        return 1;
    }

}
