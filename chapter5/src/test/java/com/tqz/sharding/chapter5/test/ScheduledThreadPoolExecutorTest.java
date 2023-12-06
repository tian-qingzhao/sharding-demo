package com.tqz.sharding.chapter5.test;

import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * scheduleAtFixedRate 方法的执行是按照固定时间间隔进行执行的，我们可以理解为等差数列的执行方式，假设 n 为初始延迟，
 * 即 n 执行，n + period 执行，然后 n + 2 * period 执行，依次往后。但是必须等待上个任务执行完毕，下个任务才能开始执行，
 * 即实际上是这么执行的，假设 run 是任务执行时间，则 n 执行，然后 n + max(period,run) 依次执行。
 * scheduleWithFixedDelay 方法的执行是上个任务执行完，然后过 delay 时间后执行下个任务，假设 n 为初始延迟，
 * 即 n 执行，n + run + delay 执行，然后 n + 2 * (run + delay) 执行。
 * 两种方法都是遇到异常后，后序都无法再执行。
 *
 * @author tianqingzhao
 * @since 2023/12/6 9:20
 */
public class ScheduledThreadPoolExecutorTest {

    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            r -> new Thread(r, "custom-scheduled-thread-pool"));

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // ============================= scheduleAtFixedRate ===============================

    /**
     * 任务任务运行时间小于 period
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleAtFixedRateTest1() throws InterruptedException {
        long RUN_TIME = 2000L;
        long PERIOD = 3000L;

        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " start " + LocalTime.now().format(FORMATTER));
                    try {
                        Thread.sleep(RUN_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " end " + LocalTime.now().format(FORMATTER));
                },
                0, PERIOD, TimeUnit.MILLISECONDS);

        Thread.sleep(10000);
    }

    /**
     * 任务任务运行时间等于 period
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleAtFixedRateTest2() throws InterruptedException {
        long RUN_TIME = 3000L;
        long PERIOD = 3000L;

        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " start " + LocalTime.now().format(FORMATTER));
                    try {
                        Thread.sleep(RUN_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " end " + LocalTime.now().format(FORMATTER));
                },
                0, PERIOD, TimeUnit.MILLISECONDS);
    }

    /**
     * 任务任务运行时间大于 period
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleAtFixedRateTest3() throws InterruptedException {
        long RUN_TIME = 5000L;
        long PERIOD = 3000L;

        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " start " + LocalTime.now().format(FORMATTER));
                    try {
                        Thread.sleep(RUN_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " end " + LocalTime.now().format(FORMATTER));
                },
                0, PERIOD, TimeUnit.MILLISECONDS);
    }

    // ============================= scheduleAtFixedRate ===============================

    /**
     * 任务任务运行时间小于 period
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleWithFixedDelayTest1() throws InterruptedException {
        long RUN_TIME = 2000L;
        long PERIOD = 3000L;

        scheduledExecutorService.scheduleWithFixedDelay(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " start " + LocalTime.now().format(FORMATTER));
                    try {
                        Thread.sleep(RUN_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " end " + LocalTime.now().format(FORMATTER));
                },
                0, PERIOD, TimeUnit.MILLISECONDS);
    }

    /**
     * 任务任务运行时间等于 period
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleWithFixedDelayTest2() throws InterruptedException {
        long RUN_TIME = 3000L;
        long PERIOD = 3000L;

        scheduledExecutorService.scheduleWithFixedDelay(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " start " + LocalTime.now().format(FORMATTER));
                    try {
                        Thread.sleep(RUN_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " end " + LocalTime.now().format(FORMATTER));
                },
                0, PERIOD, TimeUnit.MILLISECONDS);
    }

    /**
     * 任务任务运行时间大于 period
     *
     * @throws InterruptedException
     */
    @Test
    public void scheduleWithFixedDelayTest3() throws InterruptedException {
        long RUN_TIME = 5000L;
        long PERIOD = 3000L;

        scheduledExecutorService.scheduleWithFixedDelay(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " start " + LocalTime.now().format(FORMATTER));
                    try {
                        Thread.sleep(RUN_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " end " + LocalTime.now().format(FORMATTER));
                },
                0, PERIOD, TimeUnit.MILLISECONDS);
    }
}
