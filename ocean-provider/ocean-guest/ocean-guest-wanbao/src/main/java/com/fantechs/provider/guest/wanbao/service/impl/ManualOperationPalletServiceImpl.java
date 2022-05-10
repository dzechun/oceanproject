package com.fantechs.provider.guest.wanbao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.StackingWorkByAutoDto;
import com.fantechs.common.base.general.dto.mes.sfc.WanbaoBarcodeDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingListDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.guest.wanbao.service.ManualOperationPalletService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingDetService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ManualOperationPalletServiceImpl implements ManualOperationPalletService {

    @Resource
    WanbaoStackingService stackingService;
    @Resource
    WanbaoStackingDetService stackingDetService;
    @Resource
    SFCFeignApi sfcFeignApi;

    @Override
    public int workByManualOperation(PalletWorkByManualOperationDto dto) {
        if (dto.getWanbaoBarcodeDtos().isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码集合为空，请扫码后提交");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCodeNotLike", dto.getStackingCode());
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        WanbaoStackingDto stackingDto = stackingDtos.get(0);
        if (!stackingDtos.get(0).getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码跟配置产线不匹配");
        }
        if (stackingDtos.get(0).getUsageStatus() == 2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛正在使用中，请更换独堆垛");
        }
        if (stackingDtos.get(0).getStatus() == 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛无效，请重新扫码");
        }
        // 容量校验
        map.clear();
        map.put("stackingId", stackingDto.getStackingId());
        List<WanbaoStackingDet> stackingDetDtos = stackingDetService.findList(map);
        if (!stackingDetDtos.isEmpty() && new BigDecimal(stackingDetDtos.size()).compareTo(stackingDto.getMaxCapacity()) > -1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码容量已满，不可继续操作");
        }
        int count = dto.getWanbaoBarcodeDtos().size() + stackingDetDtos.size();
        if (new BigDecimal(count).compareTo(stackingDto.getMaxCapacity()) == 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码容量存放不下当前提交数量");
        }
        ResponseEntity<Integer> responseEntity = sfcFeignApi.workByManualOperation(dto);
        if (responseEntity.getCode() != 0){
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        // 修改状态
        WanbaoStacking stacking = new WanbaoStacking();
        BeanUtil.copyProperties(stackingDto, stacking);
        stacking.setUsageStatus((byte) 2);
        return stackingService.update(stacking);
    }

    @Override
    public ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId) {
        ResponseEntity<ScanByManualOperationDto> responseEntity = sfcFeignApi.scanByManualOperation(barcode, proLineId);
        if (responseEntity.getCode() != 0){
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        ScanByManualOperationDto dto = responseEntity.getData();
        return dto;
    }

    @Override
    public List<WanbaoStackingDto> scanStackingCode(String stackingCode, Long proLineId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", stackingCode);
        map.put("proLineId", proLineId);
        map.put("usageStatus", 1);
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        return stackingDtos;
    }

    @Override
    public int scanStackingCodeByAuto(StackingWorkByAutoDto dto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(dto.getStackingCode())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "未扫描到堆垛，请重新扫码");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", dto.getStackingCode());
        map.put("proLineId", dto.getProLineId());
        map.put("usageStatus", 1);
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码在平台中不存在或已被删除");
        }
        WanbaoStackingDto stackingDto = stackingDtos.get(0);
        if (!stackingDtos.get(0).getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码跟配置产线不匹配");
        }
        if (stackingDtos.get(0).getUsageStatus() == 2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛正在使用中，请更换独堆垛");
        }
        if (stackingDtos.get(0).getStatus() == 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛无效，请重新扫码");
        }
        // 容量校验
        map.clear();
        map.put("stackingId", stackingDto.getStackingId());
        List<WanbaoStackingDet> stackingDetDtos = stackingDetService.findList(map);
        if (!stackingDetDtos.isEmpty() && new BigDecimal(stackingDetDtos.size()).compareTo(stackingDto.getMaxCapacity()) > -1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), dto.getStackingCode() + "堆垛容量已满，请扫其他堆垛");
        }
        int count = 1 + stackingDetDtos.size();
        if (new BigDecimal(count).compareTo(stackingDto.getMaxCapacity()) == 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码容量存放不下当前提交数量");
        }
        Example example = new Example(WanbaoStackingDet.class);
        example.createCriteria().andEqualTo("barcode", dto.getWanbaoBarcodeDto().getBarcode());
        int countBarcodeExistNum = stackingDetService.selectCountByExample(example);
        if (countBarcodeExistNum > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "重复扫码");
        }

        // 校验条码同PO/销售明细/物料
        List<String> barcodeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(stackingDetDtos)){
            barcodeList = stackingDetDtos.stream().map(WanbaoStackingDet::getBarcode).collect(Collectors.toList());
        }
        barcodeList.add(dto.getWanbaoBarcodeDto().getBarcode());
        ResponseEntity<Boolean> response = sfcFeignApi.checkBarCode(barcodeList);
        if (response.getCode() != 0){
            throw new BizErrorException(response.getCode(), response.getMessage());
        }
        Boolean aBoolean = response.getData();
        if (!aBoolean){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), response.getMessage());
        }
        // 发送mq
        sfcFeignApi.sendMQByStacking(dto.getStackingCode());

        // 保存条码堆垛关系
        WanbaoStackingDet stackingDet = new WanbaoStackingDet();
        WanbaoBarcodeDto barcodeDto = dto.getWanbaoBarcodeDto();
        BeanUtil.copyProperties(barcodeDto, stackingDet);
        stackingDet.setStackingId(stackingDto.getStackingId());
        stackingDet.setStatus((byte) 1);
        stackingDet.setOrgId(user.getOrganizationId());
        stackingDet.setCreateTime(new Date());
        stackingDet.setCreateUserId(user.getUserId());
        stackingDet.setModifiedTime(new Date());
        stackingDet.setModifiedUserId(user.getUserId());
        stackingDet.setIsDelete((byte) 1);
        return stackingDetService.save(stackingDet);
    }

    /**
     * 查找空闲并且有条码的堆垛
     *
     * @param proLineId
     * @return
     */
    @Override
    public List<WanbaoAutoStackingListDto> findStackingByAuto(Long proLineId) {
        List<WanbaoAutoStackingDto> stackingDtos = stackingDetService.findStackingByAuto(proLineId);
        Map<String, List<WanbaoAutoStackingDto>> map = stackingDtos.stream().collect(Collectors.groupingBy(WanbaoAutoStackingDto::getStackingCode));
        List<WanbaoAutoStackingListDto> list = new ArrayList<>();
        map.forEach((key, value) -> {
            WanbaoAutoStackingListDto listDto = new WanbaoAutoStackingListDto();
            WanbaoAutoStackingDto autoStackingDto = value.get(0);
            BeanUtil.copyProperties(autoStackingDto, listDto);
            listDto.setCount(value.size());
            listDto.setList(value);
            list.add(listDto);
        });
        return list;
    }

    /**
     * 堆垛提交（A线）
     *
     * @param dto
     * @return
     */
    @Override
    public int workByAuto(WanbaoAutoStackingListDto dto) {
        return sfcFeignApi.workByAuto(dto).getCode() == 0?1:0;
    }

    /**
     * 切换堆垛
     *
     * @param oldId
     * @param newId
     * @return
     */
    @Override
    public int changeStacking(Long oldId, Long newId) {
        WanbaoStacking stacking = stackingService.selectByKey(newId);
        if (stacking.getUsageStatus() != 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "新堆垛已使用,请更换其他堆垛");
        }
        Example example = new Example(WanbaoStackingDet.class);
        example.createCriteria().andEqualTo("stackingId", oldId);
        List<WanbaoStackingDet> dets = stackingDetService.selectByExample(example);
        for (WanbaoStackingDet det : dets){
            det.setStackingId(newId);
            stackingDetService.update(det);
        }
        return 1;
    }

    /**
     * 移除堆垛上的条码
     *
     * @param stackingDetId
     * @return
     */
    @Override
    public int deleteStackingBarcode(Long stackingDetId) {
        WanbaoStackingDet stackingDet = stackingDetService.selectByKey(stackingDetId);
        WanbaoStacking stacking = stackingService.selectByKey(stackingDet.getStackingId());
        if (stacking.getUsageStatus() == 2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛已提交，不可移除条码");
        }
        int i = stackingDetService.deleteByKey(stackingDetId);
        return i;
    }

}
