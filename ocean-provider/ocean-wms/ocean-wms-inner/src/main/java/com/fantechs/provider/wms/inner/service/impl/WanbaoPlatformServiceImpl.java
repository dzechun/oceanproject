package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WanbaoPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2022/06/27.
 */
@Service
@Slf4j
public class WanbaoPlatformServiceImpl extends BaseService<WanbaoPlatform> implements WanbaoPlatformService {

    @Resource
    private WanbaoPlatformMapper wanbaoPlatformMapper;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WanbaoPlatformDetMapper wanbaoPlatformDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WanbaoPlatform> findList(Map<String, Object> map) {
        return wanbaoPlatformMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int doScan(ScanBarCodeOut scanBarCodeOut) {
        /**
         * 1、清洗条码
         * 条码类型有：厂内码、销售条码、客户条码（包含三星SN条码）
         */
        // 1、1  分割条码
        if (StringUtils.isEmpty(scanBarCodeOut.getBarCode())){
            // 停产亮灯
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "获取读头条码数据为空");
        }
        log.info("=========== 读头扫码，条码：" + scanBarCodeOut.getBarCode());
        String[] barcodeArr = scanBarCodeOut.getBarCode().split(",");

        WanbaoPlatform wanbaoPlatform = wanbaoPlatformMapper.selectByPrimaryKey(scanBarCodeOut.getPlatformId());
        //获取合格库存状态
//        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
//        List<BaseInventoryStatus> baseInventoryStatuses = baseFeignApi.findList(searchBaseInventoryStatus).getData();
        if(StringUtils.isEmpty(wanbaoPlatform.getJobOrderId()) || wanbaoPlatform.getUsageStatus()==1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"月台未绑定拣货单");
        }
        if(!scanBarCodeOut.getJobOrderId().equals(wanbaoPlatform.getJobOrderId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"月台绑定单据不匹配请切换出货单据");
        }
        /**
         * 2、 业务场景如下
         * 2、1  一个条码，先判断是否为厂内码，是则往下走流程。不是，则判断是不是三星客户条码。都不是则停线亮灯
         * 2、2  两个条码，先判断是否存在厂内码，不存在则停线亮灯。若存在则判断另一个条码是否销售条码，不是则认为是客户条码
         * 2、3  三个条码，厂内码和销售条码是否都存在，只要其中一个不存在就停线亮灯，若存在，剩下的条码则认为是客户条码
         * 2、4  五个条码，厂内码和销售条码是否都存在，只要其中一个不存在就停线亮灯，若存在，剩下三个条码则认为是客户条码。
         *       另，取其中位数最小的条码去匹配特征码表中的特征码字段（通过物料匹配）
         *
         * 3、 条码清洗依据如下：
         *      条码长度为23位且前12位是物料编码 -> 厂内码
         *      前缀为391-或391D的 -> 销售条码
         *      生产订单明细下的客户条码 -> 客户条码
         */

        // 清洗条码
        OutCleanBarcodeDto cleanBarcodeDto = new OutCleanBarcodeDto();
        WanbaoPlatformDet wanbaoPlatformDet = new WanbaoPlatformDetDto();
        boolean flag = true;
        if (barcodeArr.length == 1){
            Example example = new Example(WmsInnerInventoryDet.class);
            Example.Criteria criteria = example.createCriteria();
            // 判断是厂内码还是三星客户条码
            if (barcodeArr[0].length() == 23){
                criteria.andEqualTo("barcode", barcodeArr[0]);
            }else {
                // 三星客户条码情况，读头扫到的条码是少一位的，用模糊匹配
                criteria.andLike("customerBarcode", barcodeArr[0] + "%");
            }
            List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
            if (!wmsInnerInventoryDets.isEmpty()){
                if (wmsInnerInventoryDets.get(0).getBarcodeStatus()!=3){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"条码状态未在库");
                }
                if(wmsInnerInventoryDets.get(0).getLockStatus()==1){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"条码已被锁");
                }
//                String invName = baseInventoryStatuses.stream().filter(y->y.getInventoryStatusId().equals(wmsInnerInventoryDets.get(0).getInventoryStatusId())).collect(Collectors.toList()).get(0).getInventoryStatusName();
//                if(!invName.equals("合格")){
//                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"产品"+invName);
//                }
                cleanBarcodeDto.setOrderBarCode(wmsInnerInventoryDets.get(0).getBarcode());
                wanbaoPlatformDet.setBarcode(cleanBarcodeDto.getOrderBarCode());
                wanbaoPlatformDet.setMaterialId(wmsInnerInventoryDets.get(0).getMaterialId());
                wanbaoPlatformDet.setInventoryDetId(wmsInnerInventoryDets.get(0).getInventoryDetId());
                wanbaoPlatformDet.setStroageId(wmsInnerInventoryDets.get(0).getStorageId());
                cleanBarcodeDto.setPo(wmsInnerInventoryDets.get(0).getOption4());
                cleanBarcodeDto.setSaleCode(wmsInnerInventoryDets.get(0).getOption3());
                flag = false;
            }
        }
        if (flag){
            for (String str : barcodeArr){
                if (str.length() == 23){
                    Example example = new Example(MesSfcBarcodeProcess.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("barcode", str);
                    List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
                    if (!wmsInnerInventoryDets.isEmpty()){
                        if (StringUtils.isNotEmpty(cleanBarcodeDto.getOrderBarCode())){
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "已扫条码中存在两个或以上厂内码，不允许操作");
                        }
                        if (wmsInnerInventoryDets.get(0).getBarcodeStatus()!=3){
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"条码状态未在库");
                        }
                        if(wmsInnerInventoryDets.get(0).getLockStatus()==1){
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"条码已被锁");
                        }
//                        String invName = baseInventoryStatuses.stream().filter(y->y.getInventoryStatusId().equals(wmsInnerInventoryDets.get(0).getInventoryStatusId())).collect(Collectors.toList()).get(0).getInventoryStatusName();
//                        if(!invName.equals("合格")){
//                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"产品"+invName);
//                        }
                        cleanBarcodeDto.setOrderBarCode(wmsInnerInventoryDets.get(0).getBarcode());
                        wanbaoPlatformDet.setMaterialId(wmsInnerInventoryDets.get(0).getMaterialId());
                        wanbaoPlatformDet.setInventoryDetId(wmsInnerInventoryDets.get(0).getInventoryDetId());
                        wanbaoPlatformDet.setStroageId(wmsInnerInventoryDets.get(0).getStorageId());
                        cleanBarcodeDto.setPo(wmsInnerInventoryDets.get(0).getOption4());
                        cleanBarcodeDto.setSaleCode(wmsInnerInventoryDets.get(0).getOption3());
                    }
                }else {
                    // 清洗销售订单条码/客户条码是否重复
                    if (str.contains("391-") || str.contains("391D")){
                        if (StringUtils.isNotEmpty(cleanBarcodeDto.getSalesBarcode())){
                            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "已扫条码中存在两个或以上销售条码，不允许操作");
                        }
                        cleanBarcodeDto.setSalesBarcode(str);
                    }else {
                        Example example = new Example(WmsInnerInventoryDet.class);
                        example.createCriteria().andEqualTo("customerBarcode",str);
                        List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
                        if (wmsInnerInventoryDets != null){
                            cleanBarcodeDto.setCutsomerBarcode(str);
                        }
                    }
                }
            }
        }
        if(StringUtils.isEmpty(cleanBarcodeDto.getOrderBarCode())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未扫描到厂内码或厂内码不存在");
        }
        //判断当前条码产品是否属于当前单据
        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId",scanBarCodeOut.getJobOrderId()).andEqualTo("materialId",wanbaoPlatformDet.getMaterialId());
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInnerJobOrderDetMapper.selectByExample(example);
        if(wmsInnerJobOrderDets.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"当前出货产品不属于当前单据");
        }
        //校验销售编码
        if(wanbaoPlatform.getIfSaleOrder()==1){
            if(StringUtils.isNotEmpty(cleanBarcodeDto.getSaleCode())) {
                wmsInnerJobOrderDets = wmsInnerJobOrderDets.stream().filter(y ->StringUtils.isNotEmpty(y.getOption2()) && y.getOption2().equals(cleanBarcodeDto.getSaleCode())).collect(Collectors.toList());
                if (wmsInnerJobOrderDets.size() < 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码销售编码与出货单销售编码不一致【出库单无销售编码】");
                }
            }else {
                wmsInnerJobOrderDets = wmsInnerJobOrderDets.stream().filter(y -> StringUtils.isEmpty(y.getOption2())).collect(Collectors.toList());
                if (wmsInnerJobOrderDets.size() < 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码销售编码与出货单销售编码不一致【库存无销售编码】");
                }
            }
        }
        if(wanbaoPlatform.getIfPo()==1){
            if(StringUtils.isNotEmpty(cleanBarcodeDto.getPo())) {
                wmsInnerJobOrderDets = wmsInnerJobOrderDets.stream().filter(y -> StringUtils.isNotEmpty(y.getOption3()) && y.getOption3().equals(cleanBarcodeDto.getPo())).collect(Collectors.toList());
                if (wmsInnerJobOrderDets.size()<1){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"条码PO与出货单PO不一致【出货单无PO】");
                }
            }else {
                wmsInnerJobOrderDets = wmsInnerJobOrderDets.stream().filter(y -> StringUtils.isEmpty(y.getOption3())).collect(Collectors.toList());
                if (wmsInnerJobOrderDets.size()<1){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"条码PO与出货单PO不一致【库存无PO】");
                }
            }
        }
        //判断是否重复扫码
        example = new Example(WanbaoPlatformDet.class);
        example.createCriteria().andEqualTo("barcode",cleanBarcodeDto.getOrderBarCode());
        int count = wanbaoPlatformDetMapper.selectCountByExample(example);
        if(count>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"重复扫码");
        }
        wanbaoPlatformDet.setJobOrderDetId(wmsInnerJobOrderDets.get(0).getJobOrderDetId());
        wanbaoPlatformDet.setBarcode(cleanBarcodeDto.getOrderBarCode());
        wanbaoPlatformDet.setCustomerBarcode(cleanBarcodeDto.getCutsomerBarcode());
        wanbaoPlatformDet.setSalesBarcode(cleanBarcodeDto.getSalesBarcode());
        wanbaoPlatformDet.setPlatformId(scanBarCodeOut.getPlatformId());
        wanbaoPlatformDet.setCreateTime(new Date());
        wanbaoPlatformDet.setModifiedTime(new Date());
        return wanbaoPlatformDetMapper.insertSelective(wanbaoPlatformDet);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int bindingPlatform(WanbaoPlatform wanbaoPlatform) {
        if(StringUtils.isEmpty(wanbaoPlatform.getPlatformId(),wanbaoPlatform.getJobOrderId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        WanbaoPlatform old = wanbaoPlatformMapper.selectByPrimaryKey(wanbaoPlatform.getPlatformId());
//        if(old.getUsageStatus()==2){
//            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"月台未释放");
//        }
        old.setUsageStatus((byte)2);
        old.setJobOrderId(wanbaoPlatform.getJobOrderId());
        old.setModifiedTime(new Date());
        return super.update(old);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int submit(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arryId = ids.split(",");
        int num=0;
        for (String s : arryId) {
            WanbaoPlatform old = wanbaoPlatformMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(old)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            Example example = new Example(WanbaoPlatformDet.class);
            example.createCriteria().andEqualTo("platformDetId");
            List<WanbaoPlatformDet> wanbaoPlatformDets = wanbaoPlatformDetMapper.selectByExample(example);
            for (WanbaoPlatformDet wanbaoPlatformDet : wanbaoPlatformDets) {
                //扣减库存
            }
            //清空月台扫描记录 释放月台
            wanbaoPlatformDetMapper.deleteByExample(example);
            //默认校验PO
            //old.setIfPo((byte)1);
            //默认校验销售编码
            //old.setIfSaleOrder((byte)1);
            old.setJobOrderId(null);
            old.setModifiedUserId(sysUser.getUserId());
            old.setModifiedTime(new Date());
            old.setUsageStatus((byte)1);
            num+=wanbaoPlatformMapper.updateByPrimaryKeySelective(old);
        }
        return num;
    }

    @Override
    public List<WanbaoPlatformDetDto> findDetList(Map<String, Object> map) {
        return wanbaoPlatformDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int delete(String platformDetId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WanbaoPlatformDet wanbaoPlatformDet = wanbaoPlatformDetMapper.selectByPrimaryKey(platformDetId);
        if(StringUtils.isEmpty(wanbaoPlatformDet)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        return wanbaoPlatformDetMapper.deleteByIds(platformDetId);
    }
}
