package org.nelbds.logic;

import android.text.TextUtils;
import android.util.Log;

import org.nelbds.edgedancer.intelligence.GeneralInterpreter;
import org.nelbds.edgedancer.intelligence.tensorflow.DetectionResult;
import org.nelbds.edgedancer.procedure.State;
import org.nelbds.edgedancer.procedure.observer.TensorFlowObserver;
import org.nelbds.edgedancer.sensor.GeneralSensor;

public class ActionObserver extends TensorFlowObserver {

    private Transformer transformer;

    public ActionObserver(GeneralSensor sensor, GeneralInterpreter interpreter, State initState) {
        super(sensor, interpreter, initState);
        transformer = new Transformer();
    }

    public String getMessage() {
        return transformer.getMessage();
    }

    public void reset() {
        transformer = new Transformer();
    }

    @Override
    public State conclusion(State lastState, DetectionResult detectionResult) {
        State state = transformer.nextState(lastState, detectionResult);
        String tip;
        Log.i("Conclusion", transformer.getMessage());
        if (!TextUtils.equals(lastState.name, state.name)) {
            tip = lastState.name + "→" + state.name;
            Log.i("wtt", tip);
        }
        Long now = System.currentTimeMillis();
        System.out.println("Ending: " + now);
        return state;
    }
}