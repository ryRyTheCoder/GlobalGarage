package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.GlobalGarage.activity.request.CreateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateSellerResult;

public class CreateSellerLambda extends LambdaActivityRunner<CreateSellerRequest, CreateSellerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateSellerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateSellerRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    // Extract request from the input
                    CreateSellerRequest unauthenticatedRequest = input.fromBody(CreateSellerRequest.class);


                    return CreateSellerRequest.builder()
                            .withUsername(unauthenticatedRequest.getUsername())
                            .withEmail(unauthenticatedRequest.getEmail())
                            .withLocation(unauthenticatedRequest.getLocation())
                            .withContactInfo(unauthenticatedRequest.getContactInfo())
                            .build();
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateSellerActivity().handleRequest(request)
        );
    }
}