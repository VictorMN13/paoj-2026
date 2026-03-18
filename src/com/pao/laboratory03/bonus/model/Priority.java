package com.pao.laboratory03.bonus.model;

public enum Priority {
    LOW(1, 1.0),
    MEDIUM(2, 1.5),
    HIGH(3, 2.0),
    CRITICAL(4, 3.0);

    private int level;
    private double multiplier;

    private Priority(int level, double multiplier) {
        this.level = level;
        this.multiplier = multiplier;
    }

    public int getLevel() {
        return level;
    }

    public double calculateScore(int baseDays) {
        return baseDays * this.multiplier;
    }
}