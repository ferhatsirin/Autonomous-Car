/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GUI;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ferhat
 */
public class UserFrame extends JFrame {

    private JPanel frontP;
    private GroupLayout mainLayout;

    public final static Color BACKGROUND_COLOR = new Color(104, 165, 236);
    private final MapPanel mapPanel;
    private final ControlPanel controlPanel;
    private final BluetoothPanel bluetoothPanel;

    public UserFrame(MapPanel mapPanel, ControlPanel controlPanel, BluetoothPanel bluetoothPanel) {

        this.mapPanel = mapPanel;
        this.controlPanel = controlPanel;
        this.bluetoothPanel = bluetoothPanel;

    }

    private void initBluetoothPanel() {

        bluetoothPanel.initBluetoothPanel();
    }

    private void initControlPanel() {

        controlPanel.initControlPanel();
    }

    private void initMapPanel() {

        mapPanel.initMapPanel();
    }

    private void initComponents() {

        initBluetoothPanel();
        initControlPanel();
        initMapPanel();

    }

    private void initFrame() {

        setTitle("Search Area");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 900);
        setVisible(true);

        frontP = new JPanel();
        frontP.setBackground(BACKGROUND_COLOR);
        mainLayout = new GroupLayout(frontP);
        frontP.setLayout(mainLayout);
        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);

    }

    private void initLayout() {

        mainLayout.setHorizontalGroup(mainLayout.createParallelGroup().addComponent(bluetoothPanel)
                .addGroup(mainLayout.createSequentialGroup().addComponent(mapPanel).addComponent(controlPanel)));

        mainLayout.setVerticalGroup(mainLayout.createSequentialGroup().addComponent(bluetoothPanel)
                .addGroup(mainLayout.createParallelGroup().addComponent(mapPanel).addComponent(controlPanel)));

    }

    public void initMainFrame() {
        
        initComponents();

        initFrame();

        initLayout();

        add(frontP);

        validate();
        repaint();

    }

}
