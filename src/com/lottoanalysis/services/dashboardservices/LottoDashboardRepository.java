package com.lottoanalysis.services.dashboardservices;

import com.lottoanalysis.models.dashboard.ModifiedDrawModel;

import java.sql.SQLException;

public interface LottoDashboardRepository {

    boolean update(ModifiedDrawModel modifiedDrawModel);
}
