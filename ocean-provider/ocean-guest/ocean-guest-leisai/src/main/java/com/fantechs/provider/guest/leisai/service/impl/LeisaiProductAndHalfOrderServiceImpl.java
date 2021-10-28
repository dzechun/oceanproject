package com.fantechs.provider.guest.leisai.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.leisai.LeisaiProductAndHalfOrderDto;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProcessInputOrderImport;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProductAndHalfOrderImport;
import com.fantechs.common.base.general.entity.leisai.LeisaiProductAndHalfOrder;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProductAndHalfOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.leisai.mapper.LeisaiHtProductAndHalfOrderMapper;
import com.fantechs.provider.guest.leisai.mapper.LeisaiProductAndHalfOrderMapper;
import com.fantechs.provider.guest.leisai.service.LeisaiProductAndHalfOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/10/27.
 */
@Service
public class LeisaiProductAndHalfOrderServiceImpl extends BaseService<LeisaiProductAndHalfOrder> implements LeisaiProductAndHalfOrderService {

    @Resource
    private LeisaiProductAndHalfOrderMapper leisaiProductAndHalfOrderMapper;

    @Resource
    private LeisaiHtProductAndHalfOrderMapper leisaiHtProductAndHalfOrderMapper;

    @Override
    public List<LeisaiProductAndHalfOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return leisaiProductAndHalfOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(LeisaiProductAndHalfOrder leisaiProductAndHalfOrder) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        leisaiProductAndHalfOrder.setCreateUserId(currentUser.getUserId());
        leisaiProductAndHalfOrder.setCreateTime(new Date());
        leisaiProductAndHalfOrder.setModifiedUserId(currentUser.getUserId());
        leisaiProductAndHalfOrder.setModifiedTime(new Date());
        leisaiProductAndHalfOrder.setOrgId(currentUser.getOrganizationId());
        leisaiProductAndHalfOrderMapper.insertUseGeneratedKeys(leisaiProductAndHalfOrder);

        //新增履历表信息
        LeisaiHtProductAndHalfOrder leisaiHtProcessInputOrder = new LeisaiHtProductAndHalfOrder();
        BeanUtils.copyProperties(leisaiProductAndHalfOrder, leisaiHtProcessInputOrder);
        int i = leisaiHtProductAndHalfOrderMapper.insertSelective(leisaiHtProcessInputOrder);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(LeisaiProductAndHalfOrder leisaiProductAndHalfOrder) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        leisaiProductAndHalfOrder.setModifiedUserId(currentUser.getUserId());
        leisaiProductAndHalfOrder.setModifiedTime(new Date());
        leisaiProductAndHalfOrder.setOrgId(currentUser.getOrganizationId());
        leisaiProductAndHalfOrderMapper.updateByPrimaryKey(leisaiProductAndHalfOrder);

        //新增工序历史信息
        LeisaiHtProductAndHalfOrder leisaiHtProcessInputOrder = new LeisaiHtProductAndHalfOrder();
        BeanUtils.copyProperties(leisaiProductAndHalfOrder, leisaiHtProcessInputOrder);
        int i = leisaiHtProductAndHalfOrderMapper.insertSelective(leisaiHtProcessInputOrder);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<LeisaiProductAndHalfOrderImport> leisaiProductAndHalfOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        LinkedList<LeisaiProductAndHalfOrder> list = new LinkedList<>();
        LinkedList<LeisaiHtProductAndHalfOrder> htList = new LinkedList<>();
        LinkedList<LeisaiProcessInputOrderImport> processInputOrderImportList = new LinkedList<>();

        if (StringUtils.isNotEmpty(leisaiProductAndHalfOrderImports)){
            for (LeisaiProductAndHalfOrderImport productAndHalfOrderImport : leisaiProductAndHalfOrderImports) {
                LeisaiProductAndHalfOrder productAndHalfOrder = new LeisaiProductAndHalfOrder();
                BeanUtils.copyProperties(productAndHalfOrderImport, productAndHalfOrder);
                productAndHalfOrder.setCreateTime(new Date());
                productAndHalfOrder.setCreateUserId(user.getUserId());
                productAndHalfOrder.setModifiedTime(new Date());
                productAndHalfOrder.setModifiedUserId(user.getUserId());
                productAndHalfOrder.setOrgId(user.getOrganizationId());
                productAndHalfOrder.setStatus((byte)1);
                productAndHalfOrder.setIsDelete((byte)1);
                list.add(productAndHalfOrder);
            }

            success = leisaiProductAndHalfOrderMapper.insertList(list);

            for (LeisaiProductAndHalfOrder productAndHalfOrder : list) {
                LeisaiHtProductAndHalfOrder leisaiHtProcessInputOrder = new LeisaiHtProductAndHalfOrder();
                BeanUtils.copyProperties(productAndHalfOrder, leisaiHtProcessInputOrder);
                htList.add(leisaiHtProcessInputOrder);
            }

            leisaiHtProductAndHalfOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
