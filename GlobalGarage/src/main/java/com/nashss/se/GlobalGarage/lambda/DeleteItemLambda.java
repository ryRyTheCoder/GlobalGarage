package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.DeleteItemRequest;
import com.nashss.se.GlobalGarage.activity.results.DeleteItemResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;


public class DeleteItemLambda extends LambdaActivityRunner<DeleteItemRequest, DeleteItemResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteItemRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteItemRequest> input, Context context) {
        return super.runActivity(
            () -> {
                // Extracting itemId and garageId from path parameters
                Map<String, String> pathParameters = input.getPathParameters();
                String itemId = pathParameters.get("itemId");
                String garageId = pathParameters.get("garageId");

                // Adding sellerId from user claims
                return input.fromUserClaims(claims ->
                        DeleteItemRequest.builder()
                                .withItemId(itemId)
                                .withGarageId(garageId)
                                .withSellerId("S" + claims.get("sub"))
                                .build()
                );
            },
            (request, serviceComponent) ->
                    serviceComponent.provideDeleteItemActivity().handleRequest(request)
        );
    }
}
