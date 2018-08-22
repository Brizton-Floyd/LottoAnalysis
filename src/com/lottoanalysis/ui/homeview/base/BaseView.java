package com.lottoanalysis.ui.homeview.base;

import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.scene.layout.AnchorPane;

public abstract class BaseView<P extends BasePresenter> extends AnchorPane {

    private P presenter;

    protected P getPresenter() {
        return presenter;
    }

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    public abstract void setUpUi();

    public abstract AnchorPane display();

}
