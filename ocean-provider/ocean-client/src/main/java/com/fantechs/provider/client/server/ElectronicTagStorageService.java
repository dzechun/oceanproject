package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.entity.SmtSorting;

import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
public interface ElectronicTagStorageService {

    int  sendElectronicTagStorage(List<SmtSorting> Sorting);
}