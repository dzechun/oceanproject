package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerJobOrderServiceImpl extends BaseService<WmsInnerJobOrder> implements WmsInnerJobOrderService {
    @Resource
    private WmsInnerJobOrderMapper wmsInPutawayOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInPutawayOrderDetMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        return wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder);
    }

    /**
     * 自动分配
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> list = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wms : list) {
                if(StringUtils.isEmpty(wms)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //推荐库位
                Long storageId = wmsInPutawayOrderMapper.findStorage(wms.getMaterialId());
                storageId = storageId==null?wmsInPutawayOrderMapper.SelectStorage():storageId;
                if(StringUtils.isEmpty(storageId)){
                    throw new BizErrorException("位查询到推荐库位");
                }
                num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                        .jobOrderDetId(wms.getJobOrderDetId())
                        .inStorageId(storageId)
                        .distributionQty(wms.getPlanQty())
                        .modifiedUserId(sysUser.getUserId())
                        .modifiedTime(new Date())
                        .build());
                //库位容量减1
                baseFeignApi.minusSurplusCanPutSalver(wms.getInStorageId(),1);
            }
        }
        return num;
    }

    /**
     * 手动分配
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int handDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = currentUser();
        int num=0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue()>wmsInPutawayOrderDet.getPlanQty().doubleValue()){
                throw new BizErrorException("分配数量不能大于计划数量");
            }
            if(StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) &&wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==-1){
                //分配中
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setDistributionQty(new BigDecimal("0"));
                wms.setPlanQty(new BigDecimal(wmsInPutawayOrderDet.getPlanQty().doubleValue()-wmsInPutawayOrderDet.getDistributionQty().doubleValue()));
                wms.setOrderStatus((byte)2);
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wms);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //分配完成
                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            if(StringUtils.isEmpty(dto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(dto.stream().filter(li->li.getOrderStatus()==(byte)3).collect(Collectors.toList()).size()==dto.size()){
                //更新表头状态
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)3)
                        .build());
            }else{
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)2)
                        .build());
            }
        }
        return num;
    }

    /**
     * 取消分配
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            if(wmsInPutawayOrderDet.getOrderStatus()==(byte)4){
                throw new BizErrorException("单据作业中 无法取消");
            }
            wmsInPutawayOrderDet.setDistributionQty(new BigDecimal("0.00"));
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setOrderStatus((byte)1);
            num +=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);

            //恢复库存占用容量
            baseFeignApi.plusSurplusCanPutSalver(wmsInPutawayOrderDet.getInStorageId(),1);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据确认已完成");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInPutawayOrderDetMapper.selectByExample(example);
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                if(StringUtils.isEmpty(id)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                        .jobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId())
                        .orderStatus((byte)5)
                        .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                        .modifiedUserId(sysUser.getUserId())
                        .modifiedTime(new Date())
                        .build());

                //更改表头为作业完成状态
                wmsInPutawayOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .orderStatus((byte)5)
                        .jobOrderId(wmsInnerJobOrderDet.getJobOrderId())
                        .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                        .build());
                //更改库存为正常状态
                SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
                WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
                SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                //更改库存
                num = this.updateInventory(wmsInnerJobOrderDto,wmsInnerJobOrderDetDto);

                //反写完工入库单
                inFeignApi.writeQty(WmsInAsnOrderDet.builder()
                        .putawayQty(wmsInnerJobOrderDet.getActualQty())
                        .asnOrderDetId(wmsInnerJobOrderDet.getSourceDetId())
                        .build());
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInPutawayOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("单据确认已完成");
            }

            if(wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty())==-1){
                wmsInPutawayOrderDet.setOrderStatus((byte)4);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num+=wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //确认完成
                wmsInPutawayOrderDet.setOrderStatus((byte)5);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }

//            wmsInPutawayOrderDet.setOrderStatus((byte)5);
//            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
//            wmsInPutawayOrderDet.setModifiedTime(new Date());
//            num = wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            //更改库存
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            //更改库存
            num = this.updateInventory(wmsInnerJobOrderDto,wmsInnerJobOrderDetDto.get(0));

            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setOrderStatus((byte)5);
            int oCount = wmsInPutawayOrderDetMapper.selectCount(wmsInnerJobOrderDet);


            if(oCount==count){
                WmsInnerJobOrder ws = new WmsInnerJobOrder();
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte)5);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                ws.setWorkEndtTime(new Date());
                num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            }else{
                WmsInnerJobOrder ws = new WmsInnerJobOrder();
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte)4);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
            }
        }
        return num;
    }

    @Override
    public String checkBarcode(String barCode) {
        //查询是否存在条码
        Map<String,Object> map = InBarcodeUtil.checkBarCode(barCode);
        if(StringUtils.isEmpty(map)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        return null;
    }

    /**
     * PDA上架作业扫描库位上架
     * @param storageCode
     * @param jobOrderDetId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerJobOrderDet scanStorageBackQty(String storageCode, Long jobOrderDetId) {
        SysUser sysUser = currentUser();
        //通过储位编码查询储位id
        ResponseEntity<List<BaseStorage>> list = baseFeignApi.findList(SearchBaseStorage.builder()
                .storageCode(storageCode)
                .codeQueryMark((byte)1)
                .build());
        if(StringUtils.isEmpty(list.getData())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"库位查询失败");
        }
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInPutawayOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        BaseStorage baseStorage = list.getData().get(0);
        int num = wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                .jobOrderDetId(jobOrderDetId)
                .inStorageId(baseStorage.getStorageId())
                .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                .orderStatus((byte)5)
                .modifiedTime(new Date())
                .modifiedUserId(sysUser.getUserId())
                .build());
        if(num==0){
            throw new BizErrorException("上架失败");
        }


        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInPutawayOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDto = wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        //更改库存
        num = this.updateInventory(wmsInnerJobOrderDto,wmsInnerJobOrderDetDto.get(0));


        WmsInnerJobOrderDet wms= new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInPutawayOrderDetMapper.selectCount(wms);
        wms.setOrderStatus((byte)5);
        int oCount = wmsInPutawayOrderDetMapper.selectCount(wms);


        if(oCount==count){
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)5);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }else{
            WmsInnerJobOrder ws = new WmsInnerJobOrder();
            ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)4);
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if(StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())){
                ws.setWorkStartTime(new Date());
            }
            num +=wmsInPutawayOrderMapper.updateByPrimaryKeySelective(ws);
        }
        return wmsInnerJobOrderDet;
    }

    @Override
    public WmsInnerJobOrder packageAutoAdd(WmsInnerJobOrder wmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        wmsInnerJobOrder.setJobOrderCode(CodeUtils.getId("WORK-"));
        wmsInnerJobOrder.setCreateTime(new Date());
        wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
        wmsInnerJobOrder.setModifiedTime(new Date());
        wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(wmsInnerJobOrder);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInPutawayOrderDet.builder()
                    .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                    .createTime(new Date())
                    .createUserId(sysUser.getUserId())
                    .modifiedTime(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .build());

            WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                    .asnOrderId(wmsInnerJobOrder.getSourceOrderId())
                    .build()).getData().get(0);
            Example example = new Example(WmsInnerInventory.class);
            example.createCriteria().andEqualTo("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
            wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsInnerInventorys.setJobStatus((byte)2);
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        return wmsInnerJobOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerJobOrder record) {
        SysUser sysUser = currentUser();
        record.setJobOrderCode(CodeUtils.getId("WORK-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = wmsInPutawayOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : record.getWmsInPutawayOrderDets()) {
            wmsInPutawayOrderDet.setJobOrderId(record.getJobOrderId());
            wmsInPutawayOrderDet.setCreateTime(new Date());
            wmsInPutawayOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInPutawayOrderDet.setModifiedTime(new Date());
            wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInPutawayOrderDetMapper.insert(wmsInPutawayOrderDet);

            WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                    .asnOrderId(record.getSourceOrderId())
                    .build()).getData().get(0);
            Example example = new Example(WmsInnerInventory.class);
            example.createCriteria().andEqualTo("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsInnerInventorys = new WmsInnerInventory();
            wmsInnerInventorys.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsInnerInventorys.setJobStatus((byte)2);
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrderDet wmsInPutawayOrderDet =  wmsInPutawayOrderDetMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(s)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("putawayOrderId",s);
            wmsInPutawayOrderDetMapper.deleteByExample(example);
        }
        return wmsInPutawayOrderMapper.deleteByIds(ids);
    }

    /**
     * 库存
     * @param wmsInnerJobOrderDto
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int updateInventory(WmsInnerJobOrderDto wmsInnerJobOrderDto,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        SysUser sysUser = currentUser();
        WmsInAsnOrderDto wmsInAsnOrderDto = inFeignApi.findList(SearchWmsInAsnOrder.builder()
                .asnOrderId(wmsInnerJobOrderDto.getSourceOrderId())
                .build()).getData().get(0);

        WmsInAsnOrderDetDto wmsInAsnOrderDetDto = inFeignApi.findDetList(SearchWmsInAsnOrderDet.builder()
                .asnOrderDetId(wmsInnerJobOrderDetDto.getSourceDetId())
                .build()).getData().get(0);

        //旧
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInAsnOrderDto.getAsnCode()).andEqualTo("materialId",wmsInAsnOrderDetDto.getMaterialId());
        if(!StringUtils.isEmpty(wmsInAsnOrderDetDto.getBatchCode())){
            criteria.andEqualTo("batchCode",wmsInAsnOrderDetDto.getBatchCode());
        }
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerJobOrderDetDto.getActualQty()));
        int num = wmsInnerInventoryMapper.updateByExampleSelective(wmsIn, example);

        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrderDto.getJobOrderCode()).andEqualTo("materialId",wmsInAsnOrderDetDto.getMaterialId());
        if(!StringUtils.isEmpty(wmsInAsnOrderDetDto.getBatchCode())){
            criteria1.andEqualTo("batchCode",wmsInAsnOrderDetDto.getBatchCode());
        }
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //添加库存
            wmsInnerInventory.setRelevanceOrderCode(wmsInnerJobOrderDto.getJobOrderCode());
            wmsInnerInventory.setPackingQty(wmsInnerJobOrderDetDto.getActualQty());
            wmsInnerInventory.setJobStatus((byte)3);
            wmsInnerInventory.setInventoryId(null);
            return wmsInnerInventoryMapper.insertSelective(wmsInnerInventory);
        }else{
            wmsIn = new WmsInnerInventory();
            wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().add(wmsInnerJobOrderDetDto.getActualQty()));
            return wmsInnerInventoryMapper.updateByExampleSelective(wmsIn,example);
        }
    }

    /**
     * 获取当前登录用户
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
