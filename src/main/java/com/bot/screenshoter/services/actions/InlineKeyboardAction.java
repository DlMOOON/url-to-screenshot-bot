package com.bot.screenshoter.services.actions;

import com.bot.screenshoter.constants.InlineButtonEnum;

public interface InlineKeyboardAction {

    void handle(String chatId, InlineButtonEnum button);
}
