package com.fantechs.provider.mes.pm.service.impl;

import cn.hutool.core.comparator.CompareUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderProcessReWo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.ClassCompareUtil;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMaterialRePMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderProcessReWoMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMaterialRePMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderProcessReWoMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderMaterialRePService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderProcessReWoService;
import com.fantechs.provider.mes.pm.vo.MesPmHtWorkOrderProcessReWoVo;
import com.fantechs.provider.mes.pm.vo.MesPmWorkOrderProcessReWoVo;
import jdk.nashorn.internal.codegen.CompileUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class MesPmWorkOrderProcessReWoServiceImpl extends BaseService<MesPmWorkOrderProcessReWo> implements MesPmWorkOrderProcessReWoService {

    @Resource
    private MesPmWorkOrderProcessReWoMapper mesPmWorkOrderProcessReWoMapper;

    @Resource
    private MesPmHtWorkOrderProcessReWoMapper mesPmHtWorkOrderProcessReWoMapper;

    @Resource
    private MesPmWorkOrderMaterialRePMapper mesPmWorkOrderMaterialRePMapper;

    @Resource
    private MesPmHtWorkOrderMaterialRePMapper mesPmHtWorkOrderMaterialRePMapper;

    @Override
    public List<MesPmWorkOrderProcessReWoVo> findList(Map<String, Object> map) {
        List<MesPmWorkOrderProcessReWoVo> list = mesPmWorkOrderProcessReWoMapper.findMaterialList(map);
        for (MesPmWorkOrderProcessReWoVo mesPmWorkOrderProcessReWoVo : list) {
            if (StringUtils.isNotEmpty(mesPmWorkOrderProcessReWoVo)){
                map.put("workOrderId",mesPmWorkOrderProcessReWoVo.getWorkOrderId());
                mesPmWorkOrderProcessReWoVo.setList(mesPmWorkOrderProcessReWoMapper.findList(map));
            }
        }
        return list;
    }

    @Override
    public int batchSave(List<MesPmWorkOrderProcessReWo> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isNotEmpty(list)){
            Example example = new Example(MesPmWorkOrderProcessReWo.class);
            for (MesPmWorkOrderProcessReWo mesPmWorkOrderProcessReWo : list) {
                example.createCriteria().andEqualTo("workOrderProcessReWoId",mesPmWorkOrderProcessReWo.getWorkOrderProcessReWoId() == null ? -1 : mesPmWorkOrderProcessReWo.getWorkOrderProcessReWoId());
                MesPmWorkOrderProcessReWo pmWorkOrderProcessReWo = mesPmWorkOrderProcessReWoMapper.selectOneByExample(example);
                if (StringUtils.isEmpty(pmWorkOrderProcessReWo)){
                    this.save(mesPmWorkOrderProcessReWo);
                }else {
                    boolean b = ClassCompareUtil.compareObject(pmWorkOrderProcessReWo, mesPmWorkOrderProcessReWo);
                    if (b){
                        this.update(mesPmWorkOrderProcessReWo);
                    }
                }

            }
        }
        return 1;
    }

    @Override
    public int save(MesPmWorkOrderProcessReWo record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = mesPmWorkOrderProcessReWoMapper.insertUseGeneratedKeys(record);

        MesPmHtWorkOrderProcessReWo mesPmHtWorkOrderProcessReWo = new MesPmHtWorkOrderProcessReWo();
        BeanUtils.copyProperties(record,mesPmHtWorkOrderProcessReWo);
        mesPmHtWorkOrderProcessReWoMapper.insert(mesPmHtWorkOrderProcessReWo);

        MesPmHtWorkOrderMaterialReP mesPmHtWorkOrderMaterialReP = new MesPmHtWorkOrderMaterialReP();
        List<MesPmHtWorkOrderMaterialReP> htList = new ArrayList<>();
        for (MesPmWorkOrderMaterialRePDto mesPmWorkOrderMaterialRePDto : record.getList()) {
            mesPmWorkOrderMaterialRePDto.setWorkOrderProcessReWoId(record.getWorkOrderProcessReWoId());

            BeanUtils.copyProperties(mesPmWorkOrderMaterialRePDto,mesPmHtWorkOrderMaterialReP);
            mesPmHtWorkOrderMaterialReP.setWorkOrderProcessReWoId(mesPmHtWorkOrderProcessReWo.getHtWorkOrderProcessReWoId());
            htList.add(mesPmHtWorkOrderMaterialReP);
        }
        if (StringUtils.isNotEmpty(htList)){
            mesPmHtWorkOrderMaterialRePMapper.insertList(htList);
        }

        return i;
    }

    @Override
    public int update(MesPmWorkOrderProcessReWo entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());

        MesPmHtWorkOrderProcessReWo mesPmHtWorkOrderProcessReWo = new MesPmHtWorkOrderProcessReWo();
        BeanUtils.copyProperties(entity,mesPmHtWorkOrderProcessReWo);
        mesPmHtWorkOrderProcessReWoMapper.insert(mesPmHtWorkOrderProcessReWo);


        Example example = new Example(MesPmWorkOrderMaterialReP.class);
        List<Long> dList = new ArrayList();
        List<MesPmWorkOrderMaterialReP> addList = new ArrayList<>();
        for (MesPmWorkOrderMaterialRePDto mesPmWorkOrderMaterialRePDto : entity.getList()) {
            if (StringUtils.isEmpty(mesPmWorkOrderMaterialRePDto.getWorkOrderProcessReWoId()) || mesPmWorkOrderMaterialRePDto.getWorkOrderProcessReWoId() == 0){
                addList.add(mesPmWorkOrderMaterialRePDto);
            }
            mesPmWorkOrderMaterialRePDto.setWorkOrderProcessReWoId(entity.getWorkOrderProcessReWoId());

            dList.add(mesPmWorkOrderMaterialRePDto.getWorkOrderMaterialRePId());
        }
        example.createCriteria().andNotIn("workOrderMaterialRePId",dList);
        //获取删除的物料清单
        List<MesPmWorkOrderMaterialReP> mesPmWorkOrderMaterialRePS = mesPmWorkOrderMaterialRePMapper.selectByExample(example);
        List<MesPmHtWorkOrderMaterialReP> htList = new ArrayList<>();
        MesPmHtWorkOrderMaterialReP mesPmHtWorkOrderMaterialReP = new MesPmHtWorkOrderMaterialReP();
        mesPmWorkOrderMaterialRePS.addAll(addList);
        for (MesPmWorkOrderMaterialReP mesPmWorkOrderMaterialReP : mesPmWorkOrderMaterialRePS) {
            BeanUtils.copyProperties(mesPmWorkOrderMaterialReP,mesPmHtWorkOrderMaterialReP);
            mesPmHtWorkOrderMaterialReP.setWorkOrderProcessReWoId(mesPmHtWorkOrderProcessReWo.getHtWorkOrderProcessReWoId());
            htList.add(mesPmHtWorkOrderMaterialReP);
        }
        if (StringUtils.isNotEmpty(htList)){
            mesPmHtWorkOrderMaterialRePMapper.insertList(htList);
        }

        example.clear();
        example.createCriteria().andEqualTo("workOrderProcessReWoId",entity.getWorkOrderProcessReWoId());
        mesPmWorkOrderMaterialRePMapper.deleteByExample(example);

        mesPmWorkOrderMaterialRePMapper.insertList(entity.getList());

        return mesPmWorkOrderProcessReWoMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<MesPmHtWorkOrderProcessReWo> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        HashMap<String, Object> map = new HashMap<>();
        List<MesPmHtWorkOrderMaterialReP> htList = new ArrayList<>();
        MesPmHtWorkOrderMaterialReP mesPmHtWorkOrderMaterialReP = new MesPmHtWorkOrderMaterialReP();
        for (String id : idsArr) {
            MesPmWorkOrderProcessReWo mesPmWorkOrderProcessReWo = mesPmWorkOrderProcessReWoMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(mesPmWorkOrderProcessReWo)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            MesPmHtWorkOrderProcessReWo mesPmHtWorkOrderProcessReWo = new MesPmHtWorkOrderProcessReWo();
            BeanUtils.copyProperties(mesPmWorkOrderProcessReWo,mesPmHtWorkOrderProcessReWo);
            list.add(mesPmHtWorkOrderProcessReWo);

            map.put("workOrderProcessReWoId",id);
            List<MesPmWorkOrderMaterialRePDto> mesPmWorkOrderMaterialRePDtos = mesPmWorkOrderMaterialRePMapper.findList(map);
            if (StringUtils.isNotEmpty(mesPmWorkOrderMaterialRePDtos)){
                String rePId = "";
                for (MesPmWorkOrderMaterialRePDto mesPmWorkOrderMaterialRePDto : mesPmWorkOrderMaterialRePDtos) {
                    rePId+=mesPmWorkOrderMaterialRePDto.getWorkOrderProcessReWoId()+",";

                    BeanUtils.copyProperties(mesPmWorkOrderMaterialRePDto,mesPmHtWorkOrderMaterialReP);
                    mesPmHtWorkOrderMaterialReP.setWorkOrderProcessReWoId(mesPmHtWorkOrderProcessReWo.getHtWorkOrderProcessReWoId());
                    htList.add(mesPmHtWorkOrderMaterialReP);
                }
                mesPmWorkOrderMaterialRePMapper.deleteByIds(rePId.substring(0, rePId.length() - 1));
            }
        }
        if (StringUtils.isNotEmpty(htList)){
            mesPmHtWorkOrderMaterialRePMapper.insertList(htList);
        }

        mesPmHtWorkOrderProcessReWoMapper.insertList(list);
        return mesPmWorkOrderProcessReWoMapper.deleteByIds(ids);
    }
}
