package com.wallet.ledger_service.Config;

import com.wallet.ledger_service.Event.CreditCompletedEvent;
import com.wallet.ledger_service.Event.DebitCompletedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConfig(Class<?> targetType) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "ledger-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, targetType.getName());
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return config;
    }

    @Bean
    public ConsumerFactory<String, DebitCompletedEvent> debitCompletedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseConfig(DebitCompletedEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DebitCompletedEvent> debitCompletedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DebitCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(debitCompletedConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CreditCompletedEvent> creditCompletedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseConfig(CreditCompletedEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CreditCompletedEvent> creditCompletedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CreditCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(creditCompletedConsumerFactory());
        return factory;
    }

}
