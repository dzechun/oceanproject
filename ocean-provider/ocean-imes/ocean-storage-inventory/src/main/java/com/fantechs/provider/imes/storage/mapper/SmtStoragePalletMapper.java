package com.fantechs.provider.imes.storage.mapper;

import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtStoragePalletMapper extends MyMapper<SmtStoragePallet> {
    List<SmtStoragePalletDto> findList(Map<String, Object> map);
}