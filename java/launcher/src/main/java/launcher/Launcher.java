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

    private static final String CONFIG_FILE = "config.xml";
    private static final String ICON_FILE = "icon.png";
    private static final String TOOL_TIP = "Java Launcher";
    private static final String CAPTION = "Usage";
    private static final String TEXT = "Please right click!";
    private static final Font POPUP_MENU_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private final TrayIcon trayIcon;
    private final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public Launcher() throws IOException {
        URL url = loader.getResource(ICON_FILE);
        ImageIcon icon = new ImageIcon(url);
        trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setToolTip(TOOL_TIP);
        trayIcon.setPopupMenu(createPopupMenu());
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) // left click
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
        popup.setFont(POPUP_MENU_FONT);

        // Properties
        Properties props = loadConfig(CONFIG_FILE);
        List<MenuItem> items = createMenuItems(props);
        for (MenuItem item : items)
            popup.add(item);

        // Separator
        popup.addSeparator();

        // Exit
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        popup.add(item);

        return popup;
    }

    private List<MenuItem> createMenuItems(Properties props) {
        List<MenuItem> items = new ArrayList<>();
        Map<Object, Object> map = new TreeMap<>(props);
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            String value = (String) entry.getValue();
            String[] values = value.split(",");
            if (values.length < 2) // invalid format
                continue;
            File dir = values.length > 2 ? new File(values[2]) : null;
            items.add(createMenuItem(values[0], values[1], dir));
        }
        return items;
    }

    private MenuItem createMenuItem(String label, final String command, final File directory) {
        MenuItem item = new MenuItem(label);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Processor.execute(command, directory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return item;
    }

    private Properties loadConfig(String filename) throws IOException {
        Properties props = new Properties();
        props.loadFromXML(loader.getResourceAsStream(filename));
        return props;
    }

}
