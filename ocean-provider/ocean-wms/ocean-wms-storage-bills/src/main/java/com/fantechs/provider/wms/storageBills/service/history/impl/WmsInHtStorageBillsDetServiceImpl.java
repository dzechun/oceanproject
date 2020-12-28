package com.fantechs.provider.wms.storageBills.service.history.impl;

import com.fantechs.common.base.entity.apply.history.WmsInHtStorageBillsDet;
import com.fantechs.common.base.dto.apply.history.WmsInHtStorageBillsDetDTO;
import com.fantechs.provider.wms.storageBills.service.history.WmsInHtStorageBillsDetService;
import com.fantechs.provider.wms.storageBills.mapper.history.WmsInHtStorageBillsDetMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月25日 14:01
 * @Description: 履历仓库清单详情表接口实现
 * @Version: 1.0
 */
@Service
public class WmsInHtStorageBillsDetServiceImpl extends BaseService<WmsInHtStorageBillsDet>  implements WmsInHtStorageBillsDetService {

     @Resource
     private WmsInHtStorageBillsDetMapper wmsHtStorageBillsDetMapper;

    @Override
    public List<WmsInHtStorageBillsDet> selectAll(Map<String,Object> map) {
        Example example = new Example(WmsInHtStorageBillsDet.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    switch (k){
                        case "Name":
                            criteria.andLike(k,"%"+v+"%");
                            break;
                        default :
                            criteria.andEqualTo(k,v);
                            break;
                    }
                }
            });
        }
        return wmsHtStorageBillsDetMapper.selectByExample(example);
    }

    @Override
    public List<WmsInHtStorageBillsDet> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(WmsInHtStorageBillsDet.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    criteria.orLike(k,"%"+v+"%");
                }
            });
        }
        return wmsHtStorageBillsDetMapper.selectByExample(example);
    }

    @Override
    public WmsInHtStorageBillsDet selectByKey(Object id) {
        WmsInHtStorageBillsDet wmsHtStorageBillsDet = wmsHtStorageBillsDetMapper.selectByPrimaryKey(id);
        if(wmsHtStorageBillsDet != null && (wmsHtStorageBillsDet.getIsDelete() != null && wmsHtStorageBillsDet.getIsDelete() == 0)){
        wmsHtStorageBillsDet = null;
        }
        return wmsHtStorageBillsDet;
    }

    @Override
    public WmsInHtStorageBillsDet selectByMap(Map<String,Object> map) {
        List<WmsInHtStorageBillsDet> wmsHtStorageBillsDet = selectAll(map);
        if(StringUtils.isEmpty(wmsHtStorageBillsDet)){
            return null;
        }
        if(wmsHtStorageBillsDet.size()>1){
            return null;
        }
        return wmsHtStorageBillsDet.get(0);
    }

    @Override
    public int save(WmsInHtStorageBillsDet wmsHtStorageBillsDet) {
        wmsHtStorageBillsDet.setCreateUserId(null);
        wmsHtStorageBillsDet.setIsDelete((byte)1);
        return wmsHtStorageBillsDetMapper.insertUseGeneratedKeys(wmsHtStorageBillsDet);
    }

    @Override
    public int deleteByKey(Object id) {
        WmsInHtStorageBillsDet wmsHtStorageBillsDet = new WmsInHtStorageBillsDet();
        wmsHtStorageBillsDet.setHtStorageBillsDetId((long)id);
        wmsHtStorageBillsDet.setIsDelete((byte)0);
        return update(wmsHtStorageBillsDet);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<WmsInHtStorageBillsDet> wmsHtStorageBillsDets = selectAll(map);
        if (StringUtils.isNotEmpty(wmsHtStorageBillsDets)) {
            for (WmsInHtStorageBillsDet wmsHtStorageBillsDet : wmsHtStorageBillsDets) {
                if(deleteByKey(wmsHtStorageBillsDet.getHtStorageBillsDetId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(WmsInHtStorageBillsDet wmsHtStorageBillsDet) {
        wmsHtStorageBillsDet.setModifiedUserId(null);
        return wmsHtStorageBillsDetMapper.updateByPrimaryKeySelective(wmsHtStorageBillsDet);
    }

    @Override
    public String selectUserName(Object id) {
        return wmsHtStorageBillsDetMapper.selectUserName(id);
    }

   @Override
   public List<WmsInHtStorageBillsDetDTO> selectFilterAll(Long storageBillsId) {
       return wmsHtStorageBillsDetMapper.selectFilterAll(storageBillsId);
   }


    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
