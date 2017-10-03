package com.kiwi.scheduler;

import com.kiwi.util.AESEncryption;
import com.kiwi.util.MessagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Set;

@Slf4j
@Component
public class Reminder {

    @Autowired
    private AESEncryption encryption;

    @Autowired
    private MessagingUtil messagingUtil;

    private static Jedis getConnection() throws Exception {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        return new Jedis(redisURI);
    }

    @Scheduled(cron = "0 * * * * *")
    public void remind() throws Exception {
        LocalDateTime now = LocalDateTime.now().plusHours(9L).truncatedTo(ChronoUnit.MINUTES);
        log.info("now:" + now.toString());
        Jedis jedis = getConnection();
        Set<String> keys = jedis.keys("*" + now.toString());

        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String userId = key.split(":")[0];

            while (jedis.exists(key)) {
                String remindText = jedis.lpop(key);
                // push
                messagingUtil.pushText(userId, "[Remind]\n" + encryption.decrypto(remindText));
            }
        }
    }
}