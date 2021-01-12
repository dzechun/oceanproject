package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNote;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtShippingNote;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtShippingNoteMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteMapper;
import com.fantechs.provider.wms.out.service.WmsOutShippingNoteService;
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
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutShippingNoteServiceImpl  extends BaseService<WmsOutShippingNote> implements WmsOutShippingNoteService {

    @Resource
    private WmsOutShippingNoteMapper wmsOutShippingNoteMapper;
    @Resource
    private WmsOutShippingNoteDetMapper wmsOutShippingNoteDetMapper;
    @Resource
    private WmsOutHtShippingNoteMapper wmsOutHtShippingNoteMapper;

    @Override
    public List<WmsOutShippingNoteDto> findList(Map<String, Object> map) {
        return wmsOutShippingNoteMapper.findList(map);
    }

    @Override
    public int save(WmsOutShippingNote wmsOutShippingNote) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsOutShippingNote.getWmsOutShippingNoteDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutShippingNote.setShippingNoteCode(CodeUtils.getId("CHTZ"));
        wmsOutShippingNote.setTrafficType(StringUtils.isEmpty(wmsOutShippingNote.getTrafficType()) ? 0 : wmsOutShippingNote.getTrafficType());
        wmsOutShippingNote.setCurrencyType(StringUtils.isEmpty(wmsOutShippingNote.getCurrencyType()) ? 0 : wmsOutShippingNote.getCurrencyType());
        wmsOutShippingNote.setOutStatus((byte)0);
        wmsOutShippingNote.setStatus((byte)1);
        wmsOutShippingNote.setIsDelete((byte)1);
        wmsOutShippingNote.setCreateTime(new Date());
        wmsOutShippingNote.setCreateUserId(user.getUserId());
        wmsOutShippingNote.setOrganizationId(user.getOrganizationId());

        int result = wmsOutShippingNoteMapper.insertUseGeneratedKeys(wmsOutShippingNote);

        //履历
        WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
        BeanUtils.copyProperties(wmsOutShippingNote,wmsOutHtShippingNote);
        wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);

        for (WmsOutShippingNoteDet wmsOutShippingNoteDet : wmsOutShippingNote.getWmsOutShippingNoteDetList()) {

            wmsOutShippingNoteDet.setShippingNoteId(wmsOutShippingNote.getShippingNoteId());
            wmsOutShippingNoteDet.setCreateTime(new Date());
            wmsOutShippingNoteDet.setCreateUserId(user.getUserId());
            wmsOutShippingNoteDetMapper.insertSelective(wmsOutShippingNoteDet);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutShippingNote wmsOutShippingNote) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutShippingNote.setModifiedUserId(user.getUserId());
        wmsOutShippingNote.setModifiedTime(new Date());
        //履历
        WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
        BeanUtils.copyProperties(wmsOutShippingNote,wmsOutHtShippingNote);
        wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);

        if(!StringUtils.isEmpty(wmsOutShippingNote.getWmsOutShippingNoteDetList())){
            Example example = new Example(WmsOutShippingNoteDet.class);
            example.createCriteria().andEqualTo("shippingNoteId",wmsOutShippingNote.getShippingNoteId());
            int result = wmsOutShippingNoteDetMapper.deleteByExample(example);
            if(result > 0){
                for (WmsOutShippingNoteDet wmsOutShippingNoteDet : wmsOutShippingNote.getWmsOutShippingNoteDetList()) {
                    wmsOutShippingNoteDet.setShippingNoteId(wmsOutShippingNote.getShippingNoteId());
                    wmsOutShippingNoteDet.setCreateTime(new Date());
                    wmsOutShippingNoteDet.setCreateUserId(user.getUserId());
                    wmsOutShippingNoteDetMapper.insertSelective(wmsOutShippingNoteDet);
                }
            }else{
                throw new BizErrorException(ErrorCodeEnum.OPT20012000);
            }
        }

        return wmsOutShippingNoteMapper.updateByPrimaryKeySelective(wmsOutShippingNote);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutShippingNote wmsOutShippingNote = wmsOutShippingNoteMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutShippingNote)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //履历
            WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
            BeanUtils.copyProperties(wmsOutHtShippingNote,wmsOutHtShippingNote);
            wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);
        }
        return wmsOutHtShippingNoteMapper.deleteByIds(ids);
    }
}
