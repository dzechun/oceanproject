package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderMapper;
import com.fantechs.provider.wms.in.mapper.WmsInInPlanOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInInPlanOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class WmsInInPlanOrderServiceImpl extends BaseService<WmsInInPlanOrder> implements WmsInInPlanOrderService {

    @Resource
    private WmsInInPlanOrderMapper wmsInInPlanOrderMapper;
    @Resource
    private WmsInHtInPlanOrderMapper wmsInHtInPlanOrderMapper;
    @Resource
    private WmsInInPlanOrderDetMapper wmsInInPlanOrderDetMapper;
    @Resource
    private WmsInHtInPlanOrderDetMapper wmsInHtInPlanOrderDetMapper;

    @Override
    public List<WmsInInPlanOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return wmsInInPlanOrderMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInInPlanOrderDto wmsInInPlanOrderDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();


        //预约数量校验
        Example example = new Example(WmsInInPlanOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appointStartTime",wmsInInPlanOrderDto.getInPlanOrderCode());
       List<WmsInInPlanOrder> wmsInInPlanOrders = wmsInInPlanOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(wmsInInPlanOrders))
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"入库计划编码重复");

        wmsInInPlanOrderDto.setMakeOrderUserId(user.getUserId());
        wmsInInPlanOrderDto.setCreateUserId(user.getUserId());
        wmsInInPlanOrderDto.setCreateTime(new Date());
        wmsInInPlanOrderDto.setModifiedUserId(user.getUserId());
        wmsInInPlanOrderDto.setModifiedTime(new Date());
        wmsInInPlanOrderDto.setStatus(StringUtils.isEmpty(wmsInInPlanOrderDto.getStatus())?1: wmsInInPlanOrderDto.getStatus());
        wmsInInPlanOrderDto.setOrgId(user.getOrganizationId());
        wmsInInPlanOrderDto.setIsDelete((byte)1);
        wmsInInPlanOrderDto.setOrderStatus((byte)1);
        int i =wmsInInPlanOrderMapper.insertUseGeneratedKeys(wmsInInPlanOrderDto);

        //保存履历表
        WmsInHtInPlanOrder wmsInHtInPlanOrder = new WmsInHtInPlanOrder();
        BeanUtils.copyProperties(wmsInInPlanOrderDto, wmsInHtInPlanOrder);
        wmsInHtInPlanOrderMapper.insertSelective(wmsInHtInPlanOrder);

        if(StringUtils.isNotEmpty(wmsInInPlanOrderDto.getWmsInInPlanOrderDetDtos())) {
            List<WmsInInPlanOrderDetDto> list = new ArrayList<>();
            List<WmsInHtInPlanOrderDet> htList = new ArrayList<>();
            for (WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto : wmsInInPlanOrderDto.getWmsInInPlanOrderDetDtos()) {

                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPlanQty()) || wmsInInPlanOrderDetDto.getPlanQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"计划数量需大于0");
                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPutawayQty()) || wmsInInPlanOrderDetDto.getPutawayQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"上架数量需大于0");
                if(wmsInInPlanOrderDetDto.getPlanQty().compareTo(wmsInInPlanOrderDetDto.getPutawayQty()) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"上架数量不能大于计划数量");


                wmsInInPlanOrderDetDto.setCreateUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setCreateTime(new Date());
                wmsInInPlanOrderDetDto.setModifiedUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setModifiedTime(new Date());
                wmsInInPlanOrderDetDto.setStatus(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getStatus()) ? 1 : wmsInInPlanOrderDetDto.getStatus());
                wmsInInPlanOrderDetDto.setOrgId(user.getOrganizationId());
                wmsInInPlanOrderDetDto.setInPlanOrderId(wmsInInPlanOrderDto.getInPlanOrderId());
                list.add(wmsInInPlanOrderDetDto);
                WmsInHtInPlanOrderDet wmsInHtInPlanOrderDet = new WmsInHtInPlanOrderDet();
                BeanUtils.copyProperties(wmsInInPlanOrderDetDto, wmsInHtInPlanOrderDet);
                htList.add(wmsInHtInPlanOrderDet);
            }
            if (StringUtils.isNotEmpty(list)) wmsInInPlanOrderDetMapper.insertList(list);
            if (StringUtils.isNotEmpty(htList)) wmsInHtInPlanOrderDetMapper.insertList(htList);
        }

        return i;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInInPlanOrderDto wmsInInPlanOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(WmsInInPlanOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inPlanOrderCode",wmsInInPlanOrderDto.getInPlanOrderCode())
                .andNotEqualTo("inPlanOrderId",wmsInInPlanOrderDto.getInPlanOrderId());
        List<WmsInInPlanOrder> srmInAsnOrders = wmsInInPlanOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(srmInAsnOrders)) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"单号已存在，请勿重复添加");

        wmsInInPlanOrderDto.setModifiedTime(new Date());
        wmsInInPlanOrderDto.setModifiedUserId(user.getUserId());
        int i = wmsInInPlanOrderMapper.updateByPrimaryKeySelective(wmsInInPlanOrderDto);

        //保存履历表
        WmsInHtInPlanOrder wmsInHtInPlanOrder = new WmsInHtInPlanOrder();
        BeanUtils.copyProperties(wmsInInPlanOrderDto, wmsInHtInPlanOrder);
        wmsInHtInPlanOrderMapper.insertSelective(wmsInHtInPlanOrder);


        //保存详情表
        //更新原有明细
        ArrayList<Long> idList = new ArrayList<>();
        List<WmsInInPlanOrderDetDto> list = wmsInInPlanOrderDto.getWmsInInPlanOrderDetDtos();
        if(StringUtils.isNotEmpty(list)) {
            for (WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto : list) {
                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPlanQty()) || wmsInInPlanOrderDetDto.getPlanQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"计划数量需大于0");
                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPutawayQty()) || wmsInInPlanOrderDetDto.getPutawayQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"上架数量需大于0");
                if(wmsInInPlanOrderDetDto.getPlanQty().compareTo(wmsInInPlanOrderDetDto.getPutawayQty()) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"上架数量不能大于计划数量");

                if (StringUtils.isNotEmpty(wmsInInPlanOrderDetDto.getInPlanOrderDetId())) {
                    wmsInInPlanOrderDetMapper.updateByPrimaryKey(wmsInInPlanOrderDetDto);
                    idList.add(wmsInInPlanOrderDetDto.getInPlanOrderDetId());
                }
            }
        }

        //删除更新之外的明细
        Example example1 = new Example(WmsInInPlanOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inPlanOrderId", wmsInInPlanOrderDto.getInPlanOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("inPlanOrderDetId", idList);
        }
        wmsInInPlanOrderDetMapper.deleteByExample(example1);

        //新增剩余的明细
        if(StringUtils.isNotEmpty(list)){
            List<WmsInInPlanOrderDetDto> addlist = new ArrayList<>();
            for (WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto  : list){
                if (idList.contains(wmsInInPlanOrderDetDto.getInPlanOrderDetId())) {
                    continue;
                }
                wmsInInPlanOrderDetDto.setInPlanOrderId(wmsInInPlanOrderDto.getInPlanOrderId());
                wmsInInPlanOrderDetDto.setCreateUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setCreateTime(new Date());
                wmsInInPlanOrderDetDto.setModifiedUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setModifiedTime(new Date());
                wmsInInPlanOrderDetDto.setStatus(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getStatus())?1: wmsInInPlanOrderDetDto.getStatus());
                wmsInInPlanOrderDetDto.setOrgId(user.getOrganizationId());
                addlist.add(wmsInInPlanOrderDetDto);
            }
            wmsInInPlanOrderDetMapper.insertList(addlist);
        }
        return i;
    }








    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsInInPlanOrder> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
