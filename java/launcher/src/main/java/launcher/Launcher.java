package launcher;

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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.ImageIcon;

public class Launcher {

    private static final String ICON_FILE = "icon.png";
    private static final String CONFIG_FILE = "config.xml";

    private static final String TOOLTIP = "Java Launcher";
    private static final String CAPTION = "Usage";
    private static final String TEXT = "Please right click!";

    private final TrayIcon trayIcon;

    public Launcher() throws IOException {
        URL url = Loader.getResource(ICON_FILE);
        ImageIcon icon = new ImageIcon(url);
        trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setToolTip(TOOLTIP);
        trayIcon.setPopupMenu(createPopupMenu());
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) // left click
                    trayIcon.displayMessage(CAPTION, TEXT, MessageType.INFO);
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
        Properties props = loadProperties(CONFIG_FILE);
        List<MenuItem> items = createMenuItems(props);
        for (MenuItem item : items)
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
        List<MenuItem> items = new ArrayList<MenuItem>();
        Map<Object, Object> map = new TreeMap<Object, Object>(prop);
        for (Object obj : map.keySet()) {
            String key = (String) obj;
            String value = prop.getProperty((String) key);
            String[] values = value.split(",");
            if (values.length < 2) // invalid format
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
            items.add(item);
        }
        return items;
    }

    private Properties loadProperties(String filename) throws IOException {
        Properties props = new Properties();
        props.loadFromXML(Loader.getResourceAsStream(filename));
        return props;
    }

}
