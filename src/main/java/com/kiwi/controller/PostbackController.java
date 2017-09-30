package com.kiwi.controller;

import com.linecorp.bot.model.event.PostbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
class PostbackController {

    void eventHandle(PostbackEvent event) throws Exception {

        String postackData = event.getPostbackContent().getData();
        Map<String, String> map = event.getPostbackContent().getParams();
        log.info(postackData);
        log.info(map.toString());
        String time = map.get("datetime");


        // postackDataがsetで始まる場合、
        // set:{text}
    }
}
