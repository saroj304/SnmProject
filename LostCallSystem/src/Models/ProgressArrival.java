package Models;

import jdk.jshell.execution.Util;

public class ProgressArrival {
    public static Progress mapArrivalToProgress(Arrivals arrival){
        int end=arrival.getLength()+ ServiceRoutine.clk;
        Progress progress=new Progress(arrival.getFrom(), arrival.getTo(),end);
        return progress;
    }
}
