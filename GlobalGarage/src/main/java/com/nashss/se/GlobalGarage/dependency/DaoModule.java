package com.nashss.se.GlobalGarage.dependency;

import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.DynamoDbClientProvider;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger Module providing dependencies for DAO classes.
 */
@Module
public class DaoModule {
    /**
     * Provides a DynamoDBMapper singleton instance.
     *
     * @return DynamoDBMapper object
     */
    @Singleton
    @Provides
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient(Regions.US_EAST_2));
    }
    /**
     * Provides a ModelConverter singleton instance.
     *
     * @return ModelConverter object
     */
    @Singleton
    @Provides
    public ModelConverter provideModelConverter() {
        return new ModelConverter();
    }
}
