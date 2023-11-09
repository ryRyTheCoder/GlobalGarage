package com.nashss.se.GlobalGarage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.GlobalGarage.activity.request.CreateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateSellerResult;

public class CreateSellerLambda extends LambdaActivityRunner<CreateSellerRequest, CreateSellerResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateSellerRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateSellerRequest> input, Context context) {

        return super.runActivity(
                () -> {
                    CreateSellerRequest unauthenticatedRequest = input.fromBody(CreateSellerRequest.class);
                    final String testSellerId = "5669b497-e324-44b8-9d6b-5bf27c182b9c"; // Test seller ID for local testing

                    // Bypassing claim extraction and directly using testSellerId
                    CreateSellerRequest requestWithSellerId = CreateSellerRequest.builder()
                            .withUsername(unauthenticatedRequest.getUsername())
                            .withEmail(unauthenticatedRequest.getEmail())
                            .withLocation(unauthenticatedRequest.getLocation())
                            .withContactInfo(unauthenticatedRequest.getContactInfo())
                            .withSellerId(testSellerId)
                            .build();

                    return requestWithSellerId;
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateSellerActivity().handleRequest(request)
        );
    }
    }
//
//       return super.runActivity(
//                () -> {
//                    CreateSellerRequest unauthenticatedRequest = input.fromBody(CreateSellerRequest.class);
//                    return input.fromUserClaims(claims ->
//                            CreateSellerRequest.builder()
//                                    .withUsername(unauthenticatedRequest.getUsername())
//                                    .withEmail(unauthenticatedRequest.getEmail())
//                                    .withLocation(unauthenticatedRequest.getLocation())
//                                    .withContactInfo(unauthenticatedRequest.getContactInfo())
//                                     .withSellerId(claims.get("sub")) // Using 'sub' as sellerId
//                                    .build()
//                    );
//                },
//                (request, serviceComponent) ->
//                        serviceComponent.provideCreateSellerActivity().handleRequest(request)
//        );
//    }
//}
