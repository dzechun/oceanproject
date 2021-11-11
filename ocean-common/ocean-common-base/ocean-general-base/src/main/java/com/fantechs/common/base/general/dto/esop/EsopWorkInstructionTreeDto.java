package com.fantechs.common.base.general.dto.esop;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EsopWorkInstructionTreeDto implements Serializable {

    /**
     * WI序号
     */
    private String workInstructionSeqNums;

    private List<EsopWorkInstructionDto> esopWorkInstructionDtos;

}
