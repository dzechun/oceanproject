package com.fantechs.provider.wms.storageBills.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.WmsStorageBillsDetDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.WmsStorageBillsDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.storageBills.service.WmsStorageBillsDetService;
import com.fantechs.provider.wms.storageBills.mapper.WmsStorageBillsDetMapper;
import com.fantechs.common.base.support.BaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 17:20
 * @Description: 仓库清单详情表接口实现
 * @Version: 1.0
 */
@Service
public class WmsStorageBillsDetServiceImpl extends BaseService<WmsStorageBillsDet>  implements WmsStorageBillsDetService{

     @Resource
     private WmsStorageBillsDetMapper wmsStorageBillsDetMapper;

    @Override
    public List<WmsStorageBillsDet> selectAll(Map<String,Object> map) {
        Example example = new Example(WmsStorageBillsDet.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("IsDelete",1).orIsNull("IsDelete");
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
        return wmsStorageBillsDetMapper.selectByExample(example);
    }

    @Override
    public List<WmsStorageBillsDet> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(WmsStorageBillsDet.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("IsDelete",1).orIsNull("IsDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    criteria.orLike(k,"%"+v+"%");
                }
            });
        }
        return wmsStorageBillsDetMapper.selectByExample(example);
    }

    @Override
    public WmsStorageBillsDet selectByKey(Object id) {
        WmsStorageBillsDet wmsStorageBillsDet = wmsStorageBillsDetMapper.selectByPrimaryKey(id);
        if(wmsStorageBillsDet != null && (wmsStorageBillsDet.getIsDelete() != null && wmsStorageBillsDet.getIsDelete() == 0)){
        wmsStorageBillsDet = null;
        }
        return wmsStorageBillsDet;
    }

    @Override
    public WmsStorageBillsDet selectByMap(Map<String,Object> map) {
        List<WmsStorageBillsDet> wmsStorageBillsDet = selectAll(map);
        if(StringUtils.isEmpty(wmsStorageBillsDet)){
            return null;
        }
        if(wmsStorageBillsDet.size()>1){
            return null;
        }
        return wmsStorageBillsDet.get(0);
    }

    @Override
    public int save(WmsStorageBillsDet wmsStorageBillsDet) {
        wmsStorageBillsDet.setCreateUserId(null);
        wmsStorageBillsDet.setCreateTime(new Date());
        wmsStorageBillsDet.setIsDelete((byte)1);
        return wmsStorageBillsDetMapper.insertUseGeneratedKeys(wmsStorageBillsDet);
    }

    @Override
    public int deleteByKey(Object id) {
        WmsStorageBillsDet wmsStorageBillsDet = new WmsStorageBillsDet();
        wmsStorageBillsDet.setStorageBillsDetId((Long)id);
        wmsStorageBillsDet.setIsDelete((byte)0);
        return update(wmsStorageBillsDet);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<WmsStorageBillsDet> wmsStorageBillsDets = selectAll(map);
        if (StringUtils.isNotEmpty(wmsStorageBillsDets)) {
            for (WmsStorageBillsDet wmsStorageBillsDet : wmsStorageBillsDets) {
                if(deleteByKey(wmsStorageBillsDet.getStorageBillsDetId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(WmsStorageBillsDet wmsStorageBillsDet) {
        wmsStorageBillsDet.setModifiedUserId(null);
        wmsStorageBillsDet.setModifiedTime(new Date());
        return wmsStorageBillsDetMapper.updateByPrimaryKeySelective(wmsStorageBillsDet);
    }

    @Override
    public String selectUserName(Object id) {
        return wmsStorageBillsDetMapper.selectUserName(id);
    }

    @Override
    public List<WmsStorageBillsDetDTO> selectDTOByBillId(Long billId) {
        return wmsStorageBillsDetMapper.selectDTOByBillId(billId);
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
