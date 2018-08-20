package com.lottoanalysis.ui.dashboardview;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.homeview.HomeViewListener;
import com.lottoanalysis.ui.presenters.HomeViewPresenter;

public interface LottoDashBoardListener {

    void handleViewEvent(String operation, Drawing drawing);

}
