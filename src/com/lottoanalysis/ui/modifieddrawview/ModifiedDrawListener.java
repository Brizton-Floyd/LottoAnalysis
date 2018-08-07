package com.lottoanalysis.ui.modifieddrawview;

import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;

public interface ModifiedDrawListener {

    void setListener(LottoDashBoardListener listener);

    void show();

    void invokeService(CrudOperation crudOperation);
}
