package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.UpdateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.UpdateBuyerResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateBuyerLambda
        extends LambdaActivityRunner<UpdateBuyerRequest, UpdateBuyerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateBuyerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateBuyerRequest> input, Context context) {
        return super.runActivity(
            () -> {
                UpdateBuyerRequest unauthenticatedRequest = input.fromBody(UpdateBuyerRequest.class);
                return input.fromUserClaims(claims ->
                        UpdateBuyerRequest.builder()
                                .withUsername(unauthenticatedRequest.getUsername())
                                .withEmail(unauthenticatedRequest.getEmail())
                                .withLocation(unauthenticatedRequest.getLocation())
                                .withBuyerID("B" + claims.get("sub"))
                                .build()
                );
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateBuyerActivity().handleRequest(request)
        );
    }
}
