package com.pao.laboratory13.exercise1;

class Session {
    private State state = State.INIT;
    private String username = null;
    private int historyCount = 0;

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getHistoryCount() { return historyCount; }
    public void incrementHistory() { this.historyCount++; }
    public void resetHistory() { this.historyCount = 0; }
}
