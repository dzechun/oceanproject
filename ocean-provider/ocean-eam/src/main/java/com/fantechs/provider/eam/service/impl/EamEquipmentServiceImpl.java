package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamSparePartReEquDto;
import com.fantechs.common.base.general.dto.eam.imports.EamEquipmentImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShop;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquipmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamEquipmentServiceImpl extends BaseService<EamEquipment> implements EamEquipmentService {

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private EamHtEquipmentMapper eamHtEquipmentMapper;
    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;
    @Resource
    private EamEquipmentAttachmentMapper eamEquipmentAttachmentMapper;
    @Resource
    private EamSparePartReEquMapper eamSparePartReEquMapper;
    @Resource
    private EamEquipmentCategoryMapper eamEquipmentCategoryMapper;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EamSparePartMapper eamSparePartMapper;

    @Override
    public List<EamEquipmentDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = getUser();
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentMapper.findList(map);
    }

    @Override
    public List<EamHtEquipment> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMapper.findHtList(map);
    }

    @Override
    public int batchUpdate(List<EamEquipment> list) {
        return eamEquipmentMapper.batchUpdate(list);
    }

    @Override
    public EamEquipment detailByIp(String ip) {
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentIp",ip);
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(eamEquipment)){
            throw new BizErrorException("未查询到ip对应的设备信息");
        }
        return eamEquipment;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipment record) {
        SysUser user = getUser();

        check(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentMapper.insertUseGeneratedKeys(record);

        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(record, eamHtEquipment);
        int i = eamHtEquipmentMapper.insertSelective(eamHtEquipment);

        //条码列表
        List<EamEquipmentBarcode> eamEquipmentBarcodeList = record.getEamEquipmentBarcodeList();
        if(StringUtils.isNotEmpty(eamEquipmentBarcodeList)){
            this.barcodeIfRepeat(eamEquipmentBarcodeList,user);

            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList){
                eamEquipmentBarcode.setEquipmentId(record.getEquipmentId());
                eamEquipmentBarcode.setCreateUserId(user.getUserId());
                eamEquipmentBarcode.setCreateTime(new Date());
                eamEquipmentBarcode.setModifiedUserId(user.getUserId());
                eamEquipmentBarcode.setModifiedTime(new Date());
                eamEquipmentBarcode.setStatus(record.getStatus());
                eamEquipmentBarcode.setOrgId(user.getOrganizationId());
                eamEquipmentBarcode.setEquipmentStatus((byte)5);
            }
            eamEquipmentBarcodeMapper.insertList(eamEquipmentBarcodeList);
        }

        //附件列表
        List<EamEquipmentAttachment> eamEquipmentAttachmentList = record.getEamEquipmentAttachmentList();
        if(StringUtils.isNotEmpty(eamEquipmentAttachmentList)){
            for (EamEquipmentAttachment eamJigAttachmentList : eamEquipmentAttachmentList){
                eamJigAttachmentList.setEquipmentId(record.getEquipmentId());
                eamJigAttachmentList.setCreateUserId(user.getUserId());
                eamJigAttachmentList.setCreateTime(new Date());
                eamJigAttachmentList.setModifiedUserId(user.getUserId());
                eamJigAttachmentList.setModifiedTime(new Date());
                eamJigAttachmentList.setStatus(StringUtils.isEmpty(eamJigAttachmentList.getStatus())?1: eamJigAttachmentList.getStatus());
                eamJigAttachmentList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentAttachmentMapper.insertList(eamEquipmentAttachmentList);
        }

        //备用件列表
        List<EamSparePartReEquDto> eamEquipmentBackupList = record.getEamEquipmentBackupList();
        if(StringUtils.isNotEmpty(eamEquipmentBackupList)){
            for (EamSparePartReEquDto eamEquipmentBackup : eamEquipmentBackupList){
                eamEquipmentBackup.setEquipmentId(record.getEquipmentId());
                eamEquipmentBackup.setCreateUserId(user.getUserId());
                eamEquipmentBackup.setCreateTime(new Date());
                eamEquipmentBackup.setModifiedUserId(user.getUserId());
                eamEquipmentBackup.setModifiedTime(new Date());
                eamEquipmentBackup.setStatus(StringUtils.isEmpty(eamEquipmentBackup.getStatus())?1: eamEquipmentBackup.getStatus());
                eamEquipmentBackup.setOrgId(user.getOrganizationId());
            }
            eamSparePartReEquMapper.insertList(eamEquipmentBackupList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipment entity) {
        SysUser user = getUser();

        check(entity);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamEquipmentMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(entity, eamHtEquipment);
        eamHtEquipmentMapper.insertSelective(eamHtEquipment);

        //原来有的条码信息只更新
        List<Long> equipmentBarcodeIdList = new ArrayList<>();
        List<EamEquipmentBarcode> eamEquipmentBarcodeList = entity.getEamEquipmentBarcodeList();
        if(StringUtils.isNotEmpty(eamEquipmentBarcodeList)) {
            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList) {
                if (StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())) {
                    eamEquipmentBarcode.setCurrentUsageDays(null);
                    eamEquipmentBarcode.setStatus(entity.getStatus());
                    eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
                    equipmentBarcodeIdList.add(eamEquipmentBarcode.getEquipmentBarcodeId());
                }
            }
        }

        //删除原条码
        Example example1 = new Example(EamEquipmentBarcode.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentId", entity.getEquipmentId());
        if (equipmentBarcodeIdList.size() > 0) {
            criteria1.andNotIn("equipmentBarcodeId", equipmentBarcodeIdList);
        }
        eamEquipmentBarcodeMapper.deleteByExample(example1);

        //条码列表
        if(StringUtils.isNotEmpty(eamEquipmentBarcodeList)){
            this.barcodeIfRepeat(eamEquipmentBarcodeList,user);

            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList){
                if(equipmentBarcodeIdList.contains(eamEquipmentBarcode.getEquipmentBarcodeId())){
                    continue;
                }
                eamEquipmentBarcode.setEquipmentId(entity.getEquipmentId());
                eamEquipmentBarcode.setCreateUserId(user.getUserId());
                eamEquipmentBarcode.setCreateTime(new Date());
                eamEquipmentBarcode.setModifiedUserId(user.getUserId());
                eamEquipmentBarcode.setModifiedTime(new Date());
                eamEquipmentBarcode.setStatus(entity.getStatus());
                eamEquipmentBarcode.setOrgId(user.getOrganizationId());
                eamEquipmentBarcode.setEquipmentStatus((byte)5);
                eamEquipmentBarcodeMapper.insert(eamEquipmentBarcode);
            }
        }

        //删除原附件
        Example example2 = new Example(EamEquipmentAttachment.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("equipmentId", entity.getEquipmentId());
        eamEquipmentAttachmentMapper.deleteByExample(example2);

        //附件列表
        List<EamEquipmentAttachment> eamEquipmentAttachmentList = entity.getEamEquipmentAttachmentList();
        if(StringUtils.isNotEmpty(eamEquipmentAttachmentList)){
            for (EamEquipmentAttachment eamJigAttachmentList : eamEquipmentAttachmentList){
                eamJigAttachmentList.setEquipmentId(entity.getEquipmentId());
                eamJigAttachmentList.setCreateUserId(user.getUserId());
                eamJigAttachmentList.setCreateTime(new Date());
                eamJigAttachmentList.setModifiedUserId(user.getUserId());
                eamJigAttachmentList.setModifiedTime(new Date());
                eamJigAttachmentList.setStatus(StringUtils.isEmpty(eamJigAttachmentList.getStatus())?1: eamJigAttachmentList.getStatus());
                eamJigAttachmentList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentAttachmentMapper.insertList(eamEquipmentAttachmentList);
        }

        //删除原备用件
        Example example3 = new Example(EamSparePartReEqu.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("equipmentId", entity.getEquipmentId());
        eamSparePartReEquMapper.deleteByExample(example3);

        //备用件列表
        List<EamSparePartReEquDto> eamEquipmentBackupList = entity.getEamEquipmentBackupList();
        if(StringUtils.isNotEmpty(eamEquipmentBackupList)){
            for (EamSparePartReEquDto eamEquipmentBackup : eamEquipmentBackupList){
                eamEquipmentBackup.setEquipmentId(entity.getEquipmentId());
                eamEquipmentBackup.setCreateUserId(user.getUserId());
                eamEquipmentBackup.setCreateTime(new Date());
                eamEquipmentBackup.setModifiedUserId(user.getUserId());
                eamEquipmentBackup.setModifiedTime(new Date());
                eamEquipmentBackup.setStatus(StringUtils.isEmpty(eamEquipmentBackup.getStatus())?1: eamEquipmentBackup.getStatus());
                eamEquipmentBackup.setOrgId(user.getOrganizationId());
            }
            eamSparePartReEquMapper.insertList(eamEquipmentBackupList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        getUser();

        List<EamHtEquipment> htList = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipment eamHtEquipment = new EamHtEquipment();
            BeanUtils.copyProperties(eamEquipment, eamHtEquipment);
            htList.add(eamHtEquipment);

            //删除原条码
            Example example1 = new Example(EamEquipmentBarcode.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentId", id);
            eamEquipmentBarcodeMapper.deleteByExample(example1);

            //删除原附件
            Example example2 = new Example(EamEquipmentAttachment.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("equipmentId",id);
            eamEquipmentAttachmentMapper.deleteByExample(example2);

            //删除原备用件
            Example example3 = new Example(EamSparePartReEqu.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("equipmentId",id);
            eamSparePartReEquMapper.deleteByExample(example3);
        }

        eamHtEquipmentMapper.insertList(htList);

        return eamEquipmentMapper.deleteByIds(ids);
    }

    public void check(EamEquipment entity){
        SysUser user = getUser();
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", entity.getEquipmentCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if(StringUtils.isNotEmpty(entity.getEquipmentId())){
            criteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
        }
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    /**
     * 判断条码是否重复
     * @param eamEquipmentBarcodeList
     */
    public void barcodeIfRepeat(List<EamEquipmentBarcode> eamEquipmentBarcodeList,SysUser user){
        List<String> equipmentBarcodes = new ArrayList<>();
        List<String> assetCodes = new ArrayList<>();
        List<String> equipmentSeqNums = new ArrayList<>();

        for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList) {
            if(StringUtils.isEmpty(eamEquipmentBarcode.getEquipmentBarcode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "设备条码不能为空");
            }

            if(equipmentBarcodes.contains(eamEquipmentBarcode.getEquipmentBarcode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "设备条码重复");
            }

            if(assetCodes.contains(eamEquipmentBarcode.getAssetCode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "资产条码重复");
            }

            if(equipmentSeqNums.contains(eamEquipmentBarcode.getEquipmentSeqNum())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "设备序号重复");
            }

            Example example = new Example(EamEquipmentBarcode.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("equipmentBarcode", eamEquipmentBarcode.getEquipmentBarcode())
                    .andEqualTo("orgId",user.getOrganizationId());
            if(StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())){
                criteria.andNotEqualTo("equipmentBarcodeId",eamEquipmentBarcode.getEquipmentBarcodeId());
            }
            EamEquipmentBarcode equipmentBarcode = eamEquipmentBarcodeMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(equipmentBarcode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "设备条码重复");
            }
            equipmentBarcodes.add(eamEquipmentBarcode.getEquipmentBarcode());

            if(StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentSeqNum())) {
                example.clear();
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andEqualTo("equipmentSeqNum", eamEquipmentBarcode.getEquipmentSeqNum())
                        .andEqualTo("orgId", user.getOrganizationId());
                if (StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())) {
                    criteria2.andNotEqualTo("equipmentBarcodeId", eamEquipmentBarcode.getEquipmentBarcodeId());
                }
                EamEquipmentBarcode equipmentBarcodNum = eamEquipmentBarcodeMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(equipmentBarcodNum)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "设备序号重复");
                }
                equipmentSeqNums.add(eamEquipmentBarcode.getEquipmentSeqNum());
            }


            if(StringUtils.isNotEmpty(eamEquipmentBarcode.getAssetCode())) {
                example.clear();
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("assetCode", eamEquipmentBarcode.getAssetCode())
                         .andEqualTo("orgId",user.getOrganizationId());
                if (StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())) {
                    criteria1.andNotEqualTo("equipmentBarcodeId", eamEquipmentBarcode.getEquipmentBarcodeId());
                }
                EamEquipmentBarcode equipmentBarcode1 = eamEquipmentBarcodeMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(equipmentBarcode1)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "资产条码重复");
                }
                assetCodes.add(eamEquipmentBarcode.getAssetCode());
            }

        }
    }


    @Override
    public List<EamEquipmentDto> findNoGroup(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findNoGroup(map);
    }

    @Override
    public List<EamEquInspectionOrderDto> findListForInspectionOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findListForInspectionOrder(map);
    }

    @Override
    public List<EamEquMaintainOrderDto> findListForMaintainOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findListForMaintainOrder(map);
    }


    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EamEquipmentImport> eamEquipmentImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EamEquipment> list = new LinkedList<>();
        LinkedList<EamHtEquipment> htList = new LinkedList<>();
        LinkedList<EamEquipmentImport> eamEquipmentImportList = new LinkedList<>();

        for (int i = 0; i < eamEquipmentImports.size(); i++) {
            EamEquipmentImport eamEquipmentImport = eamEquipmentImports.get(i);
            String equipmentCode = eamEquipmentImport.getEquipmentCode();
            String equipmentName = eamEquipmentImport.getEquipmentName();
            String equipmentCategoryCode = eamEquipmentImport.getEquipmentCategoryCode();

            if (StringUtils.isEmpty(
                    equipmentCode,equipmentName,equipmentCategoryCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EamEquipment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("equipmentCode",equipmentCode);
            if (StringUtils.isNotEmpty(eamEquipmentMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            /*boolean tag = false;
            if (StringUtils.isNotEmpty(eamEquipmentImportList)){
                for (EamEquipmentImport equipmentImport: eamEquipmentImportList) {
                    if (equipmentCode.equals(equipmentImport.getEquipmentCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }*/

            //设备类别信息
            Example example1 = new Example(EamEquipmentCategory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId())
                    .andEqualTo("equipmentCategoryCode",equipmentCategoryCode);
            EamEquipmentCategory eamEquipmentCategory = eamEquipmentCategoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(eamEquipmentCategory)){
                fail.add(i+4);
                continue;
            }
            eamEquipmentImport.setEquipmentCategoryId(eamEquipmentCategory.getEquipmentCategoryId());

            //设备管理员
            String userCode = eamEquipmentImport.getUserCode();
            if(StringUtils.isNotEmpty(userCode)){
                SearchSysUser searchSysUser = new SearchSysUser();
                searchSysUser.setUserCode(userCode);
                List<SysUser> sysUsers = securityFeignApi.selectUsers(searchSysUser).getData();
                if (StringUtils.isEmpty(sysUsers)){
                    fail.add(i+4);
                    continue;
                }
                eamEquipmentImport.setEquipmentMgtUserId(sysUsers.get(0).getUserId());
            }

            //工厂
            String factoryCode = eamEquipmentImport.getFactoryCode();
            if(StringUtils.isNotEmpty(factoryCode)){
                SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
                searchBaseFactory.setFactoryCode(factoryCode);
                searchBaseFactory.setOrgId(currentUser.getOrganizationId());
                List<BaseFactoryDto> factoryDtos = baseFeignApi.findFactoryList(searchBaseFactory).getData();
                if (StringUtils.isEmpty(factoryDtos)){
                    fail.add(i+4);
                    continue;
                }
                eamEquipmentImport.setFactoryId(factoryDtos.get(0).getFactoryId());
            }

            //车间
            String workShopCode = eamEquipmentImport.getWorkShopCode();
            if(StringUtils.isNotEmpty(workShopCode)){
                SearchBaseWorkShop searchBaseWorkShop = new SearchBaseWorkShop();
                searchBaseWorkShop.setWorkShopCode(workShopCode);
                searchBaseWorkShop.setOrgId(currentUser.getOrganizationId());
                List<BaseWorkShopDto> workShopDtos = baseFeignApi.findWorkShopList(searchBaseWorkShop).getData();
                if (StringUtils.isEmpty(workShopDtos)){
                    fail.add(i+4);
                    continue;
                }
                eamEquipmentImport.setWorkShopId(workShopDtos.get(0).getWorkShopId());
            }

            //产线
            String proLineCode = eamEquipmentImport.getProLineCode();
            if(StringUtils.isNotEmpty(proLineCode)){
                SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
                searchBaseProLine.setProCode(proLineCode);
                searchBaseProLine.setOrgId(currentUser.getOrganizationId());
                List<BaseProLine> baseProLines = baseFeignApi.findList(searchBaseProLine).getData();
                if (StringUtils.isEmpty(baseProLines)){
                    fail.add(i+4);
                    continue;
                }
                eamEquipmentImport.setProLineId(baseProLines.get(0).getProLineId());
            }

            //配置项：最大使用次数和最大使用天数是否都可填
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("IfBoth");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 0){
                if(StringUtils.isNotEmpty(eamEquipmentImport.getMaxUsageTime(),eamEquipmentImport.getWarningTime())){
                    eamEquipmentImport.setMaxUsageDays(0);
                    eamEquipmentImport.setWarningDays(0);
                }
            }

            //根据长宽高计算体积
            if(StringUtils.isNotEmpty(eamEquipmentImport.getLength(),eamEquipmentImport.getWidth(),eamEquipmentImport.getHeight())){
                BigDecimal volume = eamEquipmentImport.getLength().multiply(eamEquipmentImport.getWidth()).multiply(eamEquipmentImport.getHeight());
                eamEquipmentImport.setVolume(volume);
            }

            //备用件
            String sparePartCode = eamEquipmentImport.getSparePartCode();
            if(StringUtils.isNotEmpty(sparePartCode)){
                Example example2 = new Example(EamSparePart.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("orgId",currentUser.getOrganizationId())
                        .andEqualTo("sparePartCode",sparePartCode);
                EamSparePart eamSparePart = eamSparePartMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(eamSparePart)){
                    fail.add(i+4);
                    continue;
                }
                eamEquipmentImport.setSparePartId(eamSparePart.getSparePartId());
            }

            eamEquipmentImportList.add(eamEquipmentImport);
        }

        if (StringUtils.isNotEmpty(eamEquipmentImportList)){
            HashMap<String, List<EamEquipmentImport>> map = eamEquipmentImportList.stream().collect(Collectors.groupingBy(EamEquipmentImport::getEquipmentCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                //主表
                List<EamEquipmentImport> eamEquipmentImports1 = map.get(code);
                EamEquipment eamEquipment = new EamEquipment();
                BeanUtils.copyProperties(eamEquipmentImports1.get(0),eamEquipment);
                eamEquipment.setCreateTime(new Date());
                eamEquipment.setCreateUserId(currentUser.getUserId());
                eamEquipment.setModifiedTime(new Date());
                eamEquipment.setModifiedUserId(currentUser.getUserId());
                eamEquipment.setOrgId(currentUser.getOrganizationId());
                eamEquipment.setStatus((byte)1);
                list.add(eamEquipment);
                success += eamEquipmentMapper.insertUseGeneratedKeys(eamEquipment);

                //明细
                LinkedList<EamEquipmentBarcode> barcodeList = new LinkedList<>();
                for (EamEquipmentImport eamEquipmentImport : eamEquipmentImports1) {
                    if (StringUtils.isEmpty(eamEquipmentImport.getAssetCode(),eamEquipmentImport.getEquipmentBarcode())) {
                        continue;
                    }
                    EamEquipmentBarcode eamEquipmentBarcode = new EamEquipmentBarcode();
                    BeanUtils.copyProperties(eamEquipmentImport, eamEquipmentBarcode);
                    eamEquipmentBarcode.setCurrentUsageDays(StringUtils.isEmpty(eamEquipmentBarcode.getCurrentUsageDays())?0:eamEquipmentBarcode.getCurrentUsageDays());
                    eamEquipmentBarcode.setCurrentUsageTime(StringUtils.isEmpty(eamEquipmentBarcode.getCurrentUsageTime())?0:eamEquipmentBarcode.getCurrentUsageTime());
                    eamEquipmentBarcode.setEquipmentId(eamEquipment.getEquipmentId());
                    eamEquipmentBarcode.setStatus((byte) 1);
                    eamEquipmentBarcode.setEquipmentStatus((byte)5);
                    eamEquipmentBarcode.setCreateUserId(currentUser.getUserId());
                    eamEquipmentBarcode.setCreateTime(new Date());
                    eamEquipmentBarcode.setModifiedUserId(currentUser.getUserId());
                    eamEquipmentBarcode.setModifiedTime(new Date());
                    barcodeList.add(eamEquipmentBarcode);
                }
                if(StringUtils.isNotEmpty(barcodeList)) {
                    eamEquipmentBarcodeMapper.insertList(barcodeList);
                }

                LinkedList<EamSparePartReEqu> sparePartReEqus = new LinkedList<>();
                for (EamEquipmentImport eamEquipmentImport : eamEquipmentImports1) {
                    if (StringUtils.isEmpty(eamEquipmentImport.getSparePartCode())) {
                        continue;
                    }
                    EamSparePartReEqu eamSparePartReEqu = new EamSparePartReEqu();
                    BeanUtils.copyProperties(eamEquipmentImport, eamSparePartReEqu);
                    eamSparePartReEqu.setEquipmentId(eamEquipment.getEquipmentId());
                    eamSparePartReEqu.setStatus((byte) 1);
                    eamSparePartReEqu.setOrgId(currentUser.getOrganizationId());
                    sparePartReEqus.add(eamSparePartReEqu);
                }
                if(StringUtils.isNotEmpty(sparePartReEqus)) {
                    eamSparePartReEquMapper.insertList(sparePartReEqus);
                }
            }

            /*for (EamEquipmentImport eamEquipmentImport : eamEquipmentImportList) {
                EamEquipment eamEquipment = new EamEquipment();
                BeanUtils.copyProperties(eamEquipmentImport,eamEquipment);
                eamEquipment.setCreateTime(new Date());
                eamEquipment.setCreateUserId(currentUser.getUserId());
                eamEquipment.setModifiedTime(new Date());
                eamEquipment.setModifiedUserId(currentUser.getUserId());
                eamEquipment.setOrgId(currentUser.getOrganizationId());
                eamEquipment.setStatus((byte)1);
                list.add(eamEquipment);
            }
            success = eamEquipmentMapper.insertList(list);*/

            if(StringUtils.isNotEmpty(list)){
                for (EamEquipment eamEquipment : list) {
                    EamHtEquipment eamHtEquipment = new EamHtEquipment();
                    BeanUtils.copyProperties(eamEquipment, eamHtEquipment);
                    htList.add(eamHtEquipment);
                }
                eamHtEquipmentMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

}
