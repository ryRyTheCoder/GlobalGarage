package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.CreateGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateGarageResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class CreateGarageLambda extends LambdaActivityRunner<CreateGarageRequest, CreateGarageResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateGarageRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateGarageRequest> input, Context context) {
        return super.runActivity(
            () -> {
                // Extract request from the input
                CreateGarageRequest unauthenticatedRequest = input.fromBody(CreateGarageRequest.class);
                // Extract seller ID from user claims and add it to the request
                return input.fromUserClaims(claims ->
                        CreateGarageRequest.builder()
                                // Using "S" + 'sub' as the sellerId
                                .withSellerID("S" + (claims.get("sub")))
                                .withGarageName(unauthenticatedRequest.getGarageName())
                                .withStartDate(unauthenticatedRequest.getStartDate())
                                .withEndDate(unauthenticatedRequest.getEndDate())
                                .withLocation(unauthenticatedRequest.getLocation())
                                .withDescription(unauthenticatedRequest.getDescription())
                                .build()
                );
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateGarageActivity().handleRequest(request)
        );
    }
}
