package com.lottoanalysis.ui.modifieddrawview;

import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;

public interface ModifiedDrawListener {

    void setListener(LottoDashBoardListener listener);

    void show();

    void invokeService();
}
