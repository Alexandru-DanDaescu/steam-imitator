package com.steam.steamimitator.services.openai;

public class RateLimiter {

    private final double permitsPerMinute;
    private long nextAvailableTime;
    private final Object lock = new Object();

    public RateLimiter(double permitsPerMinute) {
        this.permitsPerMinute = permitsPerMinute;
        this.nextAvailableTime = System.currentTimeMillis();
    }

    public void acquire() {
        synchronized (lock) {
            while (true) {
                long currentTime = System.currentTimeMillis();
                double timeElapsed = (currentTime - nextAvailableTime) / 60000.0;
                if (timeElapsed >= 1.0 / permitsPerMinute) {
                    nextAvailableTime = currentTime;
                    break;
                }
                long sleepTime = (long) ((1.0 / permitsPerMinute - timeElapsed) * 60000);
                try {
                    lock.wait(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
