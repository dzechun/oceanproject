package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;

import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderService;
import io.micrometer.core.instrument.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@Service
public class WmsOutPlanStockListOrderServiceImpl extends BaseService<WmsOutPlanStockListOrder> implements WmsOutPlanStockListOrderService {

    @Resource
    private WmsOutPlanStockListOrderMapper wmsOutPlanStockListOrderMapper;
    @Resource
    private WmsOutPlanStockListOrderDetMapper wmsOutPlanStockListOrderDetMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;

    @Override
    public List<WmsOutPlanStockListOrderDto> findList(SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        if(StringUtils.isEmpty(searchWmsOutPlanStockListOrder.getOrgId())){
            SysUser user=currentUser();
            searchWmsOutPlanStockListOrder.setOrgId(user.getOrganizationId());
        }
        return wmsOutPlanStockListOrderMapper.findList(searchWmsOutPlanStockListOrder);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        int num=1;
        SysUser user = currentUser();
        if(StringUtils.isEmpty(wmsOutPlanStockListOrderDto.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        String sysOrderTypeCode=wmsOutPlanStockListOrderDto.getSysOrderTypeCode();
        Byte sourceBigType=wmsOutPlanStockListOrderDto.getSourceBigType();
        String sourceSysOrderTypeCode=wmsOutPlanStockListOrderDto.getSourceSysOrderTypeCode();

        if(StringUtils.isEmpty(sysOrderTypeCode)){
            sysOrderTypeCode="OUT-PSLO";//单据类型 备料计划
            wmsOutPlanStockListOrderDto.setSysOrderTypeCode(sysOrderTypeCode);
        }
        if(StringUtils.isEmpty(sourceBigType)){
            sourceBigType=(byte)2;//来源类型 自建
            wmsOutPlanStockListOrderDto.setSourceBigType(sourceBigType);
        }
        if(StringUtils.isEmpty(sourceSysOrderTypeCode)){
            //自建的单据 单据来源类型为 生产订单
            wmsOutPlanStockListOrderDto.setSourceSysOrderTypeCode("MES-WO");
        }
        if(StringUtils.isEmpty(wmsOutPlanStockListOrderDto.getPlanStockListOrderCode())){
            //计划单号
            wmsOutPlanStockListOrderDto.setPlanStockListOrderCode(CodeUtils.getId("PSLO-"));
        }
        wmsOutPlanStockListOrderDto.setCreateTime(new Date());
        wmsOutPlanStockListOrderDto.setCreateUserId(user.getUserId());
        wmsOutPlanStockListOrderDto.setOrgId(user.getOrganizationId());
        wmsOutPlanStockListOrderDto.setIsDelete((byte) 1);
        wmsOutPlanStockListOrderDto.setStatus((byte) 1);
        num=wmsOutPlanStockListOrderMapper.insertUseGeneratedKeys(wmsOutPlanStockListOrderDto);

        List<WmsOutPlanStockListOrderDetDto> listOrderDetDtos=wmsOutPlanStockListOrderDto.getWmsOutPlanStockListOrderDetDtos();
        if(listOrderDetDtos.size()>0){
            for (WmsOutPlanStockListOrderDetDto listOrderDetDto : listOrderDetDtos) {
                MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderMapper.selectByPrimaryKey(listOrderDetDto.getWorkOrderId());
                if (StringUtils.isEmpty(mesPmWorkOrder)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(), "找不到相应的工单信息");
                }
                //自建的需要判断数量
                if(wmsOutPlanStockListOrderDto.getSourceBigType()==((byte) 2)) {
                    BigDecimal nowQty = new BigDecimal(0);
                    BigDecimal scheduleQty = new BigDecimal(0);
                    BigDecimal workOrderQty = new BigDecimal(0);
                    if (StringUtils.isNotEmpty(mesPmWorkOrder.getScheduledQty()))
                        scheduleQty = mesPmWorkOrder.getScheduledQty();//工单已排产数量

                    if (StringUtils.isNotEmpty(mesPmWorkOrder.getWorkOrderQty()))
                        workOrderQty = mesPmWorkOrder.getWorkOrderQty();//工单数量

//                    if (StringUtils.isNotEmpty(mesPmDailyPlanDet.getScheduleQty()))
//                        nowQty = mesPmDailyPlanDet.getFinishedQty();//本次排产数量

                    if (nowQty.compareTo(new BigDecimal(0)) != 1) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "排产数量必须大于0");
                    }
                    if ((nowQty.add(scheduleQty)).compareTo(workOrderQty) == 1) {
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "排产数量不能大于工单数量");
                    }
                }

                //新增明细
                listOrderDetDto.setPlanStockListOrderId(wmsOutPlanStockListOrderDto.getPlanStockListOrderId());
                listOrderDetDto.setCreateUserId(user.getUserId());
                listOrderDetDto.setCreateTime(new Date());
                listOrderDetDto.setIsDelete((byte) 1);
                listOrderDetDto.setOrgId(user.getOrganizationId());
                num = wmsOutPlanStockListOrderDetMapper.insertUseGeneratedKeys(listOrderDetDto);

            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        int num=1;

        return num;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
