package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.eam.mapper.EamEquipmentBarcodeMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.service.EamEquipmentBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentBarcodeServiceImpl extends BaseService<EamEquipmentBarcode> implements EamEquipmentBarcodeService {

    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<EamEquipmentBarcode> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }

        return eamEquipmentBarcodeMapper.findList(map);
    }

    @Override
    public int plusCurrentUsageTime(Long equipmentBarCodeId, Integer num) {
        EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodeMapper.selectByPrimaryKey(equipmentBarCodeId);
        if (StringUtils.isEmpty(eamEquipmentBarcode)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        Integer currentUsageTime=0;
        if(StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageTime()))
            currentUsageTime=eamEquipmentBarcode.getCurrentUsageTime();

        eamEquipmentBarcode.setCurrentUsageTime(currentUsageTime+num);
        eamEquipmentBarcode.setEquipmentStatus((byte)4);//更新状态为生产中
        eamEquipmentBarcode.setModifiedTime(new Date());
        return eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
    }

    /**
     * 设备预警
     */
    public int equipmentWarning(){
        String message = "";
        List<String> messages = new ArrayList<>();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("IsEquipmentCanContinueUse");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        Map<String, Object> map = new HashMap<>();
        List<EamEquipmentBarcode> list = this.findList(map);

        for (EamEquipmentBarcode eamEquipmentBarcode : list){
            EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(eamEquipmentBarcode.getEquipmentId());

            //使用次数预警
            if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageTime(), eamEquipment.getWarningTime())
                    && eamEquipment.getWarningTime() != 0) {
                if (eamEquipmentBarcode.getCurrentUsageTime().compareTo(eamEquipment.getWarningTime()) == 0
                        || eamEquipmentBarcode.getCurrentUsageTime().compareTo(eamEquipment.getWarningTime()) == 1) {
                    message = "设备条码为"+eamEquipmentBarcode.getEquipmentBarcode()+"的设备使用次数已达到警告次数";
                    messages.add(message);
                }
            }

            //使用天数预警
            if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageDays(), eamEquipment.getWarningDays())
                    && eamEquipment.getWarningDays() != 0) {
                if (eamEquipmentBarcode.getCurrentUsageDays().compareTo(eamEquipment.getWarningDays()) == 0
                        || eamEquipmentBarcode.getCurrentUsageDays().compareTo(eamEquipment.getWarningDays()) == 1) {
                    message = "设备条码为"+eamEquipmentBarcode.getEquipmentBarcode()+"的设备使用天数已达到警告天数";
                    messages.add(message);
                }
            }

            //保养使用次数预警
            if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentMaintainUsageTime(), eamEquipment.getMaintainWarningTime())) {
                if (eamEquipmentBarcode.getCurrentMaintainUsageTime().compareTo(eamEquipment.getMaintainWarningTime()) == 0
                        || eamEquipmentBarcode.getCurrentMaintainUsageTime().compareTo(eamEquipment.getMaintainWarningTime()) == 1) {
                    message = "设备条码为"+eamEquipmentBarcode.getEquipmentBarcode()+"的设备保养累计使用次数已达到保养警告次数";
                    messages.add(message);
                }
            }

            //保养使用天数预警
            if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentMaintainUsageDays(), eamEquipment.getMaintainWarningDays())) {
                if (eamEquipmentBarcode.getCurrentMaintainUsageDays().compareTo(eamEquipment.getMaintainWarningDays()) == 0
                        || eamEquipmentBarcode.getCurrentMaintainUsageDays().compareTo(eamEquipment.getMaintainWarningDays()) == 1) {
                    message = "设备条码为"+eamEquipmentBarcode.getEquipmentBarcode()+"的设备保养累计使用天数已达到保养警告天数";
                    messages.add(message);
                }
            }

            //设备达到最大次数或天数可以继续使用时,要发预警
            if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 1) {
                equipmentContinueUseWarning(eamEquipmentBarcode,eamEquipment,messages);
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

    public void equipmentContinueUseWarning(EamEquipmentBarcode eamEquipmentBarcode,EamEquipment eamEquipment,List<String> messages){
        String msg = "";
        boolean tag = false;

        if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageTime(), eamEquipment.getMaxUsageTime())
                && eamEquipment.getMaxUsageTime() != 0) {
            if (eamEquipmentBarcode.getCurrentUsageTime().compareTo(eamEquipment.getMaxUsageTime()) == 0
                    || eamEquipmentBarcode.getCurrentUsageTime().compareTo(eamEquipment.getMaxUsageTime()) == 1) {
                tag = true;
            }
        }

        if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageDays(), eamEquipment.getMaxUsageDays())
                && eamEquipment.getMaxUsageDays() != 0) {
            if (eamEquipmentBarcode.getCurrentUsageDays().compareTo(eamEquipment.getMaxUsageDays()) == 0
                    || eamEquipmentBarcode.getCurrentUsageDays().compareTo(eamEquipment.getMaxUsageDays()) == 1) {
                tag = true;
            }
        }

        if(tag){
            msg = "设备条码为"+eamEquipmentBarcode.getEquipmentBarcode()+"的设备使用次数（天数）已达到最大使用次数（天数）";
            messages.add(msg);
        }

    }
}
