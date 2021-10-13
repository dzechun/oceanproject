package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderMapper;
import com.fantechs.provider.guest.eng.service.EngContractQtyOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@Service
public class EngContractQtyOrderServiceImpl extends BaseService<EngContractQtyOrder> implements EngContractQtyOrderService {

    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<EngContractQtyOrderDto> findList(Map<String, Object> map) {
        SysUser user= getUser();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",user.getOrganizationId());
        }

        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(user.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if(StringUtils.isNotEmpty(list.getData()))
            map.put("supplierId",list.getData().get(0).getSupplierId());
        return engContractQtyOrderMapper.findList(map);
    }

    @Override
    public int saveByApi(EngContractQtyOrder engContractQtyOrder) {
        //设置已发量为0 未发量=采购量
        //engContractQtyOrder.setIssuedQty("0");
        engContractQtyOrder.setNotIssueQty(engContractQtyOrder.getPurQty());

        Example example = new Example(EngContractQtyOrder.class);
        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("contractCode",engContractQtyOrder.getContractCode());
//        criteria.andEqualTo("dominantTermCode",engContractQtyOrder.getDominantTermCode());
//        criteria.andEqualTo("materialCode",engContractQtyOrder.getMaterialCode());

        // option1 合同量单明细ID
        criteria.andEqualTo("option1",engContractQtyOrder.getOption1());
        criteria.andEqualTo("orgId",engContractQtyOrder.getOrgId());

        //根据合同号和主项次号查找现有数据 存在则更新 不存在则新增
        EngContractQtyOrder ecqoExist=engContractQtyOrderMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(ecqoExist)){
            engContractQtyOrder.setContractQtyOrderId(ecqoExist.getContractQtyOrderId());
            engContractQtyOrder.setModifiedTime(new Date());
            return engContractQtyOrderMapper.updateByPrimaryKeySelective(engContractQtyOrder);

        }
        else {
            engContractQtyOrder.setCreateTime(new Date());
            engContractQtyOrder.setCreateUserId((long) 1);
            engContractQtyOrder.setModifiedUserId((long) 1);
            engContractQtyOrder.setModifiedTime(new Date());
            engContractQtyOrder.setIsDelete((byte) 1);
            return engContractQtyOrderMapper.insertSelective(engContractQtyOrder);
        }

    }

    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }
}
