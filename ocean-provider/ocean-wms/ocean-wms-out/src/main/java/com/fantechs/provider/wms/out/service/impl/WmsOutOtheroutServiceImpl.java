package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtherout;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutOtheroutDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtOtheroutMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutOtheroutMapper;
import com.fantechs.provider.wms.out.service.WmsOutOtheroutDetService;
import com.fantechs.provider.wms.out.service.WmsOutOtheroutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class WmsOutOtheroutServiceImpl extends BaseService<WmsOutOtherout> implements WmsOutOtheroutService {

    @Resource
    private WmsOutOtheroutMapper wmsOutOtheroutMapper;
    @Resource
    private WmsOutHtOtheroutMapper wmsOutHtOtheroutMapper;
    @Resource
    private WmsOutOtheroutDetService wmsOutOtheroutDetService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutOtherout wmsOutOtherout) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutOtherout.setOtheroutCode(CodeUtils.getId("OO-"));
        wmsOutOtherout.setCreateUserId(user.getUserId());
        wmsOutOtherout.setCreateTime(new Date());
        wmsOutOtherout.setModifiedUserId(user.getUserId());
        wmsOutOtherout.setModifiedTime(new Date());
        wmsOutOtherout.setStatus(StringUtils.isEmpty(wmsOutOtherout.getStatus())?1:wmsOutOtherout.getStatus());

        int i = wmsOutOtheroutMapper.insertUseGeneratedKeys(wmsOutOtherout);

        List<WmsOutOtheroutDet> wmsOutOtheroutDets = wmsOutOtherout.getWmsOutOtheroutDets();
        for (WmsOutOtheroutDet wmsOutOtheroutDet : wmsOutOtheroutDets) {
            if (StringUtils.isEmpty(wmsOutOtheroutDet.getMaterialId(),wmsOutOtherout.getOtheroutId())){
                throw new BizErrorException(ErrorCodeEnum.GL99990100);
            }
            wmsOutOtheroutDet.setOtheroutId(wmsOutOtherout.getOtheroutId());
        }
        wmsOutOtheroutDetService.batchSave(wmsOutOtherout.getWmsOutOtheroutDets());

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutOtherout wmsOutOtherout) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutOtherout.setModifiedTime(new Date());
        wmsOutOtherout.setModifiedUserId(user.getUserId());

        //批量更新其他出库单明细
        List<WmsOutOtheroutDet> wmsOutOtheroutDets = wmsOutOtherout.getWmsOutOtheroutDets();
        if (StringUtils.isNotEmpty(wmsOutOtheroutDets)){
            wmsOutOtheroutDetService.batchUpdate(wmsOutOtheroutDets);
        }
        return wmsOutOtheroutMapper.updateByPrimaryKeySelective(wmsOutOtherout);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        Example example = new Example(WmsOutOtheroutDet.class);
        for (String id : idArray) {
            WmsOutOtherout wmsOutOtherout = wmsOutOtheroutMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutOtherout)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("otheroutId",id);
            List<WmsOutOtheroutDet> wmsOutOtheroutDets = wmsOutOtheroutDetService.selectByExample(example);
            if (StringUtils.isNotEmpty(wmsOutOtheroutDets)){
                wmsOutOtheroutDetService.batchDelete(wmsOutOtheroutDets);
            }
        }

        return wmsOutOtheroutMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsOutOtheroutDto> findList(Map<String, Object> map) {
        List<WmsOutOtheroutDto> wmsOutOtheroutDtos = wmsOutOtheroutMapper.findList(map);

        for (WmsOutOtheroutDto wmsOutOtheroutDto : wmsOutOtheroutDtos) {
            SearchWmsOutOtheroutDet searchWmsOutOtheroutDet = new SearchWmsOutOtheroutDet();
            searchWmsOutOtheroutDet.setOtheroutId(wmsOutOtheroutDto.getOtheroutId());
            List<WmsOutOtheroutDetDto> list = wmsOutOtheroutDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutOtheroutDet));
            wmsOutOtheroutDto.setWmsOutOtheroutDetDtoList(list);
        }

        return wmsOutOtheroutDtos;
    }

    @Override
    public List<WmsOutHtOtherout> findHTList(Map<String, Object> map) {
        return wmsOutHtOtheroutMapper.findHTList(map);
    }
}
