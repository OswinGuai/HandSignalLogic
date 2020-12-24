package org.nelbds.logic;

import android.util.Log;

import org.nelbds.edgedancer.intelligence.tensorflow.DetectionResult;
import org.nelbds.edgedancer.procedure.State;

public class Transformer {
    public static final String SENDING_SIGNAL = "z";
    public static final String READING_SIGNAL = "x";

    public static final int SAFE_NUM = 2;

    /**
     * the counter should be cleared when we reach new state.
     */
    private int num_reading = 0;
    private int num_sending = 0;

    public String getMessage() {
        return message;
    }

    private String message = "";

    private void clear() {
        num_reading = 0;
        num_sending = 0;
    }

    public State nextState(State lastState, DetectionResult results) {
        Log.i("wtt","=============lastState is: "+lastState.name);
        //TODO
        // in case that two buckets are found, choose the one with higher relative confidences.
        // bucket0 with 50% is better than bucket1 with 70%
        State currState = lastState;
        if (CustomState.SENDING.equals(lastState)) {
            lastState = CustomState.INITIAL_STATE;
            currState = CustomState.INITIAL_STATE;
            message = "消息结束";
        }
        if (CustomState.INITIAL_STATE.equals(lastState)) {
            int localNumReading = 0;
            for (int i = 0; i < results.getActualNumDetected(); i++) {
                String label = results.getLabels()[i];
                if (label.equals(READING_SIGNAL)) {
                    localNumReading++;
                }
                break;
            }
            if (localNumReading > 0) {
                num_reading++;
            }
            if (num_reading >= SAFE_NUM) {
                currState = CustomState.READING;
            }
        } else if (CustomState.READING.equals(lastState)) {
            int localNumSending = 0;
            for (int i = 0; i < results.getActualNumDetected(); i++) {
                String label = results.getLabels()[i];
                if (label.equals(SENDING_SIGNAL)) {
                    localNumSending++;
                } else {
                    message = message + label;
                }
                break;
            }
            if (localNumSending > 0) {
                num_sending++;
            }
            if (num_sending >= SAFE_NUM) {
                currState = CustomState.SENDING;
            }
        }
        if (currState != lastState) {
            clear();
        }
        return currState;
    }
}
