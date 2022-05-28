package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.imports.EamJigBarcodeImport;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.mapper.EamHtJigBarcodeMapper;
import com.fantechs.provider.eam.mapper.EamJigBarcodeMapper;
import com.fantechs.provider.eam.mapper.EamJigMapper;
import com.fantechs.provider.eam.service.EamJigBarcodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigBarcodeServiceImpl extends BaseService<EamJigBarcode> implements EamJigBarcodeService {

    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamHtJigBarcodeMapper eamHtJigBarcodeMapper;
    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public List<EamJigBarcodeDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }

        return eamJigBarcodeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigBarcode record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamJigBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcode", record.getJigBarcode())
                .orEqualTo("assetCode",record.getAssetCode());
        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJigBarcode)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        record.setUsageStatus((byte)2);
        eamJigBarcodeMapper.insertUseGeneratedKeys(record);

        EamHtJigBarcode eamHtJigBarcode = new EamHtJigBarcode();
        BeanUtils.copyProperties(record, eamHtJigBarcode);
        int i = eamHtJigBarcodeMapper.insertSelective(eamHtJigBarcode);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigBarcode entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamJigBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcode", entity.getJigBarcode())
                .orEqualTo("assetCode",entity.getAssetCode());
        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJigBarcode)&&!entity.getJigBarcodeId().equals(eamJigBarcode.getJigBarcodeId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtJigBarcode eamHtJigBarcode = new EamHtJigBarcode();
        BeanUtils.copyProperties(entity, eamHtJigBarcode);
        eamHtJigBarcodeMapper.insertSelective(eamHtJigBarcode);

        return eamJigBarcodeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJigBarcode> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamJigBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigBarcode eamHtJigBarcode = new EamHtJigBarcode();
            BeanUtils.copyProperties(eamJigBarcode, eamHtJigBarcode);
            list.add(eamHtJigBarcode);
        }

        eamHtJigBarcodeMapper.insertList(list);

        return eamJigBarcodeMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EamJigBarcodeImport> eamJigBarcodeImports,Long jigId) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EamJigBarcode> list = new LinkedList<>();
        LinkedList<EamHtJigBarcode> htList = new LinkedList<>();
        for (int i = 0; i < eamJigBarcodeImports.size(); i++) {
            EamJigBarcodeImport eamJigBarcodeImport = eamJigBarcodeImports.get(i);

            String jigBarcode = eamJigBarcodeImport.getJigBarcode();
            String assetCode = eamJigBarcodeImport.getAssetCode();
            if (StringUtils.isEmpty(
                    jigBarcode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EamJigBarcode.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("jigBarcode",jigBarcode);
            if (StringUtils.isNotEmpty(eamJigBarcodeMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            if(StringUtils.isNotEmpty(assetCode)) {
                example.clear();
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
                criteria1.andEqualTo("assetCode", assetCode);
                if (StringUtils.isNotEmpty(eamJigBarcodeMapper.selectOneByExample(example))) {
                    fail.add(i + 4);
                    continue;
                }
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (EamJigBarcode eamJigBarcode : list) {
                    if (eamJigBarcode.getJigBarcode().equals(jigBarcode)||(StringUtils.isNotEmpty(assetCode)&&eamJigBarcode.getAssetCode().equals(assetCode))){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            EamJigBarcode eamJigBarcode = new EamJigBarcode();
            BeanUtils.copyProperties(eamJigBarcodeImport, eamJigBarcode);
            eamJigBarcode.setJigId(jigId);
            eamJigBarcode.setCreateTime(new Date());
            eamJigBarcode.setCreateUserId(currentUser.getUserId());
            eamJigBarcode.setModifiedTime(new Date());
            eamJigBarcode.setModifiedUserId(currentUser.getUserId());
            eamJigBarcode.setStatus((byte)1);
            eamJigBarcode.setOrgId(currentUser.getOrganizationId());
            eamJigBarcode.setUsageStatus((byte)2);
            list.add(eamJigBarcode);
        }

        if (StringUtils.isNotEmpty(list)){
            success = eamJigBarcodeMapper.insertList(list);
        }

        for (EamJigBarcode eamJigBarcode : list) {
            EamHtJigBarcode eamHtJigBarcode = new EamHtJigBarcode();
            BeanUtils.copyProperties(eamJigBarcode, eamHtJigBarcode);
            htList.add(eamHtJigBarcode);
        }

        if (StringUtils.isNotEmpty(htList)){
            eamHtJigBarcodeMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    @Override
    public int plusCurrentUsageTime(Long jigBarcodeId, Integer num) {
        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectByPrimaryKey(jigBarcodeId);
        if (StringUtils.isEmpty(eamJigBarcode)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        eamJigBarcode.setCurrentUsageTime(eamJigBarcode.getCurrentUsageTime()+num);
        return eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
    }

    /**
     * 治具预警
     */
    public int jigWarning(){
        String message = "";
        List<String> messages = new ArrayList<>();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("IsJigCanContinueUse");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        Map<String, Object> map = new HashMap<>();
        List<EamJigBarcodeDto> list = this.findList(map);

        for (EamJigBarcodeDto eamJigBarcodeDto : list){
            EamJig eamJig = eamJigMapper.selectByPrimaryKey(eamJigBarcodeDto.getJigId());

            //使用次数预警
            if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentUsageTime(), eamJig.getWarningTime())
                    && eamJig.getWarningTime() != 0) {
                if (eamJigBarcodeDto.getCurrentUsageTime().compareTo(eamJig.getWarningTime()) == 0
                        || eamJigBarcodeDto.getCurrentUsageTime().compareTo(eamJig.getWarningTime()) == 1) {
                    message = "治具条码为"+eamJigBarcodeDto.getJigBarcode()+"的治具使用次数已达到警告次数";
                    messages.add(message);
                }
            }

            //使用天数预警
            if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentUsageDays(), eamJig.getWarningDays())
                    && eamJig.getWarningDays() != 0) {
                if (eamJigBarcodeDto.getCurrentUsageDays().compareTo(eamJig.getWarningDays()) == 0
                        || eamJigBarcodeDto.getCurrentUsageDays().compareTo(eamJig.getWarningDays()) == 1) {
                    message = "治具条码为"+eamJigBarcodeDto.getJigBarcode()+"的治具使用天数已达到警告天数";
                    messages.add(message);
                }
            }

            //保养使用次数预警
            if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentMaintainUsageTime(), eamJig.getMaintainWarningTime())) {
                if (eamJigBarcodeDto.getCurrentMaintainUsageTime().compareTo(eamJig.getMaintainWarningTime()) == 0
                        || eamJigBarcodeDto.getCurrentMaintainUsageTime().compareTo(eamJig.getMaintainWarningTime()) == 1) {
                    message = "治具条码为"+eamJigBarcodeDto.getJigBarcode()+"的治具保养累计使用次数已达到保养警告次数";
                    messages.add(message);
                }
            }

            //保养使用天数预警
            if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentMaintainUsageDays(), eamJig.getMaintainWarningDays())) {
                if (eamJigBarcodeDto.getCurrentMaintainUsageDays().compareTo(eamJig.getMaintainWarningDays()) == 0
                        || eamJigBarcodeDto.getCurrentMaintainUsageDays().compareTo(eamJig.getMaintainWarningDays()) == 1) {
                    message = "治具条码为"+eamJigBarcodeDto.getJigBarcode()+"的治具保养累计使用天数已达到保养警告天数";
                    messages.add(message);
                }
            }

            //治具达到最大次数或天数可以继续使用时,要发预警
            if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 1) {
                jigContinueUseWarning(eamJigBarcodeDto,eamJig,messages);
            }
        }


        //发送预警信息
        int sum = 0;
        for (String msg : messages){
            //调发送预警信息接口

            sum ++;
        }

        return sum;
    }

    public void jigContinueUseWarning(EamJigBarcodeDto eamJigBarcodeDto,EamJig eamJig,List<String> messages){
        String msg = "";
        boolean tag = false;

        if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentUsageTime(), eamJig.getMaxUsageTime())
                && eamJig.getMaxUsageTime() != 0) {
            if (eamJigBarcodeDto.getCurrentUsageTime().compareTo(eamJig.getMaxUsageTime()) == 0
                    || eamJigBarcodeDto.getCurrentUsageTime().compareTo(eamJig.getMaxUsageTime()) == 1) {
                tag = true;
            }
        }

        if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentUsageDays(), eamJig.getMaxUsageDays())
                && eamJig.getMaxUsageDays() != 0) {
            if (eamJigBarcodeDto.getCurrentUsageDays().compareTo(eamJig.getMaxUsageDays()) == 0
                    || eamJigBarcodeDto.getCurrentUsageDays().compareTo(eamJig.getMaxUsageDays()) == 1) {
                tag = true;
            }
        }

        if(tag){
            msg = "治具条码为"+eamJigBarcodeDto.getJigBarcode()+"的治具使用次数（天数）已达到最大使用次数（天数）";
            messages.add(msg);
        }

    }

}
