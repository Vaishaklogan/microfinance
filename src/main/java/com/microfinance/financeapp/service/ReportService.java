package com.microfinance.financeapp.service;

import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.WeeklyCollectionRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final WeeklyCollectionRepository collectionRepo;
    private final LoanRepository loanRepo;

    public ReportService(WeeklyCollectionRepository collectionRepo,
            LoanRepository loanRepo) {
        this.collectionRepo = collectionRepo;
        this.loanRepo = loanRepo;
    }

    // Weekly deposit amount
    public double getWeeklyDeposit(LocalDate date) {
        Double total = collectionRepo.getTotalCollectedForDate(date);
        return total == null ? 0 : total;
    }

    // Group wise weekly totals
    public Map<String, Double> getGroupWiseTotals(LocalDate date) {
        Map<String, Double> map = new HashMap<>();
        List<Object[]> data = collectionRepo.getGroupWiseCollection(date);

        for (Object[] row : data) {
            map.put((String) row[0], (Double) row[1]);
        }
        return map;
    }

    // Yearly Sunday expected collection
    public Map<LocalDate, Double> getYearlySundayPlan(int year) {
        Map<LocalDate, Double> plan = new HashMap<>();

        LocalDate date = LocalDate.of(year, 1, 1);
        while (date.getYear() == year) {
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {

                double expected = loanRepo
                        .findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                "ACTIVE", date, date)
                        .stream()
                        .mapToDouble(l -> l.getWeeklyInstallment())
                        .sum();

                plan.put(date, expected);
            }
            date = date.plusDays(1);
        }
        return plan;
    }
}
