package org.example;


import org.example.policy.basic.CompositeRatePolicy;
import org.example.policy.basic.RegularPolicy;
import org.example.calculator.DayOfWeekAmountCalculator;
import org.example.calculator.TimeAmountCalculator;
import org.example.calculator.TimeSlotAmountCalculator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        regularRateTest();
        timeRateTest();
        dayOfWeekRateTest();
        timeSlotRateTest();
    }

    // 10초당 18원
    // 현재 테스트 call 시간 0시 ~ 3분
    // 3분 = 180초
    // 180초 / 10초 = 18단위
    // 18단위 * 18원 = 324원
    public static void regularRateTest() {
        RegularPolicy ratePolicy = new RegularPolicy(
                Duration.ofSeconds(10), // 10초당
                Money.wons(18)
        );

        Phone phone = new Phone(ratePolicy);

        Call call = new Call(
                LocalDateTime.of(2026, 4, 30, 0, 0),
                LocalDateTime.of(2026, 4, 30, 0, 3)
        );

        phone.addCall(call);

        Money fee = phone.calculateFee();

        System.out.println(fee.getAmount());
    }

    // 10초당 0~19시 18원, 19~24시 15원
    // 현재 테스트 call 시간 18시 ~ 20시
    // 18~19시: 18원 구간 1시간
    // 19~20시: 15원 구간 1시간 계산
    // 18 * 3600 + 15 * 3600 = 11880
    public static void timeRateTest() {
        TimeAmountCalculator timeBased0to19 = new TimeAmountCalculator(
                LocalTime.of(0, 0),
                LocalTime.of(19, 0),
                Money.wons(18)
        );
        TimeAmountCalculator timeBased19to24 = new TimeAmountCalculator(
                LocalTime.of(19, 0),
                LocalTime.of(23, 59, 59),
                Money.wons(15)
        );

        CompositeRatePolicy ratePolicy = new CompositeRatePolicy(
                Duration.ofSeconds(10), // 10초당
                List.of(timeBased0to19, timeBased19to24)
        );

        Phone phone = new Phone(ratePolicy);

        Call call = new Call(
                LocalDateTime.of(2026, 4, 30, 18, 0),
                LocalDateTime.of(2026, 4, 30, 20, 0)
        );
        Call call2 = new Call(
                LocalDateTime.of(2026, 4, 30, 20, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59, 59)
        );

        phone.addCall(call);

        Money fee = phone.calculateFee();

        System.out.println(fee.getAmount());
    }

    // 평일 10초당 38원, 휴일 10초당 19원
    // 현재 테스트 call 시간 금요일 21시 ~ 토요일 03시
    // 금요일 21~24시: 평일 요금 3시간
    // 토요일 00~03시: 휴일 요금 3시간 계산
    // 61560
    public static void dayOfWeekRateTest() {
        DayOfWeekAmountCalculator dayOfWeekCalculator =
                new DayOfWeekAmountCalculator(
                        Money.wons(38), // 평일
                        Money.wons(19)  // 휴일
                );

        CompositeRatePolicy ratePolicy = new CompositeRatePolicy(
                Duration.ofSeconds(10), // 10초당
                List.of(dayOfWeekCalculator)
        );

        Phone phone = new Phone(ratePolicy);

        Call call = new Call(
                LocalDateTime.of(2026, 5, 1, 21, 0), // 금요일
                LocalDateTime.of(2026, 5, 2, 3, 0)   // 토요일
        );

        phone.addCall(call);

        Money fee = phone.calculateFee();

        System.out.println(fee.getAmount());
    }

    // 초기 1분 동안 10초당 50원, 1분 초과 시 10초당 20원
    // 현재 테스트 call 시간 00시 ~ 03분
    // 0~1분: 50원 구간 1분
    // 1~3분: 20원 구간 2분 계산
    // 540
    public static void timeSlotRateTest() {
        TimeSlotAmountCalculator firstMinute = new TimeSlotAmountCalculator(
                Duration.ZERO,
                Duration.ofMinutes(1),
                Money.wons(50)
        );

        TimeSlotAmountCalculator afterFirstMinute = new TimeSlotAmountCalculator(
                Duration.ofMinutes(1),
                Duration.ofDays(9999),
                Money.wons(20)
        );

        CompositeRatePolicy ratePolicy = new CompositeRatePolicy(
                Duration.ofSeconds(10), // 10초당
                List.of(firstMinute, afterFirstMinute)
        );

        Phone phone = new Phone(ratePolicy);

        Call call = new Call(
                LocalDateTime.of(2026, 4, 30, 0, 0),
                LocalDateTime.of(2026, 4, 30, 0, 3)
        );

        phone.addCall(call);

        Money fee = phone.calculateFee();

        System.out.println(fee.getAmount());
    }
}