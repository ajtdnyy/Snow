package snow;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author dvlp
 */
public class SnowPanel extends JPanel implements Runnable {

    /**
     * 运动方向常量
     */
    public static final int DOWN = 0;
    /**
     * 运动方向常量
     */
    public static final int LEFT_DOWN = 1;
    /**
     * 运动方向常量
     */
    public static final int RIGHT_DOWN = 2;
    /**
     * 当前运动方向
     */
    private int dir = DOWN;
    /**
     * 运动速度
     */
    private int speed = 10;
    /**
     * 延迟统计
     */
    private int delay = 1;
    public int width, height;
    private Robot robot;
    private BufferedImage image;
    private Random random = new Random();
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension d = tk.getScreenSize();
    private List<Snow> snows = new ArrayList<Snow>();
    private List<Snow> stop_snows = new ArrayList<Snow>();

    public SnowPanel() {
        try {
            width = d.width;
            height = d.height;
            robot = new Robot();
            image = robot.createScreenCapture(new Rectangle(d));
            Graphics g = image.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            //g.drawImage(Main.tree, 0, 0, null);
            if (Main.image == null) {
                JOptionPane.showMessageDialog(null, "未找到图片:img.png！");
            } else {
                int x = d.width - Main.image.getWidth(null);
                int y = d.height - Main.image.getHeight(null);
                g.drawImage(Main.image, x / 2, y / 2, null);
            }
            makeSnow();
            setVisible(true);
            new Thread(this).start();
        } catch (AWTException ex) {
            Logger.getLogger(SnowPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, width, height);
        g.drawImage(Main.tree, 0, height - Main.tree.getHeight(null), null);
        for (Iterator it = snows.iterator(); it.hasNext();) {
            Snow s = (Snow) it.next();
            if (s.y < d.height - 10 && !s.isStop) {
                s.move();
            } else {
                stop_snows.add(s);
                it.remove();
            }
            g.setColor(Color.white);
            g.fillRect(s.x, s.y, 1, 1);
        }
        for (Snow ss : stop_snows) {
            g.fillRect(ss.x, ss.y, 1, 1);
        }
    }

    private void makeSnow() {
        for (int i = 0; i < 20; i++) {
            snows.add(new Snow());
        }
    }

    public void run() {
        while (true) {
            try {
                if (delay % 2 == 0) {
                    makeSnow();
                }
                repaint();
                delay++;
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SnowPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class Snow {

        /**
         * 根据方向移动
         */
        public void move() {
            if (delay % 10 == 0) {
                dir = random.nextInt(5);
            }
            oldx = x;
            oldy = y;
            switch (dir) {
                case DOWN:
                    y += speed;
                    break;
                case RIGHT_DOWN:
                    x += random.nextInt(5);
                    y += speed;
                    break;
                case LEFT_DOWN:
                    x -= random.nextInt(5);
                    y += speed;
                    break;
                default:
                    y += random.nextInt(10);
            }
            if (x > width) {
                x = width;
            }
            if (y > height) {
                y = height;
            }
            if (x < 0) {
                x = 0;
            }
            if (isStop(x, y)) {
                isStop = true;
            }

        }

        public boolean isStop(int x, int y) {
            try {
                int rgb = image.getRGB(x, y);
                if (rgb == Color.black.getRGB() || rgb == Color.green.getRGB()) {
                    if (random.nextInt(10) > 6) {
                        return true;
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                return true;
            }
            return false;
        }

        public Snow() {
            x = random.nextInt(width) + 1;
            y = random.nextInt(200) + 1;
            oldx = x;
            oldy = y;
        }
        int x, y, oldx, oldy;
        boolean isStop = false;
    }
}
