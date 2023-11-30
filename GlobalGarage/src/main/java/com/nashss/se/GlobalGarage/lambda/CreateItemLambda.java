package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.CreateItemRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateItemResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateItemLambda extends LambdaActivityRunner<CreateItemRequest, CreateItemResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateItemRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateItemRequest> input, Context context) {
        return super.runActivity(
            () -> {
                // Extract the body of the request
                CreateItemRequest unauthenticatedRequest = input.fromBody(CreateItemRequest.class);

                // Construct the CreateItemRequest with extracted data
                return CreateItemRequest.builder()
                        .withSellerID("5669b497-e324-44b8-9d6b-5bf27c182b9c")
                        .withGarageID("12636ea9-c110-4026-bc00-c6bd4af93ed4")
                        .withName(unauthenticatedRequest.getName())
                        .withDescription(unauthenticatedRequest.getDescription())
                        .withPrice(unauthenticatedRequest.getPrice())
                        .withCategory(unauthenticatedRequest.getCategory())
                        .withImages(unauthenticatedRequest.getImages())
                        .build();
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateItemActivity().handleRequest(request)
        );
    }
}
