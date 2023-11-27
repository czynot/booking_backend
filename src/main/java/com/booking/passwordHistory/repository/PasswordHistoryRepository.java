package com.booking.passwordHistory.repository;

import com.booking.passwordHistory.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    @Query(value = "SELECT * FROM passwordhistory ph JOIN usertable ut ON ut.id = ph.user_id WHERE ut.id=? ORDER BY createdat", nativeQuery = true)
    List<PasswordHistory> findByUserId(Long userId);
}
