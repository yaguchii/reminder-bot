package com.kiwi.scheduler;

import com.heroku.api.HerokuAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DynoScheduler {

    // 毎日3時0分0秒(JST)にrestart
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Tokyo")
    public void restart() {
        String apiKey = System.getenv("HEROKU_API_KEY");
        HerokuAPI herokuAPI = new HerokuAPI(apiKey);
        herokuAPI.restartDynos(System.getenv("APP_NAME"));
    }
}