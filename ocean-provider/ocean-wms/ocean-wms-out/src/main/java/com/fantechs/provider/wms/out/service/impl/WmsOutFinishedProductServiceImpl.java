package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutFinishedProduct;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutFinishedProductDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutFinishedProductMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtFinishedProductMapper;
import com.fantechs.provider.wms.out.service.WmsOutFinishedProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/22.
 */
@Service
public class WmsOutFinishedProductServiceImpl extends BaseService<WmsOutFinishedProduct> implements WmsOutFinishedProductService {

     @Resource
     private WmsOutFinishedProductMapper wmsOutFinishedProductMapper;
     @Resource
     private WmsOutFinishedProductDetMapper wmsOutFinishedProductDetMapper;
     @Resource
     private WmsOutHtFinishedProductMapper wmsOutHtFinishedProductMapper;

    @Override
    public List<WmsOutFinishedProductDto> findList(Map<String, Object> map) {
        return wmsOutFinishedProductMapper.findList(map);
    }

    @Override
    public List<WmsOutHtFinishedProduct> findHTList(Map<String, Object> map) {
        return wmsOutHtFinishedProductMapper.finHTList(map);
    }

    @Override
    public int save(WmsOutFinishedProduct wmsOutFinishedProduct) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsOutFinishedProduct.getWmsOutFinishedProductDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutFinishedProduct.setFinishedProductCode(CodeUtils.getId("CPCK-"));
        wmsOutFinishedProduct.setTrafficType(StringUtils.isEmpty(wmsOutFinishedProduct.getTrafficType()) ? 0 : wmsOutFinishedProduct.getTrafficType());
        wmsOutFinishedProduct.setOutStatus(StringUtils.isEmpty(wmsOutFinishedProduct.getOutStatus()) ? 0 : wmsOutFinishedProduct.getOutStatus());
        wmsOutFinishedProduct.setStatus((byte)1);
        wmsOutFinishedProduct.setIsDelete((byte)1);
        wmsOutFinishedProduct.setCreateTime(new Date());
        wmsOutFinishedProduct.setCreateUserId(user.getUserId());

        int result = wmsOutFinishedProductMapper.insertUseGeneratedKeys(wmsOutFinishedProduct);

        //履历
        WmsOutHtFinishedProduct wmsOutHtFinishedProduct = new WmsOutHtFinishedProduct();
        BeanUtils.copyProperties(wmsOutFinishedProduct,wmsOutHtFinishedProduct);
        wmsOutHtFinishedProductMapper.insertSelective(wmsOutHtFinishedProduct);

        for (WmsOutFinishedProductDet wmsOutFinishedProductDet : wmsOutFinishedProduct.getWmsOutFinishedProductDetList()) {
            wmsOutFinishedProductDet.setCreateTime(new Date());
            wmsOutFinishedProductDet.setCreateUserId(user.getCreateUserId());
            wmsOutFinishedProductDet.setFinishedProductId(wmsOutFinishedProduct.getFinishedProductId());
            result = wmsOutFinishedProductDetMapper.insertSelective(wmsOutFinishedProductDet);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutFinishedProduct wmsOutFinishedProduct) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutFinishedProduct.setModifiedUserId(user.getUserId());
        wmsOutFinishedProduct.setModifiedTime(new Date());

        //履历
        WmsOutHtFinishedProduct wmsOutHtFinishedProduct = new WmsOutHtFinishedProduct();
        BeanUtils.copyProperties(wmsOutFinishedProduct,wmsOutHtFinishedProduct);
        wmsOutHtFinishedProductMapper.insertSelective(wmsOutHtFinishedProduct);

        if(!StringUtils.isEmpty(wmsOutFinishedProduct.getWmsOutFinishedProductDetList())){
            Example example = new Example(WmsOutFinishedProductDet.class);
            example.createCriteria().andEqualTo("finishedProductId",wmsOutFinishedProduct.getFinishedProductId());
            int result = wmsOutFinishedProductDetMapper.deleteByExample(example);
            if(result > 0){
                for (WmsOutFinishedProductDet wmsOutFinishedProductDet : wmsOutFinishedProduct.getWmsOutFinishedProductDetList()) {
                    wmsOutFinishedProductDet.setCreateTime(new Date());
                    wmsOutFinishedProductDet.setCreateUserId(user.getCreateUserId());
                    wmsOutFinishedProductDet.setFinishedProductId(wmsOutFinishedProduct.getFinishedProductId());
                    wmsOutFinishedProductDetMapper.insertSelective(wmsOutFinishedProductDet);
                }
            }else{
                throw new BizErrorException(ErrorCodeEnum.OPT20012000);
            }
        }
        return wmsOutFinishedProductMapper.updateByPrimaryKeySelective(wmsOutFinishedProduct);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutFinishedProduct wmsOutFinishedProduct = wmsOutFinishedProductMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutFinishedProduct)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //履历
            WmsOutHtFinishedProduct wmsOutHtFinishedProduct = new WmsOutHtFinishedProduct();
            BeanUtils.copyProperties(wmsOutFinishedProduct,wmsOutHtFinishedProduct);
            wmsOutHtFinishedProductMapper.insertSelective(wmsOutHtFinishedProduct);

        }

        return wmsOutFinishedProductMapper.deleteByIds(ids);
    }
}
