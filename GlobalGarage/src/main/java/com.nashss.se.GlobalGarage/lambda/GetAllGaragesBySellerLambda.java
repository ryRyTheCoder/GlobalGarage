package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.nashss.se.GlobalGarage.activity.request.GetGaragesBySellerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetGaragesBySellerResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GetAllGaragesBySellerLambda extends LambdaActivityRunner<GetGaragesBySellerRequest, GetGaragesBySellerResult>
        implements RequestHandler<LambdaRequest<GetGaragesBySellerRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetGaragesBySellerRequest> input, Context context) {
        log.info("Received GetGaragesBySellerRequest");

        return super.runActivity(
                () -> {
                    // Extract the sellerId from the path parameters
                    String sellerId = input.getPathParameters().get("sellerId");

                    // Extract the 'next' query parameter, which is the Base64 encoded lastEvaluatedKey
                    Map<String, String> queryParameters = input.getQueryStringParameters();
                    String lastEvaluatedKeyBase64 = queryParameters != null ? queryParameters.get("next") : null;

                    return GetGaragesBySellerRequest.builder()
                            .withSellerId(sellerId)
                            .withLastEvaluatedKey(lastEvaluatedKeyBase64)
                            .build();
                },
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllGaragesBySellerActivity().handleRequest(request)
        );
    }
}