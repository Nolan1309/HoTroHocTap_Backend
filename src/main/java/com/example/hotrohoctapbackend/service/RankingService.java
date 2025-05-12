package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV3.Ranking.RankingDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RankingRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Ranking;
import com.example.hotrohoctapbackend.enums.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RankingService {
    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Page<RankingDTO> getRankings(
            PeriodType periodType,
            Boolean status,
            String startDate,
            String endDate,
            Integer ranking,
            String accountName,
            int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (startDate != null && !startDate.isEmpty()) {
            start = LocalDate.parse(startDate).atStartOfDay(); // 2025-05-01T00:00:00
        }

        if (endDate != null && !endDate.isEmpty()) {
            end = LocalDate.parse(endDate).atTime(LocalTime.MAX); // 2025-05-31T23:59:59.999999999
        }

        Page<Ranking> rankingPage = rankingRepository.findRankingsWithFilters(periodType, status, start, end, ranking, accountName, pageable);

        return rankingPage.map(this::convertToDTO);
    }


    private RankingDTO convertToDTO(Ranking ranking) {
        RankingDTO rankingDTO = new RankingDTO();
        rankingDTO.setId(ranking.getId());
        rankingDTO.setAccountId(ranking.getAccount().getId());
        rankingDTO.setAccountName(ranking.getAccount().getFullname());
        rankingDTO.setAvatar(ranking.getAccount().getImage());
        rankingDTO.setPeriodType(ranking.getPeriodType());
        rankingDTO.setTotalPoints(ranking.getTotalPoints());
        rankingDTO.setRanking(Optional.ofNullable(ranking.getRanking()).orElse(0));

        rankingDTO.setStatus(ranking.getStatus());
        rankingDTO.setCreatedAt(ranking.getCreatedAt());
        rankingDTO.setUpdatedAt(ranking.getUpdatedAt());
        return rankingDTO;
    }

    public void addDailyRankingIfNotExists() {
        // Lấy ngày hiện tại
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();  // 00:00:00 hôm nay
        LocalDateTime endOfDay = startOfDay.plusDays(1);  // 23:59:59 hôm nay

        // Lấy tất cả tài khoản người dùng
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            // Kiểm tra nếu đã có bản ghi DAILY cho ngày hôm nay
            List<Ranking> existingDailyRanking = rankingRepository.findByAccountAndPeriodTypeAndUpdatedAtBetween(
                    account.getId(), PeriodType.DAILY, startOfDay, endOfDay
            );

            // Nếu chưa có bản ghi DAILY cho ngày hôm nay
            if (existingDailyRanking.isEmpty()) {
                // Tạo mới bản ghi DAILY cho người dùng
                Ranking newDailyRanking = new Ranking();
                newDailyRanking.setAccount(account);
                newDailyRanking.setPeriodType(PeriodType.DAILY);
                newDailyRanking.setTotalPoints(0);  // Điểm có thể là 0 khi mới bắt đầu trong ngày
                newDailyRanking.setRanking(null);  // Chưa có xếp hạng, sẽ tính sau
                newDailyRanking.setStatus(false);  // Đánh dấu là chưa tính xếp hạng
                newDailyRanking.setCreatedAt(LocalDateTime.now());
                newDailyRanking.setUpdatedAt(LocalDateTime.now());

                // Lưu bản ghi DAILY vào cơ sở dữ liệu
                rankingRepository.save(newDailyRanking);
            }
        }
    }

    public void updateWeeklyRankings() {

        List<Account> accounts = accountRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1); // Chủ Nhật trước đó
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);  // Chủ Nhật cuối tuần

        for (Account account : accounts) {
            int totalPointsForWeek = calculateTotalPointsForWeek(account, startOfWeek, endOfWeek);
            saveWeeklyRanking(account, totalPointsForWeek, endOfWeek);
        }
    }

    private int calculateTotalPointsForWeek(Account account, LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
        int totalPoints = 0;
        List<Ranking> dailyRankings = rankingRepository.findByAccountAndPeriodTypeAndUpdatedAtBetween(account.getId(), PeriodType.DAILY, startOfWeek, endOfWeek);
        for (Ranking dailyRanking : dailyRankings) {
            totalPoints += dailyRanking.getTotalPoints();
        }
        return totalPoints;
    }

    private void saveWeeklyRanking(Account account, int totalPoints, LocalDateTime startOfWeek) {

        Ranking existingWeeklyRanking = rankingRepository.findByAccountAndPeriodTypeAndPeriodValue(account.getId(), PeriodType.WEEKLY, startOfWeek);

        if (existingWeeklyRanking != null) {
            // Cập nhật thông tin
            existingWeeklyRanking.setTotalPoints(totalPoints);
            existingWeeklyRanking.setUpdatedAt(LocalDateTime.now());
            existingWeeklyRanking.setStatus(true);  // Đánh dấu đã tính toán xong cho tuần này
            rankingRepository.save(existingWeeklyRanking);
        } else {
            // Tạo bản ghi mới
            Ranking newWeeklyRanking = new Ranking();
            newWeeklyRanking.setAccount(account);
            newWeeklyRanking.setPeriodType(PeriodType.WEEKLY);
            newWeeklyRanking.setTotalPoints(totalPoints);
            newWeeklyRanking.setRanking(1);
            newWeeklyRanking.setStatus(true);
            newWeeklyRanking.setCreatedAt(LocalDateTime.now());
            newWeeklyRanking.setUpdatedAt(LocalDateTime.now());
            rankingRepository.save(newWeeklyRanking);
        }
    }

    public void updateWeeklyRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1); // Chủ Nhật đầu tuần
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);  // Chủ Nhật cuối tuần

        // Lấy tất cả các bản ghi xếp hạng tuần (WEEKLY)
        List<Ranking> weeklyRankings = rankingRepository.findByPeriodTypeAndDate(
                PeriodType.WEEKLY, startOfWeek, endOfWeek);

        // Sắp xếp các bản ghi tuần theo tổng điểm từ cao xuống thấp
        List<Ranking> sortedRankings = weeklyRankings.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getTotalPoints(), r1.getTotalPoints()))  // Sắp xếp theo điểm
                .collect(Collectors.toList());

        // Cập nhật thứ hạng cho các bản ghi trong tuần
        int rank = 1;
        for (Ranking ranking : sortedRankings) {
            ranking.setRanking(rank);  // Cập nhật thứ hạng
            ranking.setUpdatedAt(LocalDateTime.now());  // Cập nhật thời gian
            rankingRepository.save(ranking);  // Lưu lại bản ghi với thứ hạng mới
            rank++;  // Tăng thứ hạng cho người tiếp theo
        }
    }

    public void calculateAndUpdateDailyRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();  // 00:00:00 hôm nay
        LocalDateTime endOfDay = startOfDay.plusDays(1);  // 23:59:59 hôm nay

        List<Ranking> dailyRankings = rankingRepository.findByPeriodTypeAndDate(PeriodType.DAILY, startOfDay, endOfDay);

        // Sắp xếp theo điểm (từ cao đến thấp)
        List<Ranking> sortedRankings = dailyRankings.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getTotalPoints(), r1.getTotalPoints()))  // Sắp xếp theo tổng điểm
                .collect(Collectors.toList());

        int rank = 1;
        for (Ranking ranking : sortedRankings) {
            ranking.setRanking(rank);  // Cập nhật thứ hạng
            ranking.setUpdatedAt(LocalDateTime.now());  // Cập nhật thời gian
            rankingRepository.save(ranking);  // Lưu lại bản ghi với thứ hạng mới
            rank++;  // Tăng thứ hạng cho người tiếp theo
        }
    }

    public void addMonthlyRankingIfNotExists() {
        // Lấy tháng và năm hiện tại
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();  // 00:00:00 đầu tháng
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);  // 23:59:59 cuối tháng

        // Lấy tất cả tài khoản người dùng
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            // Kiểm tra xem có bản ghi MONTHLY cho tháng này chưa
            List<Ranking> existingMonthlyRanking = rankingRepository.findByAccountAndPeriodTypeAndUpdatedAtBetween(
                    account.getId(), PeriodType.MONTHLY, startOfMonth, endOfMonth
            );

            // Nếu chưa có bản ghi MONTHLY cho tháng hiện tại
            if (existingMonthlyRanking.isEmpty()) {
                // Tạo mới bản ghi MONTHLY cho người dùng
                Ranking newMonthlyRanking = new Ranking();
                newMonthlyRanking.setAccount(account);
                newMonthlyRanking.setPeriodType(PeriodType.MONTHLY);
                newMonthlyRanking.setTotalPoints(0);  // Điểm có thể là 0 khi mới bắt đầu trong tháng
                newMonthlyRanking.setRanking(null);  // Chưa có xếp hạng, sẽ tính sau
                newMonthlyRanking.setStatus(false);  // Đánh dấu là chưa tính xếp hạng
                newMonthlyRanking.setCreatedAt(LocalDateTime.now());
                newMonthlyRanking.setUpdatedAt(LocalDateTime.now());

                // Lưu bản ghi MONTHLY vào cơ sở dữ liệu
                rankingRepository.save(newMonthlyRanking);
            }
        }
    }

    public void calculateAndUpdateMonthlyRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();  // 00:00:00 đầu tháng
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);  // 23:59:59 cuối tháng

        List<Ranking> monthlyRankings = rankingRepository.findByPeriodTypeAndDate(PeriodType.MONTHLY, startOfMonth, endOfMonth);

        // Sắp xếp theo điểm (từ cao đến thấp)
        List<Ranking> sortedRankings = monthlyRankings.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getTotalPoints(), r1.getTotalPoints()))  // Sắp xếp theo tổng điểm
                .collect(Collectors.toList());

        int rank = 1;
        for (Ranking ranking : sortedRankings) {
            ranking.setRanking(rank);  // Cập nhật thứ hạng
            ranking.setUpdatedAt(LocalDateTime.now());  // Cập nhật thời gian
            rankingRepository.save(ranking);  // Lưu lại bản ghi với thứ hạng mới
            rank++;  // Tăng thứ hạng cho người tiếp theo
        }
    }
}
