package com.bot.slack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bot.slack.entity.paidholiday.HolidayRquestStatus;
import com.bot.slack.entity.paidholiday.PaidHoliday;

@Repository
public interface PaidHolidayRepository extends JpaRepository<PaidHoliday, Long> {
    List<PaidHoliday> findByRequestStatus(HolidayRquestStatus requestStatus);
}
