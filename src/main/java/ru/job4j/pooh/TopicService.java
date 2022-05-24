package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {

    private String lastId;

    private final ConcurrentMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp;
        if (Req.GET.equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            queue.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            lastId = req.getParam();
            if (!queue.get(req.getSourceName()).get(lastId).isEmpty()) {
                resp = new Resp(queue.get(req.getSourceName()).get(lastId).poll(), "200");
            } else {
                resp = new Resp("", "204");
            }
        } else if (Req.POST.equals(req.httpRequestType())) {
            queue.get(req.getSourceName()).get(lastId).offer(req.getParam());
            resp = new Resp(req.getParam(), "200");
        } else {
            resp = new Resp("", "501");
        }
        return resp;
    }
}