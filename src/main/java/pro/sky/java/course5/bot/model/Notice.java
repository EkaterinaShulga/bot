package pro.sky.java.course5.bot.model;


import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String textNotice;
    private long chatId;
    private LocalDateTime dateTimeSendNotice;

    public Notice() {
    }
    public Notice(String textNotice, long chatId, LocalDateTime dateTimeSendNotice) {
        this.textNotice = textNotice;
        this.chatId = chatId;
        this.dateTimeSendNotice = dateTimeSendNotice;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDateTimeSendNotice() {
        return dateTimeSendNotice;
    }

    public void setDateTimeSendNotice(LocalDateTime dateTimeSendNotice) {
        this.dateTimeSendNotice = dateTimeSendNotice;
    }

    public String getTextNotice() {
        return textNotice;
    }

    public void setTextNotice(String textNotice) {
        this.textNotice = textNotice;
    }

}

