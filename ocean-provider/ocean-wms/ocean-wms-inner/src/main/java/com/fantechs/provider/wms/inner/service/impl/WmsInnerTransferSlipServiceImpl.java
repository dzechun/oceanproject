package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerTransferSlipDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtTransferSlipMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerTransferSlipService;
import io.micrometer.core.instrument.search.Search;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private WmsInnerHtTransferSlipMapper wmsInnerHtTransferSlipMapper;
    @Resource
    private InFeignApi inFeignApi;

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
        if (StringUtils.isEmpty(wmsInnerTransferSlip.getTransferSlipStatus())){
            wmsInnerTransferSlip.setTransferSlipStatus((byte) 0);
        }
        wmsInnerTransferSlip.setOrganizationId(user.getOrganizationId());
        if (wmsInnerTransferSlip.getOrderType() == 0){
            //库内调拨的操作人和处理人相同
            wmsInnerTransferSlip.setProcessorUserId(user.getUserId());
        }

        //新增调拨单
        int i = wmsInnerTransferSlipMapper.insertUseGeneratedKeys(wmsInnerTransferSlip);

        WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
        BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
        wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDet wmsInnerTransferSlipDet : wmsInnerTransferSlipDetDtos) {
                if (StringUtils.isEmpty(wmsInnerTransferSlipDet.getPlanCartonQty())){
                    throw new BizErrorException("计划调拨箱数必须大于0");
                }

                if (wmsInnerTransferSlipDet.getPlanCartonQty().compareTo(wmsInnerTransferSlipDet.getCartonQuantity()) == 1){
                    throw new BizErrorException("计划调拨箱数不能大于箱数");
                }

                //通过栈板码获取计划包箱规格
                String palletCode = wmsInnerTransferSlipDet.getPalletCode();
                SearchMesPackageManagerListDTO searchMesPackageManagerListDTO = new SearchMesPackageManagerListDTO();
                searchMesPackageManagerListDTO.setIsFindChildren(true);
                searchMesPackageManagerListDTO.setBarcode(palletCode);
                List<MesPackageManagerDTO> mesPackageManagerDTOS = inFeignApi.list(searchMesPackageManagerListDTO).getData();
                if (StringUtils.isEmpty(mesPackageManagerDTOS)){
                    throw new BizErrorException("获取包箱信息失败");
                }
                //设置计划调拨总数
                wmsInnerTransferSlipDet.setPlanTotalQty(wmsInnerTransferSlipDet.getPlanCartonQty().multiply(mesPackageManagerDTOS.get(0).getPackageSpecificationQuantity()));
                //计划调拨总数不能大于总数
                if (wmsInnerTransferSlipDet.getPlanTotalQty().compareTo(wmsInnerTransferSlipDet.getTotal()) == 1){
                    throw new BizErrorException("计划调拨总数不能大于总数");
                }
                wmsInnerTransferSlipDet.setCreateTime(new Date());
                wmsInnerTransferSlipDet.setCreateUserId(user.getUserId());
                wmsInnerTransferSlipDet.setModifiedUserId(user.getUserId());
                wmsInnerTransferSlipDet.setModifiedTime(new Date());
                if (StringUtils.isEmpty(wmsInnerTransferSlipDet.getTransferSlipStatus())){
                    wmsInnerTransferSlipDet.setTransferSlipStatus((byte) 0);
                }
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
        wmsInnerTransferSlip.setOrganizationId(user.getOrganizationId());
        if (wmsInnerTransferSlip.getOrderType() == 0){
            //库内调拨的操作人和处理人相同
            wmsInnerTransferSlip.setProcessorUserId(user.getUserId());
        }

        //更新调拨单
        int i = wmsInnerTransferSlipMapper.updateByPrimaryKeySelective(wmsInnerTransferSlip);

        WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
        BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
        wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

        //删除原调拨单明细
        Example example = new Example(WmsInnerTransferSlipDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
        wmsInnerTransferSlipDetMapper.deleteByExample(example);

        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDet wmsInnerTransferSlipDet : wmsInnerTransferSlipDetDtos) {
                if (StringUtils.isEmpty(wmsInnerTransferSlipDet.getPlanCartonQty())){
                    throw new BizErrorException("计划调拨箱数必须大于0");
                }

                if (wmsInnerTransferSlipDet.getPlanCartonQty().compareTo(wmsInnerTransferSlipDet.getCartonQuantity()) == 1){
                    throw new BizErrorException("计划调拨箱数不能大于箱数");
                }

                //通过栈板码获取计划包箱规格
                String palletCode = wmsInnerTransferSlipDet.getPalletCode();
                SearchMesPackageManagerListDTO searchMesPackageManagerListDTO = new SearchMesPackageManagerListDTO();
                searchMesPackageManagerListDTO.setIsFindChildren(true);
                searchMesPackageManagerListDTO.setBarcode(palletCode);
                List<MesPackageManagerDTO> mesPackageManagerDTOS = inFeignApi.list(searchMesPackageManagerListDTO).getData();
                if (StringUtils.isEmpty(mesPackageManagerDTOS)){
                    throw new BizErrorException("获取包箱信息失败");
                }
                //设置计划调拨总数
                wmsInnerTransferSlipDet.setPlanTotalQty(wmsInnerTransferSlipDet.getPlanCartonQty().multiply(mesPackageManagerDTOS.get(0).getPackageSpecificationQuantity()));
                //计划调拨总数不能大于总数
                if (wmsInnerTransferSlipDet.getPlanTotalQty().compareTo(wmsInnerTransferSlipDet.getTotal()) == 1){
                    throw new BizErrorException("计划调拨总数不能大于总数");
                }
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

        Example example = new Example(WmsInnerTransferSlipDet.class);
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            example.clear();
            WmsInnerTransferSlip wmsInnerTransferSlip = wmsInnerTransferSlipMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerTransferSlip)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
            BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
            wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

            //删除调拨单明细
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
            wmsInnerTransferSlipDetMapper.deleteByExample(example);
        }

        //删除调拨单
        return wmsInnerTransferSlipMapper.deleteByIds(ids);

    }
}
