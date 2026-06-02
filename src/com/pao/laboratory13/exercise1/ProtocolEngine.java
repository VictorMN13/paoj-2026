package com.pao.laboratory13.exercise1;

public class ProtocolEngine {
    private Session session;

    public ProtocolEngine() {
        this.session = new Session();
    }

    public String procesareCmd(String cmd) {
        if (cmd == null || cmd.trim().isEmpty()) {
            return null;
        }

        String[] tokens = cmd.trim().split(" ");
        String command = tokens[0];

        switch (command) {
            case "AUTH":
                return executeAuth(tokens);
            case "OPEN":
                return executeOpen(tokens);
            case "SEND":
                return executeSend(tokens);
            case "BROADCAST":
                return executeBroadcast(tokens);
            case "HISTORY":
                return executeHistory(tokens);
            case "CLOSE":
                return executeClose(tokens);
            default:
                return "ERR E_PARSE UNKNOWN_COMMAND";
        }
    }

    private String executeAuth(String[] tokens) {
        if (tokens.length < 2)
            return "ERR E_PARSE AUTH";

        if (session.getState() == State.CLOSED)
            return "ERR E_STATE CLOSED";

        session.setState(State.AUTH);
        session.setUsername(tokens[1]);
        session.resetHistory();
        return "OK AUTH user=" + tokens[1];
    }

    private String executeOpen(String[] tokens) {
        if (tokens.length > 1)
            return "ERR E_PARSE OPEN";

        if (session.getState() == State.OPEN)
            return "ERR E_STATE ALREADY_OPEN";
        if (session.getState() == State.CLOSED)
            return "ERR E_STATE CLOSED";
        if (session.getState() == State.INIT)
            return "ERR E_STATE NOT_OPEN";

        session.setState(State.OPEN);
        return "OK OPEN";
    }

    private String executeSend(String[] tokens) {
        if (tokens.length < 2)
            return "ERR E_PARSE SEND";

        if (session.getState() == State.CLOSED)
            return "ERR E_STATE CLOSED";
        if (session.getState() != State.OPEN)
            return "ERR E_STATE NOT_OPEN";

        session.incrementHistory();
        return "OK OPEN sent";
    }

    private String executeBroadcast(String[] tokens) {
        if (tokens.length < 2)
            return "ERR E_PARSE BROADCAST";

        if (session.getState() == State.CLOSED)
            return "ERR E_STATE CLOSED";
        if (session.getState() != State.OPEN)
            return "ERR E_STATE NOT_OPEN";

        session.incrementHistory();
        return "OK OPEN broadcast";
    }

    private String executeHistory(String[] tokens) {
        if (tokens.length > 1)
            return "ERR E_PARSE HISTORY";

        if (session.getState() == State.CLOSED)
            return "ERR E_STATE CLOSED";
        if (session.getState() != State.OPEN)
            return "ERR E_STATE NOT_OPEN";

        return "OK OPEN history=" + session.getHistoryCount();
    }

    private String executeClose(String[] tokens) {
        if (tokens.length > 1)
            return "ERR E_PARSE CLOSE";

        if (session.getState() == State.CLOSED)
            return "ERR E_STATE CLOSED";
        if (session.getState() == State.INIT || session.getState() == State.AUTH)
            return "ERR E_STATE NOT_OPEN";

        session.setState(State.CLOSED);
        return "OK CLOSED";
    }
}
