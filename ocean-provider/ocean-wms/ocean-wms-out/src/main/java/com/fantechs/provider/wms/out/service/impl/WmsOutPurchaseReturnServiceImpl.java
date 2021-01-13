package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPurchaseReturn;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPurchaseReturnMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPurchaseReturnDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPurchaseReturnMapper;
import com.fantechs.provider.wms.out.service.WmsOutPurchaseReturnService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */
@Service
public class WmsOutPurchaseReturnServiceImpl  extends BaseService<WmsOutPurchaseReturn> implements WmsOutPurchaseReturnService {

    @Resource
    private WmsOutPurchaseReturnMapper wmsOutPurchaseReturnMapper;
    @Resource
    private WmsOutHtPurchaseReturnMapper wmsOutHtPurchaseReturnMapper;
    @Resource
    private WmsOutPurchaseReturnDetMapper wmsOutPurchaseReturnDetMapper;

    @Override
    public int save(WmsOutPurchaseReturn wmsOutPurchaseReturn) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsOutPurchaseReturn.getWmsOutPurchaseReturnDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutPurchaseReturn.setPurchaseReturnCode(CodeUtils.getId("CGTH-"));
        wmsOutPurchaseReturn.setReturnStatus(StringUtils.isEmpty(wmsOutPurchaseReturn.getReturnStatus()) ? 0 : wmsOutPurchaseReturn.getReturnStatus());
        wmsOutPurchaseReturn.setStatus((byte)1);
        wmsOutPurchaseReturn.setIsDelete((byte)1);
        wmsOutPurchaseReturn.setCreateTime(new Date());
        wmsOutPurchaseReturn.setCreateUserId(user.getUserId());

        int result = wmsOutPurchaseReturnMapper.insertUseGeneratedKeys(wmsOutPurchaseReturn);

        WmsOutHtPurchaseReturn wmsOutHtPurchaseReturn = new WmsOutHtPurchaseReturn();
        BeanUtils.copyProperties(wmsOutPurchaseReturn,wmsOutHtPurchaseReturn);
        wmsOutHtPurchaseReturnMapper.insertSelective(wmsOutHtPurchaseReturn);

        for (WmsOutPurchaseReturnDet wmsOutPurchaseReturnDet : wmsOutPurchaseReturn.getWmsOutPurchaseReturnDetList()) {
            wmsOutPurchaseReturnDet.setCreateTime(new Date());
            wmsOutPurchaseReturnDet.setCreateUserId(user.getCreateUserId());
            wmsOutPurchaseReturnDet.setPurchaseReturnId(wmsOutPurchaseReturn.getPurchaseReturnId());
            result = wmsOutPurchaseReturnDetMapper.insertSelective(wmsOutPurchaseReturnDet);
        }

        return result;
    }

    @Override
    public int update(WmsOutPurchaseReturn wmsOutPurchaseReturn) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutPurchaseReturn.setModifiedUserId(user.getUserId());
        wmsOutPurchaseReturn.setModifiedTime(new Date());

        WmsOutHtPurchaseReturn wmsOutHtPurchaseReturn = new WmsOutHtPurchaseReturn();
        BeanUtils.copyProperties(wmsOutPurchaseReturn,wmsOutHtPurchaseReturn);
        wmsOutHtPurchaseReturnMapper.insertSelective(wmsOutHtPurchaseReturn);

        if(!StringUtils.isEmpty(wmsOutPurchaseReturn.getWmsOutPurchaseReturnDetList())){
            Example example = new Example(WmsOutPurchaseReturnDet.class);
            example.createCriteria().andEqualTo("purchaseReturnId",wmsOutPurchaseReturn.getPurchaseReturnId());
            int result = wmsOutPurchaseReturnDetMapper.deleteByExample(example);
            if(result > 0){
                for (WmsOutPurchaseReturnDet wmsOutFinishedProductDet : wmsOutPurchaseReturn.getWmsOutPurchaseReturnDetList()) {
                    wmsOutFinishedProductDet.setCreateTime(new Date());
                    wmsOutFinishedProductDet.setCreateUserId(user.getCreateUserId());
                    wmsOutFinishedProductDet.setPurchaseReturnId(wmsOutPurchaseReturn.getPurchaseReturnId());
                    wmsOutPurchaseReturnDetMapper.insertSelective(wmsOutFinishedProductDet);
                }
            }else{
                throw new BizErrorException(ErrorCodeEnum.OPT20012000);
            }
        }

        return wmsOutPurchaseReturnMapper.updateByPrimaryKeySelective(wmsOutPurchaseReturn);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }


        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutPurchaseReturn wmsOutPurchaseReturn = wmsOutPurchaseReturnMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutPurchaseReturn)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsOutHtPurchaseReturn wmsOutHtPurchaseReturn = new WmsOutHtPurchaseReturn();
            BeanUtils.copyProperties(wmsOutPurchaseReturn,wmsOutHtPurchaseReturn);
            wmsOutHtPurchaseReturnMapper.insertSelective(wmsOutHtPurchaseReturn);
        }

        return wmsOutPurchaseReturnMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsOutPurchaseReturnDto> findList(Map<String, Object> dynamicConditionByEntity) {
        return wmsOutPurchaseReturnMapper.findList(dynamicConditionByEntity);
    }

    @Override
    public List<WmsOutHtPurchaseReturn> findHTList(Map<String, Object> dynamicConditionByEntity) {
        return wmsOutHtPurchaseReturnMapper.findHTList(dynamicConditionByEntity);
    }
}
