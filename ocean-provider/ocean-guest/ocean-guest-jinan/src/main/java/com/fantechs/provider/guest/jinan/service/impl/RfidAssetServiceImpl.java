package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.jinan.Import.RfidAssetImport;
import com.fantechs.common.base.general.entity.jinan.RfidAsset;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtAsset;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidAssetMapper;
import com.fantechs.provider.guest.jinan.mapper.RfidHtAssetMapper;
import com.fantechs.provider.guest.jinan.service.RfidAssetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class RfidAssetServiceImpl extends BaseService<RfidAsset> implements RfidAssetService {

    @Resource
    private RfidAssetMapper rfidAssetMapper;
    @Resource
    private RfidHtAssetMapper rfidHtAssetMapper;

    @Override
    public List<RfidAsset> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidAssetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(RfidAsset record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(RfidAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetCode", record.getAssetCode())
                .andEqualTo("orgId", user.getOrganizationId());
        RfidAsset rfidAsset = rfidAssetMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidAsset)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if (StringUtils.isNotEmpty(record.getAssetBarcode())) {
            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("assetBarcode", record.getAssetBarcode())
                    .andEqualTo("orgId", user.getOrganizationId());
            RfidAsset rfidAsset1 = rfidAssetMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(rfidAsset1)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "RFID序列号重复");
            }
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        rfidAssetMapper.insertUseGeneratedKeys(record);

        RfidHtAsset rfidHtAsset = new RfidHtAsset();
        BeanUtils.copyProperties(record, rfidHtAsset);
        int i = rfidHtAssetMapper.insertSelective(rfidHtAsset);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(RfidAsset entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(RfidAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetCode", entity.getAssetCode())
                .andEqualTo("orgId", user.getOrganizationId())
                .andNotEqualTo("assetId",entity.getAssetId());
        RfidAsset rfidAsset = rfidAssetMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidAsset)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if (StringUtils.isNotEmpty(entity.getAssetBarcode())) {
            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("assetBarcode", entity.getAssetBarcode())
                    .andEqualTo("orgId", user.getOrganizationId())
                    .andNotEqualTo("assetId", entity.getAssetId());
            RfidAsset rfidAsset1 = rfidAssetMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(rfidAsset1)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "RFID序列号重复");
            }
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        rfidAssetMapper.updateByPrimaryKeySelective(entity);

        RfidHtAsset rfidHtAsset = new RfidHtAsset();
        BeanUtils.copyProperties(entity, rfidHtAsset);
        int i = rfidHtAssetMapper.insertSelective(rfidHtAsset);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<RfidAssetImport> rfidAssetImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<RfidAsset> list = new LinkedList<>();
        LinkedList<RfidHtAsset> htList = new LinkedList<>();
        for (int i = 0; i < rfidAssetImports.size(); i++) {
            RfidAssetImport rfidAssetImport = rfidAssetImports.get(i);

            String assetCode = rfidAssetImport.getAssetCode();
            String assetName = rfidAssetImport.getAssetName();
            if (StringUtils.isEmpty(
                    assetCode,assetName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(RfidAsset.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("assetCode",assetCode);
            if (StringUtils.isNotEmpty(rfidAssetMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            String assetBarcode = rfidAssetImport.getAssetBarcode();
            if (StringUtils.isNotEmpty(assetBarcode)) {
                example.clear();
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("assetBarcode", assetBarcode)
                        .andEqualTo("orgId", user.getOrganizationId());
                if (StringUtils.isNotEmpty(rfidAssetMapper.selectOneByExample(example))) {
                    fail.add(i+4);
                    continue;
                }
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (RfidAsset rfidAsset : list) {
                    if (rfidAsset.getAssetCode().equals(assetCode)
                            ||(StringUtils.isNotEmpty(assetBarcode,rfidAsset.getAssetBarcode())
                            &&rfidAsset.getAssetBarcode().equals(assetBarcode))){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            RfidAsset rfidAsset = new RfidAsset();
            BeanUtils.copyProperties(rfidAssetImport, rfidAsset);
            rfidAsset.setCreateTime(new Date());
            rfidAsset.setCreateUserId(user.getUserId());
            rfidAsset.setModifiedTime(new Date());
            rfidAsset.setModifiedUserId(user.getUserId());
            rfidAsset.setStatus((byte)1);
            rfidAsset.setOrgId(user.getOrganizationId());
            list.add(rfidAsset);
        }

        if (StringUtils.isNotEmpty(list)) {
            success = rfidAssetMapper.insertList(list);

            for (RfidAsset rfidAsset : list) {
                RfidHtAsset rfidHtAsset = new RfidHtAsset();
                BeanUtils.copyProperties(rfidAsset, rfidHtAsset);
                htList.add(rfidHtAsset);
            }
            if (StringUtils.isNotEmpty(htList)) {
                rfidHtAssetMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
