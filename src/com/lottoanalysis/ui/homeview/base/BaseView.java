package com.lottoanalysis.ui.homeview.base;

import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
