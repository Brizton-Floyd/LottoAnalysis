package com.lottoanalysis.ui.dashboardview;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.ui.homeview.HomeViewListener;

public interface LottoDashBoardListener {

    void performViewStartUp();

    void injectLottoDrawData();

    void loadEditableDrawView(Drawing drawing);

    void setListener(HomeViewListener homeViewListener);

    void renableTableView();
}
