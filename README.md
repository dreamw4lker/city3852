# 7Bus-web

## Что это за проект?
Это web-приложение, повторяющее некоторую функциональность сайта traffic22.ru.

Здесь есть карта. Можно смотреть, как по ней передвигаются автобусы, маршрутки, трамваи и троллейбусы, а если приблизить её - увидеть прогнозы прибытия транспорта на остановку.

## Для чего это всё?
Просто так, было интересно применить некоторые технологии для получения данных со стороннего сайта. 

А вообще - это курсовой проект.

## Какие технологии здесь используются?

1. Java 1.8
2. Spring Framework 4
3. Jetty 9.4.11
4. Карта OpenStreetMap и библиотека OpenLayers4
5. Ещё несколько библиотек, посмотреть список которых можно в [pom.xml](city3852/pom.xml)

## Развёртывание приложения

1. Установить СУБД PostgreSQL версии не ниже 9.4
2. Создать базу данных *database_name*
3. Выполнить скрипт инициализации [init-script.sql](init-script.sql)
4. Создать файл *3852.properties* в папке *city3852/web/src/main/resources/*
5. Указать следующие параметры в файле *3852.properties*:
<pre>
   jdbc.driverClassName=org.postgresql.Driver
   jdbc.url=jdbc:postgresql://database_host:database_port/database_name
   jdbc.username=database_login
   jdbc.password=database_password
</pre>
