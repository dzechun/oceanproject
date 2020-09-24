package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/24.
 */

public interface SmtSignatureService extends IService<SmtSignature> {

  int insert(SmtSignature smtSignature);

  int batchDel(String ids);

  int updateById(SmtSignature smtSignature);

  SmtSignature selectByKey(Long id);

  List<SmtSignature> findList(SearchSmtSignature searchSmtSignature);
}
