import net.sf.jcarrierpigeon.WindowPosition;
import net.sf.jtelegraph.Telegraph;
import net.sf.jtelegraph.TelegraphQueue;
import net.sf.jtelegraph.TelegraphType;
import org.jdesktop.swingx.JXDatePicker;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by Abdul Basit on 4/13/14.
 */
public class TimeEntry implements NativeKeyListener {
    private JComboBox comboBox1;
    private JTextField textField1;
    private JButton Submit;
    private JXDatePicker datePicker;
    public JPanel panel;
    private JTextField textField2;
    public static JFrame frame;
    private TelegraphQueue telegraphQueue;

    private javax.swing.JMenuBar jMenuBar;

    public static TimeEntry timeEntryInstance;

    public TimeEntry() throws NativeHookException {

        GlobalScreen.registerNativeHook();
        GlobalScreen.getInstance().addNativeKeyListener(this);

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }

        Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Task: " + comboBox1.getModel().getSelectedItem());
                System.out.println("Description: " + textField1.getText());
                System.out.println("Date: " + datePicker.getDate());
                System.out.println("Hours: " + textField2.getText());
                System.out.println("User: " + System.getProperty("user.name"));
                System.out.println("");

                if (datePicker.getDate().after(new Date())) {
                    panel.setBackground(Color.RED);
                } else {
                    panel.setBackground(Color.GREEN);
                }

            }
        });

        new Timer((int) (1000 * 60 * 2), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Telegraph tele = new Telegraph("Lychee Alert", "Please enter time", TelegraphType.NOTIFICATION_INFO, WindowPosition.BOTTOMRIGHT, 5000);
                telegraphQueue = new TelegraphQueue();
                telegraphQueue.add(tele);
            }
        }).start();

        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("lychee.png"), "Lychee");
        Image image = imageIcon.getImage();
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        frame.setIconImage(new ImageIcon(this.getClass().getResource("lychee.png")).getImage());
        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWidget();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        createMenuBar();

    }

    private void createMenuBar() {
        jMenuBar = new javax.swing.JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(Settings.getInstance().getPanel());
            }
        });
        fileMenu.add(settings);
        jMenuBar.add(fileMenu);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            public void run() {
                frame = new JFrame("TimeEntry");
                try {
                    TimeEntry timeEntry = new TimeEntry();
                    frame.setContentPane(timeEntry.panel);
                    frame.setJMenuBar(timeEntry.jMenuBar);
                    TimeEntry.timeEntryInstance = timeEntry;
                } catch (NativeHookException e) {
                    e.printStackTrace();
                }
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);



            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        //System.out.println(nativeKeyEvent.paramString());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyTyped(final NativeKeyEvent nativeKeyEvent) {
        Runnable runnable = new Runnable() {
            public void run() {
                //System.out.println(nativeKeyEvent.paramString());
                if (nativeKeyEvent.getKeyChar() == ';' && nativeKeyEvent.getModifiers() == NativeInputEvent.CTRL_MASK + NativeInputEvent.ALT_MASK) {
                    showWidget();
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    public void showWidget() {
        textField1.setText(null);
        textField2.setText(null);
        datePicker.setDate(new Date());
        panel.setBackground(Color.white);

        frame.setVisible(true);
        int state = frame.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        frame.setExtendedState(state);
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(false);

    }

}
