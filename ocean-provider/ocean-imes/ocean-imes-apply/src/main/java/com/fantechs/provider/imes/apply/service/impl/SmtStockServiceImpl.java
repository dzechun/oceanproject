package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtStockDto;
import com.fantechs.common.base.entity.apply.SmtStock;
import com.fantechs.common.base.entity.apply.SmtStockDet;
import com.fantechs.common.base.entity.apply.search.SearchSmtStock;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtStockDetMapper;
import com.fantechs.provider.imes.apply.mapper.SmtStockMapper;
import com.fantechs.provider.imes.apply.service.SmtStockService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
@Service
public class SmtStockServiceImpl  extends BaseService<SmtStock> implements SmtStockService {

    @Resource
    private SmtStockMapper smtStockMapper;
    @Resource
    private SmtStockDetMapper smtStockDetMapper;

    @Override
    public List<SmtStockDto> findList(SearchSmtStock searchSmtStock) {
        return smtStockMapper.findList(searchSmtStock);
    }
}
