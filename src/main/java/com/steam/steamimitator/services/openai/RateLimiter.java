package com.steam.steamimitator.services.openai;

public class RateLimiter {

    private final double permitsPerMinute;
    private long lastRequestTime;

    public RateLimiter(double permitsPerMinute) {
        this.permitsPerMinute = permitsPerMinute;
        this.lastRequestTime = System.currentTimeMillis();
    }

    public synchronized void acquire() {
        long currentTime = System.currentTimeMillis();
        double timeElapsed = (currentTime - lastRequestTime) / 60000.0;

        if (timeElapsed < 1.0 / permitsPerMinute) {
            long sleepTime = (long) ((1.0 / permitsPerMinute - timeElapsed) * 60000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastRequestTime = System.currentTimeMillis();
    }
}
