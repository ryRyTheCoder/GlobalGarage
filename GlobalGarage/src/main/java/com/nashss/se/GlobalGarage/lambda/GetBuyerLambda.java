package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.GetBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetBuyerResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GetBuyerLambda extends LambdaActivityRunner<GetBuyerRequest, GetBuyerResult>
        implements RequestHandler<LambdaRequest<GetBuyerRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetBuyerRequest> input, Context context) {
        log.info("handleRequest for GetBuyerLambda");
        return super.runActivity(
            () -> {
                // Extracting buyerId from either path parameters or request body
                Map<String, String> pathParameters = input.getPathParameters();
                String buyerId = pathParameters != null ? pathParameters.get("buyerId") : null;
                if (buyerId == null) {
                    // If not in path parameters, try getting it from the request body
                    buyerId = input.fromBody(GetBuyerRequest.class).getBuyerId();
                }

                return GetBuyerRequest.builder()
                        .withBuyerId(buyerId)
                        .build();
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetBuyerActivity().handleRequest(request)
        );
    }
}
