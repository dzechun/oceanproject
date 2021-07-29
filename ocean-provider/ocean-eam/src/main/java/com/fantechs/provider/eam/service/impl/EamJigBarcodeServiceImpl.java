package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigBarcode;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigBarcodeMapper;
import com.fantechs.provider.eam.mapper.EamJigBarcodeMapper;
import com.fantechs.provider.eam.service.EamJigBarcodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<EamJigBarcodeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigBarcodeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigBarcode record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
        int i = eamHtJigBarcodeMapper.insert(eamHtJigBarcode);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigBarcode entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
        eamHtJigBarcodeMapper.insert(eamHtJigBarcode);

        return eamJigBarcodeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
}
