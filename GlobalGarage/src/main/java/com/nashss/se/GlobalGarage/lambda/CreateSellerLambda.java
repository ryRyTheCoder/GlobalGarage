package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.CreateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateSellerResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateSellerLambda extends LambdaActivityRunner<CreateSellerRequest, CreateSellerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateSellerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateSellerRequest> input, Context context) {

        return super.runActivity(
            () -> {
                CreateSellerRequest unauthenticatedRequest = input.fromBody(CreateSellerRequest.class);
                return input.fromUserClaims(claims ->
                        CreateSellerRequest.builder()
                                .withUsername(unauthenticatedRequest.getUsername())
                                .withEmail(unauthenticatedRequest.getEmail())
                                .withLocation(unauthenticatedRequest.getLocation())
                                .withContactInfo(unauthenticatedRequest.getContactInfo())
                                // Using 'sub' as sellerId
                                .withSellerId(claims.get("sub"))
                                .build()
                        );
            },
            (request, serviceComponent) ->
                                    serviceComponent.provideCreateSellerActivity().handleRequest(request)
    );
    }
}
