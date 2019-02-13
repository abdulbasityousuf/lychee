import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings {
    private JButton updateButton;
    private JTextPane settingsStuffTextPane;
    private JPanel panel;
    private static Settings settings;

    public Settings() {

        settingsStuffTextPane.setText("Settings!");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TimeEntry.frame.setContentPane(TimeEntry.timeEntryInstance.panel);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}
