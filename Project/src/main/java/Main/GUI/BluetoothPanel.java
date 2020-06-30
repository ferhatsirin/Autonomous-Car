/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GUI;

import Main.Bluetooth.BluetoothConnection;
import Main.Bluetooth.BluetoothDevice;
import Main.Bluetooth.BluetoothScan;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ferhat
 */
public class BluetoothPanel extends JPanel {

    private GroupLayout bluetoothLayout;
    private JButton refreshBtn;
    private Image refreshImg;
    private JComboBox<BluetoothDevice> bluetoothList;
    private JButton connectBtn;
    private JLabel connectLabel;
    private final BluetoothScan bluetooth;
    private final BluetoothConnection bluetoothCon;

    public BluetoothPanel(BluetoothConnection blueCon) {

        bluetoothCon = blueCon;
        bluetooth = new BluetoothScan();
    }

    public void initBluetoothPanel() {

        setBackground(UserFrame.BACKGROUND_COLOR);
        bluetoothLayout = new GroupLayout(this);
        setLayout(bluetoothLayout);
        bluetoothLayout.setAutoCreateGaps(true);
        bluetoothLayout.setAutoCreateContainerGaps(true);

        refreshBtn = new JButton();
        refreshBtn.setToolTipText("Refresh");
        refreshBtn.setBackground(UserFrame.BACKGROUND_COLOR);
        refreshBtn.addActionListener(new BluetoothRefreshListener());

        try {
            refreshImg = ImageIO.read(getClass().getClassLoader().getResource("img" + File.separator + "refresh.png"));
            refreshImg = refreshImg.getScaledInstance(50, 30, Image.SCALE_SMOOTH);
            refreshBtn.setIcon(new ImageIcon(refreshImg));
            refreshBtn.setBorder(null);

        } catch (IOException ex) {
            Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        bluetoothList = new JComboBox<>();

        connectBtn = new JButton();
        connectBtn.setText("Connect");
        connectBtn.addActionListener(new BluetoothConnectionListener());

        connectLabel = new JLabel();
        connectLabel.setText("Disconnected");

        bluetoothLayout.setHorizontalGroup(bluetoothLayout.createSequentialGroup().addComponent(refreshBtn)
                .addComponent(bluetoothList, 0, 250, 250).addComponent(connectBtn).addComponent(connectLabel));

        bluetoothLayout.setVerticalGroup(bluetoothLayout.createParallelGroup().addComponent(refreshBtn)
                .addComponent(bluetoothList, 0, 30, 30).addComponent(connectBtn, GroupLayout.Alignment.CENTER)
                .addComponent(connectLabel, GroupLayout.Alignment.CENTER));

    }

    public void setConnectionText(String text) {
        connectLabel.setText(text);
        if (!bluetoothCon.isConnected()) {
            connectBtn.setText("Connect");
        }else{
            connectBtn.setText("Disconnect");
        }
    }

    private class BluetoothRefreshListener implements ActionListener {

        private ExecutorService executor;
        private Future<?> submit;

        public BluetoothRefreshListener() {
            executor = Executors.newSingleThreadExecutor();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (submit == null || submit.isDone()) {
                submit = executor.submit(() -> {
                    String last = connectLabel.getText();
                    connectLabel.setText("Searching...");
                    bluetooth.scanDevices();
                    bluetoothList.removeAllItems();
                    for (BluetoothDevice d : bluetooth.getDevices()) {
                        bluetoothList.addItem(d);
                    }
                    if (bluetoothCon.isConnected()) {
                        bluetoothList.addItem(bluetoothCon.getDevice());
                        bluetoothList.setSelectedItem(bluetoothCon.getDevice());
                    }
                    bluetoothList.revalidate();
                    bluetoothList.repaint();
                    connectLabel.setText(last);
                });
            }
        }
    }

    private class BluetoothConnectionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (!bluetoothCon.isConnected()) {
                BluetoothDevice device = (BluetoothDevice) bluetoothList.getSelectedItem();
                if (device != null) {
                    Boolean result = bluetoothCon.connectDevice(device);
                    if (result) {
                        setConnectionText("Connected: " + device.getName());
                    }
                }
            } else {
                bluetoothCon.closeConnection();
            }
        }
    }

}
