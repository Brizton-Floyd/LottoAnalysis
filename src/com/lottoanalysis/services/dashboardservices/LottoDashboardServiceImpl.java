package com.lottoanalysis.services.dashboardservices;

import com.lottoanalysis.models.dashboard.ModifiedDrawModel;

public class LottoDashboardServiceImpl implements LottoDashboardService {


    private ModifiedDrawModel modifiedDrawModel;
    private LottoDashboardRepository lottoDashboardRepository;

    public LottoDashboardServiceImpl(ModifiedDrawModel modifiedDrawModel, LottoDashboardRepository lottoDashboardRepository) {

        this.modifiedDrawModel = modifiedDrawModel;
        this.lottoDashboardRepository = lottoDashboardRepository;

    }

    @Override
    public boolean executeUpdate() {

        return lottoDashboardRepository.update(modifiedDrawModel);

    }
}
