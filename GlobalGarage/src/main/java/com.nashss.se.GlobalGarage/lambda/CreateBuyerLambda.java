package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.GlobalGarage.activity.request.CreateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateBuyerResult;

public class CreateBuyerLambda extends LambdaActivityRunner<CreateBuyerRequest, CreateBuyerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateBuyerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateBuyerRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateBuyerRequest unauthenticatedRequest = input.fromBody(CreateBuyerRequest.class);
                    final String testBuyerId = "5669b497-e324-44b8-9d6b-5bf27c182b9c"; // Replace with actual logic to extract buyer ID

                    // Bypassing claim extraction and directly using testBuyerId for local testing
                    return CreateBuyerRequest.builder()
                            .withUsername(unauthenticatedRequest.getUsername())
                            .withEmail(unauthenticatedRequest.getEmail())
                            .withLocation(unauthenticatedRequest.getLocation())
                            .withBuyerId(testBuyerId) // Assuming the buyer ID is set here for testing
                            .build();
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateBuyerActivity().handleRequest(request)
        );
    }
}

