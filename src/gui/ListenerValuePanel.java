package gui;

import de.yadrone.base.navdata.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nymann on 16-03-2016.
 */
public class ListenerValuePanel extends JPanel implements AttitudeListener,
        AcceleroListener {
    JTextArea jTextArea = new JTextArea();
    JWindow jWindow = new JWindow();
    JLayeredPane jLayeredPane = new JLayeredPane();

    public ListenerValuePanel(NavDataManager nm){
        nm.addAttitudeListener(this);
        nm.addAcceleroListener(this);
    }

    public void ListenerValueGUI(int width, int height) {
        JPanel jPanel = new JPanel();

        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth();
        jWindow.setLayout(new BorderLayout());
        jWindow.setAlwaysOnTop(true);
        jWindow.setBackground(new Color(0, 0, 0, 0));
        jWindow.setPreferredSize(new Dimension(width, height));
        jWindow.setLocation(screenWidth - width, 10);
        jWindow.add(jLayeredPane, BorderLayout.CENTER);

        jLayeredPane.setBounds(0, 0, width, height);
        jPanel.setBounds(0, 0, width, height);
        jPanel.setBackground(Color.black);
        jPanel.setOpaque(true);

        Font font = new Font("Verdana", Font.BOLD, 34);
        jTextArea.setBounds(0, 0, width, height);
        jTextArea.setBackground(new Color(0, 0, 0, 0));
        jTextArea.setForeground(Color.white);
        jTextArea.setFont(font);
        jTextArea.setText("HEY");
        jTextArea.setEditable(false);

        jLayeredPane.add(jPanel, 0, 0);
        jLayeredPane.add(jTextArea, 1, 0);
        jWindow.pack();
        jWindow.setVisible(true);
    }

    public void appendText(String text) {
        SwingUtilities.invokeLater(() -> jTextArea.setText(text));
        jWindow.repaint();
    }


    @Override
    public void attitudeUpdated(float pitch, float roll, float yaw) {
        appendText("A-Pitch: " + pitch + ". A-Roll: " + roll + ". A-Yaw: " +
                yaw + ".");
    }

    @Override
    public void attitudeUpdated(float pitch, float roll) {
        appendText("A-Pitch: " + pitch + ". A-Roll: " + roll + ".");
    }

    @Override
    public void windCompensation(float pitch, float roll) {
        appendText("W-Pitch: " + pitch + ". W-Roll: " + roll + ".");
    }

    @Override
    public void receivedRawData(AcceleroRawData acceleroRawData) {
        System.out.println("acceleroRawData updated.");
    }

    @Override
    public void receivedPhysData(AcceleroPhysData acceleroPhysData) {
        System.out.println("acceleroPhysData updated.");
    }
}
