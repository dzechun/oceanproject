package com.fantechs.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDetDto;
import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysCustomFormDetMapper;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.mapper.SysDefaultCustomFormDetMapper;
import com.fantechs.security.mapper.SysDefaultCustomFormMapper;
import com.fantechs.security.service.SysDefaultCustomFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class SysDefaultCustomFormServiceImpl extends BaseService<SysDefaultCustomForm> implements SysDefaultCustomFormService {

    @Resource
    private SysDefaultCustomFormMapper sysDefaultCustomFormMapper;
    @Resource
    private SysDefaultCustomFormDetMapper sysDefaultCustomFormDetMapper;
    @Resource
    private SysCustomFormMapper sysCustomFormMapper;
    @Resource
    private SysCustomFormDetMapper sysCustomFormDetMapper;

    @Override
    public List<SysDefaultCustomFormDto> findList(Map<String, Object> map) {
        return sysDefaultCustomFormMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncDefaultData(Long orgId){
        int i = 0;
        //存放需新增的数据
        List<SysDefaultCustomFormDto> list = new ArrayList<>();
        //存放关联子表单id
        List<Long> subIdList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("orgId", orgId);
        List<SysCustomFormDto> SysCustomFormDtos = sysCustomFormMapper.findList(map);

        Map<String, Object> defaultMap = new HashMap<>();
        List<SysDefaultCustomFormDto> sysDefaultCustomFormDtos = sysDefaultCustomFormMapper.findList(defaultMap);

        //筛选出 默认自定义表单 对比 自定义表单 新增的数据
        for (SysDefaultCustomFormDto sysDefaultCustomFormDto : sysDefaultCustomFormDtos){
            boolean tag = false;
            if(StringUtils.isNotEmpty(SysCustomFormDtos)) {
                for (SysCustomFormDto sysCustomFormDto : SysCustomFormDtos) {
                    if (sysDefaultCustomFormDto.getCustomFormCode().equals(sysCustomFormDto.getCustomFormCode())) {
                        tag = true;
                        break;
                    }
                }
            }

            if(!tag){
                list.add(sysDefaultCustomFormDto);
                if(StringUtils.isNotEmpty(sysDefaultCustomFormDto.getSubId())){
                    subIdList.add(sysDefaultCustomFormDto.getSubId());
                }
            }
        }


        //复制数据
        List<String> formCode = new ArrayList<>();
        for(SysDefaultCustomFormDto sysDefaultCustomFormDto : list){
            //非父表单且非子表单
            if(StringUtils.isEmpty(sysDefaultCustomFormDto.getSubId())&&!subIdList.contains(sysDefaultCustomFormDto.getCustomFormId())){
                i += copyDefaultData(sysDefaultCustomFormDto,orgId, formCode);
            }

            //父表单
            if(StringUtils.isNotEmpty(sysDefaultCustomFormDto.getSubId())){
                for(SysDefaultCustomFormDto subDefaultCustomFormDto : list){
                    if(sysDefaultCustomFormDto.getSubId().equals(subDefaultCustomFormDto.getCustomFormId())){
                        i += copyDefaultData(sysDefaultCustomFormDto,subDefaultCustomFormDto,orgId, formCode);
                    }
                }
            }
        }

        return i;
    }


    /**
     *  主表数据复制
     * @param sysDefaultCustomFormDto
     * @param orgId
     * @return
     */
    public int copyDefaultData(SysDefaultCustomFormDto sysDefaultCustomFormDto,Long orgId, List<String> formCode){
        long count = formCode.stream().filter(item -> item.equals(sysDefaultCustomFormDto.getCustomFormCode())).count();
        if (count <= 0){
            SysCustomForm sysCustomForm = new SysCustomForm();
            BeanUtils.autoFillEqFields(sysDefaultCustomFormDto,sysCustomForm);
            sysCustomForm.setCustomFormId(null);
            sysCustomForm.setOrgId(orgId);
            sysCustomFormMapper.insertUseGeneratedKeys(sysCustomForm);
            formCode.add(sysCustomForm.getCustomFormCode());


            List<SysCustomFormDet> Dets = new ArrayList<>();
            List<SysDefaultCustomFormDetDto> formDets = sysDefaultCustomFormDto.getFormDets();
            if(StringUtils.isNotEmpty(formDets)){
                for (SysDefaultCustomFormDetDto sysDefaultCustomFormDetDto : formDets){
                    SysCustomFormDet sysCustomFormDet = new SysCustomFormDet();
                    BeanUtils.autoFillEqFields(sysDefaultCustomFormDetDto,sysCustomFormDet);
                    sysCustomFormDet.setCustomFormId(sysCustomForm.getCustomFormId());
                    sysCustomFormDet.setOrgId(orgId);
                    Dets.add(sysCustomFormDet);
                }
                sysCustomFormDetMapper.insertList(Dets);
            }
        }

        return 1;
    }

    /**
     * 主表与关联子表数据复制
     * @param sysDefaultCustomFormDto  主表
     * @param subDefaultCustomFormDto  关联子表
     * @param orgId  组织id
     * @return
     */
    public int copyDefaultData(SysDefaultCustomFormDto sysDefaultCustomFormDto,SysDefaultCustomFormDto subDefaultCustomFormDto,Long orgId, List<String> formCode){

        //关联子表
        SysCustomForm sysCustomForm = new SysCustomForm();
        BeanUtil.copyProperties(subDefaultCustomFormDto, sysCustomForm);
//        BeanUtils.autoFillEqFields(subDefaultCustomFormDto,sysCustomForm);
        sysCustomForm.setCustomFormId(null);
        sysCustomForm.setOrgId(orgId);
        sysCustomFormMapper.insertUseGeneratedKeys(sysCustomForm);
        formCode.add(sysCustomForm.getCustomFormCode());

        List<SysCustomFormDet> Dets = new ArrayList<>();
        List<SysDefaultCustomFormDetDto> formDets = subDefaultCustomFormDto.getFormDets();
        if(StringUtils.isNotEmpty(formDets)){
            for (SysDefaultCustomFormDetDto sysDefaultCustomFormDetDto : formDets){
                SysCustomFormDet sysCustomFormDet = new SysCustomFormDet();
                BeanUtils.autoFillEqFields(sysDefaultCustomFormDetDto,sysCustomFormDet);
                sysCustomFormDet.setCustomFormId(sysCustomForm.getCustomFormId());
                sysCustomFormDet.setOrgId(orgId);
                Dets.add(sysCustomFormDet);
            }
            sysCustomFormDetMapper.insertList(Dets);
        }

        //主表
        sysDefaultCustomFormDto.setSubId(sysCustomForm.getCustomFormId());
        this.copyDefaultData(sysDefaultCustomFormDto,orgId, formCode);

        return 2;
    }


}
