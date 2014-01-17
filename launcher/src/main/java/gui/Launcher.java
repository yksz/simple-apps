package gui;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import logic.Processor;

import util.Loader;

public class Launcher {

    private static final String ICON_FILE_NAME   = "icon.png";
    private static final String CONFIG_FILE_NAME = "config.xml";

    private static final String TOOL_TIP = "Java Launcher";
    private static final String CAPTION = "Usage";
    private static final String TEXT = "Please right click!";

    private TrayIcon trayIcon;

    public Launcher() throws IOException {
        URL url = Loader.getResource(ICON_FILE_NAME);
        ImageIcon icon = new ImageIcon(url);
        trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setToolTip(TOOL_TIP);
        trayIcon.setPopupMenu(createPopupMenu());
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();

                // left click
                if (button == MouseEvent.BUTTON1) {
                    trayIcon.displayMessage(CAPTION, TEXT, MessageType.INFO);
                }
            }
        });
    }

    public void start() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        tray.add(trayIcon);
    }

    private PopupMenu createPopupMenu() throws IOException {
        PopupMenu popup = new PopupMenu();
        popup.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Properties
        final Properties prop = this.loadProperties(CONFIG_FILE_NAME);
        List<MenuItem> itemList = this.createMenuItems(prop);
        for (MenuItem item : itemList)
            popup.add(item);

        // Separator
        popup.addSeparator();

        // Exit
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        popup.add(item);
        return popup;
    }

    private List<MenuItem> createMenuItems(Properties prop) {
        List<MenuItem> itemList = new ArrayList<MenuItem>();
        Map<Object, Object> map = new TreeMap<Object, Object>(prop);

        for (Object obj : map.keySet()) {
            final String key = (String) obj;
            final String value = prop.getProperty((String) key);
            final String[] values = value.split(",");

            // Invalid format
            if (values.length < 2)
                continue;

            MenuItem item = new MenuItem(values[0]);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        if (values.length > 2)
                            Processor.execute(values[1], new File(values[2]));
                        else
                            Processor.execute(values[1], null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            itemList.add(item);
        }

        return itemList;
    }

    private Properties loadProperties(String filename) throws IOException {
        File file = Loader.getResourceAsFile(filename);
        Properties prop = new Properties();
        prop.loadFromXML(new FileInputStream(file));
        return prop;
    }

}
