package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.general.entity.esop.history.EsopHtEquipment;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.mapper.EsopEquipmentMapper;
import com.fantechs.provider.esop.mapper.EsopHtEquipmentMapper;
import com.fantechs.provider.esop.service.EsopEquipmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EsopEquipmentServiceImpl extends BaseService<EsopEquipment> implements EsopEquipmentService {

    @Resource
    private EsopEquipmentMapper esopEquipmentMapper;
    @Resource
    private EsopHtEquipmentMapper esopHtEquipmentMapper;


    @Override
    public List<EsopEquipmentDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = getUser();
            map.put("orgId", user.getOrganizationId());
        }
        return esopEquipmentMapper.findList(map);
    }

    @Override
    public List<EsopHtEquipment> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return esopHtEquipmentMapper.findHtList(map);
    }

    @Override
    public int batchUpdate(List<EsopEquipment> list) {
        return esopEquipmentMapper.batchUpdate(list);
    }

/*
    @Override
    public EsopEquipment detailByIp(String ip) {
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentIp",ip);
        EsopEquipment EsopEquipment = esopEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(EsopEquipment)){
            throw new BizErrorException("未查询到ip对应的设备信息");
        }
        return EsopEquipment;
    }
*/

    @Override
    public EsopEquipment detailByMacAddress(String macAddress) {
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentMacAddress",macAddress);
        EsopEquipment EsopEquipment = esopEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(EsopEquipment)){
            throw new BizErrorException("未查询到mac地址对应的设备信息");
        }
        return EsopEquipment;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EsopEquipment record) {
        SysUser user = getUser();
        record.setOrgId(user.getOrganizationId());
        check(record);

        if(StringUtils.isNotEmpty(record.getEquipmentSeqNum())) {
            Example numExample = new Example(EsopEquipment.class);
            Example.Criteria numCriteria = numExample.createCriteria();
            numCriteria.andEqualTo("equipmentSeqNum", record.getEquipmentSeqNum());
            numCriteria.andEqualTo("proLineId", record.getProLineId());
            numCriteria.andEqualTo("orgId", record.getOrgId());
            if (StringUtils.isNotEmpty(esopEquipmentMapper.selectOneByExample(numExample))) {
                throw new BizErrorException("同一产线的设备序号不能重复");
            }
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        esopEquipmentMapper.insertUseGeneratedKeys(record);

        EsopHtEquipment EsopHtEquipment = new EsopHtEquipment();
        BeanUtils.copyProperties(record, EsopHtEquipment);
        int i = esopHtEquipmentMapper.insertSelective(EsopHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EsopEquipment entity) {
        SysUser user = getUser();
        check(entity);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = esopEquipmentMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        EsopHtEquipment EsopHtEquipment = new EsopHtEquipment();
        BeanUtils.copyProperties(entity, EsopHtEquipment);
        esopHtEquipmentMapper.insertSelective(EsopHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        getUser();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EsopEquipment EsopEquipment = esopEquipmentMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(EsopEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EsopHtEquipment EsopHtEquipment = new EsopHtEquipment();
            BeanUtils.copyProperties(EsopEquipment, EsopHtEquipment);
        }

        return esopEquipmentMapper.deleteByIds(ids);
    }

    public void check(EsopEquipment entity){
        getUser();
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", entity.getEquipmentCode());
        if(StringUtils.isNotEmpty(entity.getEquipmentId())){
            criteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
        }
        EsopEquipment EsopEquipment = esopEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(EsopEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentMacAddress())){
            Example macExample = new Example(EsopEquipment.class);
            Example.Criteria macCriteria = macExample.createCriteria();
            macCriteria.andEqualTo("equipmentMacAddress", entity.getEquipmentMacAddress());
            if(StringUtils.isNotEmpty(entity.getEquipmentId())){
                macCriteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
            }
            if (StringUtils.isNotEmpty(esopEquipmentMapper.selectOneByExample(macExample))) {
                throw new BizErrorException("设备mac地址不能重复");
            }
        }
    }


    @Override
    public List<EsopEquipmentDto> findNoGroup(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return esopEquipmentMapper.findNoGroup(map);
    }



    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

/*
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EsopEquipmentImport> esopEquipmentImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EsopEquipment> list = new LinkedList<>();
        *//*LinkedList<EsopEquipmentImport> equipmentImports = new LinkedList<>();*//*
        for (int i = 0; i < esopEquipmentImports.size(); i++) {
            EsopEquipmentImport esopEquipmentImport = esopEquipmentImports.get(i);

            String ip = esopEquipmentImport.getEquipmentIp();
            String macAddress = esopEquipmentImport.getEquipmentMacAddress();
            String seqNum = esopEquipmentImport.getEquipmentSeqNum();
            String code = esopEquipmentImport.getEquipmentCode();
            String name = esopEquipmentImport.getEquipmentName();
            if (StringUtils.isEmpty(
                    ip,macAddress,seqNum,code,name
            )) {
                fail.add(i + 4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EsopEquipment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("equipmentCode", code);
            List<EsopEquipment> strList = esopEquipmentMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(strList)) {
                fail.add(i + 4);
                continue;
            }
            example.clear();
            criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("equipmentMacAddress", macAddress);
            List<EsopEquipment> strList1 = esopEquipmentMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(strList1)) {
                fail.add(i + 4);
                continue;
            }

            //判断产线是否存在
            SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
            searchBaseProLine.setOrgId(currentUser.getOrganizationId());
            searchBaseProLine.setProName(esopEquipmentImport.getProLineName());
            List<BaseProLine> proLineData = easeFeignApi.findList(searchBaseProLine).getData();
            if (StringUtils.isEmpty(proLineData)) {
                fail.add(i + 4);
                continue;
            }

            //判断车间是否存在
            SearchBaseWorkShop searchBaseWorkShop = new SearchBaseWorkShop();
            searchBaseWorkShop.setOrgId(currentUser.getOrganizationId());
            searchBaseWorkShop.setWorkShopName(esopEquipmentImport.getWorkShopName());
            searchBaseWorkShop.setWorkShopId(proLineData.get(0).getWorkShopId());
            List<BaseWorkShopDto> workShopData = easeFeignApi.findWorkShopList(searchBaseWorkShop).getData();
            if (StringUtils.isEmpty(workShopData) ) {
                fail.add(i + 4);
                continue;
            }



            //判断工序是否存在
            SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
            searchBaseProcess.setOrgId(currentUser.getOrganizationId());
            searchBaseProcess.setProcessName(esopEquipmentImport.getProcessName());
            List<BaseProcess>  processData = easeFeignApi.findProcessList(searchBaseProcess).getData();
            if (StringUtils.isEmpty(processData)) {
                fail.add(i + 4);
                continue;
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(esopEquipmentImports)){
                for (EsopEquipmentImport esopImport : esopEquipmentImports) {
                    if (esopImport.getEquipmentCode().equals(code)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i + 4);
                continue;
            }
            EsopEquipment esopEquipment = new EsopEquipment();
            BeanUtils.copyProperties(esopEquipmentImport, esopEquipment);
            esopEquipment.setFactoryId(workShopData.get(0).getFactoryId());
            esopEquipment.setWorkShopId(workShopData.get(0).getWorkShopId());
            esopEquipment.setProLineId(proLineData.get(0).getProLineId());
            esopEquipment.setProcessId(processData.get(0).getProcessId());
            esopEquipment.setCreateTime(new Date());
            esopEquipment.setCreateUserId(currentUser.getUserId());
            esopEquipment.setModifiedTime(new Date());
            esopEquipment.setModifiedUserId(currentUser.getUserId());
            esopEquipment.setOrgId(currentUser.getOrganizationId());
            esopEquipment.setStatus((byte)1);
            list.add(esopEquipment);
        }

            success = esopEquipmentMapper.insertList(list);

            *//*for (BaseProcess baseProcess : list) {
                BaseHtProcess baseHtProcess = new BaseHtProcess();
                BeanUtils.copyProperties(baseProcess, baseHtProcess);
                htList.add(baseHtProcess);
            }

            baseHtProcessMapper.insertList(htList);*//*
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行数", fail);
        return resultMap;
    }*/
}
