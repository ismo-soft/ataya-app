    package com.ataya.company.service.kafka.producer;

    import com.ataya.company.dto.product.ProductDto;
    import com.ataya.company.dto.store.StoreDto;
    import lombok.RequiredArgsConstructor;
    import org.springframework.kafka.core.KafkaTemplate;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class CompanyServiceProducer {

        private final KafkaTemplate<String, ProductDto> productKafkaTemplate;
        private final KafkaTemplate<String, StoreDto> storeKafkaTemplate;

        public void sendProduct(ProductDto productDto) {
            productKafkaTemplate.send("product-topic", productDto);
        }

        public void sendStore(StoreDto storeDto) {
            storeKafkaTemplate.send("store-topic", storeDto);
        }

    }
