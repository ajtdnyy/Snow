/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snow;

import com.sun.awt.AWTUtilities;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author dvlp
 */
public class Main extends JFrame implements ActionListener {

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dimension = toolkit.getScreenSize();
    public static Image tree;
    public static Image image;

    public Main() {
        setUndecorated(true);
        setBounds(0, 0, dimension.width, dimension.height);
        setAlwaysOnTop(true);
        //AWTUtilities.setWindowOpacity(this, 0.5f);
        AWTUtilities.setWindowOpaque(this, false);//设置窗体完全透明
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initImage("img.png");
        try {
            SystemTray st = SystemTray.getSystemTray();
            if (SystemTray.isSupported() && image != null) {
                st.add(new TrayIcon(image, "下雪程序", initMenu()));
            }
        } catch (AWTException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    System.exit(0);
                }
            }
        });
        add(new SnowPanel());
    }

    private static void initImage(String path) {
        try {
            InputStream in = Main.class.getResourceAsStream(path);
            if (tree == null) {
                tree = ImageIO.read(Main.class.getResourceAsStream("tree.png"));
            }
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PopupMenu initMenu() {
        PopupMenu popmenu = new PopupMenu();
        MenuItem open = new MenuItem();
        MenuItem exit = new MenuItem();

        open.setLabel("更换图片");
        open.setActionCommand("open");
        open.addActionListener(this);

        exit.setLabel("退出");
        exit.setActionCommand("exit");
        exit.addActionListener(this);

        popmenu.add(open);
        popmenu.add(exit);
        return popmenu;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("open")) {
            JFileChooser jfc = new JFileChooser();
            int index = jfc.showOpenDialog(null);
            File file = null;
            if (index == JFileChooser.APPROVE_OPTION) {
                file = jfc.getSelectedFile();
                initImage(file.getPath());
            }
        } else if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        }
    }
}
