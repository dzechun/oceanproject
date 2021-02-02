package com.fantechs.provider.client.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lfz on 2020/12/10.
 */
@Data
public class PtlSortingDTO  implements Serializable {
    private static final long serialVersionUID = -722605634717752977L;
    private String sortingCode;
    private List<PtlSortingDetailDTO> ptlSortingDetailDTOList;
    private String user;
}
