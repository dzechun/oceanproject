package com.fantechs.provider.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.WanbaoBarcodeRultDataDto;
import com.fantechs.common.base.general.dto.basic.imports.WanbaoBarcodeRultDataImportDto;
import com.fantechs.common.base.general.entity.basic.WanbaoBarcodeRultData;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.InsertConsumer;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.WanbaoBarcodeRultDataMapper;
import com.fantechs.provider.base.service.BaseMaterialService;
import com.fantechs.provider.base.service.WanbaoBarcodeRultDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2022/02/22.
 */
@Service
public class WanbaoBarcodeRultDataServiceImpl extends BaseService<WanbaoBarcodeRultData> implements WanbaoBarcodeRultDataService {

    @Resource
    private WanbaoBarcodeRultDataMapper wanbaoBarcodeRultDataMapper;
    @Resource
    BaseMaterialService materialService;

    @Override
    public List<WanbaoBarcodeRultDataDto> findList(Map<String, Object> map) {
        return wanbaoBarcodeRultDataMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WanbaoBarcodeRultDataImportDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        logger.info("=========== list:" + list.size());


        // ?????????????????????????????????????????????key??????????????????value????????????
        Map<Object, Long> mapGroup = list.stream()
                .filter(item ->StringUtils.isNotEmpty(item.getMaterialCode()))
                .collect(Collectors.groupingBy(WanbaoBarcodeRultDataImportDto::getMaterialCode, Collectors.counting()));
        long count = mapGroup.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey()).count();
        logger.info("======== count: " + count);
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????");
        }

        // ??????????????????
        List<WanbaoBarcodeRultDataDto> rultDataDtos = wanbaoBarcodeRultDataMapper.findList(new HashMap<>());

        // ??????
        List<BaseMaterialDto> materialDtos = materialService.findAll(new HashMap<>());
        if (materialDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????");
        }

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????

        List<WanbaoBarcodeRultData> saveList = new ArrayList<>();
        List<WanbaoBarcodeRultData> updateList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            WanbaoBarcodeRultData rultData = new WanbaoBarcodeRultData();
            rultData.setCreateTime(new Date());
            rultData.setCreateUserId(user.getUserId());
            rultData.setModifiedTime(new Date());
            rultData.setModifiedUserId(user.getUserId());
            rultData.setOrgId(user.getOrganizationId());
            rultData.setStatus((byte) 1);

            WanbaoBarcodeRultDataImportDto importDto = list.get(i);
            if (StringUtils.isEmpty(importDto.getIdentificationCode())){
                fail.add(i+1);
                continue;
            }
            if (importDto.getIdentificationCode().length() != 3){
                fail.add(i+1);
                continue;
            }

            List<WanbaoBarcodeRultDataDto> rultDataDtoList = rultDataDtos.stream().filter(u -> (u.getIdentificationCode().equals(importDto.getIdentificationCode()))).collect(Collectors.toList());
            if(rultDataDtoList.size()>0){
                BeanUtil.copyProperties(rultDataDtoList.get(0), rultData);
            }
            if (StringUtils.isNotEmpty(rultData.getIdentificationCode())){
                // ??????
                rultData.setIdentificationCode(importDto.getIdentificationCode());
                if (StringUtils.isNotEmpty(importDto.getMaterialCode())){

                    List<BaseMaterialDto> dtoList=materialDtos.stream().filter(u -> (u.getMaterialCode().equals(importDto.getMaterialCode()))).collect(Collectors.toList());
                     if(dtoList.size()>0){
                         rultData.setMaterialId(dtoList.get(0).getMaterialId().toString());
                         rultData.setProductCode(dtoList.get(0).getMaterialCode());
                     }

                    if (StringUtils.isEmpty(rultData.getMaterialId())){
                        fail.add(i+1);
                        continue;
                    }
                }
                if (rultData.getDataStatus().equals((byte) 0)){
                    if (StringUtils.isNotEmpty(rultData.getMaterialId())){
                        rultData.setDataStatus((byte) 1);
                    }
                    updateList.add(rultData);
                }else {
                    fail.add(i+1);
                    continue;
                }
            }else {
                // ??????
                rultData.setIdentificationCode(importDto.getIdentificationCode());
                rultData.setDataStatus((byte) 0);
                if (StringUtils.isNotEmpty(importDto.getMaterialCode())){
                    List<BaseMaterialDto> dtoList=materialDtos.stream().filter(u -> (u.getMaterialCode().equals(importDto.getMaterialCode()))).collect(Collectors.toList());
                    if(dtoList.size()>0){
                        rultData.setMaterialId(dtoList.get(0).getMaterialId().toString());
                        rultData.setProductCode(dtoList.get(0).getMaterialCode());
                        rultData.setDataStatus((byte) 1);
                    }
                    else{
                        fail.add(i+1);
                        continue;
                    }

                }
                saveList.add(rultData);
            }
        }
        logger.info("======saveList:" + saveList.size());
        // ????????????
        if (!saveList.isEmpty()){
            // ????????????????????????
            InsertConsumer.insertData(saveList, wanbaoBarcodeRultDataMapper::insertList);
            success = success + saveList.size();
        }
        if (!updateList.isEmpty()){
            wanbaoBarcodeRultDataMapper.batchUpdate(updateList);
            success = success + saveList.size();
        }

        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }

    @Override
    public int updateByMaterial(List<Long> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(WanbaoBarcodeRultData.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dataStatus", (byte) 0);
        List<WanbaoBarcodeRultData> rultDataList = wanbaoBarcodeRultDataMapper.selectByExample(example);

        List<WanbaoBarcodeRultData> updateList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WanbaoBarcodeRultData rultData = rultDataList.get(i);
            rultData.setMaterialId(list.get(i).toString());
            rultData.setDataStatus((byte) 1);
            rultData.setModifiedUserId(user.getUserId());
            rultData.setModifiedTime(new Date());
            updateList.add(rultData);
        }
        if (!updateList.isEmpty()){
            return wanbaoBarcodeRultDataMapper.batchUpdate(updateList);
        }
        return 0;
    }
}
