package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenTopicToo() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";

    /* Режим topic. Пытаемся вставить данные в отсутствующий топик. Если нет соответствующей проверки,
    упадет с NullPointerException. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
    /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
    Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        /*=====================================================================*/
    /* Режим topic. Добавляем данные в топик weather. Теперь данные должны попасть
    в обе индивидуальные очереди*/
        topicService.process(
                new Req("POST", "topic", "weather", "humidity=70")
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result3 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565. */
        Resp result4 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407.
         * Очередь пустая */
        Resp result5 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        /*===========================================================*/
        /* Режим topic. Забираем данные из индивидуальной очереди в топике traffic. Очередь client407.
         * Еще не подписывались. Очередь пустая */
        Resp result6 = topicService.process(
                new Req("GET", "topic", "traffic", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
        assertThat(result3.text(), is("humidity=70"));
        assertThat(result4.text(), is("humidity=70"));
        assertThat(result5.text(), is(""));
        assertThat(result6.text(), is(""));
    }
}