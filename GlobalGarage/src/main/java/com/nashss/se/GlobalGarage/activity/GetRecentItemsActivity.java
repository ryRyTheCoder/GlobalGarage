package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetRecentItemsRequest;
import com.nashss.se.GlobalGarage.activity.results.GetRecentItemsResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.models.ItemModel;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Implementation of the GetRecentItemsActivity for the GlobalGarage's GetRecentItems API.
 * This API allows fetching recent items based on their listed date.
 */
public class GetRecentItemsActivity {
    private final Logger log = LogManager.getLogger();
    private final ItemDAO itemDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new GetRecentItemsActivity object.
     *
     * @param itemDao to access the Item table.
     * @param modelConverter to convert entities to models.
     */
    @Inject
    public GetRecentItemsActivity(ItemDAO itemDao, ModelConverter modelConverter) {
        this.itemDao = itemDao;
        this.modelConverter = modelConverter;
    }

    /**
     * This method handles the incoming request by fetching recent items.
     *
     * @param request request object to get recent items
     * @return result object indicating the outcome of the fetch process
     */
    public GetRecentItemsResult handleRequest(final GetRecentItemsRequest request) {
        log.info("Received GetRecentItemsRequest {}", request);

        try {
            // Set status to 'available'
            String status = "available";

            Pair<List<Item>, Map<String, AttributeValue>> resultPair = itemDao.getRecentItems(
                    status, request.getLimit(), request.getLastEvaluatedKey());
            List<ItemModel> itemModels = resultPair.getLeft().stream()
                    .map(modelConverter::toItemModel)
                    .collect(Collectors.toList());

            // Encoding the lastEvaluatedKey
            String encodedLastEvaluatedKey = encodeLastEvaluatedKey(resultPair.getRight());

            return new GetRecentItemsResult(true, "Recent available items fetched successfully.",
                    itemModels, encodedLastEvaluatedKey);
        } catch (Exception e) {
            log.error("Error fetching recent available items", e);
            return new GetRecentItemsResult(false, "Failed to fetch recent available items.", null, null);
        }
    }


    private String encodeLastEvaluatedKey(Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null || lastEvaluatedKey.isEmpty()) {
            return null;
        }
        try {
            byte[] bytes = new ObjectMapper().writeValueAsBytes(lastEvaluatedKey);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (JsonProcessingException e) {
            log.error("Error encoding last evaluated key", e);
            throw new RuntimeException("Error encoding last evaluated key", e);
        }
    }
}
