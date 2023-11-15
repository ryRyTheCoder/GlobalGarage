package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.GlobalGarage.activity.request.GetOneGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.GetOneGarageResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GetOneGarageLambda extends LambdaActivityRunner<GetOneGarageRequest, GetOneGarageResult>
        implements RequestHandler<LambdaRequest<GetOneGarageRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();
    private final URLDecoder decoder = new URLDecoder();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetOneGarageRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
                () -> {
                    // Extracting sellerId and garageId from path parameters
                    Map<String, String> pathParameters = input.getPathParameters();
                    String sellerId = pathParameters != null ? pathParameters.get("sellerId") : null;
                    String garageId = pathParameters != null ? pathParameters.get("garageId") : null;

                    return GetOneGarageRequest.builder()
                            .withSellerId(decoder.decode(sellerId, StandardCharsets.UTF_8))
                            .withGarageId(decoder.decode(garageId, StandardCharsets.UTF_8))
                            .build();
                },
                (request, serviceComponent) -> serviceComponent.provideGetOneGarageActivity().handleRequest(request)
        );
    }
}
