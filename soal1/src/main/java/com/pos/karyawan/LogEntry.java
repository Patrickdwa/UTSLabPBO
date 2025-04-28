package com.pos.karyawan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private String user;
    private String action;
    private String timestamp;

    public LogEntry(String user, String action) {
        this.user = user;
        this.action = action;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters
    public String getUser() { return user; }
    public String getAction() { return action; }
    public String getTimestamp() { return timestamp; }
}