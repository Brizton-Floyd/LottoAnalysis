package com.lottoanalysis.ui.presenters.base;

import com.lottoanalysis.models.drawhistory.DrawHistoryModelChanged;
import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.ui.homeview.base.BaseView;

public abstract class BasePresenter<V extends BaseView, M extends DrawModelBase> implements DrawHistoryModelChanged {

    private V view;
    private M model;

    public BasePresenter(V view, M model) {
        this.view = view;
        this.model = model;
    }

    public V getView() {
        return view;
    }

    protected M getModel() {
        return model;
    }

}
