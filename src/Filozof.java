import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
public class Filozof extends Thread {
    static int variant;
    static int MAX;
//    int MAX;
    static Semaphore [] widelec;
    Random losuj;
    int mojNum;
    public Filozof ( int nr ) {
        mojNum=nr;
        losuj = new Random(mojNum);
    }
    public static void setMAX(int ile){
        MAX = ile;
    }
    public void run ( ) {
        switch (variant){
            case 1:
                while (true){
                System.out.println("Myślę ¦ " + mojNum);
                try {
                    Thread.sleep((long) (7000 * Math.random()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                widelec[mojNum].acquireUninterruptibly(); //przechwycenie L widelca
                widelec[(mojNum + 1) % MAX].acquireUninterruptibly(); //przechwycenie P widelca

                System.out.println("Zaczyna jeść " + mojNum);
                try {
                    Thread.sleep((long) (5000 * Math.random()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Kończy jeść " + mojNum);
                widelec[mojNum].release(); //zwolnienie L widelca
                widelec[(mojNum + 1) % MAX].release(); //zwolnienie P widelca
                }
            case 2:
                while (true) {
                    System.out.println("Myślę ¦ " + mojNum);
                    try {
                        Thread.sleep((long) (5000 * Math.random()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (mojNum == 0) {
                        widelec[(mojNum + 1) % MAX].acquireUninterruptibly();
                        widelec[mojNum].acquireUninterruptibly();
                    } else {
                        widelec[mojNum].acquireUninterruptibly();
                        widelec[(mojNum + 1) % MAX].acquireUninterruptibly();
                    }

                    System.out.println("Zaczyna jeść " + mojNum);
                    try {
                        Thread.sleep((long) (3000 * Math.random()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Kończy jeść " + mojNum);
                    widelec[mojNum].release();
                    widelec[(mojNum + 1) % MAX].release();
                }
            case 3:
                while (true) {
                    System.out.println("Myślę ¦ " + mojNum);
                    try {
                        Thread.sleep((long) (5000 * Math.random()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    int strona = losuj.nextInt(2);
                    boolean podnioslDwaWidelce = false;
                    do {
                        if (strona == 0) {
                            widelec[mojNum].acquireUninterruptibly();
                            if (!(widelec[(mojNum + 1) % MAX].tryAcquire())) {
                                widelec[mojNum].release();
                            } else {
                                podnioslDwaWidelce = true;
                            }
                        } else {
                            widelec[(mojNum + 1) % MAX].acquireUninterruptibly();
                            if (!(widelec[mojNum].tryAcquire())) {
                                widelec[(mojNum + 1) % MAX].release();
                            } else {
                                podnioslDwaWidelce = true;
                            }
                        }
                    } while (podnioslDwaWidelce == false);
                    System.out.println("Zaczyna jeść " + mojNum);
                    try {
                        Thread.sleep((long) (3000 * Math.random()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Kończy jeść " + mojNum);
                    widelec[mojNum].release();
                    widelec[(mojNum + 1) % MAX].release();
                }
        }

    }
    public static void main ( String [] args ) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Wybierz wariant, który chcesz uruchomić:");
            System.out.println("1: Żaden widelec nie jest trzymany przez 2 filozofów jednocześnie");
            System.out.println("2: Czterech spośród pięciu filozofów, najpierw sięga po widelec z lewej strony, a potem ten z prawej");
            System.out.println("3: Rzut monety w rozwiązaniu problemu ucztujących Filozofów");
            variant=sc.nextInt();
        } while (variant<1 || variant>3);
        int flzf = 0;
        do {
            System.out.println("Podaj liczbę filozofów:");
            flzf=sc.nextInt();
        } while (flzf < 2 || flzf > 100);
        setMAX(flzf);
        widelec = new Semaphore[MAX];
//        System.out.println("dl widelec: "+widelec.length);
//        System.out.println("opcja "+variant);
//        System.out.println("ile "+MAX);
        for ( int i =0; i<MAX; i++) {
            widelec [ i ]=new Semaphore ( 1 ) ;
        }
        for ( int i =0; i<MAX; i++) {
            new Filozof(i).start();
        }
    }
}