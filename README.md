## Технологии

* Java 9
* Spring Framework
* Maven (управление зависимостями)

[![Java](https://img.shields.io/badge/Java-9-blue)](https://www.oracle.com/java/technologies/javase/jdk9-archive-downloads.html) 
[![Maven](https://img.shields.io/badge/Maven-Dependency-orange)](https://maven.apache.org/)

---

## Сборка проекта

Если jar-файл ещё не собран, выполните команду Maven:

```
mvn clean package
````

После сборки jar-файл появится в папке `target`:

```
target/homework-0.0.1-SNAPSHOT.jar
```

---

## Запуск приложения

Перейдите в папку проекта, щелкните левой кнопкой мыши и нажмите на "Открыть в Терминале".

Запуск через Java:

```
java -jar target/homework-0.0.1-SNAPSHOT.jar
```

Приложение будет доступно по адресу: http://localhost:8081
