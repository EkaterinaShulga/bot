package pro.sky.java.course5.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.java.course5.bot.model.Notice;
import pro.sky.java.course5.bot.repositories.NoticeRepository;
import pro.sky.java.course5.bot.service.TelegramBotUpdatesListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    public TelegramBotUpdatesListenerTest() {
    }

    private Notice one;
    private Notice two;
    private List<Notice> notices;
    private List<Notice> greetingNotice;
    LocalDateTime timeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);


    @BeforeEach
    public void setUp() {
        notices = new ArrayList<>();
        greetingNotice = new ArrayList<>();
        one = new Notice("Сдать курсовую", 8437932L, timeNow);
        two = new Notice("Подготовиться к собеседованию", 8437932L,
                LocalDateTime.of(2022, Month.DECEMBER, 21, 10, 15, 00));
        notices.add(one);
        notices.add(two);
        greetingNotice.add(one);

    }


    @Test
    public void getGreetingNotice() {
        Mockito.when(this.noticeRepository.findNoticeByDateTimeSendNotice(timeNow)).thenReturn(greetingNotice);
        Assertions.assertEquals(greetingNotice, noticeRepository.findNoticeByDateTimeSendNotice(timeNow));

    }

    @Test
    public void getAllNotices() {
        Mockito.when(noticeRepository.findAll()).thenReturn(notices);
        Assertions.assertEquals(notices, noticeRepository.findAll());
    }
}
