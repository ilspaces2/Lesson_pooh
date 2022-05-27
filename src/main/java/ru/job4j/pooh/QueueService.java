package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class QueueService implements Service {

    private final ConcurrentMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp;
        if (Req.POST.equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).offer(req.getParam());
            resp = new Resp(req.getParam(), "200");
        } else if (Req.GET.equals(req.httpRequestType())) {
            var tmp = queue.get(req.getSourceName());
            resp = tmp == null || tmp.isEmpty()
                    ? new Resp("", "204")
                    : new Resp(tmp.poll(), "200");
        } else {
            resp = new Resp("", "501");
        }
        return resp;
    }
}