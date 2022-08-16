package com.fantechs.service;


import com.fantechs.entity.ProLineBoardModel;
import com.fantechs.entity.search.SearchProLineBoard;

public interface ProLineBoardService {

    ProLineBoardModel findList(SearchProLineBoard searchProLineBoard);
}
