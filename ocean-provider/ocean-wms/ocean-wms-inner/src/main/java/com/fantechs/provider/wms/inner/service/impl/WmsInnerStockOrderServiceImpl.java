package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInventoryVerificationDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInventoryVerification;
import com.fantechs.common.base.general.entity.wms.inner.WmsInventoryVerificationDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */
@Service
public class WmsInnerStockOrderServiceImpl extends BaseService<WmsInventoryVerification> implements WmsInnerStockOrderService {

    @Resource
    private WmsInnerStockOrderMapper wmsInventoryVerificationMapper;
    @Resource
    private WmsInnerStockOrderDetMapper wmsInventoryVerificationDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public List<WmsInventoryVerificationDto> findList(Map<String, Object> map) {
        return wmsInventoryVerificationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInventoryVerification record) {
        SysUser sysUser = currentUser();
        record.setInventoryVerificationCode(CodeUtils.getId("INPD-"));
        record.setStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = wmsInventoryVerificationMapper.insertUseGeneratedKeys(record);
        //储位盘点/全盘
        if(record.getInventoryVerificationType()==(byte)1 && record.getInventoryVerificationType()==(byte)3){
            if(record.getInventoryVerificationType()==(byte)1&&record.getMaxStorageCount()> record.getStorageList().size()){
                throw new BizErrorException("所选储位数不能大于最大储位数");
            }
            //盘点类型：储位盘点
            //查询库位下的所以库存货品
            if(record.getInventoryVerificationType()==(byte)1&&StringUtils.isEmpty(record)){
                throw new BizErrorException("储位不能未空");
            }
            List<WmsInventoryVerificationDet> wmsInventoryVerificationDets = this.findInvGoods(record.getInventoryVerificationType(),record.getInventoryVerificationId(), record.getStorageList());
            int res = wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
            if(res<0){
                throw new BizErrorException("新增盘点单失败");
            }
        }else if(record.getInventoryVerificationType()==(byte)2){
            //货品
            for (WmsInventoryVerificationDet inventoryVerificationDet : record.getInventoryVerificationDets()) {
                inventoryVerificationDet.setInventoryVerificationId(record.getInventoryVerificationId());
                inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
                inventoryVerificationDet.setCreateTime(new Date());
                inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
                inventoryVerificationDet.setModifiedTime(new Date());
            }
            int res = wmsInventoryVerificationDetMapper.insertList(record.getInventoryVerificationDets());
            if(res<0){
                throw new BizErrorException("新增盘点单失败");
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInventoryVerification entity) {
        SysUser sysUser = currentUser();
        if(entity.getType()==(byte)2 && entity.getStatus()!=2 || entity.getStatus()!=3){
            throw new BizErrorException(entity.getStatus()==1?"单据未激活，无法登记":"无法登记");
        }
        if(entity.getType()==(byte) 2){
            entity.setStatus((byte)3);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        //删除原有数据
        Example example = new Example(WmsInventoryVerificationDet.class);
        example.createCriteria().andEqualTo("inventoryVerificationId",entity.getInventoryVerificationId());
        wmsInventoryVerificationDetMapper.deleteByExample(example);
        for (WmsInventoryVerificationDet inventoryVerificationDet : entity.getInventoryVerificationDets()) {
            inventoryVerificationDet.setInventoryVerificationId(entity.getInventoryVerificationId());
            inventoryVerificationDet.setCreateUserId(sysUser.getUserId());
            inventoryVerificationDet.setCreateTime(new Date());
            inventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
            inventoryVerificationDet.setModifiedTime(new Date());
            if(entity.getType()==(byte) 2){
                inventoryVerificationDet.setWorkName(sysUser.getUserName());
            }
        }
        int num = wmsInventoryVerificationDetMapper.insertList(entity.getInventoryVerificationDets());
        num+=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(entity);
        return num;
    }

    /**
     * 按储位获取库存货品
     * @param id
     * @param storageList
     * @return
     */
    private List<WmsInventoryVerificationDet> findInvGoods(Byte type,Long id,List<Long> storageList){
        SysUser sysUser = currentUser();
        List<WmsInventoryVerificationDet> list = new ArrayList<>();
        //查询库位下所有库存货品
        //储位盘点
//        if(type==2){
//            for (Long storageId : storageList) {
//                SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
//                searchSmtStorageInventory.setStorageId(storageId);
//                //盘点锁 1 否 2 是
//                searchSmtStorageInventory.setStocktakeLock((byte)1);
//                //获取储位库存
//                ResponseEntity<List<SmtStorageInventoryDto>> responseEntity = storageInventoryFeignApi.findList(searchSmtStorageInventory);
//                if(responseEntity.getCode()!=0){
//                    throw new BizErrorException("获取储位库存失败");
//                }
//                List<SmtStorageInventoryDto> smtStorageInventoryDtos = responseEntity.getData();
//                for (SmtStorageInventoryDto smtStorageInventoryDto : smtStorageInventoryDtos) {
//                    WmsInventoryVerificationDet wmsInventoryVerificationDet = new WmsInventoryVerificationDet();
//                    wmsInventoryVerificationDet.setInventoryVerificationId(id);
//                    wmsInventoryVerificationDet.setMaterialId(smtStorageInventoryDto.getMaterialId());
//                    wmsInventoryVerificationDet.setStorageId(smtStorageInventoryDto.getStorageId());
//                    wmsInventoryVerificationDet.setRegister((byte)2);
//                    wmsInventoryVerificationDet.setOriginalQty(smtStorageInventoryDto.getQuantity());
//                    wmsInventoryVerificationDet.setCreateTime(new Date());
//                    wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
//                    wmsInventoryVerificationDet.setModifiedTime(new Date());
//                    wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
//                    list.add(wmsInventoryVerificationDet);
//                }
//            }
//        }else if(type==3){
//            //全盘
//            SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
//            searchSmtStorageInventory.setStocktakeLock((byte)1);
//            ResponseEntity<List<SmtStorageInventoryDto>> responseEntity = storageInventoryFeignApi.findList(searchSmtStorageInventory);
//            if(responseEntity.getCode()!=0){
//                throw new BizErrorException("获取储位库存失败");
//            }
//            List<SmtStorageInventoryDto> smtStorageInventoryDtos = responseEntity.getData();
//            for (SmtStorageInventoryDto smtStorageInventoryDto : smtStorageInventoryDtos) {
//                WmsInventoryVerificationDet wmsInventoryVerificationDet = new WmsInventoryVerificationDet();
//                wmsInventoryVerificationDet.setInventoryVerificationId(id);
//                wmsInventoryVerificationDet.setMaterialId(smtStorageInventoryDto.getMaterialId());
//                wmsInventoryVerificationDet.setStorageId(smtStorageInventoryDto.getStorageId());
//                wmsInventoryVerificationDet.setRegister((byte)2);
//                wmsInventoryVerificationDet.setOriginalQty(smtStorageInventoryDto.getQuantity());
//                wmsInventoryVerificationDet.setCreateTime(new Date());
//                wmsInventoryVerificationDet.setCreateUserId(sysUser.getUserId());
//                wmsInventoryVerificationDet.setModifiedTime(new Date());
//                wmsInventoryVerificationDet.setModifiedUserId(sysUser.getUserId());
//                list.add(wmsInventoryVerificationDet);
//            }
//        }
        return list;
    }

    /**
     * 盘点激活
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor=RuntimeException.class)
    public int activation(String ids,Byte btnType){
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInventoryVerification wmsInventoryVerification = wmsInventoryVerificationMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInventoryVerification)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInventoryVerificationDet.class);
            example.createCriteria().andEqualTo("inventoryVerificationId",wmsInventoryVerification.getInventoryVerificationId());
            List<WmsInventoryVerificationDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            //储位盘点将盘点单的所有储位下库存更改上锁状态及基础信息储位上锁 货品盘点将
            if(btnType ==(byte)1){
                //打开
                num += this.unlockOrLock((byte) 2,list,wmsInventoryVerification);
                wmsInventoryVerification.setStatus((byte)2);
            }else if(btnType ==(byte)2){
                //作废
             num += this.unlockOrLock((byte) 1,list,wmsInventoryVerification);
             wmsInventoryVerification.setStatus((byte)4);
            }
        }
        return num;
    }

    /**
     * 盘点确认
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int ascertained(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInventoryVerification wmsInventoryVerification = wmsInventoryVerificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInventoryVerification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInventoryVerificationDet.class);
            example.createCriteria().andEqualTo("inventoryVerificationId", wmsInventoryVerification.getInventoryVerificationId());
            List<WmsInventoryVerificationDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<WmsInventoryVerificationDet> wmsInventoryVerificationDets = new ArrayList<>();
            for (WmsInventoryVerificationDet wmsInventoryVerificationDet : list) {
                //盘点
                if(wmsInventoryVerification.getProjectType()==(byte)1){
                    //是否存在差异量 不存在则解锁库存盘点锁 存在则进行复盘
                    if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getDiscrepancyQty())){
                        wmsInventoryVerificationDets.add(wmsInventoryVerificationDet);
                    }
                }else{
                    //复盘 //解锁更新库存
                    wmsInventoryVerificationDets.add(wmsInventoryVerificationDet);
                }
            }
            //解锁及复盘库存
            num+=this.unlockOrLock((byte) 3,wmsInventoryVerificationDets,wmsInventoryVerification);
            //更改盘点状态（已完成）
            wmsInventoryVerification.setStatus((byte)5);
            wmsInventoryVerification.setModifiedTime(new Date());
            wmsInventoryVerification.setModifiedUserId(sysUser.getUserId());
            num +=wmsInventoryVerificationMapper.updateByPrimaryKeySelective(wmsInventoryVerification);
        }
        return num;
    }

    /**
     * 差异处理
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int difference(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInventoryVerification wmsInventoryVerification = wmsInventoryVerificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInventoryVerification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(WmsInventoryVerificationDet.class);
            example.createCriteria().andEqualTo("inventoryVerificationId", wmsInventoryVerification.getInventoryVerificationId());
            List<WmsInventoryVerificationDet> list = wmsInventoryVerificationDetMapper.selectByExample(example);
            List<WmsInventoryVerificationDet> wmsInventoryVerificationDets = new ArrayList<>();

            //新增复盘盘点单
            WmsInventoryVerification ws = new WmsInventoryVerification();
            BeanUtil.copyProperties(wmsInventoryVerification,ws);
            ws.setInventoryVerificationId(null);
            ws.setProjectType((byte)2);
            ws.setRelatedOrderCode(wmsInventoryVerification.getInventoryVerificationCode());
            ws.setInventoryVerificationCode(CodeUtils.getId("INPD-"));
            ws.setCreateUserId(sysUser.getUserId());
            ws.setCreateTime(new Date());
            ws.setModifiedTime(new Date());
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setStatus((byte)2);
           num += wmsInventoryVerificationMapper.insertUseGeneratedKeys(ws);

            for (WmsInventoryVerificationDet wmsInventoryVerificationDet : list) {
                if(StringUtils.isEmpty(wmsInventoryVerificationDet.getDiscrepancyQty())&&wmsInventoryVerificationDet.getDiscrepancyQty().compareTo(BigDecimal.ZERO)==1){
                    WmsInventoryVerificationDet det = new WmsInventoryVerificationDet();
                    det.setInventoryVerificationId(ws.getInventoryVerificationId());
                    det.setSourceDetId(wmsInventoryVerificationDet.getInventoryVerificationDetId());
                    det.setStorageId(wmsInventoryVerificationDet.getStorageId());
                    det.setMaterialId(wmsInventoryVerificationDet.getMaterialId());
                    det.setUpDiscrepancyQty(wmsInventoryVerificationDet.getDiscrepancyQty());
                    det.setOriginalQty(wmsInventoryVerificationDet.getOriginalQty());
                    det.setRegister((byte)2);
                    det.setCreateUserId(sysUser.getUserId());
                    det.setCreateTime(new Date());
                    det.setModifiedTime(new Date());
                    det.setModifiedUserId(sysUser.getUserId());
                    wmsInventoryVerificationDets.add(det);
                }
            }
            num+=wmsInventoryVerificationDetMapper.insertList(wmsInventoryVerificationDets);
        }
        return num;
    }

    /**
     * 库存上锁或解锁 1解锁 2上锁 3复盘
     * @param list
     * @return
     */
    private int unlockOrLock(byte lockType,List<WmsInventoryVerificationDet> list,WmsInventoryVerification wmsInventoryVerification){
        SysUser sysUser = currentUser();
        int num = 0;
//        for (WmsInventoryVerificationDet wmsInventoryVerificationDet : list) {
//            //库存按储位物料上盘点锁
//            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
//            smtStorageInventory.setStorageId(wmsInventoryVerificationDet.getStorageId());
//            smtStorageInventory.setMaterialId(wmsInventoryVerificationDet.getMaterialId());
//            smtStorageInventory.setStocktakeLock(lockType==(byte) 2?(byte)2:1);
//            ResponseEntity responseEntity = storageInventoryFeignApi.lock(smtStorageInventory);
//            if(responseEntity.getCode()!=0){
//                throw new BizErrorException(responseEntity.getCode(),"激活失败："+responseEntity.getMessage());
//            }
//            //复盘
//            if(lockType==(byte) 3){
//                //更新库存 盘盈 根据盘点单生成一条库存 盘亏 按顺序扣减库存
//                SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
//                searchSmtStorageInventory.setStorageId(wmsInventoryVerificationDet.getStorageId());
//                searchSmtStorageInventory.setMaterialId(wmsInventoryVerificationDet.getMaterialId());
//                searchSmtStorageInventory.setStocktakeLock((byte)2);
//                ResponseEntity<List<SmtStorageInventoryDto>> res = storageInventoryFeignApi.findList(searchSmtStorageInventory);
//                if(res.getCode()!=0){
//                    throw new BizErrorException("获取库存失败");
//                }
//                SmtStorageInventoryDto smtStorageInventoryDtos = res.getData().get(0);
//                //盘点数大于库存数 新增一条盘单库存
//                if(!StringUtils.isEmpty(wmsInventoryVerificationDet.getInventoryQty()) && wmsInventoryVerificationDet.getInventoryQty().compareTo(smtStorageInventoryDtos.getQuantity())==1){
//                    SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
//                    smtStorageInventoryDet.setStorageInventoryId(smtStorageInventoryDtos.getStorageInventoryId());
//                    smtStorageInventoryDet.setGodownEntry(wmsInventoryVerification.getInventoryVerificationCode());
//                    smtStorageInventoryDet.setMaterialQuantity(wmsInventoryVerificationDet.getInventoryQty());
//                    smtStorageInventoryDet.setProductionCode(wmsInventoryVerificationDet.getBatchCode());
//                    smtStorageInventoryDet.setCreateUserId(sysUser.getUserId());
//                    smtStorageInventoryDet.setCreateTime(new Date());
//                    smtStorageInventoryDet.setModifiedTime(new Date());
//                    smtStorageInventoryDet.setModifiedUserId(sysUser.getUserId());
//                }else{
//                    //盘点数小于库存数 按顺序扣减库存
//                    SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
//                    searchSmtStorageInventoryDet.setStorageInventoryId(smtStorageInventoryDtos.getStorageInventoryId());
//                    ResponseEntity<List<SmtStorageInventoryDetDto>> rs = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet);
//                    if(rs.getCode()!=0){
//                        throw new BizErrorException("获取库存失败");
//                    }
//                    List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtoList = rs.getData();
//                    BigDecimal qty = wmsInventoryVerificationDet.getInventoryQty();
//                    for (SmtStorageInventoryDetDto smtStorageInventoryDetDto : smtStorageInventoryDetDtoList) {
//                        //如果数量等于0结束
//                        if(qty.compareTo(BigDecimal.ZERO)==0){
//                            break;
//                        }
//                        if(qty.compareTo(smtStorageInventoryDetDto.getMaterialQuantity())==1){
//                            //扣减库存
//                            if(smtStorageInventoryDetDto.getMaterialQuantity().compareTo(BigDecimal.ZERO)==1){
//                                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
//                                smtStorageInventoryDet.setStorageInventoryDetId(smtStorageInventoryDetDto.getStorageInventoryDetId());
//                                if(qty.compareTo(smtStorageInventoryDetDto.getMaterialQuantity())==1){
//                                    smtStorageInventoryDet.setMaterialQuantity(BigDecimal.ZERO);
//                                }else{
//                                    smtStorageInventoryDet.setMaterialQuantity(smtStorageInventoryDetDto.getMaterialQuantity().subtract(qty));
//                                }
//                                qty.subtract(smtStorageInventoryDetDto.getMaterialQuantity());
//                                ResponseEntity reDet = storageInventoryFeignApi.updateStorageInventoryDet(smtStorageInventoryDet);
//                                if(reDet.getCode()!=0){
//                                    throw new BizErrorException("盘点库存失败");
//                                }
//                            }
//                        }
//                    }
//                    //更新主表库存数
//                    smtStorageInventory = new SmtStorageInventory();
//                    smtStorageInventory.setStorageInventoryId(smtStorageInventoryDtos.getStorageInventoryId());
//                    smtStorageInventory.setQuantity(smtStorageInventoryDtos.getQuantity().subtract(wmsInventoryVerificationDet.getInventoryQty()));
//                    ResponseEntity resmt = storageInventoryFeignApi.update(smtStorageInventory);
//                    if(resmt.getCode()!=0){
//                        throw new BizErrorException("库存盘点失败");
//                    }
//                }
//            }
//            num++;
//        }
        return num;
    }
    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }
}
