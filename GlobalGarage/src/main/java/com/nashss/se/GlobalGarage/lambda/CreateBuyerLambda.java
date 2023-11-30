package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.CreateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateBuyerResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateBuyerLambda extends LambdaActivityRunner<CreateBuyerRequest, CreateBuyerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateBuyerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateBuyerRequest> input, Context context) {
        return super.runActivity(
            () -> {
                CreateBuyerRequest unauthenticatedRequest = input.fromBody(CreateBuyerRequest.class);

                return input.fromUserClaims(claims -> CreateBuyerRequest.builder()
                        .withUsername(unauthenticatedRequest.getUsername())
                        .withEmail(unauthenticatedRequest.getEmail())
                        .withLocation(unauthenticatedRequest.getLocation())
                        .withBuyerId(claims.get("sub"))
                        .build()
                );
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateBuyerActivity().handleRequest(request)
        );
    }
}
