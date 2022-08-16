package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJo;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutDespatchOrderReJoMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutDespatchOrderReJoReDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderReJoService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */
@Service
public class WmsOutDespatchOrderReJoServiceImpl extends BaseService<WmsOutDespatchOrderReJo> implements WmsOutDespatchOrderReJoService {

    @Resource
    private WmsOutDespatchOrderReJoMapper wmsOutDespatchOrderReJoMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private WmsOutDespatchOrderReJoReDetMapper wmsOutDespatchOrderReJoReDetMapper;

    @Override
    public List<WmsOutDespatchOrderReJoDto> findList(SearchWmsOutDespatchOrderReJo searchWmsOutDespatchOrderReJo) {
        return wmsOutDespatchOrderReJoMapper.findList(searchWmsOutDespatchOrderReJo);
    }

    @Override
    public int save(WmsOutDespatchOrderReJo record) {
        SysUser sysUser = currentUser();
        int num = 0;
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderId(record.getJobOrderId());
        ResponseEntity<List<WmsInnerJobOrderDetDto>> responseEntity = innerFeignApi.findList(searchWmsInnerJobOrderDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        List<WmsInnerJobOrderDetDto> list = responseEntity.getData();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        wmsOutDespatchOrderReJoMapper.insertUseGeneratedKeys(record);
        for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : list) {
            WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet = new WmsOutDespatchOrderReJoReDetDto();
            wmsOutDespatchOrderReJoReDet.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
            wmsOutDespatchOrderReJoReDet.setDespatchOrderReJoId(record.getDespatchOrderReJoId());
            wmsOutDespatchOrderReJoReDet.setCreateTime(new Date());
            wmsOutDespatchOrderReJoReDet.setCreateUserId(sysUser.getUserId());
            wmsOutDespatchOrderReJoReDet.setModifiedTime(new Date());
            wmsOutDespatchOrderReJoReDet.setModifiedUserId(sysUser.getUserId());
            wmsOutDespatchOrderReJoReDet.setOrgId(sysUser.getOrganizationId());
            num +=wmsOutDespatchOrderReJoReDetMapper.insertSelective(wmsOutDespatchOrderReJoReDet);
        }
        return num;
    }

    @Override
    public int update(WmsOutDespatchOrderReJo entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());

        int num = 0;
        Example example = new Example(WmsOutDespatchOrderReJoReDet.class);
        example.createCriteria().andEqualTo("despatchOrderReJoId",entity.getDespatchOrderReJoId());
        wmsOutDespatchOrderReJoReDetMapper.deleteByExample(example);

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderId(entity.getJobOrderId());
        ResponseEntity<List<WmsInnerJobOrderDetDto>> responseEntity = innerFeignApi.findList(searchWmsInnerJobOrderDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        List<WmsInnerJobOrderDetDto> list = responseEntity.getData();
        entity.setCreateTime(new Date());
        entity.setCreateUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setOrgId(sysUser.getOrganizationId());
        wmsOutDespatchOrderReJoMapper.insertUseGeneratedKeys(entity);
        for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : list) {
            WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet = new WmsOutDespatchOrderReJoReDetDto();
            wmsOutDespatchOrderReJoReDet.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
            wmsOutDespatchOrderReJoReDet.setDespatchOrderReJoId(entity.getDespatchOrderReJoId());
            wmsOutDespatchOrderReJoReDet.setCreateTime(new Date());
            wmsOutDespatchOrderReJoReDet.setCreateUserId(sysUser.getUserId());
            wmsOutDespatchOrderReJoReDet.setModifiedTime(new Date());
            wmsOutDespatchOrderReJoReDet.setModifiedUserId(sysUser.getUserId());
            wmsOutDespatchOrderReJoReDet.setOrgId(sysUser.getOrganizationId());
            num +=wmsOutDespatchOrderReJoReDetMapper.insertSelective(wmsOutDespatchOrderReJoReDet);
        }
        return num;
    }

    /**
     * 获取当前登陆用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
