package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.nashss.se.GlobalGarage.activity.request.GetAllGaragesRequest;

import com.nashss.se.GlobalGarage.activity.results.GetAllGaragesResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.Map;

public class GetAllGaragesLambda extends LambdaActivityRunner<GetAllGaragesRequest, GetAllGaragesResult>
        implements RequestHandler<LambdaRequest<GetAllGaragesRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetAllGaragesRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(() -> {
                    // Retrieve the 'next' query parameter, which is the Base64 encoded lastEvaluatedKey
                    Map<String, String> queryParameters = input.getQueryStringParameters();
                    String lastEvaluatedKeyBase64 = queryParameters != null ? queryParameters.get("next") : null;

                    return GetAllGaragesRequest.builder()
                            .withLastEvaluatedKey(lastEvaluatedKeyBase64)
                            .build();
                },
                (request, serviceComponent) -> serviceComponent.provideGetAllGaragesActivity().handleRequest(request)
        );
    }
}