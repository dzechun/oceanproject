package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.jinan.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidAssetMapper;
import com.fantechs.provider.guest.jinan.mapper.RfidBaseStationLogMapper;
import com.fantechs.provider.guest.jinan.mapper.RfidBaseStationMapper;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class RfidBaseStationLogServiceImpl extends BaseService<RfidBaseStationLog> implements RfidBaseStationLogService {

    @Resource
    private RfidBaseStationLogMapper rfidBaseStationLogMapper;
    @Resource
    private RfidBaseStationMapper rfidBaseStationMapper;
    @Resource
    private RfidAssetMapper rfidAssetMapper;

    @Override
    public List<RfidBaseStationLog> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidBaseStationLogMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int checkData(RfidBaseStationData rfidBaseStationData) {
        List<RfidBaseStationLog> logs = new LinkedList<>();

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("orgId",user.getOrganizationId());
        map.put("baseStationCode",rfidBaseStationData.getBaseStationCode());
        List<RfidBaseStation> rfidBaseStationList = rfidBaseStationMapper.findList(map);
        if(StringUtils.isEmpty(rfidBaseStationList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        RfidBaseStation rfidBaseStation = rfidBaseStationList.get(0);

        List<RfidBaseStationAssetData> submitAssetList = rfidBaseStationData.getList();//提交的数据
        List<RfidBaseStationReAsset> reAssetList = rfidBaseStation.getList();//绑定的数据
        List<String> submitAssetBarcodeList = submitAssetList.stream().map(RfidBaseStationAssetData::getAssetBarcode).collect(Collectors.toList());
        List<String> reAssetBarcodeList = reAssetList.stream().map(RfidBaseStationReAsset::getAssetBarcode).collect(Collectors.toList());

        List<String> lackList=new ArrayList(reAssetBarcodeList);
        List<String> commonList=new ArrayList(reAssetBarcodeList);
        List<String> surplusList=new ArrayList(submitAssetBarcodeList);
        lackList.removeAll(submitAssetBarcodeList);//缺少的rfid
        commonList.removeAll(lackList);//正常的rfid
        surplusList.removeAll(reAssetBarcodeList);//多余的rfid

        Example example = new Example(RfidAsset.class);
        if(StringUtils.isNotEmpty(lackList)){
            for (String s : lackList){
                RfidBaseStationLog rfidBaseStationLog = new RfidBaseStationLog();
                rfidBaseStationLog.setAreaName(rfidBaseStation.getAreaName());
                rfidBaseStationLog.setBaseStationName(rfidBaseStation.getBaseStationName());
                rfidBaseStationLog.setReadResult((byte)0);
                rfidBaseStationLog.setAssetBarcode(s);
                rfidBaseStationLog.setFeedbackContent("未读取到当前RFID");

                example.clear();
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("assetBarcode",s);
                RfidAsset rfidAsset = rfidAssetMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(rfidAsset)){
                    rfidBaseStationLog.setAssetName(rfidAsset.getAssetName());
                }

                //rfidBaseStationLog.setReadTime(new Date());

                rfidBaseStationLog.setCreateUserId(user.getUserId());
                rfidBaseStationLog.setCreateTime(new Date());
                rfidBaseStationLog.setModifiedUserId(user.getUserId());
                rfidBaseStationLog.setModifiedTime(new Date());
                rfidBaseStationLog.setStatus(StringUtils.isEmpty(rfidBaseStationLog.getStatus())?1: rfidBaseStationLog.getStatus());
                rfidBaseStationLog.setOrgId(user.getOrganizationId());
                logs.add(rfidBaseStationLog);
            }
        }
        if(StringUtils.isNotEmpty(commonList)){
            for (String s : commonList){
                RfidBaseStationLog rfidBaseStationLog = new RfidBaseStationLog();
                rfidBaseStationLog.setAreaName(rfidBaseStation.getAreaName());
                rfidBaseStationLog.setBaseStationName(rfidBaseStation.getBaseStationName());
                rfidBaseStationLog.setReadResult((byte)1);
                rfidBaseStationLog.setAssetBarcode(s);
                rfidBaseStationLog.setFeedbackContent("读取正常");

                example.clear();
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("assetBarcode",s);
                RfidAsset rfidAsset = rfidAssetMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(rfidAsset)){
                    rfidBaseStationLog.setAssetName(rfidAsset.getAssetName());
                }

                for (RfidBaseStationAssetData rfidBaseStationAssetData : submitAssetList){
                    if(rfidBaseStationAssetData.getAssetBarcode().equals(s)){
                        rfidBaseStationLog.setReadTime(rfidBaseStationAssetData.getReadTime());
                    }
                }

                rfidBaseStationLog.setCreateUserId(user.getUserId());
                rfidBaseStationLog.setCreateTime(new Date());
                rfidBaseStationLog.setModifiedUserId(user.getUserId());
                rfidBaseStationLog.setModifiedTime(new Date());
                rfidBaseStationLog.setStatus(StringUtils.isEmpty(rfidBaseStationLog.getStatus())?1: rfidBaseStationLog.getStatus());
                rfidBaseStationLog.setOrgId(user.getOrganizationId());
                logs.add(rfidBaseStationLog);
            }
        }
        if(StringUtils.isNotEmpty(surplusList)){
            for (String s : surplusList){
                RfidBaseStationLog rfidBaseStationLog = new RfidBaseStationLog();
                rfidBaseStationLog.setAreaName(rfidBaseStation.getAreaName());
                rfidBaseStationLog.setBaseStationName(rfidBaseStation.getBaseStationName());
                rfidBaseStationLog.setReadResult((byte)1);
                rfidBaseStationLog.setAssetBarcode(s);
                rfidBaseStationLog.setFeedbackContent("当前RFID不属于该基站");

                example.clear();
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("assetBarcode",s);
                RfidAsset rfidAsset = rfidAssetMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(rfidAsset)){
                    rfidBaseStationLog.setAssetName(rfidAsset.getAssetName());
                }

                for (RfidBaseStationAssetData rfidBaseStationAssetData : submitAssetList){
                    if(rfidBaseStationAssetData.getAssetBarcode().equals(s)){
                        rfidBaseStationLog.setReadTime(rfidBaseStationAssetData.getReadTime());
                    }
                }

                rfidBaseStationLog.setCreateUserId(user.getUserId());
                rfidBaseStationLog.setCreateTime(new Date());
                rfidBaseStationLog.setModifiedUserId(user.getUserId());
                rfidBaseStationLog.setModifiedTime(new Date());
                rfidBaseStationLog.setStatus(StringUtils.isEmpty(rfidBaseStationLog.getStatus())?1: rfidBaseStationLog.getStatus());
                rfidBaseStationLog.setOrgId(user.getOrganizationId());
                logs.add(rfidBaseStationLog);
            }
        }

        return rfidBaseStationLogMapper.insertList(logs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(RfidBaseStationLog record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        return rfidBaseStationLogMapper.insertSelective(record);
    }
}
