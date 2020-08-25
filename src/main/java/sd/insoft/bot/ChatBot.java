package sd.insoft.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sd.insoft.model.TelegramUser;
import sd.insoft.service.TelegramUserService;

import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot{

    Logger log = LoggerFactory.getLogger(ChatBot.class);

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    private final QueryProcessing queryProcessing;
    private final TelegramUserService userService;

    public ChatBot(QueryProcessing queryProcessing, TelegramUserService telegramUserService) {
        this.queryProcessing = queryProcessing;
        this.userService = telegramUserService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;
        Message message = update.getMessage();
        TelegramUser user = userService.findByChatId(message.getChatId());
        if (user == null) {
            TelegramUser newUser = new TelegramUser(
                    message.getChatId(),
                    message.getChat().getFirstName(),
                    message.getChat().getLastName(),
                    message.getChat().getUserName());
            userService.saveUser(newUser);
            log.debug("Add new User: {}", newUser);
            user = newUser;
        }
        BotContext context = BotContext.getContext(this, user, message);

        log.debug("{} text:{}", user.getFirstName(), message.getText());

        queryProcessing.processMessage(context);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public List<TelegramUser> getAllUsers(){
        return userService.findAll();
    }
}
