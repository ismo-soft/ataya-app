package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.product.SuspendItemRequest;
import com.ataya.contributor.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, SuspendItemRequest> kafkaTemplate;

    @Override
    public void suspendItemFromInventory(String itemId, Double quantity) {

        kafkaTemplate.send("suspend-item-from-inventory",
                SuspendItemRequest.builder()
                        .itemId(itemId)
                        .quantity(quantity)
                        .build());

    }

    @Override
    public void releaseSuspendedInventory(String itemId, Double quantity) {
        kafkaTemplate.send("release-suspended-inventory",
                SuspendItemRequest.builder()
                        .itemId(itemId)
                        .quantity(quantity)
                        .build());
    }
}
