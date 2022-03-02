package Models;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Call {
	private int max;
	private int totalUsers;

	private ArrayList<Progress> callsInProgress;
	private ArrayList<Arrivals> callsToArrive;

	Call(int totalLinks, int totalNumberofUsers) {
		this.callsInProgress = new ArrayList<Progress>();
		this.callsToArrive = new ArrayList<Arrivals>();

		// attributes of simulation
		this.max = totalLinks;// links
		this.totalUsers = totalNumberofUsers;

		Caller.callers = new ArrayList<Integer>();
		for (int i = 0; i <= totalUsers; i++) {
			Caller.callers.add(0);
		}

		Random ran = new Random();
		int firstCaller = 1;
		int lastCaller = totalNumberofUsers;
		for (int i = 0; i <= (ThreadLocalRandom.current().nextInt(20, 30)); i++) {

			int from = ThreadLocalRandom.current().nextInt(firstCaller, lastCaller + 1);
			int to = ThreadLocalRandom.current().nextInt(firstCaller, lastCaller + 1);

			while (true) {
				if (from != to) {
					break;
				}
				to = ThreadLocalRandom.current().nextInt(firstCaller, lastCaller + 1);
			}

			int length = ThreadLocalRandom.current().nextInt(40, 60);

			int arrivalTime = ThreadLocalRandom.current().nextInt(1060, 1100);
			this.callsToArrive.add(new Arrivals(from, to, length, arrivalTime));
		}

	}

	public Boolean addProgressToCallsInProgress(Progress progress) {

		if (Caller.checkCallPossibility(progress.getFrom(), progress.getTo())) {

			Caller.callers.set(progress.getFrom(), 1);
			Caller.callers.set(progress.getTo(), 1);
			this.callsInProgress.add(progress);
			return true;
		}
		++Counter.busy;
		System.out.println("Call Dropped due to one of the participant being busy");

		return false;
	}

	public void removeProgressFromCallsInProgress(Progress progress) {
		Caller.callers.set(progress.getFrom(), 0);
		Caller.callers.set(progress.getTo(), 0);
		this.callsInProgress.remove(progress);
		System.out.println("Call has successfully Ended:" + progress);
		++Counter.completed;
	}

	public void checkProgressCalls() {
		ArrayList<Progress> progressOnCurrentclkToOut = new ArrayList<Progress>();
		if (this.getCallsInProgress().size() != 0) {

			for (Progress progress : this.getCallsInProgress()) {
				if (progress.getEnd() == ServiceRoutine.clk) {
					progressOnCurrentclkToOut.add(progress);
				}
			}

			for (Progress progress : progressOnCurrentclkToOut) {

//
				if (progress.getEnd() == ServiceRoutine.clk) {
					this.removeProgressFromCallsInProgress(progress);
					if (this.getCallsInProgress().size() == 0) {
					
						break;
					}
				}

			}
		}

	}

	public void checkArrivalCalls() {
		ArrayList<Arrivals> arrivalOnCurrentclk = new ArrayList<Arrivals>();
		if (this.getCallsToArrive().size() != 0) {

			for (Arrivals arrival : this.getCallsToArrive()) {

				if (arrival.getArrivalTime() == ServiceRoutine.clk) {
					arrivalOnCurrentclk.add(arrival);
				}

			}
		}
		for (Arrivals arrival : arrivalOnCurrentclk) {
			if (addArrivalCallToProgress(arrival)) {
				System.out.println("Has been added to progress:" + arrival);
			} else {
	
				System.out.println("Lost Call = " + arrival);
			}
			++Counter.processed;
			if (this.getCallsToArrive().size() == 0) {
				break;
			}

		}
	}

	public Boolean addArrivalCallToProgress(Arrivals arrival) {
		callsToArrive.remove(arrival);
		if ((callsInProgress.size()) != (this.max)) {
			Progress progress = ProgressArrival.mapArrivalToProgress(arrival);
			return this.addProgressToCallsInProgress(progress);

		}
		++Counter.blocked;
		System.out.println("The Links are full due to which call has been dropped.");
		return false;
	}

	public ArrayList<Arrivals> getCallsToArrive() {
		return callsToArrive;
	}

	public void setCallsToArrive(ArrayList<Arrivals> callsToArrive) {
		this.callsToArrive = callsToArrive;
	}

	public ArrayList<Progress> getCallsInProgress() {
		return callsInProgress;
	}

	public void setCallsInProgress(ArrayList<Progress> callsInProgress) {
		this.callsInProgress = callsInProgress;
	}
}
