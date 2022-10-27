package pro.sky.java.course5.bot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.java.course5.bot.model.Notice;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    List<Notice> findNoticeByDateTimeSendNotice(LocalDateTime timeNow);


}

