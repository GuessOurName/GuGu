package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldPort;
    private JButton btnStart;
    private JTextField txtShowMsg;
    private JTextField txtOnlineMsg;
    private JScrollPane scrollPane;
    private JTextArea textAreaShowMsg;
    private JScrollPane scrollPane_1;
    private JTextArea textAreaOnlineMsg;

    private boolean isStart = false;
    private OnStartServersListener listener;
    private static final MainWindow mainWindow = new MainWindow();
    private List<String> onlineUsernameList = new ArrayList<>();




    public interface OnStartServersListener {
        void start();
        void stop();
    }

    public String getPort(){
        return textFieldPort.getText();
    }

    public void setOnStartServersListener (OnStartServersListener listener) {
        this.listener = listener;
    }

    public void setShowMsg(String str) {
        textAreaShowMsg.append(str + "\n");
    }

    public void addOnlineUsers(String str) {
        onlineUsernameList.add(str);
        showOnlineUsers();
    }

    public void removeOfflineUsers(String str) {
        if (onlineUsernameList.contains(str)) {
            onlineUsernameList.remove(str);
        }
        showOnlineUsers();
    }

    private void showOnlineUsers() {
        textAreaOnlineMsg.setText("");
        for (String str : onlineUsernameList) {
            textAreaOnlineMsg.append(str + "\n");
        }
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setForeground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        textFieldPort = new JTextField();
        textFieldPort.setBackground(Color.GRAY);
        textFieldPort.setForeground(Color.BLACK);
        textFieldPort.setText("27777");
        textFieldPort.setColumns(10);

        btnStart = new JButton("Start");
        btnStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (isStart) {
                    listener.stop();
                    btnStart.setText("Start");
                    setShowMsg("Server stoped...");
                    isStart = false;
                } else {
                    listener.start();
                    btnStart.setText("Stop");
                    setShowMsg("Server started...");
                    isStart = true;
                }
            }
        });
        btnStart.setForeground(Color.BLACK);
        btnStart.setBackground(Color.BLUE);

        txtShowMsg = new JTextField();
        txtShowMsg.setBackground(Color.LIGHT_GRAY);
        txtShowMsg.setForeground(Color.BLACK);
        txtShowMsg.setEditable(false);
        txtShowMsg.setText("Show Message");
        txtShowMsg.setColumns(10);

        txtOnlineMsg = new JTextField();
        txtOnlineMsg.setBackground(Color.LIGHT_GRAY);
        txtOnlineMsg.setEditable(false);
        txtOnlineMsg.setForeground(Color.BLACK);
        txtOnlineMsg.setText("Online Users");
        txtOnlineMsg.setColumns(10);

        scrollPane = new JScrollPane();

        scrollPane_1 = new JScrollPane();
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(txtShowMsg, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                        .addComponent(textFieldPort, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(txtOnlineMsg, GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnStart, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                        .addComponent(scrollPane_1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                                .addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(2)
                                                .addComponent(textFieldPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btnStart))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtShowMsg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtOnlineMsg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                                .addContainerGap())
        );

        textAreaOnlineMsg = new JTextArea();
        textAreaOnlineMsg.setBackground(Color.GRAY);
        textAreaOnlineMsg.setForeground(Color.BLACK);
        scrollPane_1.setViewportView(textAreaOnlineMsg);

        textAreaShowMsg = new JTextArea();
        textAreaShowMsg.setBackground(Color.GRAY);
        textAreaShowMsg.setForeground(Color.BLACK);
        scrollPane.setViewportView(textAreaShowMsg);
        contentPane.setLayout(gl_contentPane);
    }
}