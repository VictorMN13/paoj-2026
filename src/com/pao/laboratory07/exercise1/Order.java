package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private OrderState currentState;
    private final Stack<OrderState> history = new Stack<>();

    public Order(OrderState initialState) {
        this.currentState = initialState;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (currentState.isFinal()) {
            throw new OrderIsAlreadyFinalException("Order is already in a final state.");
        }

        history.push(currentState);

        currentState = switch (currentState) {
            case PLACED -> OrderState.PROCESSED;
            case PROCESSED -> OrderState.SHIPPED;
            case SHIPPED -> OrderState.DELIVERED;
            default -> currentState;
        };

        System.out.println("Order state updated to: " + currentState);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (currentState.isFinal()) {
            throw new CannotCancelFinalOrderException("Cannot cancel a final state order.");
        }

        history.push(currentState);
        currentState = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException("Cannot undo the initial order state.");
        }

        currentState = history.pop();
        System.out.println("Order state reverted to: " + currentState);
    }
}
