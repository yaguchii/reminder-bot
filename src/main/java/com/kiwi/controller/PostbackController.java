package com.kiwi.controller;

import com.kiwi.util.MessagingUtil;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import retrofit2.Response;

import java.net.URI;
import java.util.Map;

@Slf4j
@Controller
class PostbackController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MessagingUtil messagingUtil;

    private static Jedis getConnection() throws Exception {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        return new Jedis(redisURI);
    }

    void eventHandle(PostbackEvent event) throws Exception {

        Response<UserProfileResponse> response =
                LineMessagingServiceBuilder
                        .create(System.getenv("LINE_BOT_CHANNEL_TOKEN"))
                        .build()
                        .getProfile(event.getSource().getUserId())
                        .execute();

        if (response.isSuccessful()) {
            UserProfileResponse profile = response.body();
            Map<String, String> map = event.getPostbackContent().getParams();
            String key = profile.getUserId() + ":" + map.get("datetime");
            String value = event.getPostbackContent().getData().split(":")[1];
            Jedis jedis = getConnection();
            jedis.lpush(key, value);
            // push
            messagingUtil.pushText(event.getSource().getUserId(), "Set up okay.");
        }
    }
}
