package org.nelbds.logic;

import org.nelbds.edgedancer.procedure.State;

public class CustomState {
    public static final State INITIAL_STATE = new org.nelbds.edgedancer.procedure.State(1, "初始状态");
    public static final State READING = new org.nelbds.edgedancer.procedure.State(2, "读取信号");
    public static final State SENDING = new org.nelbds.edgedancer.procedure.State(3, "发送消息");
}
