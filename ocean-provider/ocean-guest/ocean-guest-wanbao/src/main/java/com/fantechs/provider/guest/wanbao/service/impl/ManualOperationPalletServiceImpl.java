package com.fantechs.provider.guest.wanbao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
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
import org.springframework.transaction.annotation.Transactional;
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
    @LcnTransaction
    @Transactional(rollbackFor = RuntimeException.class)
    public int workByManualOperation(PalletWorkByManualOperationDto dto) {
        long startTime = System.currentTimeMillis();
        if (dto.getWanbaoBarcodeDtos().isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCodeNotLike", dto.getStackingCode());
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
        }
        WanbaoStackingDto stackingDto = stackingDtos.get(0);
        if (!stackingDtos.get(0).getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }
        if (stackingDtos.get(0).getUsageStatus() == 2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????????????????");
        }
        if (stackingDtos.get(0).getStatus() == 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
        }
        long stackingTime = System.currentTimeMillis();
        log.info("=========== ??????????????????:" + (stackingTime-startTime));
        // ????????????
        map.clear();
        map.put("stackingId", stackingDto.getStackingId());
        List<WanbaoStackingDet> stackingDetDtos = stackingDetService.findList(map);
        if (!stackingDetDtos.isEmpty() && new BigDecimal(stackingDetDtos.size()).compareTo(stackingDto.getMaxCapacity()) > -1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????????????????");
        }
        int count = dto.getWanbaoBarcodeDtos().size() + stackingDetDtos.size();
        if (new BigDecimal(count).compareTo(stackingDto.getMaxCapacity()) == 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
        }
        long stackdetTime = System.currentTimeMillis();
        log.info("=========== ??????????????????:" + (stackdetTime-stackingTime));
        ResponseEntity<Integer> responseEntity = sfcFeignApi.workByManualOperation(dto);
        if (responseEntity.getCode() != 0){
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        }
        // ????????????
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
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
        }
        return stackingDtos;
    }

    @Override
    public int scanStackingCodeByAuto(StackingWorkByAutoDto dto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(dto.getStackingCode())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", dto.getStackingCode());
        map.put("proLineId", dto.getProLineId());
        map.put("usageStatus", 1);
        List<WanbaoStackingDto> stackingDtos = stackingService.findList(map);
        if (stackingDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
        }
        WanbaoStackingDto stackingDto = stackingDtos.get(0);
        if (!stackingDtos.get(0).getProLineId().equals(dto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }
        if (stackingDtos.get(0).getUsageStatus() == 2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????????????????");
        }
        if (stackingDtos.get(0).getStatus() == 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
        }
        // ????????????
        map.clear();
        map.put("stackingId", stackingDto.getStackingId());
        List<WanbaoStackingDet> stackingDetDtos = stackingDetService.findList(map);
        if (!stackingDetDtos.isEmpty() && new BigDecimal(stackingDetDtos.size()).compareTo(stackingDto.getMaxCapacity()) > -1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), dto.getStackingCode() + "???????????????????????????????????????");
        }
        int count = 1 + stackingDetDtos.size();
        if (new BigDecimal(count).compareTo(stackingDto.getMaxCapacity()) == 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
        }
        Example example = new Example(WanbaoStackingDet.class);
        example.createCriteria().andEqualTo("barcode", dto.getWanbaoBarcodeDto().getBarcode());
        int countBarcodeExistNum = stackingDetService.selectCountByExample(example);
        if (countBarcodeExistNum > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????");
        }

        // ???????????????PO/????????????/??????
        List<String> barcodeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(stackingDetDtos)){
            barcodeList = stackingDetDtos.stream().map(WanbaoStackingDet::getBarcode).collect(Collectors.toList());
            barcodeList.add(dto.getWanbaoBarcodeDto().getBarcode());
            ResponseEntity<Boolean> response = sfcFeignApi.checkBarCode(barcodeList);
            if (response.getCode() != 0){
                throw new BizErrorException(response.getCode(), response.getMessage());
            }
            Boolean aBoolean = response.getData();
            if (!aBoolean){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), response.getMessage());
            }
        }
        // ??????mq
        sfcFeignApi.sendMQByStacking(dto.getStackingCode());

        // ????????????????????????
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
     * ????????????????????????????????????
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
        //??????????????? 2022-05-27
        list.sort(Comparator.comparing(WanbaoAutoStackingListDto::getCount).reversed());
        return list;
    }

    /**
     * ???????????????A??????
     *
     * @param dto
     * @return
     */
    @Override
    @LcnTransaction
    @Transactional(rollbackFor = RuntimeException.class)
    public int workByAuto(WanbaoAutoStackingListDto dto) {
        WanbaoStacking stacking = stackingService.selectByKey(dto.getStackingId());
        stacking.setUsageStatus((byte) 2);
        stackingService.update(stacking);
        return sfcFeignApi.workByAuto(dto).getCode() == 0?1:0;
    }

    /**
     * ????????????
     *
     * @param newId
     * @param stackingDetId
     * @return
     */
    @Override
    public int changeStacking(Long newId, Long stackingDetId) {
        WanbaoStacking stacking = stackingService.selectByKey(newId);
        if (stacking.getUsageStatus() != 1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????,?????????????????????");
        }
        List<String> barcodeList = new ArrayList<>();
        WanbaoStackingDet stackingDet = stackingDetService.selectByKey(stackingDetId);
        Long oldId = stackingDet.getStackingId();

        Example example = new Example(WanbaoStackingDet.class);
        example.createCriteria().andEqualTo("stackingId", newId);
        List<WanbaoStackingDet> stackingDets = stackingDetService.selectByExample(example);
        log.info("========= stackingDets:" + JSON.toJSONString(stackingDets));
        if (StringUtils.isNotEmpty(stackingDets)){
            WanbaoStacking newstacking = stackingService.selectByKey(stackingDets.get(0).getStackingId());
            if (new BigDecimal(stackingDets.size() + 1).compareTo(newstacking.getMaxCapacity()) == 1){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
            }

            barcodeList = stackingDets.stream().map(WanbaoStackingDet::getBarcode).collect(Collectors.toList());
            barcodeList.add(stackingDet.getBarcode());

            ResponseEntity<Boolean> response = sfcFeignApi.checkBarCode(barcodeList);
            if (response.getCode() != 0){
                throw new BizErrorException(response.getCode(), response.getMessage());
            }
            Boolean aBoolean = response.getData();
            log.info("========= aBoolean:" + aBoolean);
            if (!aBoolean){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), response.getMessage());
            }
        }else {
            // ????????????????????????????????????????????????
        }
        stackingDet.setStackingId(newId);
        int num = stackingDetService.update(stackingDet);

        // ?????????????????????????????????
        example.clear();
        example.createCriteria().andEqualTo("stackingId", oldId);
        int count = stackingDetService.selectCountByExample(example);
        if (count <= 0){
            WanbaoStacking oldStacking = stackingService.selectByKey(oldId);
            oldStacking.setUsageStatus((byte) 1);
            stackingService.update(oldStacking);
        }
        return num;
    }

    /**
     * ????????????????????????
     *
     * @param stackingDetId
     * @return
     */
    @Override
    public int deleteStackingBarcode(Long stackingDetId) {
        WanbaoStackingDet stackingDet = stackingDetService.selectByKey(stackingDetId);
        WanbaoStacking stacking = stackingService.selectByKey(stackingDet.getStackingId());
        if (stacking.getUsageStatus() == 2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }
        int i = stackingDetService.deleteByKey(stackingDetId);
        Example example = new Example(WanbaoStackingDet.class);
        example.createCriteria().andEqualTo("stackingId", stacking.getStackingId());
        int count = stackingDetService.selectCountByExample(example);
        if (count <= 0){
            stacking.setUsageStatus((byte) 1);
            stackingService.update(stacking);
        }
        return i;
    }

}
