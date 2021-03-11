package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtInventoryScrapMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryScrapDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryScrapMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryScrapService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/03/10.
 */
@Service
public class WmsInnerInventoryScrapServiceImpl extends BaseService<WmsInnerInventoryScrap> implements WmsInnerInventoryScrapService {

    @Resource
    private WmsInnerInventoryScrapMapper wmsInnerInventoryScrapMapper;
    @Resource
    private WmsInnerHtInventoryScrapMapper wmsInnerHtInventoryScrapMapper;
    @Resource
    private WmsInnerInventoryScrapDetMapper wmsInnerInventoryScrapDetMapper;

    @Override
    public List<WmsInnerInventoryScrapDto> findList(Map<String, Object> map) {
        return wmsInnerInventoryScrapMapper.findList(map);
    }

    @Override
    public List<WmsInnerHtInventoryScrap> findHtList(Map<String, Object> map) {
        return wmsInnerHtInventoryScrapMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInnerInventoryScrap wmsInnerInventoryScrap) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        int i = 0;

        wmsInnerInventoryScrap.setInventoryScrapCode(CodeUtils.getId("IS"));
        wmsInnerInventoryScrap.setCreateTime(new Date());
        wmsInnerInventoryScrap.setCreateUserId(user.getUserId());
        wmsInnerInventoryScrap.setModifiedTime(new Date());
        wmsInnerInventoryScrap.setModifiedUserId(user.getUserId());
        wmsInnerInventoryScrap.setInventoryScrapStatus(StringUtils.isEmpty(wmsInnerInventoryScrap.getInventoryScrapStatus()) ? 0 : wmsInnerInventoryScrap.getInventoryScrapStatus());
        wmsInnerInventoryScrap.setOrganizationId(user.getOrganizationId());
        i = wmsInnerInventoryScrapMapper.insertUseGeneratedKeys(wmsInnerInventoryScrap);

        //履历
        WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
        BeanUtils.copyProperties(wmsInnerInventoryScrap,wmsInnerHtInventoryScrap);
        wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

        for (WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet : wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets()) {

            wmsInnerInventoryScrapDet.setInventoryScrapId(wmsInnerInventoryScrap.getInventoryScrapId());
            wmsInnerInventoryScrapDet.setCreateTime(new Date());
            wmsInnerInventoryScrapDet.setCreateUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setModifiedTime(new Date());
            wmsInnerInventoryScrapDet.setModifiedUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setOrganizationId(user.getOrganizationId());
            i = wmsInnerInventoryScrapDetMapper.insertSelective(wmsInnerInventoryScrapDet);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsInnerInventoryScrap wmsInnerInventoryScrap) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i = 0;

        wmsInnerInventoryScrap.setOrganizationId(user.getOrganizationId());
        wmsInnerInventoryScrap.setModifiedUserId(user.getUserId());
        wmsInnerInventoryScrap.setModifiedTime(new Date());

        i = wmsInnerInventoryScrapMapper.updateByPrimaryKeySelective(wmsInnerInventoryScrap);

        //履历
        WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
        BeanUtils.copyProperties(wmsInnerInventoryScrap,wmsInnerHtInventoryScrap);
        wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

        //删除明细
        Example example = new Example(WmsInnerInventoryScrapDet.class);
        example.createCriteria().andEqualTo("inventoryScrapId",wmsInnerInventoryScrap.getInventoryScrapId());
        wmsInnerInventoryScrapDetMapper.deleteByExample(example);

        for (WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet : wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets()) {

            wmsInnerInventoryScrapDet.setInventoryScrapId(wmsInnerInventoryScrap.getInventoryScrapId());
            wmsInnerInventoryScrapDet.setCreateTime(new Date());
            wmsInnerInventoryScrapDet.setCreateUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setModifiedTime(new Date());
            wmsInnerInventoryScrapDet.setModifiedUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setOrganizationId(user.getOrganizationId());
            i = wmsInnerInventoryScrapDetMapper.insertSelective(wmsInnerInventoryScrapDet);
        }

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            WmsInnerInventoryScrap wmsInnerInventoryScrap = wmsInnerInventoryScrapMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerInventoryScrap)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
            BeanUtils.copyProperties(wmsInnerInventoryScrap,wmsInnerHtInventoryScrap);
            wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

            //删除明细
            Example example = new Example(WmsInnerInventoryScrapDet.class);
            example.createCriteria().andEqualTo("inventoryScrapId",wmsInnerInventoryScrap.getInventoryScrapId());
            wmsInnerInventoryScrapDetMapper.deleteByExample(example);
        }

        //删除盘存转报废单
        return wmsInnerInventoryScrapMapper.deleteByIds(ids);
    }
}
