package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDetDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.InOutParamsDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseInAndOutRuleImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseInAndOutRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by mr.lei on 2021/12/30.
 */
@Service
public class BaseInAndOutRuleServiceImpl extends BaseService<BaseInAndOutRule> implements BaseInAndOutRuleService {

    @Resource
    private BaseInAndOutRuleMapper baseInAndOutRuleMapper;
    @Resource
    private BaseInAndOutRuleDetMapper baseInAndOutRuleDetMapper;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;
    @Resource
    private BaseHtInAndOutRuleMapper baseHtInAndOutRuleMapper;
    @Resource
    private BaseHtInAndOutRuleDetMapper baseHtInAndOutRuleDetMapper;

    @Override
    public List<BaseInAndOutRuleDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return baseInAndOutRuleMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseInAndOutRule record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请选择仓库");
        }

        //查询是否存在相同记录
        Example example = new Example(BaseInAndOutRule.class);
        example.createCriteria().andEqualTo("warehouseId",record.getWarehouseId()).andEqualTo("category",record.getCategory());
        List<BaseInAndOutRule> list = baseInAndOutRuleMapper.selectByExample(example);
        if(list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库已经存在规则");
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = baseInAndOutRuleMapper.insertUseGeneratedKeys(record);

        BaseHtInAndOutRule baseHtInAndOutRule = new BaseHtInAndOutRule();
        BeanUtils.copyProperties(record,baseHtInAndOutRule);
        baseHtInAndOutRuleMapper.insertSelective(baseHtInAndOutRule);

        List<BaseInAndOutRuleDet> baseInAndOutRuleDets = new ArrayList<>();
        List<BaseHtInAndOutRuleDet> baseHtInAndOutRuleDets = new ArrayList<>();
        for (BaseInAndOutRuleDetDto baseInAndOutRuleDetDto : record.getBaseInAndOutRuleDets()) {
            if(StringUtils.isEmpty(baseInAndOutRuleDetDto.getStoredProcedureName())){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请选择规则");
            }
            if(StringUtils.isEmpty(baseInAndOutRuleDetDto.getPriority())){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请输入优先级");
            }

            //查询出入参数
            List<InOutParamsDto> inOutParamsDtos = baseInAndOutRuleMapper.findInOutParamMode(baseInAndOutRuleDetDto.getStoredProcedureName());
            if(inOutParamsDtos.size()>0){
                Map<String,List<InOutParamsDto>> map = inOutParamsDtos.stream().collect(Collectors.groupingBy(InOutParamsDto::getParameterMode));
                Set<String> set = map.keySet();
                for (String s : set) {
                    StringBuffer sb = new StringBuffer();
                    List<InOutParamsDto> outParamsDtos = map.get(s);
                    int i=0;
                    for (InOutParamsDto outParamsDto : outParamsDtos) {
                        sb.append(outParamsDto.getParameterName());
                        if(i!=outParamsDtos.size()){
                            sb.append(",");
                        }
                        i++;
                    }
                    if(s.equals("IN")){
                        baseInAndOutRuleDetDto.setInParameters(sb.toString());
                    }else if(s.equals("OUT")){
                        baseInAndOutRuleDetDto.setOutParameters(sb.toString());
                    }
                }
            }
            baseInAndOutRuleDetDto.setInAndOutRuleId(record.getInAndOutRuleId());
            baseInAndOutRuleDetDto.setCreateTime(new Date());
            baseInAndOutRuleDetDto.setCreateUserId(sysUser.getUserId());
            baseInAndOutRuleDetDto.setModifiedTime(new Date());
            baseInAndOutRuleDetDto.setModifiedUserId(sysUser.getUserId());
            baseInAndOutRuleDetDto.setOrgId(sysUser.getOrganizationId());
            baseInAndOutRuleDets.add(baseInAndOutRuleDetDto);

            BaseHtInAndOutRuleDet baseHtInAndOutRuleDet = new BaseHtInAndOutRuleDet();
            BeanUtils.copyProperties(baseInAndOutRuleDetDto,baseHtInAndOutRuleDet);
            baseHtInAndOutRuleDets.add(baseHtInAndOutRuleDet);
        }
        if(baseInAndOutRuleDets.size()>0){
            baseInAndOutRuleDetMapper.insertList(baseInAndOutRuleDets);
            baseHtInAndOutRuleDetMapper.insertList(baseHtInAndOutRuleDets);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseInAndOutRule entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(entity.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请选择仓库");
        }

        //查询是否存在相同记录
        Example example = new Example(BaseInAndOutRule.class);
        example.createCriteria().andEqualTo("warehouseId",entity.getWarehouseId()).andEqualTo("category",entity.getCategory()).andNotEqualTo("inAndOutRuleId",entity.getInAndOutRuleId());
        List<BaseInAndOutRule> list = baseInAndOutRuleMapper.selectByExample(example);
        if(list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库已经存在规则");
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());

        example = new Example(BaseInAndOutRuleDet.class);
        example.createCriteria().andEqualTo("inAndOutRuleId",entity.getInAndOutRuleId());
        baseInAndOutRuleDetMapper.deleteByExample(example);

        BaseHtInAndOutRule baseHtInAndOutRule = new BaseHtInAndOutRule();
        BeanUtils.copyProperties(entity,baseHtInAndOutRule);
        baseHtInAndOutRuleMapper.insertSelective(baseHtInAndOutRule);

        List<BaseInAndOutRuleDet> baseInAndOutRuleDets = new ArrayList<>();
        List<BaseHtInAndOutRuleDet> baseHtInAndOutRuleDets = new ArrayList<>();
        for (BaseInAndOutRuleDetDto baseInAndOutRuleDetDto : entity.getBaseInAndOutRuleDets()) {
            if(StringUtils.isEmpty(baseInAndOutRuleDetDto.getStoredProcedureName())){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请选择规则");
            }
            if(StringUtils.isEmpty(baseInAndOutRuleDetDto.getPriority())){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"请输入优先级");
            }
            //查询出入参数
            List<InOutParamsDto> inOutParamsDtos = baseInAndOutRuleMapper.findInOutParamMode(baseInAndOutRuleDetDto.getStoredProcedureName());
            if(inOutParamsDtos.size()>0){
                Map<String,List<InOutParamsDto>> map = inOutParamsDtos.stream().collect(Collectors.groupingBy(InOutParamsDto::getParameterMode));
                Set<String> set = map.keySet();
                for (String s : set) {
                    StringBuffer sb = new StringBuffer();
                    List<InOutParamsDto> outParamsDtos = map.get(s);
                    int i=0;
                    for (InOutParamsDto outParamsDto : outParamsDtos) {
                        sb.append(outParamsDto.getParameterName());
                        if(i!=outParamsDtos.size()){
                            sb.append(",");
                        }
                        i++;
                    }
                    if(s.equals("IN")){
                        baseInAndOutRuleDetDto.setInParameters(sb.toString());
                    }else if(s.equals("OUT")){
                        baseInAndOutRuleDetDto.setOutParameters(sb.toString());
                    }
                }
            }
            baseInAndOutRuleDetDto.setInAndOutRuleId(entity.getInAndOutRuleId());
            baseInAndOutRuleDetDto.setCreateTime(new Date());
            baseInAndOutRuleDetDto.setCreateUserId(sysUser.getUserId());
            baseInAndOutRuleDetDto.setModifiedTime(new Date());
            baseInAndOutRuleDetDto.setModifiedUserId(sysUser.getUserId());
            baseInAndOutRuleDetDto.setOrgId(sysUser.getOrganizationId());
            baseInAndOutRuleDets.add(baseInAndOutRuleDetDto);

            BaseHtInAndOutRuleDet baseHtInAndOutRuleDet = new BaseHtInAndOutRuleDet();
            BeanUtils.copyProperties(baseInAndOutRuleDetDto,baseHtInAndOutRuleDet);
            baseHtInAndOutRuleDets.add(baseHtInAndOutRuleDet);
        }
        if(baseInAndOutRuleDets.size()>0){
            baseInAndOutRuleDetMapper.insertList(baseInAndOutRuleDets);
            baseHtInAndOutRuleDetMapper.insertList(baseHtInAndOutRuleDets);
        }

        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            BaseInAndOutRule baseInAndOutRule = baseInAndOutRuleMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInAndOutRule)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(BaseInAndOutRuleDet.class);
            example.createCriteria().andEqualTo("inAndOutRuleId",baseInAndOutRule.getInAndOutRuleId());
            baseInAndOutRuleDetMapper.deleteByExample(example);
        }
        return super.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseInAndOutRuleImport> list) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        Map<String,String > map = new HashMap<>();
        List<Map<String,String>> failMap = new ArrayList<>();  //记录操作失败行数
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        int i=0;
        for (BaseInAndOutRuleImport baseInAndOutRuleImport : list) {
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseCode(baseInAndOutRuleImport.getWarehouseCode());
            searchBaseWarehouse.setCodeQueryMark(1);
            List<BaseWarehouse> baseWarehouses = baseWarehouseMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouse));
            if(baseWarehouses.size()==0){
                map.put(baseInAndOutRuleImport.getWarehouseCode(),"仓库名称、计划入库单号不鞥呢为空");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            BaseInAndOutRule baseInAndOutRule = new BaseInAndOutRule();
            switch (baseInAndOutRuleImport.getCategory()){
                case "入库":
                    baseInAndOutRule.setCategory((byte)1);
                    break;
                case "出库":
                    baseInAndOutRule.setCategory((byte)2);
                    break;
                case "批次":
                    baseInAndOutRule.setCategory((byte)3);
            }
            baseInAndOutRule.setCreateTime(new Date());
            baseInAndOutRule.setCreateUserId(sysUser.getUserId());
            baseInAndOutRule.setModifiedTime(new Date());
            baseInAndOutRule.setModifiedUserId(sysUser.getUserId());
            baseInAndOutRule.setOrgId(sysUser.getOrganizationId());
            baseInAndOutRule.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            baseInAndOutRuleMapper.insertSelective(baseInAndOutRule);
        }

        //添加日志
        SysImportAndExportLog log = new SysImportAndExportLog();
        log.setSucceedCount(list.size() - fail.size());
        log.setFailCount(fail.size());
        log.setFailInfo(failMap.toString());
        log.setOperatorUserId(sysUser.getUserId());
        log.setTotalCount(list.size());
        log.setType((byte)1);
        securityFeignApi.add(log);

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public List<BaseHtInAndOutRuleDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return baseHtInAndOutRuleMapper.findList(map);
    }

    @Override
    public List<String> findView(Byte category) {
        String type = null;
        switch (category){
            case 1:
                type="IN";
                break;
            case 2:
                type="OUT";
                break;
            case 3:
                type="BA";
                break;
            default:
                type="IN";
        }
        return baseInAndOutRuleMapper.findView(type);
    }

    @Override
    public Long inRule(Long warehouseId, Long materialId, BigDecimal qty) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseInAndOutRule.class);
        example.createCriteria().andEqualTo("warehouseId",warehouseId);
        List<BaseInAndOutRule> list = baseInAndOutRuleMapper.selectByExample(example);
        list = list.stream().filter(x->x.getCategory()==1).collect(Collectors.toList());
        if(list.size()<1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未查询到仓库"+warehouseId+"维护的入库规则");
        }
        List<BaseInAndOutRuleDetDto> baseInAndOutRuleDetDtos = baseInAndOutRuleDetMapper.findList(ControllerUtil.dynamicCondition("inAndOutRuleId",list.get(0).getInAndOutRuleId()));
        //按优先级生序排序
        baseInAndOutRuleDetDtos = baseInAndOutRuleDetDtos.stream().sorted(Comparator.comparing(BaseInAndOutRuleDetDto::getPriority)).collect(Collectors.toList());
        Long storageId = null;
        for (BaseInAndOutRuleDetDto baseInAndOutRuleDetDto : baseInAndOutRuleDetDtos) {
            storageId = baseInAndOutRuleMapper.inRuleExecute(baseInAndOutRuleDetDto.getStoredProcedureName(),warehouseId, materialId, qty);

            //后续逻辑 计算库容
        }
        return storageId;
    }

    @Override
    public List<String> outRule(Long warehouseId, Long storageId, Long materialId, BigDecimal qty) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseInAndOutRule.class);
        example.createCriteria().andEqualTo("warehouseId",warehouseId);
        List<BaseInAndOutRule> list = baseInAndOutRuleMapper.selectByExample(example);
        list = list.stream().filter(x->x.getCategory()==1).collect(Collectors.toList());
        if(list.size()<1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未查询到仓库"+warehouseId+"维护的入库规则");
        }
        List<BaseInAndOutRuleDetDto> baseInAndOutRuleDetDtos = baseInAndOutRuleDetMapper.findList(ControllerUtil.dynamicCondition("inAndOutRuleId",list.get(0).getInAndOutRuleId()));
        //按优先级生序排序
        baseInAndOutRuleDetDtos = baseInAndOutRuleDetDtos.stream().sorted(Comparator.comparing(BaseInAndOutRuleDetDto::getPriority)).collect(Collectors.toList());
        List<String>  barcodeList = new ArrayList<>();
        for (BaseInAndOutRuleDetDto baseInAndOutRuleDetDto : baseInAndOutRuleDetDtos) {
            barcodeList = baseInAndOutRuleMapper.outRuleExecute(baseInAndOutRuleDetDto.getStoredProcedureName(),warehouseId, storageId, materialId, qty.intValue());
            //后续逻辑 计算库容
        }
        return barcodeList;
    }
}
