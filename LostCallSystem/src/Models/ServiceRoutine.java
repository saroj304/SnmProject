package Models;
public class ServiceRoutine {
    public static  void pause(){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int clk=1057;
}
