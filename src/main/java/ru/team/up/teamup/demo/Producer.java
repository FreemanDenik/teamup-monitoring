// Чтобы убрать ошибки при раскомментировании надо так же раскомментировать Random в entity/AppModuleName

//package ru.team.up.teamup.demo;
//
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import ru.team.up.teamup.entity.*;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//@Deprecated
//public class Producer {
//    public static void main(String[] args) throws InterruptedException {
//        Logger logger = LoggerFactory.getLogger(Producer.class);
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
//        KafkaProducer<String, Report> producer = new KafkaProducer<>(properties);
//
//        for (int i = 0; i < 100; i++) {
//            Map<String, Object> param = new HashMap<>();
//            param.put("count", i);
//            param.put("isEnable", true);
//            param.put("lastUpdate", new Date());
//
//            InitiatorType initiatorType;
//            int initiatorTypeCount = InitiatorType.values().length;
//            switch (i % initiatorTypeCount) {
//                case 1:
//                    initiatorType = InitiatorType.USER;
//                    break;
//                case 2:
//                    initiatorType = InitiatorType.MANAGER;
//                    break;
//                case 3:
//                    initiatorType = InitiatorType.ADMIN;
//                    break;
//                default:
//                    initiatorType = InitiatorType.SYSTEM;
//            }
//
//            Report report = new Report("" + i, Control.AUTO, AppModuleName.getAppModule(AppModuleName.class), initiatorType, "name_" +
//                    initiatorType.name(), 100L + i, new Date(), ReportStatus.SUCCESS, param);
//
//            ProducerRecord<String, Report> record = new ProducerRecord<>("input-data", initiatorType.name(),
//                    report);
//
//            producer.send(record, (metadata, exception) -> {
//                if (exception == null) {
//                    logger.info("received new metadata, topic: " + metadata.topic() + " partition: " +
//                            metadata.partition() + " offsets: " + metadata.offset() + " time: " +
//                            metadata.timestamp());
//                } else {
//                    logger.error("error producing", exception);
//                }
//            });
//            Thread.sleep(5000);
//        }
//        producer.flush();
//        producer.close();
//    }
//}
