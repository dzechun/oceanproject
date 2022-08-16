package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseSignatureImport;
import com.fantechs.common.base.general.entity.basic.BaseSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface BaseSignatureService extends IService<BaseSignature> {

  List<BaseSignature> findList(Map<String, Object> map);
  Map<String, Object> importExcel(List<BaseSignatureImport> baseSignatureImports);
}
