package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.GlobalGarage.activity.request.CreateGarageRequest;

import com.nashss.se.GlobalGarage.activity.results.CreateGarageResult;

public class CreateGarageLambda extends LambdaActivityRunner<CreateGarageRequest, CreateGarageResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateGarageRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateGarageRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateGarageRequest unauthenticatedRequest = input.fromBody(CreateGarageRequest.class);
                    final String testSellerId = "5669b497-e324-44b8-9d6b-5bf27c182b9c"; // Test seller ID for local testing

                    // Use testSellerId for local testing, replace with claim extraction in production
                    return CreateGarageRequest.builder()
                            .withSellerID(testSellerId)
                            .withGarageName(unauthenticatedRequest.getGarageName())
                            .withStartDate(unauthenticatedRequest.getStartDate())
                            .withEndDate(unauthenticatedRequest.getEndDate())
                            .withLocation(unauthenticatedRequest.getLocation())
                            .withDescription(unauthenticatedRequest.getDescription())
                            .build();
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateGarageActivity().handleRequest(request)
        );
    }
}

//        return super.runActivity(
//                () -> {
//                // Extract request from the input
//                CreateGarageRequest unauthenticatedRequest = input.fromBody(CreateGarageRequest.class);
//        // Extract seller ID from user claims and add it to the request
//        return input.fromUserClaims(claims ->
//        CreateGarageRequest.builder()
//        .withSellerID(claims.get("sub")) // Using 'sub' as the sellerId
//        .withGarageName(unauthenticatedRequest.getGarageName())
//        .withStartDate(unauthenticatedRequest.getStartDate())
//        .withEndDate(unauthenticatedRequest.getEndDate())
//        .withLocation(unauthenticatedRequest.getLocation())
//        .withDescription(unauthenticatedRequest.getDescription())
//        .build()
//        );
//        },
//        (request, serviceComponent) ->
//        serviceComponent.provideCreateGarageActivity().handleRequest(request)
//        );