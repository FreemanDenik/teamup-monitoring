# TeamUp

---
## Запуск стека приложений

Для запуска стека приложений (MongoDB, Zookeeper, Kafka) выполните в терминале IDEA:

    docker-compose up -d


## Запуск мониторинга

1. Запустить TeamupMonitoringApplication
2. Для проверки запустить Producer
* Создаёт и отправляет в Kafka 100 сообщений в виде сущностей Report
* Сообщения распределяются по 4-м партициям исходя из типа отправителя (SYSTEM, USER, MANAGER, ADMIN)
3. После запуска Kafka продюсера в отдельном сервисе, package demo необходимо удалить
