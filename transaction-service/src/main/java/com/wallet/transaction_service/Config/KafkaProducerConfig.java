package com.wallet.transaction_service.Config;

import com.wallet.transaction_service.Event.CompensateDebitEvent;
import com.wallet.transaction_service.Event.CreditRequestedEvent;
import com.wallet.transaction_service.Event.TransactionInitiatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return config;
    }


    @Bean
    public ProducerFactory<String, TransactionInitiatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(baseConfig());
    }

    @Bean
    public KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public ProducerFactory<String, CreditRequestedEvent> creditRequestedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseConfig());
    }

    @Bean
    @Qualifier("creditRequestedKafkaTemplate")
    public KafkaTemplate<String, CreditRequestedEvent> creditRequestedKafkaTemplate() {
        return new KafkaTemplate<>(creditRequestedProducerFactory());
    }

    @Bean
    public ProducerFactory<String, CompensateDebitEvent> compensateDebitProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseConfig());
    }

    @Bean
    public KafkaTemplate<String, CompensateDebitEvent> compensateDebitKafkaTemplate() {
        return new KafkaTemplate<>(compensateDebitProducerFactory());
    }
}