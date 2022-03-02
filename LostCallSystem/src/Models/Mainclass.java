package Models;
import java.time.Clock;
import java.util.Scanner;


public class Mainclass {
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Links :");
        int totalLinks=scan.nextInt();

        System.out.println("Enter Total Users :");
        int totalUsers=scan.nextInt();

        Call call=new Call(totalLinks,totalUsers);

        while(true){

            call.checkArrivalCalls();
            call.checkProgressCalls();

            System.out.println("Current Clock="+ServiceRoutine.clk++);
            ServiceRoutine.pause();
            if(ServiceRoutine.clk==4000){
                break;
            }
            System.out.println("The Progress Queue:");
            for (Progress progress:call.getCallsInProgress()) {
                System.out.println(progress);
            }
            System.out.println("The Arrival Queue:");
            for (Arrivals arrival:call.getCallsToArrive()) {
                System.out.println(arrival);
            }
            Counter.getCallCounter();
            System.out.println("-------------------------------------------------");
        }
    }
}
