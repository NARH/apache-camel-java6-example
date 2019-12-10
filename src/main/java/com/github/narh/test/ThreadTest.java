package com.github.narh.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadTest {

  static Logger log = LoggerFactory.getLogger(ThreadTest.class);

  public static void main(String... args) throws Exception {
    log.info("=== START ===");
    ThreadTest threadTest = new ThreadTest(10, 100);
    threadTest.execute();
    while(!threadTest.isCompleted()) {
      if(0 == threadTest.getCompleted() % 5) {
        Thread.sleep(500);
        log.info("{}% completed.", threadTest.getCompleted());
      }
    }
    log.info("=== END ===");
  }

  final int poolSize;
  final int taskSize;
  ThreadPoolExecutor exec;

  public ThreadTest(final int poolSize, final int taskSize) {
    this.poolSize = poolSize;
    this.taskSize = taskSize;
    exec = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
  }

  public void shutdown() {
    exec.shutdownNow();
  }

  public long getCompleted() {
    return exec.getCompletedTaskCount() - exec.getActiveCount();
  }

  public long getActiveCount() {
    return exec.getActiveCount();
  }

  public boolean isCompleted() {
    return 0 == Long.valueOf(exec.getCompletedTaskCount()).compareTo(Long.valueOf(taskSize));
  }
  public void execute() throws Exception {
    for(int i = 0; i < taskSize; i++) exec.execute(new Task(String.format("foo-%d", i), i));
    exec.shutdown();
  }

  public static class Task implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());
    private final String name;
    private final int wait;

    public Task(final String name, int wait) {
      this.name = name;
      this.wait = wait;
    }

    @Override
    public void run() {
      log.trace("===> START TASK {}", Thread.currentThread().getId());
      try {
        Thread.sleep(wait*100);
        log.info("name = {}", name);
      } catch(InterruptedException e) {
        log.error(e.getMessage(), e);
      }
      log.trace("<=== END TASK {}", Thread.currentThread().getId());
    }

    public void shutdown() throws Exception {
      log.info("=== THREAD {} shutdown.", Thread.currentThread().getId());
    }
  }
}
