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
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.WanbaoBarcodeRultDataMapper;
import com.fantechs.provider.base.service.BaseMaterialService;
import com.fantechs.provider.base.service.WanbaoBarcodeRultDataService;
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

        // 物料重复数据过滤
        Set<WanbaoBarcodeRultDataImportDto> importDtosSet = new TreeSet<>(Comparator.comparing(WanbaoBarcodeRultDataImportDto::getMaterialCode, Comparator.nullsFirst(Comparator.naturalOrder())));
        importDtosSet.addAll(list);
        list = importDtosSet.stream().collect(Collectors.toList());

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

            // 对比识别码
            for (WanbaoBarcodeRultDataDto dataDto : rultDataDtos){
                if (dataDto.getIdentificationCode().equals(importDto.getIdentificationCode())){
                    BeanUtil.copyProperties(dataDto, rultData);
                    break;
                }
            }
            if (StringUtils.isNotEmpty(rultData.getIdentificationCode())){
                // 修改
                rultData.setIdentificationCode(importDto.getIdentificationCode());
                if (StringUtils.isNotEmpty(importDto.getMaterialCode())){
                    // 对比物料
                    for (BaseMaterialDto materialDto : materialDtos){
                        if (materialDto.getMaterialCode().equals(importDto.getMaterialCode())){
                            rultData.setMaterialId(materialDto.getMaterialId().toString());
                            rultData.setProductCode(materialDto.getMaterialCode());
                            break;
                        }
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
                    // 对比物料
                    for (BaseMaterialDto materialDto : materialDtos){
                        if (materialDto.getMaterialCode().equals(importDto.getMaterialCode())){
                            rultData.setMaterialId(materialDto.getMaterialId().toString());
                            rultData.setProductCode(materialDto.getMaterialCode());
                            rultData.setDataStatus((byte) 1);
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(rultData.getMaterialId())){
                        fail.add(i+1);
                        continue;
                    }
                }

                saveList.add(rultData);
            }

            // 批量保存
            if (!saveList.isEmpty()){
                Set<WanbaoBarcodeRultData> saveSet = new TreeSet<>(Comparator.comparing(WanbaoBarcodeRultData::getIdentificationCode, Comparator.nullsFirst(Comparator.naturalOrder())));
                saveSet.addAll(saveList);
                saveList = saveSet.stream().collect(Collectors.toList());
                wanbaoBarcodeRultDataMapper.insertList(saveList);
                success = success + saveList.size();
            }
            if (!updateList.isEmpty()){
                wanbaoBarcodeRultDataMapper.batchUpdate(updateList);
                success = success + saveList.size();
            }

        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
