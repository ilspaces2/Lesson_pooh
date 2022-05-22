[![Java CI with Maven](https://github.com/ilspaces2/job4j_pooh/actions/workflows/maven.yml/badge.svg)](https://github.com/ilspaces2/job4j_pooh/actions/workflows/maven.yml)
# Pooh JMS

* В этом проекте мы сделаем аналог асинхронной очереди.
* Приложение запускает Socket и ждет клиентов.
* Клиенты могут быть двух типов: отправители (publisher), получатели (subscriber).
* качестве клиента будем использовать cURL. https://curl.se/download.html
* В качестве протокола будем использовать HTTP. 