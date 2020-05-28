package com.skcc.book.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.book.config.KafkaProperties;
import com.skcc.book.domain.Book;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.repository.BookRepository;
import com.skcc.book.domain.UpdateBookEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BookKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(BookKafkaConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String TOPIC ="topic_kafka";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private BookRepository bookRepository;

    private ExecutorService executorService = Executors.newCachedThreadPool();


    public BookKafkaConsumer(KafkaProperties kafkaProperties, BookRepository bookRepository) {
        this.kafkaProperties = kafkaProperties;
        this.bookRepository = bookRepository;
    }


    @PostConstruct
    public void start(){
        log.info("Kafka consumer starting ...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer.subscribe(Collections.singleton(TOPIC));
        log.info("Kafka consumer started");

        executorService.execute(()-> {
            try {

                while (!closed.get()){
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                    for(ConsumerRecord<String, String> record: records){
                        log.info("Consumed message in {} : {}", TOPIC, record.value());
                        ObjectMapper objectMapper = new ObjectMapper();
                        UpdateBookEvent updateBookEvent = objectMapper.readValue(record.value(), UpdateBookEvent.class);
                        Book book = bookRepository.findById(updateBookEvent.getBookId()).get();
                        book.setBookStatus(BookStatus.valueOf(updateBookEvent.getBookStatus()));
                        bookRepository.save(book);

                    }

                }
                kafkaConsumer.commitSync();

            }catch (WakeupException e){
                if(!closed.get()){
                    throw e;
                }

            }catch (Exception e){
                log.error(e.getMessage(), e);
            }finally {
                log.info("kafka consumer close");
                kafkaConsumer.close();
            }

            }



        );
    }


    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
