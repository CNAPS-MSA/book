package com.skcc.book.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.book.config.KafkaProperties;
import com.skcc.book.domain.event.BookChanged;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;

@Service
public class BookProducerImpl implements BookProducer {

    private final Logger log = LoggerFactory.getLogger(BookProducerImpl.class);

    private static final String TOPIC_CATALOG = "topic_catalog";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(BookProducerImpl.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public BookProducerImpl(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize(){
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    public void sendBookCreateEvent(BookChanged bookChanged)throws ExecutionException, InterruptedException, JsonProcessingException{

        String message = objectMapper.writeValueAsString(bookChanged);
         producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
    }

    public void sendBookDeleteEvent(BookChanged bookDeleteEvent)throws ExecutionException, InterruptedException, JsonProcessingException{

        String message = objectMapper.writeValueAsString(bookDeleteEvent);
         producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
    }

    @PreDestroy
    public void shutdown(){
        log.info("Shutdown Kafka producer");
        producer.close();
    }



}
