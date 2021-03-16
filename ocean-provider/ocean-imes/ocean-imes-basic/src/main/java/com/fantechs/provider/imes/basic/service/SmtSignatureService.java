package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.imports.SmtSignatureImport;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface SmtSignatureService extends IService<SmtSignature> {

  List<SmtSignature> findList(SearchSmtSignature searchSmtSignature);
  Map<String, Object> importExcel(List<SmtSignatureImport> smtSignatureImports);
}
