package com.nashss.se.GlobalGarage.dependency;

import com.nashss.se.GlobalGarage.activity.*;


import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Global Garage Service.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {



    /**
     * Provides the relevant activity.
     * @return CreateSellerActivity
     */
    CreateSellerActivity provideCreateSellerActivity();
    CreateGarageActivity provideCreateGarageActivity();
    CreateBuyerActivity provideCreateBuyerActivity();
    CreateItemActivity provideCreateItemActivity();
    GetAllGaragesActivity provideGetAllGaragesActivity();

}
