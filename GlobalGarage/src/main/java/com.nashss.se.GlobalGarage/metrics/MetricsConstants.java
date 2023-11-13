package com.nashss.se.GlobalGarage.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {

    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "GlobalGarageService";
    public static final String NAMESPACE_NAME = "GlobalGarageService";

    // Constants for Seller metrics
    public static final String CREATE_SELLER_SUCCESS_COUNT = "CreateSeller.Success.Count";
    public static final String CREATE_SELLER_FAIL_COUNT = "CreateSeller.Failure.Count";
    public static final String SELLER_NOTFOUND_COUNT = "GetSeller.SellerNotFoundException.Count";

    // Constants for Garage metrics
    public static final String CREATE_GARAGE_SUCCESS_COUNT = "CreateGarage.Success.Count";
    public static final String CREATE_GARAGE_FAIL_COUNT = "CreateGarage.Failure.Count";
    public static final String GARAGE_NOTFOUND_COUNT = "GetGarage.GarageNotFoundException.Count";

    // Constants for Buyer metrics
    public static final String CREATE_BUYER_SUCCESS_COUNT = "CreateBuyer.Success.Count";
    public static final String CREATE_BUYER_FAIL_COUNT = "CreateBuyer.Failure.Count";
    public static final String BUYER_NOTFOUND_COUNT = "GetBuyer.BuyerNotFoundException.Count";

    // Constants for Item metrics
    public static final String CREATE_ITEM_SUCCESS_COUNT = "CreateItem.Success.Count";
    public static final String CREATE_ITEM_FAIL_COUNT = "CreateItem.Failure.Count";
    public static final String ITEM_NOTFOUND_COUNT = "GetItem.ItemNotFoundException.Count";


    // Constants for updating records
    public static final String UPDATE_SELLER_SUCCESS_COUNT = "UpdateSeller.Success.Count";
    public static final String UPDATE_SELLER_FAIL_COUNT = "UpdateSeller.Failure.Count";
    public static final String UPDATE_BUYER_SUCCESS_COUNT = "UpdateBuyer.Success.Count";
    public static final String UPDATE_BUYER_FAIL_COUNT = "UpdateBuyer.Failure.Count";
    public static final String UPDATE_ITEM_SUCCESS_COUNT = "UpdateItem.Success.Count";
    public static final String UPDATE_ITEM_FAIL_COUNT = "UpdateItem.Failure.Count";
    public static final String UPDATE_GARAGE_SUCCESS_COUNT = "UpdateGarage.Success.Count";
    public static final String UPDATE_GARAGE_FAIL_COUNT = "UpdateGarage.Failure.Count";

}
