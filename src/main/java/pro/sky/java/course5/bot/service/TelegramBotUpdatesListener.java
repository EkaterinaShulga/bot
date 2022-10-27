package pro.sky.java.course5.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import liquibase.pro.packaged.L;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.java.course5.bot.model.Notice;
import pro.sky.java.course5.bot.repositories.NoticeRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    static private final String NOTICE_TEXT_PATTERN = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
    static private final Pattern pattern = Pattern.compile(NOTICE_TEXT_PATTERN);
    private final NoticeRepository noticeRepository;
    private final TelegramBot telegramBot;
    private final String model = "01.01.2022 20:00 Сделать домашнюю работу";

    private final String date = "/2022-10-27";
    private final String commandForDate = "/date";


    public TelegramBotUpdatesListener(NoticeRepository noticeRepository, TelegramBot telegramBot) {
        this.noticeRepository = noticeRepository;
        this.telegramBot = telegramBot;
    }

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String st = "/start";

            if (update.message().text().equals(st)) {
                sendHello(update);
            } else if (update.message().text().equals(date)) {
                checkNoticeByDate(date, update);
            } else {
                createNotice(update);
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendHello(Update update) {
        String welcomeMessage = "Привет я бот. Если вы хотите внести запись, она обязательно должна иметь" +
                " следующий вид: " + model;
        String welcomeMessage2 = "  Вы можете проверить наличие записей на конкретную дату" +
                " для этого введи команду следющего вида: " + commandForDate;
        long chatId = update.message().chat().id();
        SendResponse response = telegramBot.execute(new SendMessage(chatId, welcomeMessage + welcomeMessage2));
        if (response.isOk()) {
            System.out.println("Cообщение отправлено");
        } else {
            System.out.println("Сообщение не было отправлено, код ошибки " + response.errorCode());
        }
    }

    public void createNotice(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String dateAndTimeString = matcher.group(1);
            String item = matcher.group(3);
            LocalDateTime dateAndTime = LocalDateTime.parse(dateAndTimeString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Notice notice = new Notice();
            notice.setTextNotice(item);
            notice.setChatId(chatId);
            notice.setDateTimeSendNotice(dateAndTime);
            noticeRepository.save(notice);
            String messageForBase = "Я сохранил вашу задачу в базе данных.";
            telegramBot.execute(new SendMessage(chatId, messageForBase));
            logger.info("Данные сохранены в базе данных");
        } else {
            String warningMessage = "Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: "
                    + model + ". Попробуйте еще раз.";
            telegramBot.execute(new SendMessage(chatId, warningMessage));
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotice() {
        LocalDateTime timeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Notice> allSuitableNotices = noticeRepository.findNoticeByDateTimeSendNotice(timeNow);
        for (Notice notice : allSuitableNotices) {
            telegramBot.execute(new SendMessage(notice.getChatId(), notice.getTextNotice()));
            logger.info("Напоминание отправлено");
        }
    }

    public void checkNoticeByDate(String date, Update update) {
        long chatId = update.message().chat().id();
        telegramBot.execute(new SendMessage(chatId, "Введите дату в формате " + date));
        String message = "На указанную дату имеются следующие записи";
        StringBuilder st = new StringBuilder(date);
        st.deleteCharAt(0);
        String onlyDate = st.toString();
        LocalDate dateCheck = LocalDate.parse(onlyDate);
        List<Notice> allNotice = noticeRepository.findAll();
        for (Notice notice : allNotice) {
            LocalDateTime dateTimeNotice = notice.getDateTimeSendNotice();
            LocalDate date1 = dateTimeNotice.toLocalDate();
            if (date1.equals(dateCheck)) {
                telegramBot.execute(new SendMessage(notice.getChatId(), message + notice.getTextNotice()));
                logger.info("Информация отправлена");
            }


        }


    }

}






