package sd.insoft.bot;

import org.telegram.telegrambots.meta.api.objects.Message;
import sd.insoft.model.TelegramUser;

public class BotContext {

    private final ChatBot chatBot;
    private final TelegramUser user;
    private final Message message;

    public static BotContext getContext (ChatBot chatBot, TelegramUser user, Message message){
        return new BotContext(chatBot, user, message);
    }

    private BotContext(ChatBot chatBot, TelegramUser user, Message message) {
        this.chatBot = chatBot;
        this.user = user;
        this.message = message;
    }

    public ChatBot getChatBot() {
        return chatBot;
    }

    public TelegramUser getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }
}
