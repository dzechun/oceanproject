package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNote;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryNote;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmDeliveryNoteDetMapper;
import com.fantechs.provider.srm.mapper.SrmDeliveryNoteMapper;
import com.fantechs.provider.srm.mapper.SrmHtDeliveryNoteMapper;
import com.fantechs.provider.srm.service.SrmDeliveryNoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class SrmDeliveryNoteServiceImpl extends BaseService<SrmDeliveryNote> implements SrmDeliveryNoteService {

    @Resource
    private SrmDeliveryNoteMapper srmDeliveryNoteMapper;
    @Resource
    private SrmHtDeliveryNoteMapper srmHtDeliveryNoteMapper;
    @Resource
    private SrmDeliveryNoteDetMapper srmDeliveryNoteDetMapper;

    @Override
    public List<SrmDeliveryNoteDto> findList(Map<String, Object> map) {
        return srmDeliveryNoteMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SrmDeliveryNote srmDeliveryNote) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        srmDeliveryNote.setCreateTime(new Date());
        srmDeliveryNote.setCreateUserId(user.getUserId());
        srmDeliveryNote.setModifiedTime(new Date());
        srmDeliveryNote.setModifiedUserId(user.getUserId());
        srmDeliveryNote.setStatus(StringUtils.isEmpty(srmDeliveryNote.getStatus())?1:srmDeliveryNote.getStatus());
        srmDeliveryNote.setAsnCode(getOdd());

        int i = srmDeliveryNoteMapper.insertUseGeneratedKeys(srmDeliveryNote);
        List<SrmDeliveryNoteDet> srmDeliveryNoteDets = srmDeliveryNote.getSrmDeliveryNoteDets();
        if (StringUtils.isNotEmpty(srmDeliveryNoteDets)){
            for (SrmDeliveryNoteDet srmDeliveryNoteDet : srmDeliveryNoteDets) {
                srmDeliveryNoteDet.setDeliveryNoteId(srmDeliveryNote.getDeliveryNoteId());
            }
            srmDeliveryNoteDetMapper.insertList(srmDeliveryNoteDets);
        }

        SrmHtDeliveryNote srmHtDeliveryNote = new SrmHtDeliveryNote();
        BeanUtils.copyProperties(srmDeliveryNote,srmHtDeliveryNote);
        srmHtDeliveryNoteMapper.insert(srmHtDeliveryNote);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SrmDeliveryNote srmDeliveryNote) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        srmDeliveryNote.setModifiedTime(new Date());
        srmDeliveryNote.setModifiedUserId(user.getUserId());

        SrmHtDeliveryNote srmHtDeliveryNote = new SrmHtDeliveryNote();
        BeanUtils.copyProperties(srmDeliveryNote,srmHtDeliveryNote);
        srmHtDeliveryNoteMapper.insert(srmHtDeliveryNote);

        return srmDeliveryNoteMapper.updateByPrimaryKeySelective(srmDeliveryNote);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SrmHtDeliveryNote> srmHtDeliveryNotes= new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            SrmDeliveryNote srmDeliveryNote = srmDeliveryNoteMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(srmDeliveryNote)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            SrmHtDeliveryNote srmHtDeliveryNote = new SrmHtDeliveryNote();
            BeanUtils.copyProperties(srmDeliveryNote,srmHtDeliveryNote);
            srmHtDeliveryNotes.add(srmHtDeliveryNote);
        }

        srmHtDeliveryNoteMapper.insertList(srmHtDeliveryNotes);

        return srmDeliveryNoteMapper.deleteByIds(ids);
    }

    /**
     * 生成ASN单号
     * @return
     */
    public String getOdd(){
        String before = "ASN";
        String amongst = new SimpleDateFormat("yyMMdd").format(new Date());
        SrmDeliveryNote srmDeliveryNote = srmDeliveryNoteMapper.getMax();
        String asnCode = before+amongst+"0000";
        if (StringUtils.isNotEmpty(srmDeliveryNote)){
            asnCode = srmDeliveryNote.getAsnCode();
        }
        Integer maxCode = Integer.parseInt(asnCode.substring(9, asnCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
