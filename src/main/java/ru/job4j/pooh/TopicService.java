package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {

    private final ConcurrentMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp;
        if (Req.GET.equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            var tmpGet = queue.get(req.getSourceName());
            tmpGet.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            var idUser = tmpGet.get(req.getParam());
            resp = idUser == null || idUser.isEmpty()
                    ? new Resp("", "204")
                    : new Resp(idUser.poll(), "200");
        } else if (Req.POST.equals(req.httpRequestType())) {
            var tmpPost = queue.get(req.getSourceName());
            if (tmpPost != null) {
                tmpPost.values().forEach(el -> el.offer(req.getParam()));
                resp = new Resp(req.getParam(), "200");
            } else {
                resp = new Resp("", "404");
            }
        } else {
            resp = new Resp("", "501");
        }
        return resp;
    }
}