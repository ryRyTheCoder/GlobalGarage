package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.CreateItemRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateItemResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class CreateItemLambda extends LambdaActivityRunner<CreateItemRequest, CreateItemResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateItemRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateItemRequest> input, Context context) {
        return super.runActivity(
            () -> {
                // Extracting garageId from path parameters
                Map<String, String> pathParameters = input.getPathParameters();
                String garageId = pathParameters.get("garageId");

                // Extracting item details from the request body
                CreateItemRequest unauthenticatedRequest = input.fromBody(CreateItemRequest.class);

                // Constructing CreateItemRequest with sellerId from user claims
                return input.fromUserClaims(claims ->
                        CreateItemRequest.builder()
                                .withSellerID("S" + claims.get("sub"))
                                .withGarageID(garageId)
                                .withName(unauthenticatedRequest.getName())
                                .withDescription(unauthenticatedRequest.getDescription())
                                .withPrice(unauthenticatedRequest.getPrice())
                                .withCategory(unauthenticatedRequest.getCategory())
                                .withImages(unauthenticatedRequest.getImages())
                                .build()
                );
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateItemActivity().handleRequest(request)
        );
    }
}
