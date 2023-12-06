package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.UpdateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.UpdateSellerResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateSellerLambda
        extends LambdaActivityRunner<UpdateSellerRequest, UpdateSellerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateSellerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateSellerRequest> input, Context context) {
        return super.runActivity(
            () -> {
                UpdateSellerRequest unauthenticatedRequest = input.fromBody(UpdateSellerRequest.class);
                return input.fromUserClaims(claims ->
                    UpdateSellerRequest.builder()
                            .withUsername(unauthenticatedRequest.getUsername())
                            .withEmail(unauthenticatedRequest.getEmail())
                            .withLocation(unauthenticatedRequest.getLocation())
                            .withContactInfo(unauthenticatedRequest.getContactInfo())
                            .withSellerID("S" + claims.get("sub"))
                            .build()
                );
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateSellerActivity().handleRequest(request)
        );
    }
}
