## Микросервисное веб-приложение Spring Boot, Kafka, Graylog, Jaeger, KrakenD

Веб-приложение состоит из 3-х микросервисов, которые передают сообщения при помощи Kafka.
Для реализации API-шлюза используется KrakenD. Трассировка и логгирование осуществляется технологиями Jaeger и Graylog соответственно.


## Развертывание

1. Развертывание приложения осуществляется при помощи Kubernetes. Рекомендуется использовать minikube и docker гипервизор.
2. Клонируйте репозиторий: `https://github.com/Strocksmin/Microservices-WebApp-SpringBoot.git`
3. Перейдите в главную директорию: `cd Microservices-WebApp-SpringBoot`
4. Запустите необходимые Deployment-контроллеры: `source deploy.txt`

## Использование

Для демонстрации работоспособности приложения создана Postman-коллекция `TVKSP 8.postman_collection.json`, в которой содержатся все запросы к API. 

## Конфигурация

По желанию вы можете изменить secret для замены пароля Redis:

- `kubectl create secret generic redis-passwd --from-literal=passwd=12345`: Замените 12345 на желаемый пароль.

## Структура

Каждый сервис находится в отдельном пакете, рабочие сервисы имеют постфикс `_service`
