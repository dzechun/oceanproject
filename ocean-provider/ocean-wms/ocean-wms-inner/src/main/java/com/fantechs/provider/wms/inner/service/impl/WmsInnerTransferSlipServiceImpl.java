package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerTransferSlipDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerTransferSlipService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/05.
 */
@Service
public class WmsInnerTransferSlipServiceImpl extends BaseService<WmsInnerTransferSlip> implements WmsInnerTransferSlipService {

    @Resource
    private WmsInnerTransferSlipMapper wmsInnerTransferSlipMapper;
    @Resource
    private WmsInnerTransferSlipDetMapper wmsInnerTransferSlipDetMapper;

    @Override
    public List<WmsInnerTransferSlipDto> findList(Map<String, Object> map) {
        List<WmsInnerTransferSlipDto> wmsInnerTransferSlipDtos = wmsInnerTransferSlipMapper.findList(map);
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDtos)){
            for (WmsInnerTransferSlipDto wmsInnerTransferSlipDto : wmsInnerTransferSlipDtos) {
                SearchWmsInnerTransferSlipDet searchWmsInnerTransferSlipDet = new SearchWmsInnerTransferSlipDet();
                searchWmsInnerTransferSlipDet.setTransferSlipId(wmsInnerTransferSlipDto.getTransferSlipId());
                List<WmsInnerTransferSlipDetDto> list = wmsInnerTransferSlipDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerTransferSlipDet));
                if (StringUtils.isNotEmpty(list)){
                    wmsInnerTransferSlipDto.setWmsInnerTransferSlipDetDtos(list);
                }
            }
        }
        return wmsInnerTransferSlipDtos;
    }

    @Override
    public int save(WmsInnerTransferSlip wmsInnerTransferSlip) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsInnerTransferSlip.setTransferSlipCode(CodeUtils.getId("DB—"));
        wmsInnerTransferSlip.setCreateTime(new Date());
        wmsInnerTransferSlip.setCreateUserId(user.getUserId());
        wmsInnerTransferSlip.setModifiedTime(new Date());
        wmsInnerTransferSlip.setModifiedUserId(user.getUserId());
        wmsInnerTransferSlip.setTransferSlipTime(new Date());
        wmsInnerTransferSlip.setTransferSlipStatus((byte) 0);
        wmsInnerTransferSlip.setOrganizationId(user.getOrganizationId());

        //新增调拨单
        int i = wmsInnerTransferSlipMapper.insertUseGeneratedKeys(wmsInnerTransferSlip);

        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDet wmsInnerTransferSlipDet : wmsInnerTransferSlipDetDtos) {
                wmsInnerTransferSlipDet.setCreateTime(new Date());
                wmsInnerTransferSlipDet.setCreateUserId(user.getUserId());
                wmsInnerTransferSlipDet.setMaterialId(user.getUserId());
                wmsInnerTransferSlipDet.setModifiedTime(new Date());
                wmsInnerTransferSlipDet.setTransferSlipStatus((byte) 0);
                wmsInnerTransferSlipDet.setTransferSlipId(wmsInnerTransferSlip.getTransferSlipId());
                wmsInnerTransferSlipDets.add(wmsInnerTransferSlipDet);
            }
            if (StringUtils.isNotEmpty(wmsInnerTransferSlipDets)){
                wmsInnerTransferSlipDetMapper.insertList(wmsInnerTransferSlipDets);
            }
        }

        return i;
    }

    @Override
    public int update(WmsInnerTransferSlip wmsInnerTransferSlip) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsInnerTransferSlip.setTransferSlipTime(new Date());

        //更新调拨单
        int i = wmsInnerTransferSlipMapper.updateByPrimaryKeySelective(wmsInnerTransferSlip);

        //删除原调拨单明细
        Example example = new Example(WmsInnerTransferSlipDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
        wmsInnerTransferSlipMapper.deleteByExample(example);

        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDet wmsInnerTransferSlipDet : wmsInnerTransferSlipDetDtos) {
                wmsInnerTransferSlipDet.setTransferSlipId(wmsInnerTransferSlip.getTransferSlipId());
                wmsInnerTransferSlipDet.setModifiedTime(new Date());
                wmsInnerTransferSlipDet.setModifiedUserId(user.getUserId());
                wmsInnerTransferSlipDets.add(wmsInnerTransferSlipDet);
            }
            if (StringUtils.isNotEmpty(wmsInnerTransferSlipDets)){
                wmsInnerTransferSlipDetMapper.insertList(wmsInnerTransferSlipDets);
            }

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
            WmsInnerTransferSlip wmsInnerTransferSlip = wmsInnerTransferSlipMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerTransferSlip)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //删除调拨单明细
            Example example = new Example(WmsInnerTransferSlipDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
            wmsInnerTransferSlipMapper.deleteByExample(example);
        }

        //删除调拨单
        return wmsInnerTransferSlipDetMapper.deleteByIds(ids);

    }
}
