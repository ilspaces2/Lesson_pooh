package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReqTest {

    @Test
    public void whenQueueModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls
                + "Content-Length: 14" + ls
                + "Content-Type: application/x-www-form-urlencoded" + ls
                + "" + ls
                + "temperature=18" + ls;
        Req req = Req.of(content);
        assertThat(req.httpRequestType(), is("POST"));
        assertThat(req.getPoohMode(), is("queue"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParam(), is("temperature=18"));
    }

    @Test
    public void whenQueueModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req req = Req.of(content);
        assertThat(req.httpRequestType(), is("GET"));
        assertThat(req.getPoohMode(), is("queue"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParam(), is(""));
    }

    @Test
    public void whenTopicModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /topic/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls
                + "Content-Length: 14" + ls
                + "Content-Type: application/x-www-form-urlencoded" + ls
                + "" + ls
                + "temperature=18" + ls;
        Req req = Req.of(content);
        assertThat(req.httpRequestType(), is("POST"));
        assertThat(req.getPoohMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParam(), is("temperature=18"));
    }

    @Test
    public void whenTopicModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req req = Req.of(content);
        assertThat(req.httpRequestType(), is("GET"));
        assertThat(req.getPoohMode(), is("topic"));
        assertThat(req.getSourceName(), is("weather"));
        assertThat(req.getParam(), is("client407"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRequestEmpty() {
        String content = " ";
        Req.of(content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRequestHaveOneLine() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls;
        Req.of(content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenHttpRequestTypeError() {
        String ls = System.lineSeparator();
        String content = "G /topic/weather/client407 HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req.of(content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenHttpRequestTypeProtocolError() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req.of(content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenPoohModeError() {
        String ls = System.lineSeparator();
        String content = "GET /weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req.of(content);
    }
}