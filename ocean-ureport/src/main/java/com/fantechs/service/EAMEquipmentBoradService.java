package com.fantechs.service;

import com.fantechs.entity.EAMEquipmentBorad;
import com.fantechs.entity.search.SearchProLineBoard;

import java.text.ParseException;
import java.util.List;

public interface EAMEquipmentBoradService {
    List<EAMEquipmentBorad> findList(SearchProLineBoard searchProLineBoard) throws ParseException;
}
