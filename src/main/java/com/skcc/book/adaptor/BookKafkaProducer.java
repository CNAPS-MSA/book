package com.skcc.book.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.book.config.KafkaProperties;
import com.skcc.book.domain.BookCatalogEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Service
public class BookKafkaProducer {

    private final Logger log = LoggerFactory.getLogger(BookKafkaProducer.class);

    private static final String TOPIC_CATALOG = "topic_catalog";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(BookKafkaProducer.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public BookKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize(){
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    public PublishResult sendBookCreateEvent(BookCatalogEvent bookCatalogEvent)throws ExecutionException, InterruptedException, JsonProcessingException{

        String message = objectMapper.writeValueAsString(bookCatalogEvent);
        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
        return new PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));
    }

    public PublishResult sendBookDeleteEvent(BookCatalogEvent bookDeleteEvent)throws ExecutionException, InterruptedException, JsonProcessingException{

        String message = objectMapper.writeValueAsString(bookDeleteEvent);
        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
        return new PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));
    }

    @PreDestroy
    public void shutdown(){
        log.info("Shutdown Kafka producer");
        producer.close();
    }


    private static class PublishResult {
        public final String topic;
        public final int partition;
        public final long offset;
        public final Instant timestamp;

        private PublishResult(String topic, int partition, long offset, Instant timestamp) {
            this.topic = topic;
            this.partition = partition;
            this.offset = offset;
            this.timestamp = timestamp;
        }
    }
}
