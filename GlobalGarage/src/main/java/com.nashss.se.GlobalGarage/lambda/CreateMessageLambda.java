package com.nashss.se.GlobalGarage.lambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.GlobalGarage.activity.request.CreateMessageRequest;

import com.nashss.se.GlobalGarage.activity.results.CreateMessageResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateMessageLambda extends LambdaActivityRunner<CreateMessageRequest, CreateMessageResult>
        implements RequestHandler<LambdaRequest<CreateMessageRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<CreateMessageRequest> input, Context context) {
        log.info("Received CreateMessageRequest");

        return super.runActivity(
                () -> {
                    // Extract the request from the input
                    CreateMessageRequest createMessageRequest = input.fromBody(CreateMessageRequest.class);

                    return CreateMessageRequest.builder()
                            .withRelatedItemID(createMessageRequest.getRelatedItemID())
                            .withSenderType(createMessageRequest.getSenderType())
                            .withSenderID(createMessageRequest.getSenderID())
                            .withReceiverType(createMessageRequest.getReceiverType())
                            .withReceiverID(createMessageRequest.getReceiverID())
                            .withContent(createMessageRequest.getContent())
                            .build();
                },
                (request, serviceComponent) -> serviceComponent.provideCreateMessageActivity().handleRequest(request)
        );
    }
}
