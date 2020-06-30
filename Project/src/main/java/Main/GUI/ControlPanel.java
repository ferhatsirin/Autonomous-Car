/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GUI;

import Main.CarControl.CarCommunication;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author ferhat
 */
public class ControlPanel extends JPanel {

    private GroupLayout controlLayout;
    private JLabel compassLabel;
    private Image compassImg;
    private JTextField turnTextField;
    private JLabel turnLabel;
    private JButton turnBtn;
    private JLabel forwardLabel;
    private JTextField forwardTextField;
    private JButton forwardBtn;
    private JLabel currentPosLabel;
    private JTextField currentPosText;
    private JButton temperatureBtn;
    private JLabel tempLabel;
    private JTextField tempTextField;
    private JLabel flameLabel;
    private JLabel flameImgLabel;
    private Image flameImg;
    private Image coldImg;
    private Image tempImg;
    private ImageIcon flameIcon;
    private ImageIcon coldIcon;
    private final CarCommunication carComm;

    public ControlPanel(CarCommunication carComm) {
        this.carComm = carComm;
    }

    public void initControlPanel() {

        setBackground(UserFrame.BACKGROUND_COLOR);
        controlLayout = new GroupLayout(this);
        setLayout(controlLayout);
        controlLayout.setAutoCreateGaps(true);
        controlLayout.setAutoCreateContainerGaps(true);

        compassLabel = new JLabel();

        try {
            compassImg = ImageIO.read(getClass().getClassLoader().getResource("img" + File.separator + "compass.png"));
            compassImg = compassImg.getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            compassLabel.setIcon(new ImageIcon(compassImg));
            compassLabel.setBorder(null);

        } catch (IOException ex) {
            Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        turnTextField = new JTextField();
        turnTextField.setText("0");
        turnTextField.addActionListener(new TurnBtnListener());
        turnLabel = new JLabel();
        turnLabel.setText("Degree");
        turnBtn = new JButton();
        turnBtn.setText("Turn");
        turnBtn.addActionListener(new TurnBtnListener());

        forwardLabel = new JLabel();
        forwardLabel.setText("cm");
        forwardTextField = new JTextField();
        forwardTextField.setText("0");
        forwardTextField.addActionListener(new ForwardBtnListener());

        forwardBtn = new JButton();
        forwardBtn.setText("Go Forward");
        forwardBtn.addActionListener(new ForwardBtnListener());

        currentPosLabel = new JLabel();
        currentPosLabel.setText("Current Position");
        currentPosText = new JTextField();
        currentPosText.setText("50,50,0'");
        currentPosText.setEditable(false);

        temperatureBtn = new JButton();
        temperatureBtn.setToolTipText("Temperature");
        temperatureBtn.setBackground(UserFrame.BACKGROUND_COLOR);
        temperatureBtn.setBorder(null);
        temperatureBtn.addActionListener(new TemperatureBtnListener());

        tempTextField = new JTextField();
        tempTextField.setEditable(false);
        tempTextField.setText("0.0");

        tempLabel = new JLabel();
        tempLabel.setText("°C");

        flameLabel = new JLabel();
        flameLabel.setText("Flame Sensor");
        flameImgLabel = new JLabel();
        flameImgLabel.setBorder(null);
        try {

            tempImg = ImageIO.read(getClass().getClassLoader().getResource("img" + File.separator + "temp.png"));
            tempImg = tempImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

            temperatureBtn.setIcon(new ImageIcon(tempImg));

            flameImg = ImageIO.read(getClass().getClassLoader().getResource("img" + File.separator + "flame.png"));
            flameImg = flameImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            flameIcon = new ImageIcon(flameImg);

            coldImg = ImageIO.read(getClass().getClassLoader().getResource("img" + File.separator + "no_flame.png"));
            coldImg = coldImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            coldIcon = new ImageIcon(coldImg);

            flameImgLabel.setIcon(coldIcon);

        } catch (IOException ex) {
            Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        controlLayout.setHorizontalGroup(controlLayout.createParallelGroup().addComponent(compassLabel)
                .addGroup(GroupLayout.Alignment.CENTER, controlLayout.createSequentialGroup().addComponent(turnTextField, 0, 40, 40)
                        .addComponent(turnLabel).addComponent(turnBtn)).addGroup(GroupLayout.Alignment.CENTER, controlLayout.createSequentialGroup()
                .addComponent(forwardTextField, 0, 40, 40).addComponent(forwardLabel)
                .addComponent(forwardBtn)).addGroup(GroupLayout.Alignment.CENTER, controlLayout.createSequentialGroup().addComponent(currentPosLabel)
                .addComponent(currentPosText, 0, 90, 90)).addGroup(GroupLayout.Alignment.CENTER, controlLayout.createSequentialGroup()
                .addComponent(temperatureBtn).addComponent(tempTextField, 0, 70, 70).addComponent(tempLabel, 0, 40, 40))
                .addGroup(GroupLayout.Alignment.CENTER, controlLayout.createSequentialGroup().addComponent(flameLabel, 0, 100, 100).addComponent(flameImgLabel, 0, 50, 50)));

        controlLayout.setVerticalGroup(controlLayout.createSequentialGroup().addComponent(compassLabel)
                .addGroup(controlLayout.createParallelGroup().addComponent(turnTextField, 0, 30, 30).addComponent(turnLabel, GroupLayout.Alignment.CENTER)
                        .addComponent(turnBtn, GroupLayout.Alignment.CENTER)).addContainerGap(20, 20).addGroup(controlLayout.createParallelGroup().addComponent(forwardTextField, 0, 30, 30)
                .addComponent(forwardLabel, GroupLayout.Alignment.CENTER).addComponent(forwardBtn, GroupLayout.Alignment.CENTER)).addContainerGap(20, 20)
                .addGroup(controlLayout.createParallelGroup().addComponent(currentPosLabel, GroupLayout.Alignment.CENTER)
                        .addComponent(currentPosText, 0, 30, 30)).addContainerGap(20, 20).addGroup(controlLayout.createParallelGroup()
                .addComponent(temperatureBtn, GroupLayout.Alignment.CENTER).addComponent(tempTextField, GroupLayout.Alignment.CENTER, 0, 30, 30)
                .addComponent(tempLabel, GroupLayout.Alignment.CENTER, 0, 30, 30)).addContainerGap(20, 20).addGroup(controlLayout.createParallelGroup()
                .addComponent(flameLabel, GroupLayout.Alignment.CENTER).addComponent(flameImgLabel, 0, 50, 50)));
    }

    public void setFlameIcon(boolean val) {

        if (val) {
            flameImgLabel.setIcon(flameIcon);
        } else {
            flameImgLabel.setIcon(coldIcon);

        }

        flameImgLabel.revalidate();
        flameImgLabel.repaint();
    }

    public void updateCarPositionText(String text) {

        currentPosText.setText(text);
    }

    public void setTempText(String text) {

        tempTextField.setText(text);
    }

    private class ForwardBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int unit = Integer.parseInt(forwardTextField.getText());
            if (0 < unit && unit <= 800) {
                carComm.sendForwardCommand(unit);
            } else {
                //JOptionPane.showMessageDialog(frame, "Number must be bigger than 0 !!! ", "Warning!!!", JOptionPane.WARNING_MESSAGE);
                forwardTextField.setText("0");
            }
        }
    }

    private class TurnBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int unit = Integer.parseInt(turnTextField.getText());
            if (0 <= unit ) {
                unit = unit%360;
                carComm.sendTurnCommand(unit);
            } else {
                //JOptionPane.showMessageDialog(frame, "Number must be bigger than 0 !!! ", "Warning!!!", JOptionPane.WARNING_MESSAGE);
                turnTextField.setText("0");
            }
        }
    }

    private class TemperatureBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            carComm.sendReadTemperatureCommand();
        }
    }
}
