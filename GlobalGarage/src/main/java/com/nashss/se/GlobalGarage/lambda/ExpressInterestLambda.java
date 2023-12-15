package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.ExpressInterestRequest;
import com.nashss.se.GlobalGarage.activity.results.ExpressInterestResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ExpressInterestLambda extends LambdaActivityRunner<ExpressInterestRequest, ExpressInterestResult>
        implements RequestHandler<AuthenticatedLambdaRequest<ExpressInterestRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ExpressInterestRequest> input, Context context) {
        return super.runActivity(
            () -> {
                // Extract request from the input and buyer ID from user claims
                return input.fromUserClaims(claims -> {
                    // Deserialize request from the input
                    ExpressInterestRequest unauthenticatedRequest = input.fromBody(ExpressInterestRequest.class);
                    // Extract buyer ID from user claims
                    String buyerID = "B" + claims.get("sub");
                    // Build the complete ExpressInterestRequest using the builder pattern
                    return ExpressInterestRequest.builder()
                            .withBuyerID(buyerID)
                            .withItemID(unauthenticatedRequest.getItemID())
                            .withGarageID(unauthenticatedRequest.getGarageID())
                            .build();
                });
            },
            (request, serviceComponent) ->
                    serviceComponent.provideExpressInterestActivity().handleRequest(request)
        );
    }
}
