# WhereToGo.NSK Backend

Серверная часть приложения WhereToGo.NSK

## Общая информация

### Основные сервисы

* Пользователи
* Места
* Категории
* Рекомендации

## Техническая информация

### Технологический стек

* Java 17.0.8
* SpringBoot 2.7.5
* docker-compose 3.7
* Apache Maven 3.8.6

### Сборка проекта

        mvn clean package

После сборки в папке /target появится файл с расширением *.jar, котороый можно запустить с помощью команды:

        java -jar /path/to/*.jar

### Sonar

Для проверки тестового покрытия кода необходимо провести сборку проекта:

        mvn clean install

А затем вызвать sonar с помощью:

        mvn sonar:sonar

Также можно использовать комбинированную команду:

        mvn clean install sonar:sonar

### Docker-контейнер:

Сборка контейнера:

        docker-compose up -d

Пересобрать контейнер при изменении *.jar файла:

        docker-compose build app
        docker-compose up -d

### Swagger-UI

Посмотреть сведения об API: [Swagger-UI](http://localhost:8080/swagger-ui/)