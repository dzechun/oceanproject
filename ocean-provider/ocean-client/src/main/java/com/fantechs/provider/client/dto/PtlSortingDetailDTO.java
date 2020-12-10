package com.fantechs.provider.client.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/12/10.
 */
@Data
public class PtlSortingDetailDTO implements Serializable{
    private static final long serialVersionUID = 6304317132206501702L;
    private  String cwWarehouseCode;
    private String materialCode;
}
