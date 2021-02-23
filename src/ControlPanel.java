import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import org.jxmapviewer.*;
import org.jxmapviewer.google.GoogleMapsTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.bmng.CylindricalProjectionTileFactory;
import org.jxmapviewer.viewer.esri.ESRITileFactory;


public class ControlPanel {
    public static void main(String [] arg)
    {
        //LoginWindows mloginWindows = new LoginWindows(0,0,500,500,"無人機(船)控制器");
        mainWindows m = new mainWindows(0,0,1920,1080,"無人機(船)控制中心");
        //Sample6 s = new Sample6();
    }
}

class LoginWindows extends JFrame{
    // 建立物件
    JLabel userText,PasswdText,Logo1Buffer,Logo2Buffer,Title;
    JButton loginBtn;
    ImageIcon Logo1,Logo2;
    JTextField user;
    JPasswordField passwd;
    JPanel G1;

    LoginWindows(int x , int y , int w , int h , String name)
    {
        // 建立視窗
        this.setBounds(x,y,w,h);
        this.setTitle(name);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 建立登入按鈕
        loginBtn = new JButton("登入");
        loginBtn.setBounds(180,350,120,80);
        loginBtn.setFont(new Font("標楷體",Font.PLAIN,24));
        this.add(loginBtn);

        // 建立文字 : 帳號
        userText = new JLabel("請輸入帳號:");
        userText.setBounds(20,180,200,80);
        userText.setFont(new Font("標楷體",Font.PLAIN,24));
        this.add(userText);

        // 建立文字 : 密碼
        PasswdText = new JLabel("請輸入密碼:");
        PasswdText.setBounds(20,250,200,80);
        PasswdText.setFont(new Font("標楷體",Font.PLAIN,24));
        this.add(PasswdText);

        // 建立輸入框 : 帳號
        user = new JTextField("請輸入帳號");
        user.setBounds(160,210,300,30);
        user.setFont(new Font("標楷體",Font.PLAIN,21));
        this.add(user);

        // 建立輸入框 : 密碼
        passwd = new JPasswordField();
        passwd.setBounds(160,280,300,30);
        passwd.setFont(new Font("標楷體",Font.PLAIN,21));
        this.add(passwd);

        // 建立組別
        G1 = new JPanel();
        G1.setBounds(100,30,300,160);
        G1.setBorder(BorderFactory.createEtchedBorder());
        G1.setLayout(null);
        this.add(G1);

        // 建立圖片1
        Logo1 = new ImageIcon("image/boat.png");
        Logo1 = new ImageIcon(Logo1.getImage().getScaledInstance(160,160,Image.SCALE_SMOOTH));
        Logo1Buffer = new JLabel(Logo1);
        Logo1Buffer.setBounds(0,0,160,160);
        G1.add(Logo1Buffer);

        // 建立圖片2
        Logo2 = new ImageIcon("image/quadcopter.png");
        Logo2 = new ImageIcon(Logo2.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH));
        Logo2Buffer = new JLabel(Logo2);
        Logo2Buffer.setBounds(160,10,160,160);
        G1.add(Logo2Buffer);

        // 建立標題
        Title = new JLabel("無人機(船)控制程式");
        Title.setBounds(30,-10,300,80);
        Title.setFont(new Font("標楷體",Font.BOLD,24));
        G1.add(Title);

        // 建立視窗
        this.setVisible(true);

        // 啟動功能
        init();
    }

    public void init()
    {
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println();
                if(user.getText().equals("10957022") && passwd.getText().equals("h125087083"))
                {
                    JOptionPane.showMessageDialog(null,"登入成功");
                    mainWindows m = new mainWindows(0,0,1920,1080,"無人機(船)控制中心");
                    close();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"登入失敗");
                }

            }
        });
    }

    private void close()
    {
        this.dispose();
    }
}


class mainWindows extends JFrame{
    JPanel G1,G2,G3,G4;
    JButton ConnectBtn,disConnectBtn;
    JTextField addressField;
    JLabel StatusText;
    JRadioButton wifiBtn,loraBtn;
    ButtonGroup BG1;
    JScrollPane tablePane;
    JTable table;
    String [][] TableData = {{"無人機1號","目前在線","可遠端操控"},{"無人船1號","目前不在線","不可遠端操控"}};
    String [] TableTittle = {"裝置名稱","在線狀態","可控狀態"};
    JXMapKit mapKit;
    TileFactoryInfo info;
    DefaultTileFactory tileFactory;
    GeoPosition frankfurt;


    mainWindows(int x , int y , int w , int h , String name)
    {
        // 建立視窗
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle(name);
        this.setBounds(x,y,w,h);

        // 建立按鈕組
        G1 = new JPanel();
        G1.setBorder(BorderFactory.createTitledBorder("連線控制區"));
        G1.setBounds(10,10,500,220);
        G1.setLayout(null);
        this.add(G1);

        // 建立按紐 : 連線
        ConnectBtn = new JButton("建立連線");
        ConnectBtn.setBounds(20,30,150,50);
        ConnectBtn.setFont(new Font("標楷體",Font.PLAIN,24));
        G1.add(ConnectBtn);

        // 建立按紐 : 中斷
        disConnectBtn = new JButton("中斷連線");
        disConnectBtn.setBounds(180,30,150,50);
        disConnectBtn.setFont(new Font("標楷體",Font.PLAIN,24));
        G1.add(disConnectBtn);

        // 建立位置輸入框
        addressField = new JTextField("請輸入位置(IP or MAC)");
        addressField.setFont(new Font("標楷體",Font.PLAIN,21));
        addressField.setBounds(20,90,310,30);
        G1.add(addressField);

        // 建立連線狀態組
        G2 = new JPanel();
        G2.setBorder(BorderFactory.createTitledBorder("連線狀態"));
        G2.setBounds(350,25,120,180);
        G2.setLayout(null);
        G1.add(G2);

        // 狀態文字
        StatusText = new JLabel("暫無狀態");
        StatusText.setBounds(20,60,90,50);
        StatusText.setFont(new Font("標楷體",Font.PLAIN,21));
        G2.add(StatusText);

        // 建立按紐組
        BG1 = new ButtonGroup();
        wifiBtn = new JRadioButton("WiFi");
        wifiBtn.setBounds(20,120,120,80);
        wifiBtn.setFont(new Font("標楷體",Font.PLAIN,24));
        BG1.add(wifiBtn);
        loraBtn = new JRadioButton("LoRa");
        loraBtn.setFont(new Font("標楷體",Font.PLAIN,24));
        loraBtn.setBounds(180,120,120,80);
        BG1.add(loraBtn);
        G1.add(wifiBtn);
        G1.add(loraBtn);

        // 建立裝置清單組
        G3 = new JPanel();
        G3.setBorder(BorderFactory.createTitledBorder("在線裝置"));
        G3.setBounds(10,240,500,350);
        G3.setLayout(null);
        this.add(G3);

        // 建立表格
        table = new JTable(TableData,TableTittle);
        table.setBounds(50,50,450,310);
        table.setVisible(true);
        table.setFont(new Font("標楷體",Font.PLAIN,21));


        // 建立滾動容器
        tablePane = new JScrollPane(table);
        tablePane.setBounds(20,20,460,310);
        G3.add(tablePane);

        // 建立地圖組
        G4 = new JPanel();
        G4.setBorder(BorderFactory.createTitledBorder("地圖顯示區"));
        G4.setBounds(520,10,1365,975);
        G4.setLayout(null);
        this.add(G4);

        // 建立地圖
        mapKit = new JXMapKit();
        info = new GoogleMapsTileFactoryInfo("AIzaSyBqaQDaRrftRMm7ZofnBwtuO1FwzNvHNiI");
        tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);
        mapKit.setTileFactory(tileFactory);
        frankfurt = new GeoPosition(24.68, 121.08);
        mapKit.setZoom(17);
        mapKit.setAddressLocation(frankfurt);
        mapKit.setBounds(20,30,1325,925);
        G4.add(mapKit);

        // 建立監聽事件
        init();

        // 顯示視窗
        this.setVisible(true);
    }

    public void init()
    {
        // 建立連線按鈕事件
        ConnectBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}



// 建立自己的文字物件
class mLabel extends JLabel
{
    mLabel(int x , int y , int w , int h ,int FontSize, String str)
    {
        this.setText(str);
        this.setBounds(x,y,w,h);
        this.setFont(new Font("標楷體",Font.PLAIN,FontSize));
    }

    public void changeFont(int Size , String style , Font f)
    {

        this.setFont(new Font(style,f.getStyle(),Size));
    }
}

// 建立自己的按鈕物件
class mButton extends JButton
{
    mButton(int x , int y , int w,int h,String str ,int TextSize)
    {
        this.setText(str);
        this.setBounds(x,y,w,h);
        this.setFont(new Font("標楷體",Font.PLAIN,TextSize));
    }
}

