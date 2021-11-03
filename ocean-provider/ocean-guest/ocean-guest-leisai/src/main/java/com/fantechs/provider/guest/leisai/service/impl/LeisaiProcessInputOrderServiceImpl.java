package com.fantechs.provider.guest.leisai.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.leisai.LeisaiProcessInputOrderDto;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProcessInputOrderImport;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProductAndHalfOrderImport;
import com.fantechs.common.base.general.entity.leisai.LeisaiProcessInputOrder;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProcessInputOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.leisai.mapper.LeisaiHtProcessInputOrderMapper;
import com.fantechs.provider.guest.leisai.mapper.LeisaiProcessInputOrderMapper;
import com.fantechs.provider.guest.leisai.service.LeisaiProcessInputOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */
@Service
public class LeisaiProcessInputOrderServiceImpl extends BaseService<LeisaiProcessInputOrder> implements LeisaiProcessInputOrderService {

    @Resource
    private LeisaiProcessInputOrderMapper leisaiProcessInputOrderMapper;
    @Resource
    private LeisaiHtProcessInputOrderMapper leisaiHtProcessInputOrderMapper;

    @Override
    public List<LeisaiProcessInputOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return leisaiProcessInputOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(LeisaiProcessInputOrder processInputOrder) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        processInputOrder.setCreateUserId(currentUser.getUserId());
        processInputOrder.setCreateTime(new Date());
        processInputOrder.setModifiedUserId(currentUser.getUserId());
        processInputOrder.setModifiedTime(new Date());
        processInputOrder.setOrgId(currentUser.getOrganizationId());
        leisaiProcessInputOrderMapper.insertUseGeneratedKeys(processInputOrder);

        //新增履历表信息
        LeisaiHtProcessInputOrder leisaiHtProcessInputOrder = new LeisaiHtProcessInputOrder();
        BeanUtils.copyProperties(processInputOrder, leisaiHtProcessInputOrder);
        int i = leisaiHtProcessInputOrderMapper.insertSelective(leisaiHtProcessInputOrder);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(LeisaiProcessInputOrder processInputOrder) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        processInputOrder.setModifiedUserId(currentUser.getUserId());
        processInputOrder.setModifiedTime(new Date());
        processInputOrder.setOrgId(currentUser.getOrganizationId());
        leisaiProcessInputOrderMapper.updateByPrimaryKey(processInputOrder);

        //新增工序历史信息
        LeisaiHtProcessInputOrder leisaiHtProcessInputOrder = new LeisaiHtProcessInputOrder();
        BeanUtils.copyProperties(processInputOrder, leisaiHtProcessInputOrder);
        int i = leisaiHtProcessInputOrderMapper.insertSelective(leisaiHtProcessInputOrder);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<LeisaiProcessInputOrderImport> processInputOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        LinkedList<LeisaiProcessInputOrder> list = new LinkedList<>();
        LinkedList<LeisaiHtProcessInputOrder> htList = new LinkedList<>();
        LinkedList<LeisaiProductAndHalfOrderImport> processInputOrderImportList = new LinkedList<>();

        if (StringUtils.isNotEmpty(processInputOrderImports)){
            for (LeisaiProcessInputOrderImport processInputOrderImport : processInputOrderImports) {
                LeisaiProcessInputOrder processInputOrder = new LeisaiProcessInputOrder();
                BeanUtils.copyProperties(processInputOrderImport, processInputOrder);
                processInputOrder.setCreateTime(new Date());
                processInputOrder.setCreateUserId(user.getUserId());
                processInputOrder.setModifiedTime(new Date());
                processInputOrder.setModifiedUserId(user.getUserId());
                processInputOrder.setOrgId(user.getOrganizationId());
                processInputOrder.setStatus((byte)1);
                list.add(processInputOrder);
            }

            success = leisaiProcessInputOrderMapper.insertList(list);

            for (LeisaiProcessInputOrder processInputOrder : list) {
                LeisaiHtProcessInputOrder leisaiHtProcessInputOrder = new LeisaiHtProcessInputOrder();
                BeanUtils.copyProperties(processInputOrder, leisaiHtProcessInputOrder);
                htList.add(leisaiHtProcessInputOrder);
            }

            leisaiHtProcessInputOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<LeisaiProcessInputOrder> list) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int i = 1;
        if (StringUtils.isNotEmpty(list)){
            List<LeisaiProcessInputOrder> addList = new ArrayList<>();
            List<LeisaiHtProcessInputOrder> addHtList = new ArrayList<>();
            for (LeisaiProcessInputOrder processInputOrder : list) {
                processInputOrder.setCreateUserId(currentUser.getUserId());
                processInputOrder.setCreateTime(new Date());
                processInputOrder.setModifiedUserId(currentUser.getUserId());
                processInputOrder.setModifiedTime(new Date());
                processInputOrder.setOrgId(currentUser.getOrganizationId());
                addList.add(processInputOrder);

                //新增履历表信息
                LeisaiHtProcessInputOrder leisaiHtProcessInputOrder = new LeisaiHtProcessInputOrder();
                BeanUtils.copyProperties(processInputOrder, leisaiHtProcessInputOrder);
                addHtList.add(leisaiHtProcessInputOrder);
            }
            if(StringUtils.isNotEmpty(addList))
                i= leisaiProcessInputOrderMapper.insertList(addList);
            if(StringUtils.isNotEmpty(addHtList))
                leisaiHtProcessInputOrderMapper.insertList(addHtList);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchUpdate(List<LeisaiProcessInputOrder> list) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int i = 1;
        if (StringUtils.isNotEmpty(list)){
            List<LeisaiProcessInputOrder> addList = new ArrayList<>();
            List<LeisaiHtProcessInputOrder> addHtList = new ArrayList<>();
            for (LeisaiProcessInputOrder processInputOrder : list) {
                processInputOrder.setModifiedUserId(currentUser.getUserId());
                processInputOrder.setModifiedTime(new Date());
                processInputOrder.setOrgId(currentUser.getOrganizationId());
                leisaiProcessInputOrderMapper.updateByPrimaryKeySelective(processInputOrder);

                //新增履历表信息
                LeisaiHtProcessInputOrder leisaiHtProcessInputOrder = new LeisaiHtProcessInputOrder();
                BeanUtils.copyProperties(processInputOrder, leisaiHtProcessInputOrder);
                leisaiHtProcessInputOrderMapper.updateByPrimaryKeySelective(leisaiHtProcessInputOrder);
            }
        }
        return i;
    }



}
