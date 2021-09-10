import java.io.*;
import java.net.*;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

public class MainGUI {
    private JPanel panel;
    private JButton bPing;
    private JTextField tfAddress;
    private JTextArea taLog;

    public static void main(String[] args) throws UnknownHostException, IOException {
        FlatIntelliJLaf.setup();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainGUI mainGUI = new MainGUI();
                mainGUI.gui();
            }
        });


    }


    private void gui() {
        JFrame frame = new JFrame();
        frame.setTitle(getClass().getPackage().getName());
        frame.setContentPane(panel);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JRootPane rootPane = SwingUtilities.getRootPane(bPing);
        rootPane.setDefaultButton(bPing);

        bPing.addActionListener(e -> {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Main main
                            = new Main();
                    for (int i = 0; i < 5; i++) {
                        String ip = tfAddress.getText();
                        String line = "";
                        try {
                            line = main.sendPingRequest(ip);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                        String finalLine = line;
                        SwingUtilities.invokeLater(() -> taLog.append(finalLine));
                    }
                }
            }).start();


        });
    }


}
