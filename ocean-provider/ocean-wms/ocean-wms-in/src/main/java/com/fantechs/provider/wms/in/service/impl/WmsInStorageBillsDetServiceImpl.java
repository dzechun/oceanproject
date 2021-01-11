package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.WmsInStorageBillsDetDTO;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBillsDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.in.service.WmsInStorageBillsDetService;
import com.fantechs.provider.wms.in.mapper.WmsInStorageBillsDetMapper;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.service.history.WmsInHtStorageBillsDetService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 17:20
 * @Description: 仓库清单详情表接口实现
 * @Version: 1.0
 */
@Service
public class WmsInStorageBillsDetServiceImpl extends BaseService<WmsInStorageBillsDet>  implements WmsInStorageBillsDetService {

     @Resource
     private WmsInStorageBillsDetMapper wmsStorageBillsDetMapper;
     @Resource
     private WmsInHtStorageBillsDetService wmsHtStorageBillsDetService;

    @Override
    public List<WmsInStorageBillsDet> selectAll(Map<String,Object> map) {
        Example example = new Example(WmsInStorageBillsDet.class);
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
        return wmsStorageBillsDetMapper.selectByExample(example);
    }

    @Override
    public List<WmsInStorageBillsDet> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(WmsInStorageBillsDet.class);
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
        return wmsStorageBillsDetMapper.selectByExample(example);
    }

    @Override
    public WmsInStorageBillsDet selectByKey(Object id) {
        WmsInStorageBillsDet wmsStorageBillsDet = wmsStorageBillsDetMapper.selectByPrimaryKey(id);
        if(wmsStorageBillsDet != null && (wmsStorageBillsDet.getIsDelete() != null && wmsStorageBillsDet.getIsDelete() == 0)){
        wmsStorageBillsDet = null;
        }
        return wmsStorageBillsDet;
    }

    @Override
    public WmsInStorageBillsDet selectByMap(Map<String,Object> map) {
        List<WmsInStorageBillsDet> wmsStorageBillsDet = selectAll(map);
        if(StringUtils.isEmpty(wmsStorageBillsDet)){
            return null;
        }
        if(wmsStorageBillsDet.size()>1){
            return null;
        }
        return wmsStorageBillsDet.get(0);
    }

    @Override
    public int save(WmsInStorageBillsDet wmsStorageBillsDet) {
        wmsStorageBillsDet.setCreateUserId(null);
        wmsStorageBillsDet.setIsDelete((byte)1);
        if(wmsStorageBillsDetMapper.insertSelective(wmsStorageBillsDet)<=0){
            return 0;
        }
        recordHistory(wmsStorageBillsDet.getStorageBillsDetId(),"新增");
        return 1;
    }

    @Override
    public int deleteByKey(Object id) {
        WmsInStorageBillsDet wmsStorageBillsDet = new WmsInStorageBillsDet();
        wmsStorageBillsDet.setStorageBillsDetId((Long)id);
        wmsStorageBillsDet.setIsDelete((byte)0);
        return update(wmsStorageBillsDet);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<WmsInStorageBillsDet> wmsStorageBillsDets = selectAll(map);
        if (StringUtils.isNotEmpty(wmsStorageBillsDets)) {
            for (WmsInStorageBillsDet wmsStorageBillsDet : wmsStorageBillsDets) {
                if(deleteByKey(wmsStorageBillsDet.getStorageBillsDetId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(WmsInStorageBillsDet wmsStorageBillsDet) {
        wmsStorageBillsDet.setModifiedUserId(null);
        if(wmsStorageBillsDetMapper.updateByPrimaryKeySelective(wmsStorageBillsDet)<=0){
            return 0;
        }
        recordHistory(wmsStorageBillsDet.getStorageBillsDetId(),"更新");
        return 1;
    }

    @Override
    public String selectUserName(Object id) {
        return wmsStorageBillsDetMapper.selectUserName(id);
    }

    @Override
    public List<WmsInStorageBillsDetDTO> selectFilterAll(Long storageBillsId) {
        return wmsStorageBillsDetMapper.selectFilterAll(storageBillsId);
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

    /**
     * 记录操作历史
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        WmsInHtStorageBillsDet wmsHtStorageBillsDet = new WmsInHtStorageBillsDet();
        wmsHtStorageBillsDet.setOperation(operation);
        WmsInStorageBillsDet wmsStorageBillsDet = selectByKey(id);
        if (StringUtils.isEmpty(wmsStorageBillsDet)){
            return;
        }
        BeanUtils.autoFillEqFields(wmsStorageBillsDet,wmsHtStorageBillsDet);
        wmsHtStorageBillsDetService.save(wmsHtStorageBillsDet);
    }
}
