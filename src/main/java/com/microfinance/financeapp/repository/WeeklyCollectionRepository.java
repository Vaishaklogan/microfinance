package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.WeeklyCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyCollectionRepository extends JpaRepository<WeeklyCollection, Long> {

        @Query("""
                            SELECT SUM(w.amountPaid)
                            FROM WeeklyCollection w
                            WHERE w.collectionDate = :date
                        """)
        Double getTotalCollectedForDate(LocalDate date);

        @Query("""
                            SELECT w.loan.member.group.groupName, SUM(w.amountPaid)
                            FROM WeeklyCollection w
                            WHERE w.collectionDate = :date
                            GROUP BY w.loan.member.group.groupName
                        """)
        List<Object[]> getGroupWiseCollection(LocalDate date);
}
