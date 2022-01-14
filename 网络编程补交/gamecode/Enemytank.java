package gamecode;

public class Enemytank extends Tank implements Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Enemytank(int x, int y, int dir) {
        super(50, x, y, dir, 1);
    }

    @Override
    public void run() {
        while (isLive()) {
            try {
                Thread.sleep(SleepTime.LONG_SLEEPTIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setDir((int) (Math.random() * 4));
            for (int i = 0; i < 5; i++) {
                move(getDir());
                try {
                    Thread.sleep(SleepTime.SHORT_SLEEPTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(bullets.size()==0)
                shoot();


            //System.out.println("敌方坦克完成了一次动作");
        }
    }

}