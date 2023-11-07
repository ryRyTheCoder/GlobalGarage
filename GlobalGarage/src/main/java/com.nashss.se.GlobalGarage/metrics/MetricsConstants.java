package com.nashss.se.GlobalGarage.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {

    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "GlobalGarageService";
    public static final String NAMESPACE_NAME = "GlobalGarageService";

    // Constants for Seller metrics
    public static final String CREATESELLER_SUCCESS_COUNT = "CreateSeller.Success.Count";
    public static final String CREATESELLER_FAIL_COUNT = "CreateSeller.Failure.Count";
    public static final String SELLER_NOTFOUND_COUNT = "GetSeller.SellerNotFoundException.Count";
}
