import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.jxmapviewer.*;
import org.jxmapviewer.google.GoogleMapsTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;


// 程式進入點
public class ControlPanel {
    public static void main(String [] arg)
    {
        //LoginWindows mloginWindows = new LoginWindows(0,0,500,500,"無人機(船)控制器");
        new StatusWindows(0,0,1920,1080,"測試");
    }
}

// 建立登入視窗
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
                    StatusWindows s = new StatusWindows(0,0,1920,1080,"無人機(船)控制中心");
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

// 建立主介面
class mainWindows extends JFrame{
    JPanel G1,G2,G3,G4,G5,G6;
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
    mButton ImageBtn,TimerBtn,OutputBtn,WBtn,SBtn,DBtn,ABtn,UpBtn,DownBtn,RightBtn,LeftBtn;


    mainWindows(int x , int y , int w , int h , String name)
    {
        // 建立視窗
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle(name);
        this.setBounds(x,y,w,h);
        this.setFocusable(true);

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
        G3.setBounds(10,230,500,350);
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

        // 建立指示組別
        G5 = new JPanel();
        G5.setLayout(null);
        G5.setBorder(BorderFactory.createTitledBorder("控制指示"));
        G5.setBounds(10,580,500,280);
        this.add(G5);

        // 建立控制向組
        WBtn = new mButton(100,80,50,50,"W",24);
        ABtn = new mButton(40,150,50,50,"A",24);
        SBtn = new mButton(100,150,50,50,"S",24);
        DBtn = new mButton(160,150,50,50,"D",24);

        UpBtn = new mButton(330,80,60,50,"UP",16);
        DownBtn = new mButton(330,150,60,50,"DOWN",12);
        RightBtn = new mButton(400,150,60,50,"RIGHT",10);
        LeftBtn = new mButton(260,150,60,50,"LEFT",12);

        G5.add(WBtn);   G5.add(UpBtn);
        G5.add(ABtn);   G5.add(DownBtn);
        G5.add(SBtn);   G5.add(RightBtn);
        G5.add(DBtn);   G5.add(LeftBtn);

        // 建立選項按鈕組別
        G6 = new JPanel();
        G6.setLayout(null);
        G6.setBorder(BorderFactory.createTitledBorder("多元選項區"));
        G6.setBounds(10,860,500,125);
        this.add(G6);

        // 建立影像擷取按鈕
        ImageBtn = new mButton(20,40,140,60,"影像擷取",21);
        G6.add(ImageBtn);

        // 建立計時器按鈕
        TimerBtn = new mButton(180,40,140,60,"計時器",21);
        G6.add(TimerBtn);

        // 建立資料視覺化按鈕
        OutputBtn = new mButton(340,40,140,60,"資料視覺化",18);
        G6.add(OutputBtn);

        // 建立監聽事件
        init();

        // 顯示視窗
        this.setVisible(true);
    }

    public void init()
    {
        // 建立連線按鈕事件
        ConnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"連線中");
                StatusText.setText("完成連線");
            }
        });

        // 鍵盤監聽事件
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyChar() == 'w')
                {
                    WBtn.setBackground(Color.pink);
                }
                if(e.getKeyChar() == 'a')
                {
                    ABtn.setBackground(Color.pink);
                }
                if(e.getKeyChar() == 's')
                {
                    SBtn.setBackground(Color.pink);
                }
                if(e.getKeyChar() == 'd')
                {
                    DBtn.setBackground(Color.pink);
                }


                if(e.getKeyCode() == 37)
                {
                    LeftBtn.setBackground(Color.pink);
                }
                if(e.getKeyCode() == 38)
                {
                    UpBtn.setBackground(Color.pink);
                }
                if(e.getKeyCode() == 39)
                {
                    RightBtn.setBackground(Color.pink);
                }
                if(e.getKeyCode() == 40)
                {
                    DownBtn.setBackground(Color.pink);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyChar() == 'w')
                {
                    WBtn.setBackground(null);
                }
                if(e.getKeyChar() == 'a')
                {
                    ABtn.setBackground(null);
                }
                if(e.getKeyChar() == 's')
                {
                    SBtn.setBackground(null);
                }
                if(e.getKeyChar() == 'd')
                {
                    DBtn.setBackground(null);
                }


                if(e.getKeyCode() == 37)
                {
                    LeftBtn.setBackground(null);
                }
                if(e.getKeyCode() == 38)
                {
                    UpBtn.setBackground(null);
                }
                if(e.getKeyCode() == 39)
                {
                    RightBtn.setBackground(null);
                }
                if(e.getKeyCode() == 40)
                {
                    DownBtn.setBackground(null);
                }
            }
        });
    }
}

// 建立資訊界面
class StatusWindows extends JFrame
{
    // 全域變數宣告
    mGroup G1,G2,G3,G4,G5,G6 ;
    mLabel Status,Lat,Lon,Hight,Speed,Acc_X,Acc_Y,Acc_Z,Direction,Pressure, Environmental_status,CPU_load,CPU_temperature,Body_temperature,LoraOrWifiSign,Battery,GPSSign;
    StatusWindows(int x , int y , int w , int h , String name)
    {
        // 建立視窗
        this.setBounds(x,y,w,h);
        this.setTitle(name);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char key = e.getKeyChar();
                if(key == 'w')
                {

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });

        // 建立組別 : 機體資訊
        G1 = new mGroup(10,20 ,350,960,"機體資訊");
        this.add(G1);
        int X = 30 ;
        int Y = 40 ;
        int W = 320 ;
        int H = 30 ;
        int FontSize = 21 ;
        // 建立資訊
        Status = new mLabel(X,Y+Y*0,W,H,FontSize,"現在裝置     無人機 ");
        Lat = new mLabel(X,Y+Y*1,W,H,FontSize,"經度         0.000");
        Lon = new mLabel(X,Y+Y*2,W,H,FontSize,"緯度         0.000");
        Hight =  new mLabel(X,Y+Y*3,W,H,FontSize,"高度         0 m");
        Speed = new mLabel(X,Y+Y*4,W,H,FontSize,"速度         0 m/s");
        Acc_X = new mLabel(X,Y+Y*5,W,H,FontSize,"X速度        0");
        Acc_Y = new mLabel(X,Y+Y*6,W,H,FontSize,"Y速度        0");
        Acc_Z = new mLabel(X,Y+Y*7,W,H,FontSize,"Z速度        0");
        Direction = new mLabel(X,Y+Y*8,W,H,FontSize,"方向         0 °");
        Pressure = new mLabel(X,Y+Y*9,W,H,FontSize,"壓力         0 bps");
        Environmental_status = new mLabel(X,Y+Y*10,W,H,FontSize,"環境         - ");
        CPU_load = new mLabel(X,Y+Y*11,W,H,FontSize,"CPU          0 % ");
        CPU_temperature = new mLabel(X,Y+Y*12,W,H,FontSize,"CPU溫度      0 ℃ ");
        Body_temperature = new mLabel(X,Y+Y*13,W,H,FontSize,"機體溫度     0 ℃ ");
        LoraOrWifiSign = new mLabel(X,Y+Y*14,W,H,FontSize,"信號源強度   - ");
        GPSSign = new mLabel(X,Y+Y*15,W,H,FontSize,"GPS強度      - ");
        Battery = new mLabel(X,Y+Y*16,W,H,FontSize,"電池電量     0 % ");

        G1.add(Status); G1.add(Lat); G1.add(Lon); G1.add(Hight); G1.add(Speed); G1.add(Acc_X); G1.add(Acc_Y); G1.add(Acc_Z); G1.add(Direction);
        G1.add(Pressure); G1.add(Environmental_status); G1.add(CPU_load); G1.add(CPU_temperature); G1.add(Body_temperature); G1.add(LoraOrWifiSign);
        G1.add(GPSSign); G1.add(Battery);

        // 建立組別 : 機體姿態
        G2 = new mGroup(370,10,450,300,"機體姿態<繪圖>");
        this.add(G2);

        // 建立繪圖容器
        G2.add(new Interface(10,15,420,270));

        // 建立組別 : 環境掃描
        G3 = new mGroup(370,320,450,300,"環境掃描(超音波或聲納<繪圖>)");
        this.add(G3);

        // 建立繪圖容器
        G3.add(new Interface2(10,15,420,270));

        // 建立組別 : log
        G4 = new mGroup(370,630,750,350,"封包Log");
        this.add(G4);

        // 建立組別 : 遠端命令
        G5 = new mGroup(1135,630,750,350,"遠端命令");
        this.add(G5);

        // 建立組別 : 及時串流
        G6 = new mGroup(830,10,1055,610,"即時串流");
        this.add(G6);

        this.setVisible(true);
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

// 建立自己的組別物件
class mGroup extends JPanel
{
    mGroup(int x , int y , int w , int h , String name)
    {
        this.setBorder(BorderFactory.createTitledBorder(name));
        this.setBounds(x,y,w,h);
        this.setLayout(null);
    }
}

class Interface extends JPanel implements Runnable
{
    int x,y,w,h ;
    int L_Y_offset= -7 , R_Y_offset=-7;

    Interface(int x , int y , int w , int h)
    {
        Thread t = new Thread(this);
        t.start();
        this.setLayout(null);
        this.setBounds(x,y,w,h);
        this.x = x; this.y = y; this.w = w; this.h = h ;
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int [] xpoints = {x,x,x+w,x+w};
        int [] ypoints = {(y+h)/2+L_Y_offset,y+h,y+h,(y+h)/2+R_Y_offset};
        int npoint = 4 ;

            // 姿態控制參數
             L_Y_offset++;
             R_Y_offset--;

        int LongX1 = 100;     int ShortX1 = 150;
        int LongY1 = 15;      int ShortY1 = 30;
        int LongX2 = 320;     int ShortX2 = 270;
        int LongY2 = 15;      int ShortY2 = 30;
        g.setColor(Color.cyan);
        g.fillRect(x,y,w,h);
        g.setColor(new Color(139,69,19));
        g.fillPolygon(xpoints,ypoints,npoint);

        // 畫尺規
        g.setColor(Color.BLACK);
        int n = 30 ;
        for(int i= 0 ; i < 7 ;i++ )
        {
            LongY1+=30; LongY2+=30;
            g.drawLine(LongX1,LongY1,LongX2,LongY2);
            g.drawString(""+n,LongX2+10 ,LongY2);
            g.drawString(""+n,LongX1-20 ,LongY1);
            n-=10;
        }
        n = 25;
        for(int i= 0 ; i < 7 ;i++ )
        {
            ShortY1+=30; ShortY2+=30;
            g.drawLine(ShortX1,ShortY1,ShortX2,ShortY2);
            g.drawString(""+n,ShortX2+10,ShortY2);
            g.drawString(""+n,ShortX1-20,ShortY1);
            n-=10;
        }
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                Thread.sleep(100);
            }catch (Exception e)
            {

            }
            repaint();
        }
    }
}


class Interface2 extends JPanel implements Runnable
{
    int x,y,h,w ,r=10,angle = 0;

    Interface2(int x, int y, int w , int h)
    {
        Thread t = new Thread(this);
        t.start();
        this.setBounds(x,y,w,h);
        this.setLayout(null);
        this.setVisible(true);
        this.x = x ;
        this.y = y ;
        this.w = w ;
        this.h = h ;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int xPoint = (int) (w/2 + 150*Math.cos(angle*Math.PI/180));
        int yPoint = (int) (h/2 + 150*Math.sin(angle*Math.PI/180));
        angle++;
        g.setColor(Color.black);
        g.fillRect(x,y,w,h);
        g.setColor(Color.GREEN);
        g.drawLine(x,h/2,x+w,h/2);
        g.drawLine(w/2,y,w/2,y+h);
        g.drawOval(w/2-20,h/2-20,40,40);
        g.drawOval(w/2-50,h/2-50,100,100);
        g.drawOval(w/2-80,h/2-80,160,160);
        g.drawOval(w/2-120,h/2-120,240,240);
        g.drawOval(w/2-150,h/2-150,300,300);
        g.setColor(Color.red);
        g.drawLine(w/2,h/2,xPoint,yPoint);
    }

    @Override
    public void run() {
        while (true)
        {
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}






