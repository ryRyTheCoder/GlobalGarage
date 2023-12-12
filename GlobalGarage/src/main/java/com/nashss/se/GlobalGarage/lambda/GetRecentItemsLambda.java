package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.GetRecentItemsRequest;
import com.nashss.se.GlobalGarage.activity.results.GetRecentItemsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Lambda function to handle requests for getting recent items.
 * This class extends a LambdaActivityRunner and implements RequestHandler for AWS Lambda requests.
 */
public class GetRecentItemsLambda extends LambdaActivityRunner<GetRecentItemsRequest, GetRecentItemsResult>
        implements RequestHandler<LambdaRequest<GetRecentItemsRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetRecentItemsRequest> input, Context context) {
        log.info("Received GetRecentItemsRequest");

        return super.runActivity(
            () -> {
                // Retrieve the 'next' query parameter, which is the Base64 encoded lastEvaluatedKey
                Map<String, String> queryParameters = input.getQueryStringParameters();
                String lastEvaluatedKeyBase64 = queryParameters != null ? queryParameters.get("next") : null;

                return GetRecentItemsRequest.builder()
                        .withLastEvaluatedKey(lastEvaluatedKeyBase64)
                        .build();
            },
            (request, serviceComponent) -> serviceComponent.provideGetRecentItemsActivity().handleRequest(request)
        );
    }
}
