package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.jinan.Import.RfidBaseStationImport;
import com.fantechs.common.base.general.entity.jinan.*;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtBaseStation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.mapper.*;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationService;
import org.springframework.beans.BeanUtils;
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
public class RfidBaseStationServiceImpl extends BaseService<RfidBaseStation> implements RfidBaseStationService {

    @Resource
    private RfidBaseStationMapper rfidBaseStationMapper;
    @Resource
    private RfidHtBaseStationMapper rfidHtBaseStationMapper;
    @Resource
    private RfidBaseStationReAssetMapper rfidBaseStationReAssetMapper;
    @Resource
    private RfidAreaMapper rfidAreaMapper;
    @Resource
    private RfidAssetMapper rfidAssetMapper;


    @Override
    public List<RfidBaseStation> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidBaseStationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(RfidBaseStation record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        codeIfRepeat(record,user);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        rfidBaseStationMapper.insertUseGeneratedKeys(record);

        /*List<RfidBaseStationReAsset> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (RfidBaseStationReAsset rfidBaseStationReAsset : list){
                //RFID是否重复
                Example example = new Example(RfidBaseStationReAsset.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("assetId", rfidBaseStationReAsset.getAssetId())
                        .andEqualTo("orgId", user.getOrganizationId());
                RfidBaseStationReAsset rfidBaseStationReAsset1 = rfidBaseStationReAssetMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(rfidBaseStationReAsset1)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"RFID序列号为"+rfidBaseStationReAsset1.getAssetBarcode()+"的资产已绑定其他基站");
                }

                rfidBaseStationReAsset.setBaseStationId(record.getBaseStationId());
                rfidBaseStationReAsset.setCreateUserId(user.getUserId());
                rfidBaseStationReAsset.setCreateTime(new Date());
                rfidBaseStationReAsset.setModifiedUserId(user.getUserId());
                rfidBaseStationReAsset.setModifiedTime(new Date());
                rfidBaseStationReAsset.setStatus(StringUtils.isEmpty(rfidBaseStationReAsset.getStatus())?1: rfidBaseStationReAsset.getStatus());
                rfidBaseStationReAsset.setOrgId(user.getOrganizationId());
            }
            rfidBaseStationReAssetMapper.insertList(list);
        }*/

        RfidHtBaseStation rfidHtBaseStation = new RfidHtBaseStation();
        BeanUtils.copyProperties(record, rfidHtBaseStation);
        int i = rfidHtBaseStationMapper.insertSelective(rfidHtBaseStation);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(RfidBaseStation entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        codeIfRepeat(entity,user);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        rfidBaseStationMapper.updateByPrimaryKeySelective(entity);

        //删除原RFID信息
        /*Example example1 = new Example(RfidBaseStationReAsset.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("baseStationId", entity.getBaseStationId());
        rfidBaseStationReAssetMapper.deleteByExample(example1);

        List<RfidBaseStationReAsset> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (RfidBaseStationReAsset rfidBaseStationReAsset : list){
                //RFID是否重复
                Example example = new Example(RfidBaseStationReAsset.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("assetId", rfidBaseStationReAsset.getAssetId())
                        .andEqualTo("orgId", user.getOrganizationId());
                RfidBaseStationReAsset rfidBaseStationReAsset1 = rfidBaseStationReAssetMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(rfidBaseStationReAsset1)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"RFID序列号为"+rfidBaseStationReAsset1.getAssetBarcode()+"的资产已绑定其他基站");
                }

                rfidBaseStationReAsset.setBaseStationId(entity.getBaseStationId());
                rfidBaseStationReAsset.setCreateUserId(user.getUserId());
                rfidBaseStationReAsset.setCreateTime(new Date());
                rfidBaseStationReAsset.setModifiedUserId(user.getUserId());
                rfidBaseStationReAsset.setModifiedTime(new Date());
                rfidBaseStationReAsset.setStatus(StringUtils.isEmpty(rfidBaseStationReAsset.getStatus())?1: rfidBaseStationReAsset.getStatus());
                rfidBaseStationReAsset.setOrgId(user.getOrganizationId());
            }
            rfidBaseStationReAssetMapper.insertList(list);
        }*/


        RfidHtBaseStation rfidHtBaseStation = new RfidHtBaseStation();
        BeanUtils.copyProperties(entity, rfidHtBaseStation);
        int i = rfidHtBaseStationMapper.insertSelective(rfidHtBaseStation);

        return i;
    }

    private void codeIfRepeat(RfidBaseStation baseStation,SysUser user){
        //判断编码是否重复
        Example example = new Example(RfidBaseStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseStationCode", baseStation.getBaseStationCode())
                .andEqualTo("orgId", user.getOrganizationId());
        if(StringUtils.isNotEmpty(baseStation.getBaseStationId())){
            criteria.andNotEqualTo("baseStationId",baseStation.getBaseStationId());
        }
        RfidBaseStation rfidBaseStation = rfidBaseStationMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidBaseStation)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("baseStationMac", baseStation.getBaseStationMac())
                .andEqualTo("orgId", user.getOrganizationId());
        if(StringUtils.isNotEmpty(baseStation.getBaseStationId())){
            criteria1.andNotEqualTo("baseStationId",baseStation.getBaseStationId());
        }
        RfidBaseStation rfidBaseStation1 = rfidBaseStationMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidBaseStation1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"基站MAC重复");
        }

        if (StringUtils.isNotEmpty(baseStation.getAreaId())) {
            example.clear();
            Example.Criteria criteria2 = example.createCriteria();
            criteria2.andEqualTo("areaId", baseStation.getAreaId())
                    .andEqualTo("orgId", user.getOrganizationId());
            if(StringUtils.isNotEmpty(baseStation.getBaseStationId())){
                criteria2.andNotEqualTo("baseStationId",baseStation.getBaseStationId());
            }
            RfidBaseStation rfidBaseStation2 = rfidBaseStationMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(rfidBaseStation2)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "该区域已绑定其他基站");
            }
        }

        if (StringUtils.isNotEmpty(baseStation.getBaseStationIp())) {
            example.clear();
            Example.Criteria criteria3 = example.createCriteria();
            criteria3.andEqualTo("baseStationIp", baseStation.getBaseStationIp())
                    .andEqualTo("orgId", user.getOrganizationId());
            if(StringUtils.isNotEmpty(baseStation.getBaseStationId())){
                criteria3.andNotEqualTo("baseStationId",baseStation.getBaseStationId());
            }
            RfidBaseStation rfidBaseStation3 = rfidBaseStationMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(rfidBaseStation3)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "基站IP重复");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<RfidHtBaseStation> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            RfidBaseStation rfidBaseStation = rfidBaseStationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(rfidBaseStation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            RfidHtBaseStation rfidHtBaseStation = new RfidHtBaseStation();
            BeanUtils.copyProperties(rfidBaseStation,rfidHtBaseStation);
            list.add(rfidHtBaseStation);

            //删除原RFID信息
            Example example1 = new Example(RfidBaseStationReAsset.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("baseStationId", rfidBaseStation.getBaseStationId());
            rfidBaseStationReAssetMapper.deleteByExample(example1);
        }
        rfidHtBaseStationMapper.insertList(list);

        return rfidBaseStationMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<RfidBaseStationImport> rfidBaseStationImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<RfidBaseStation> list = new LinkedList<>();
        LinkedList<RfidHtBaseStation> htList = new LinkedList<>();
        LinkedList<RfidBaseStationImport> rfidBaseStationImportList = new LinkedList<>();

        for (int i = 0; i < rfidBaseStationImports.size(); i++) {
            RfidBaseStationImport rfidBaseStationImport = rfidBaseStationImports.get(i);

            String baseStationCode = rfidBaseStationImport.getBaseStationCode();
            String baseStationName = rfidBaseStationImport.getBaseStationName();
            String baseStationMac = rfidBaseStationImport.getBaseStationMac();

            if (StringUtils.isEmpty(
                    baseStationCode,baseStationName,baseStationMac
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(RfidBaseStation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("baseStationCode", baseStationCode)
                    .andEqualTo("orgId", user.getOrganizationId());
            RfidBaseStation rfidBaseStation = rfidBaseStationMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(rfidBaseStation)){
                fail.add(i+4);
                continue;
            }

            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("baseStationMac", rfidBaseStationImport.getBaseStationMac())
                    .andEqualTo("orgId", user.getOrganizationId());
            RfidBaseStation rfidBaseStation1 = rfidBaseStationMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(rfidBaseStation1)){
                fail.add(i+4);
                continue;
            }

            if (StringUtils.isNotEmpty(rfidBaseStationImport.getBaseStationIp())) {
                example.clear();
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andEqualTo("baseStationIp", rfidBaseStationImport.getBaseStationIp())
                        .andEqualTo("orgId", user.getOrganizationId());
                RfidBaseStation rfidBaseStation2 = rfidBaseStationMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(rfidBaseStation2)) {
                    fail.add(i + 4);
                    continue;
                }
            }

            //区域信息
            String areaCode = rfidBaseStationImport.getAreaCode();
            if(StringUtils.isNotEmpty(areaCode)){
                Example example1 = new Example(RfidArea.class);
                Example.Criteria criteria3 = example1.createCriteria();
                criteria3.andEqualTo("areaCode", areaCode)
                        .andEqualTo("orgId", user.getOrganizationId());
                RfidArea rfidArea = rfidAreaMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(rfidArea)){
                    fail.add(i+4);
                    continue;
                }
                rfidBaseStationImport.setAreaId(rfidArea.getAreaId());
            }

            if (StringUtils.isNotEmpty(rfidBaseStationImport.getAreaId())) {
                example.clear();
                Example.Criteria criteria4 = example.createCriteria();
                criteria4.andEqualTo("areaId", rfidBaseStationImport.getAreaId())
                        .andEqualTo("orgId", user.getOrganizationId());
                RfidBaseStation rfidBaseStation4 = rfidBaseStationMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(rfidBaseStation4)) {
                    fail.add(i + 4);
                    continue;
                }
            }

            //资产信息
            String assetCode = rfidBaseStationImport.getAssetCode();
            if(StringUtils.isNotEmpty(assetCode)){
                Example example2 = new Example(RfidAsset.class);
                Example.Criteria criteria5 = example2.createCriteria();
                criteria5.andEqualTo("orgId",user.getOrganizationId())
                        .andEqualTo("assetCode",assetCode);
                RfidAsset rfidAsset = rfidAssetMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(rfidAsset)){
                    fail.add(i+4);
                    continue;
                }
                rfidBaseStationImport.setAssetId(rfidAsset.getAssetId());
            }

            rfidBaseStationImportList.add(rfidBaseStationImport);
        }

        if (StringUtils.isNotEmpty(rfidBaseStationImportList)){
            HashMap<String, List<RfidBaseStationImport>> map = rfidBaseStationImportList.stream().collect(Collectors.groupingBy(RfidBaseStationImport::getBaseStationCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                //主表
                List<RfidBaseStationImport> rfidBaseStationImports1 = map.get(code);
                RfidBaseStation rfidBaseStation = new RfidBaseStation();
                BeanUtils.copyProperties(rfidBaseStationImports1.get(0),rfidBaseStation);
                rfidBaseStation.setCreateTime(new Date());
                rfidBaseStation.setCreateUserId(user.getUserId());
                rfidBaseStation.setModifiedTime(new Date());
                rfidBaseStation.setModifiedUserId(user.getUserId());
                rfidBaseStation.setOrgId(user.getOrganizationId());
                rfidBaseStation.setStatus((byte)1);
                success += rfidBaseStationMapper.insertUseGeneratedKeys(rfidBaseStation);

                RfidHtBaseStation rfidHtBaseStation = new RfidHtBaseStation();
                BeanUtils.copyProperties(rfidBaseStation,rfidHtBaseStation);
                htList.add(rfidHtBaseStation);

                //资产列表
                List<RfidBaseStationReAsset> rfidBaseStationReAssetList = new LinkedList<>();
                for (RfidBaseStationImport rfidBaseStationImport : rfidBaseStationImports1) {
                    RfidBaseStationReAsset rfidBaseStationReAsset = new RfidBaseStationReAsset();
                    BeanUtils.copyProperties(rfidBaseStationImport, rfidBaseStationReAsset);
                    rfidBaseStationReAsset.setBaseStationId(rfidBaseStation.getBaseStationId());
                    rfidBaseStationReAsset.setStatus((byte) 1);
                    rfidBaseStationReAsset.setCreateUserId(user.getUserId());
                    rfidBaseStationReAsset.setCreateTime(new Date());
                    rfidBaseStationReAsset.setModifiedUserId(user.getUserId());
                    rfidBaseStationReAsset.setModifiedTime(new Date());
                    rfidBaseStationReAsset.setOrgId(user.getOrganizationId());
                    rfidBaseStationReAssetList.add(rfidBaseStationReAsset);
                }
                if(StringUtils.isNotEmpty(rfidBaseStationReAssetList)) {
                    rfidBaseStationReAssetMapper.insertList(rfidBaseStationReAssetList);
                }
            }
            rfidHtBaseStationMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
