package com.nashss.se.GlobalGarage.lambda;

import com.nashss.se.GlobalGarage.activity.request.GetItemRequest;
import com.nashss.se.GlobalGarage.activity.results.GetItemResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GetItemLambda extends LambdaActivityRunner<GetItemRequest, GetItemResult>
        implements RequestHandler<LambdaRequest<GetItemRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetItemRequest> input, Context context) {
        log.info("handleRequest for GetItemLambda");
        return super.runActivity(
            () -> {
                // Extracting itemId and garageId from either path parameters or request body
                Map<String, String> pathParameters = input.getPathParameters();
                String itemId = pathParameters != null ? pathParameters.get("itemId") : null;
                String garageId = pathParameters != null ? pathParameters.get("garageId") : null;
                if (itemId == null || garageId == null) {
                    // If not in path parameters, try getting them from the request body
                    GetItemRequest requestFromBody = input.fromBody(GetItemRequest.class);
                    itemId = itemId != null ? itemId : requestFromBody.getItemId();
                    garageId = garageId != null ? garageId : requestFromBody.getGarageId();
                }

                return GetItemRequest.builder()
                        .withItemId(itemId)
                        .withGarageId(garageId)
                        .build();
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetItemActivity().handleRequest(request)
        );
    }
}
