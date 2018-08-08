package com.lottoanalysis.ui.modifieddrawview;

import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;

import java.util.Map;

public interface ModifiedDrawListener {

    void setListener(LottoDashBoardListener listener);

    void show();

    void invokeService(CrudOperation crudOperation);

    void updateModelList(Map<Integer, String> valAndKeys);
}
