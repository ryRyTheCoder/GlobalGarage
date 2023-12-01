package com.nashss.se.GlobalGarage.dependency;

import com.nashss.se.GlobalGarage.activity.CreateBuyerActivity;
import com.nashss.se.GlobalGarage.activity.CreateGarageActivity;
import com.nashss.se.GlobalGarage.activity.CreateItemActivity;
import com.nashss.se.GlobalGarage.activity.CreateMessageActivity;
import com.nashss.se.GlobalGarage.activity.CreateSellerActivity;
import com.nashss.se.GlobalGarage.activity.GetAllGaragesActivity;
import com.nashss.se.GlobalGarage.activity.GetGaragesBySellerActivity;
import com.nashss.se.GlobalGarage.activity.GetOneGarageActivity;
import com.nashss.se.GlobalGarage.activity.GetSellerActivity;

import dagger.Component;

import javax.inject.Singleton;
/**
 * Dagger component for providing dependency injection in the Global Garage Service.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides an instance of CreateBuyerActivity.
     *
     * @return CreateBuyerActivity instance.
     */
    CreateBuyerActivity provideCreateBuyerActivity();

    /**
     * Provides an instance of CreateGarageActivity.
     *
     * @return CreateGarageActivity instance.
     */
    CreateGarageActivity provideCreateGarageActivity();

    /**
     * Provides an instance of CreateItemActivity.
     *
     * @return CreateItemActivity instance.
     */
    CreateItemActivity provideCreateItemActivity();

    /**
     * Provides an instance of CreateMessageActivity.
     *
     * @return CreateMessageActivity instance.
     */
    CreateMessageActivity provideCreateMessageActivity();

    /**
     * Provides an instance of CreateSellerActivity.
     *
     * @return CreateSellerActivity instance.
     */
    CreateSellerActivity provideCreateSellerActivity();

    /**
     * Provides an instance of GetAllGaragesActivity.
     *
     * @return GetAllGaragesActivity instance.
     */
    GetAllGaragesActivity provideGetAllGaragesActivity();

    /**
     * Provides an instance of GetGaragesBySellerActivity.
     *
     * @return GetGaragesBySellerActivity instance.
     */
    GetGaragesBySellerActivity provideGetAllGaragesBySellerActivity();

    /**
     * Provides an instance of GetOneGarageActivity.
     *
     * @return GetOneGarageActivity instance.
     */
    GetOneGarageActivity provideGetOneGarageActivity();

    /**
     * Provides an instance of GetSellerActivity.
     *
     * @return GetSellerActivity instance.
     */
    GetSellerActivity provideGetSellerActivity();
}