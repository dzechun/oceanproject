package com.fantechs.provider.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseKeyMaterialDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.WanbaoBarcodeRultDataDto;
import com.fantechs.common.base.general.dto.basic.imports.WanbaoBarcodeRultDataImportDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.WanbaoBarcodeRultData;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.WanbaoBarcodeRultDataMapper;
import com.fantechs.provider.base.service.BaseMaterialService;
import com.fantechs.provider.base.service.WanbaoBarcodeRultDataService;
import com.fantechs.provider.base.util.InsertConsumer;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        // 根据指定属性分组，并统计数量（key：指定属性，value：数量）
        Map<Object, Long> mapGroup = list.stream()
                .filter(item ->StringUtils.isNotEmpty(item.getMaterialCode()))
                .collect(Collectors.groupingBy(WanbaoBarcodeRultDataImportDto::getMaterialCode, Collectors.counting()));
        long count = mapGroup.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey()).count();
        logger.info("======== count: " + count);
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "表格中存在重复的物料编码");
        }

        // 系统已有数据
        List<WanbaoBarcodeRultDataDto> rultDataDtos = wanbaoBarcodeRultDataMapper.findList(new HashMap<>());

        // 物料
        List<BaseMaterialDto> materialDtos = materialService.findAll(new HashMap<>());
        if (materialDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "系统物料信息不存在，不可导入");
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

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
                // 修改
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
                // 新增
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
        // 批量保存
        if (!saveList.isEmpty()){
            // 分批次的批量插入
            InsertConsumer.insertData(saveList, wanbaoBarcodeRultDataMapper::insertList);
            success = success + saveList.size();
        }
        if (!updateList.isEmpty()){
            wanbaoBarcodeRultDataMapper.batchUpdate(updateList);
            success = success + saveList.size();
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
