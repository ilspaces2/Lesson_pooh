package ru.job4j.pooh;

import java.util.HashMap;
import java.util.Map;

/**
 * Request - класс, служит для парсинга входящего запроса.
 * httpRequestType - GET или POST. Он указывает на тип запроса.
 * poohMode - указывает на режим работы: queue или topic.
 * sourceName - имя очереди или топика.
 * param - содержимое запроса.
 */

public class Req {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String TOPIC = "topic";
    private static final String QUEUE = "queue";
    private static final String HTTP = "HTTP/1.1";

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        Req req = requestParser(content);
        return new Req(req.httpRequestType(), req.getPoohMode(), req.getSourceName(), req.getParam());
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

    private static Req requestParser(String content) {
        Map<String, String[]> map = validateReq(content);
        String param = "";
        if (GET.equals(map.get("startLine")[0]) && TOPIC.equals(map.get("url")[1])) {
            param = map.get("url")[3];
        } else if (POST.equals(map.get("startLine")[0])
                && map.get("reqText")[map.get("reqText").length - 2].isBlank()) {
            param = map.get("reqText")[map.get("reqText").length - 1];
        }
        return new Req(map.get("startLine")[0], map.get("url")[1],
                map.get("url")[2], param);
    }

    /**
     * <p>
     * Проверка входного запроса :
     * 1. если он пустой или меньше 3х строчек,
     * 2. если ошибка в методе POST или GET или в типе протокла,
     * 3. если неправильный режим,
     * Тогда выбрасываем исключение.
     * <p>
     * Иначе возвращаем мапу с массивами :
     * 1. всего контента,
     * 2. первой строки,
     * 3. url из первой строки.
     */
    private static Map<String, String[]> validateReq(String content) {
        Map<String, String[]> map = new HashMap<>();
        String[] requestText = content.split(System.lineSeparator());
        if (requestText[0].isBlank() || requestText.length < 3) {
            throw new IllegalArgumentException("Request empty");
        }
        String[] startLine = requestText[0].split(" ");
        if ((!POST.equals(startLine[0]) && !GET.equals(startLine[0])) || !HTTP.equals(startLine[2])) {
            throw new IllegalArgumentException("HttpRequest error");
        }
        String[] url = startLine[1].split("/");
        if (!TOPIC.equals(url[1]) && !QUEUE.equals(url[1])) {
            throw new IllegalArgumentException("Mode error");
        }
        map.put("reqText", requestText);
        map.put("startLine", startLine);
        map.put("url", url);
        return Map.copyOf(map);
    }
}