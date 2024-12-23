package com.bot.slack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bot.slack.entity.paidholiday.PaidHoliday;
import com.bot.slack.repository.PaidHolidayRepository;


@Service
public class PaidHolidayService {
    @Autowired
    private PaidHolidayRepository paidHolidayRepository;

    public PaidHoliday createPaidHoliday(PaidHoliday paidHoliday) {
        return paidHolidayRepository.save(paidHoliday);
    }
}
