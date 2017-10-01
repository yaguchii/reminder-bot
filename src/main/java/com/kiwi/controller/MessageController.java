package com.kiwi.controller;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
class MessageController {

    void eventHandle(MessageEvent<TextMessageContent> event) throws Exception {

        // Noが選択されたときのtextは無視する
        if (event.getMessage().getText().equals("No")) {
            return;
        }

        sendConfirmMessage(event.getReplyToken(), event.getMessage().getText());
    }

    private void sendConfirmMessage(String replyToken, String message) throws Exception {

        List<Action> actions = new ArrayList<>();
        DatetimePickerAction datetimePickerAction = new DatetimePickerAction(
                "Yes",
                "set:" + message,
                "datetime");
        MessageAction messageAction = new MessageAction(
                "No",
                "No");

        actions.add(datetimePickerAction);
        actions.add(messageAction);

        ConfirmTemplate confirmTemplate = new ConfirmTemplate("Do you want to set a reminder?", actions);
        TemplateMessage templateMessage = new TemplateMessage(
                "this is a confirm template",
                confirmTemplate);

        ReplyMessage replyMessage = new ReplyMessage(
                replyToken,
                templateMessage
        );
        Response<BotApiResponse> response =
                LineMessagingServiceBuilder
                        .create(System.getenv("LINE_BOT_CHANNEL_TOKEN"))
                        .build()
                        .replyMessage(replyMessage)
                        .execute();

        log.info(response.code() + ":" + response.message());
    }
}
