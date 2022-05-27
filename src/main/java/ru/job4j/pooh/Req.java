package ru.job4j.pooh;

/**
 * Request - класс, служит для парсинга входящего запроса.
 * httpRequestType - GET или POST. Он указывает на тип запроса.
 * poohMode - указывает на режим работы: queue или topic.
 * sourceName - имя очереди или топика.
 * param - содержимое запроса.
 */

public class Req {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String TOPIC = "topic";
    public static final String QUEUE = "queue";
    public static final String HTTP = "HTTP/1.1";

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
        String[] requestText = content.split(System.lineSeparator());
        String[] startLine = requestText[0].split(" ");
        String[] url = startLine[1].split("/");
        String param = "";
        if (GET.equals(startLine[0]) && TOPIC.equals(url[1])) {
            param = url[3];
        } else if (POST.equals(startLine[0])) {
            param = requestText[requestText.length - 1];
        }
        return new Req(startLine[0], url[1], url[2], param);
    }
}