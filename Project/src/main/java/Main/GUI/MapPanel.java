/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GUI;

import Main.CarControl.CarCommunication;
import Main.CarControl.CarControl;
import Main.CarControl.CarMap;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author ferhat
 */
public class MapPanel extends JPanel {

    private GroupLayout mapLayout;
    private MapControlPanel mapPanelControl;
    private JScrollPane scrollPane;
    private JLabel mapSizeLabel;
    private JButton mapBtn;
    private JTextField mapSizeText;
    private JButton showGridBtn;
    private JLabel targetLabel;
    private JTextField targetLabelPoint;
    private JButton goTargetBtn;
    private JButton pathBtn;
    private JButton showPathBtn;
    private JButton stopBtn;
    private JButton saveMapBtn;
    private final UserControl userControl;
    private final CarControl carControl;
    private final CarCommunication carComm;

    public MapPanel(UserControl userControl,CarControl carControl,CarCommunication carComm) {
        this.userControl = userControl;
        this.carControl =carControl;
        this.carComm =carComm;
    }

    public void initMapPanel() {

        setBackground(UserFrame.BACKGROUND_COLOR);
        mapLayout = new GroupLayout(this);
        setLayout(mapLayout);
        mapLayout.setAutoCreateGaps(true);
        mapLayout.setAutoCreateContainerGaps(true);

        mapPanelControl =new MapControlPanel(this,carControl);
        mapPanelControl.setBackground(Color.WHITE);
        mapPanelControl.setMapSize(100);

        scrollPane = new JScrollPane(mapPanelControl);
        scrollPane.setBackground(Color.WHITE);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mapSizeLabel = new JLabel();
        mapSizeLabel.setText("Map Size (m²)");

        mapSizeText = new JTextField();
        mapSizeText.addActionListener(new MapSizeListener());
        mapSizeText.setText("1");

        mapBtn = new JButton();
        mapBtn.setText("Set Map");
        mapBtn.addActionListener(new MapSizeListener());
        mapBtn.doClick();

        showGridBtn = new JButton();
        showGridBtn.setText("Hide Grid");
        showGridBtn.addActionListener(new GridBtnListener());

        targetLabel = new JLabel();
        targetLabel.setText("Target Point (x,y)");

        targetLabelPoint = new JTextField();
        targetLabelPoint.setText("0,0");
        targetLabelPoint.setEditable(false);

        pathBtn = new JButton();
        pathBtn.setText("Find a path");
        pathBtn.addActionListener(new PathBtnListener());

        showPathBtn = new JButton();
        showPathBtn.setText("Hide Path");
        showPathBtn.addActionListener(new ShowPathBtnListener());

        goTargetBtn = new JButton();
        goTargetBtn.setText("Go");
        goTargetBtn.addActionListener(new GoBtnListener());

        stopBtn = new JButton();
        stopBtn.setText("Stop Mission");
        stopBtn.addActionListener(new StopBtnListener());

        saveMapBtn = new JButton();
        saveMapBtn.setText("Save Map");
        saveMapBtn.addActionListener(new SaveMapBtnListener());

        mapLayout.setHorizontalGroup(mapLayout.createParallelGroup().addComponent(scrollPane)
                .addGroup(GroupLayout.Alignment.CENTER, mapLayout.createSequentialGroup()
                        .addComponent(mapSizeLabel).addComponent(true, mapSizeText, 0, 30, 30)
                        .addComponent(mapBtn).addComponent(showGridBtn).addContainerGap(30, 30).addComponent(targetLabel)
                        .addComponent(targetLabelPoint, 0, 70, 70).addComponent(pathBtn).addComponent(showPathBtn)
                        .addComponent(goTargetBtn).addComponent(stopBtn).addComponent(saveMapBtn)));

        mapLayout.setVerticalGroup(mapLayout.createSequentialGroup().addComponent(scrollPane).addGap(20)
                .addGroup(mapLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(mapSizeLabel).addComponent(mapSizeText, 0, 30, 30)
                        .addComponent(mapBtn).addComponent(showGridBtn).addComponent(targetLabel)
                        .addComponent(targetLabelPoint, 0, 30, 30).addComponent(pathBtn).addComponent(showPathBtn)
                        .addComponent(goTargetBtn).addComponent(stopBtn).addComponent(saveMapBtn)));

    }

    public void updateCarPanelPosition() {

        mapPanelControl.setCarTranslation();
    }

    public void setTargetPosition(String text) {

        targetLabelPoint.setText(text);
    }

    private class MapSizeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int size = Integer.parseInt(mapSizeText.getText());

            if (1 <= size) {
                size *= 100;
                mapPanelControl.setMapSize(size);
                userControl.updateCarPositionText();
            } else {
                JOptionPane.showMessageDialog(userControl.getMainFrame(), "Map size must be bigger than 0 !!! ", "Warning!!!", JOptionPane.WARNING_MESSAGE);
                mapSizeText.setText("1");
            }
        }
    }

    private class GridBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mapPanelControl.isGridVisible()) {
                mapPanelControl.setGridVisible(false);
                showGridBtn.setText("Show Grid");
            } else {
                mapPanelControl.setGridVisible(true);
                showGridBtn.setText("Hide Grid");
            }
        }
    }

    private class ShowPathBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mapPanelControl.isPathVisible()) {
                mapPanelControl.setPathVisible(false);
                showPathBtn.setText("Show Path");
            } else {

                mapPanelControl.setPathVisible(true);
                showPathBtn.setText("Hide Path");
            }
        }
    }

    private class PathBtnListener implements ActionListener {

        private ExecutorService executor;
        private Future<?> submit;

        public PathBtnListener() {

            executor = Executors.newSingleThreadExecutor();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (submit == null || submit.isDone()) {
                submit = executor.submit(() -> {
                    CarMap carMap=carControl.getCarMap();
                    carMap.shortestPath(carControl.getCar().getCarArrayPosition(), mapPanelControl.getTargetArrayPosition());
                });
                repaint();
            }
        }
    }

    private class GoBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            carControl.getDriveControl().goTarget();
        }

    }

    private class StopBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            carComm.sendStopMissionCommand();
            carControl.getDriveControl().setStopMission(true);
        }
    }

    private class SaveMapBtnListener implements ActionListener {

        private final FileDialog dialog;

        public SaveMapBtnListener() {

            dialog = new FileDialog(userControl.getMainFrame(), "Select Directory to Save", FileDialog.SAVE);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            dialog.setVisible(true);
            File[] files = dialog.getFiles();

            if (0 < files.length) {
                BufferedImage img = new BufferedImage(mapPanelControl.getWidth(), mapPanelControl.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D cg = img.createGraphics();
                mapPanelControl.paintAll(cg);

                try {
                    ImageIO.write(img, "png", new File(files[0].getAbsolutePath().concat(".png")));
                } catch (IOException ex) {
                    Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
