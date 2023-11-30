package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.GetSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetSellerResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GetSellerLambda extends LambdaActivityRunner<GetSellerRequest, GetSellerResult>
        implements RequestHandler<LambdaRequest<GetSellerRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetSellerRequest> input, Context context) {
        log.info("handleRequest for GetSellerLambda");
        return super.runActivity(
            () -> {
                // Extracting sellerId from path parameters
                Map<String, String> pathParameters = input.getPathParameters();
                String sellerId = pathParameters != null ? pathParameters.get("sellerId") : null;

                return GetSellerRequest.builder()
                        .withSellerId(sellerId)
                        .build();
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetSellerActivity().handleRequest(request)
        );
    }
}
