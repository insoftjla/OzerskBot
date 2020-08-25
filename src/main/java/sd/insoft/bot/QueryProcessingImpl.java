package sd.insoft.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sd.insoft.model.Company;
import sd.insoft.model.TelegramUser;
import sd.insoft.model.Weather;
import sd.insoft.repo.CompanyRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import sd.insoft.repo.WeatherRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:httpQuery.properties")
public class QueryProcessingImpl implements QueryProcessing {

    @Value("${yandex.maps.query.drugstore}")
    String drugShop;
    @Value("${yandex.maps.query.shop}")
    String shop;
    @Value("${yandex.maps.query.education}")
    String education;
    @Value("${city.name}")
    String cityName;

    Logger log = LoggerFactory.getLogger(QueryProcessingImpl.class);

    StringBuilder stringBuilder;

    private ChatBot chatBot;
    private TelegramUser user;
    private Message message;


    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final WeatherRepository weatherRepository;
    private final CompanyRepository companyRepository;

    public QueryProcessingImpl(
            WeatherRepository weatherRepository,
            CompanyRepository companyRepository) {
        this.weatherRepository = weatherRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void processMessage(BotContext context) {
        this.chatBot = context.getChatBot();
        this.user = context.getUser();
        this.message = context.getMessage();
        SendMessage sendMessage;

        sendMessage = checkState() ? dialogCmd() : firstCmd();

        sendMsn(sendMessage);
    }

    private SendMessage startCmd() {
        stringBuilder.append(String.format("Привет, %s!😊\n", user.getFirstName()));
        stringBuilder.append("Я помогаю находить нужную информацию о нашем любимом городе \uD83D\uDD0E\n");
        stringBuilder.append("Нажми ❓Помощь чтобы узнать, как пользоваться системой\n");

        return textMsn(stringBuilder.toString());
    }

    private SendMessage defaultMsn() {
        return textMsn("Я молодой бот 👶 и диалоги еще вести не могу, но мой создатель 👨 сказал, что это только вопрос времени");
    }

    private SendMessage weatherCmd() {

        Weather weather = weatherRepository.getWeather();
        log.debug(weather.toString());

        stringBuilder.append(String.format("🌤 В настоящее время в Озёрске\n\n"));
        stringBuilder.append(String.format("🌡 Температура: %s ℃\n\n", (int) weather.getTemp()));
        stringBuilder.append(String.format("💦 Влажность воздуха: %s%%\n\n", weather.getHumidity()));
        stringBuilder.append(String.format("🌏 Атмосферное давление: %s мм рт.ст.\n\n", weather.getPressure()));
        stringBuilder.append(String.format("🌬 Ветер: %s м/с", (int) weather.getWindSpeed()));
        return textMsn(stringBuilder.toString());
    }

    private SendMessage companyCmd(String query) {
        List<Company> companyList = companyRepository.getAllCompany(query);
        companyList.forEach(company -> {
            String tmp;
            if ((tmp = company.getName()) != null) stringBuilder.append("\uD83C\uDFEC ").append(tmp).append("\n");
            if ((tmp = company.getDescription()) != null)
                stringBuilder.append("\uD83C\uDFE0 ").append(tmp).append("\n");
            if ((tmp = company.getHoursText()) != null) stringBuilder.append("⏰ ").append(tmp).append("\n");
            if ((tmp = company.getPhone()) != null && !tmp.isEmpty())
                stringBuilder.append("☎️ ").append(tmp).append("\n");
            if ((tmp = company.getUrl()) != null) stringBuilder.append("Сайт: ").append(tmp).append("\n");
            stringBuilder.append("\n");

        });
        return textMsn(stringBuilder.toString());
    }

    private SendMessage helpCmd(){
        stringBuilder.append("Итак. Несмотря на то, что я хочу стать человечнее, я все еще программа 🤖 Ты можешь облегчить мне работу, если научишься говорить на моем языке 💁\n\n");
        stringBuilder.append("На данном этапе разработки я могу отвечать только на запросы, которые ты видишь на клавиатуре \uD83D\uDC47\n\n");
        stringBuilder.append("Разработка ведется исключительно на энтузиазме моего создателя и поэтому мы будем рады любой помощи \uD83D\uDCB6\n\n");
        stringBuilder.append("Расскажи какой бы функционал ты добавил или просто поблагодари \uD83D\uDC4D\n");
        stringBuilder.append("А также не забудь сообщить об ошибке ⁉️ \uD83D\uDD96\n\n");
        stringBuilder.append("Обратная связь:\n");
        stringBuilder.append("\uD83D\uDCE8 email: insoftjla@gmail.com\n");
        stringBuilder.append("telegram: @insoftjla\n");
        return textMsn(stringBuilder.toString());
    }

    private SendMessage textMsn(String text) {
        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(text)
                .setReplyMarkup(replyKeyboardMarkup)
                .disableWebPagePreview();
    }

    private boolean checkState() {
        Integer stateId = user.getStateId();
        return !(stateId == null || stateId < 1);
    }

    // обработка первой команды
    private SendMessage firstCmd() {
        SendMessage sendMessage = defaultMsn();

        setMainMenu();

        stringBuilder = new StringBuilder();
        String cmd = message.hasText() ? message.getText().split("%")[0] : "";
        switch (cmd) {
            case "/start":
                sendMessage = startCmd();
                break;
            case "/toall":
                if (!user.getAdmin()) break;
                sendToAll();
                break;
            case "\uD83C\uDF24 Погода":
                sendMessage = weatherCmd();
                break;
            case "\uD83D\uDC8A Аптеки":
                stringBuilder.append("\uD83D\uDE91 Вызов скорой помощи - 112\n\n");
                sendMessage = companyCmd(drugShop);
                break;
            case "\uD83D\uDED2 Магазины":
                sendMessage = companyCmd(shop);
                break;
            case "\uD83C\uDFEB Образование":
                sendMessage = companyCmd(education);
                break;
            case "❓Помощь":
                sendMessage = helpCmd();
                break;
        }
        return sendMessage;
    }

    // обработка диалога
    private SendMessage dialogCmd() {
        return null;
    }

    public void sendMsn(SendMessage sendMessage) {
        try {
            chatBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("[sendMsn] Exception:{}", e);
        }

    }

    // Рассылка всем пользователям в базе;
    public void sendToAll() {

        List<TelegramUser> allUsers = chatBot.getAllUsers();
        List<SendMessage> sendMessages = new ArrayList<>();
        String text = message
                .getText()
                .substring(message.getText().indexOf("%") + 1);
        allUsers.forEach(user -> {
            sendMessages.add(new SendMessage()
                    .setChatId(user.getChatId())
                    .setText(text));
        });
        sendMessages.forEach(this::sendMsn);
    }

    private void setMainMenu() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        firstRow.add("🌤 Погода");
        firstRow.add("❓Помощь");
        secondRow.add("\uD83D\uDC8A Аптеки");
        secondRow.add("\uD83D\uDED2 Магазины");
        secondRow.add("🏫 Образование");
        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
}
