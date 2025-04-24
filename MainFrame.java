
package mazegame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

 class Point 
{
     double x;
    double y;
}
public class MainFrame extends javax.swing.JFrame 
{ private int ballX; // Topun X koordinatı
    private int ballY; // Topun Y koordinatı
    private final int BALL_SIZE = 10; // Topun boyutu
    private final int STEP = 8; // Hareket adım büyüklüğü

    private boolean[][] trianglePixels; // Üçgen piksellerini tutan matris
    private Image triangleImage; // Üçgen üzerine yerleştirilecek resim
    private int[][] rotatingTriangleVertices = {{180, 500}, {305, 284}, {430, 500}}; // Dönen üçgenin köşeleri
    private double rotationAngle = 0; // Dönen üçgenin açısı
    private final int WIN_X = 620; // Kazanma koşulu X koordinatı
    private final int WIN_Y = 450; // Kazanma koşulu Y koordinatı

    private int[][][] rotatingTriangleHistory; // Dönen üçgenin geçmiş koordinatları
    private int historyIndex = 0; // Geçmiş koordinatlar dizisinin geçici indeksi

    public MainFrame() {
        setTitle("Dönen Üçgenler ile Labirent");
        setSize(640, 660);  // pencere boyutu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        triangleImage = new ImageIcon("triangle_image.png").getImage(); // Resmi yükle
        initializeTrianglePixels(); // Üçgen piksellerini başlat
       setRandomStartPosition(20, 630, 50, 600);
 // Rastgele başlangıç pozisyonu ayarla

        // Geçmiş koordinatlar dizisini başlat
        rotatingTriangleHistory = new int[100][3][2];

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP -> ballY = Math.max(ballY - STEP, 0);
                    case KeyEvent.VK_DOWN -> ballY = Math.min(ballY + STEP, getHeight() - BALL_SIZE);
                    case KeyEvent.VK_LEFT -> ballX = Math.max(ballX - STEP, 0);
                    case KeyEvent.VK_RIGHT -> ballX = Math.min(ballX + STEP, getWidth() - BALL_SIZE);
                }

                if (checkCollision()) {
                    endGame(false); // Çarpışma durumu
                } else if (checkWin()) {
                    showConfetti(); // Konfeti patlat
                    endGame(true); // Kazanma durumu
                }

                repaint(); // Ekranı yeniden çiz
            }
        });

        Timer rotationTimer = new Timer(720, e -> {
            rotateTriangle(); // Üçgeni döndür
            repaint();
        });
        rotationTimer.start(); // Üçgeni döndürmeye başla
    }

    private void initializeTrianglePixels() {
        trianglePixels = new boolean[getWidth()][getHeight()];
        //statik üçgenler
        rasterizeTriangle(20, 50, 80, 50, 20, 100); 
        rasterizeTriangle(20, 100, 80, 100, 80, 150); 
         rasterizeTriangle(20, 150, 80, 150, 20, 200); 
        rasterizeTriangle(20, 200, 80, 200, 80, 250); 
         rasterizeTriangle(20, 300, 80, 350, 20, 400); 
        rasterizeTriangle(20, 400, 80, 400, 20, 450); 
         rasterizeTriangle(80, 450, 20, 500, 80, 500); 
        rasterizeTriangle(20, 500, 80, 550, 20, 600); 
         rasterizeTriangle(20, 600, 20, 650, 80, 650); 
        rasterizeTriangle(80, 250, 80, 300, 130, 300); 
         rasterizeTriangle(80, 400, 80, 450, 130, 450); 
        rasterizeTriangle(130, 500, 80, 550, 130, 550); 
         rasterizeTriangle(80, 600, 130, 600, 80, 650); 
       
         rasterizeTriangle(130, 50, 230, 50, 180, 100); 
         rasterizeTriangle(130, 100, 180, 100, 130, 150); 
        rasterizeTriangle(130, 150, 230, 150, 180, 200); 
         rasterizeTriangle(130, 250, 180, 250, 130, 300); 
        rasterizeTriangle(180, 250, 130, 300, 180, 300); 
         rasterizeTriangle(130, 350, 130, 450, 180, 400); 
        rasterizeTriangle(130, 600, 180, 600, 130, 650); 
         rasterizeTriangle(230, 50, 280, 50, 230, 100);
         rasterizeTriangle(230, 150, 280, 150, 230, 200); 
         rasterizeTriangle(180, 600, 180, 650, 230, 650); 
        rasterizeTriangle(280, 50, 330, 50, 280, 100); 
         rasterizeTriangle(280, 150, 330, 150, 280, 200); 
        rasterizeTriangle(330, 150, 330, 200, 280, 200); 
         rasterizeTriangle(20, 250, 80, 250, 20, 300); 
        rasterizeTriangle(280, 600, 230, 650, 330, 650); 
         rasterizeTriangle(330, 50, 380, 50, 380, 100); 
        rasterizeTriangle(380, 250, 380, 300, 430, 300); 
         rasterizeTriangle(380, 600, 330, 650, 380, 650); 
        rasterizeTriangle(430, 500, 380, 550, 480, 550); 
         rasterizeTriangle(430, 550, 380, 600, 430, 600); 
        rasterizeTriangle(430, 600, 380, 650, 480, 650); 
         rasterizeTriangle(380, 100, 380, 150, 430, 150); 
        rasterizeTriangle(380, 150, 380, 200, 430, 200); 
         rasterizeTriangle(380, 300, 380, 350, 430, 350); 
        rasterizeTriangle(380, 50, 480, 50, 430, 100); 
        
        rasterizeTriangle(480, 50, 530, 50, 480, 100); 
        rasterizeTriangle(530, 50, 530, 100, 580, 100); 
         rasterizeTriangle(580, 50, 630, 50, 580, 100); 
        rasterizeTriangle(530, 100, 480, 150, 580, 150); 
        
    rasterizeTriangle(480, 150, 480, 200, 530, 200); 
        rasterizeTriangle(630, 150, 580, 200, 630, 200); 
         rasterizeTriangle(630, 200, 580, 250, 630, 250); 
        rasterizeTriangle(480, 250, 530, 250, 480, 300); 
         rasterizeTriangle(530, 250, 580, 250, 580, 300); 
        rasterizeTriangle(480, 300, 530, 300, 530, 350); 
         rasterizeTriangle(580, 300, 630, 300, 580, 350); 
        rasterizeTriangle(480, 400, 530, 400, 480, 450); 
         rasterizeTriangle(530, 400, 580, 400, 580, 450); 
        rasterizeTriangle(480, 550, 530, 550, 530, 500); 
         rasterizeTriangle(580, 450, 580, 500, 630, 500); 
      //  rasterizeTriangle(630, 150, 330, 200, 280, 200); 
         rasterizeTriangle(630, 500, 580, 550, 630, 550); 
        rasterizeTriangle(530, 600, 530, 650, 580, 650); 
        rasterizeTriangle(630, 550, 580, 650, 630, 650); 
         rasterizeTriangle(580, 100, 630, 50, 630, 100); 
          rasterizeTriangle(580, 350, 630, 300, 630, 400); 
    }
private void setRandomStartPosition(int minX, int maxX, int minY, int maxY) {
    Random random = new Random();
    boolean validPosition = false;

    while (!validPosition) {
        // Belirli aralıkta rastgele x ve y koordinatları üret
        ballX = minX + random.nextInt(maxX - minX - BALL_SIZE);
        ballY = minY + random.nextInt(maxY - minY - BALL_SIZE);

        // Çarpışma kontrolü yapılır, geçerli pozisyon bulunana kadar döngü devam eder
        if (!checkCollision()) {
            validPosition = true;
        }
    }
}
 
    private void rasterizeTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int minX = Math.min(x1, Math.min(x2, x3));
        int maxX = Math.max(x1, Math.max(x2, x3));
        int minY = Math.min(y1, Math.min(y2, y3));
        int maxY = Math.max(y1, Math.max(y2, y3));

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (isPointInTriangle(x, y, x1, y1, x2, y2, x3, y3)) {
                    trianglePixels[x][y] = true;
                }
            }
        }
    }
private boolean checkCollision() {
    // Çarpışma kontrolü için topun dönen üçgenle çarpması da kontrol edilecek
    // Geçmiş koordinatları kullanmıyoruz, sadece mevcut koordinatları kontrol ediyoruz.
    for (int x = ballX; x < ballX + BALL_SIZE; x++) {
        for (int y = ballY; y < ballY + BALL_SIZE; y++) {
            if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight() && trianglePixels[x][y]) {
                return true; // Üçgenin içiyle çarpma
            }
        }
    }

    // Dönen üçgenin kenarlarını kontrol et
    if (isPointOnTriangleEdge(ballX, ballY,
            rotatingTriangleVertices[0][0], rotatingTriangleVertices[0][1],
            rotatingTriangleVertices[1][0], rotatingTriangleVertices[1][1])) {
        return true; // Üçgenin kenarına çarpma
    }
    if (isPointOnTriangleEdge(ballX, ballY,
            rotatingTriangleVertices[1][0], rotatingTriangleVertices[1][1],
            rotatingTriangleVertices[2][0], rotatingTriangleVertices[2][1])) {
        return true; // Üçgenin kenarına çarpma
    }
    if (isPointOnTriangleEdge(ballX, ballY,
            rotatingTriangleVertices[2][0], rotatingTriangleVertices[2][1],
            rotatingTriangleVertices[0][0], rotatingTriangleVertices[0][1])) {
        return true; // Üçgenin kenarına çarpma
    }

    // Dönen üçgenin içine giren topu kontrol et
    if (isPointInTriangle(ballX, ballY,
            rotatingTriangleVertices[0][0], rotatingTriangleVertices[0][1],
            rotatingTriangleVertices[1][0], rotatingTriangleVertices[1][1],
            rotatingTriangleVertices[2][0], rotatingTriangleVertices[2][1])) {
        return true; // Üçgenin içine giren top
    }

    return false;
}

// Üçgenin içine giren noktayı kontrol et
private boolean isPointInTriangle(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3) {
    double denominator = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);
    double a = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / denominator;
    double b = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / denominator;
    double c = 1 - a - b;

    return a >= 0 && b >= 0 && c >= 0; // Nokta üçgenin içinde ise
}

// Üçgenin kenarında olup olmadığını kontrol et
private boolean isPointOnTriangleEdge(int px, int py, int x1, int y1, int x2, int y2) {
    double edgeLength = Math.hypot(x2 - x1, y2 - y1); // Kenarın uzunluğu
    double dist1 = Math.hypot(px - x1, py - y1); // Noktanın ilk kenara olan mesafesi
    double dist2 = Math.hypot(px - x2, py - y2); // Noktanın ikinci kenara olan mesafesi

    // Eğer bu mesafelerin toplamı kenarın uzunluğuna eşitse, nokta kenarda demektir
    return Math.abs((dist1 + dist2) - edgeLength) < 1; // Hata payı olarak 1 pixel
}



    private boolean checkWin() {
        return ballX > WIN_X && ballY > WIN_Y; // Kazanma noktası sağ-alt köşe
    }

  private void endGame(boolean won) 
  {
    String message = won ? "Tebrikler! Kazandınız!" : "Üzgünüm! Çarptınız.";
    int option = JOptionPane.showConfirmDialog(this, message + "\nTekrar oynamak ister misiniz?", "Oyun Bitti", JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
        setRandomStartPosition(20, 630, 50, 600); // Yeni rastgele başlangıç pozisyonu ayarla
        rotationAngle = 0; // Dönen üçgenin açısını sıfırla
        repaint(); // Ekranı yeniden çiz
    } else {
        System.exit(0); // Oyunu kapat
    }
}

private void showConfetti() {
    Random random = new Random();
    Graphics g = getGraphics(); // Geçici bir çizim bağlamı alıyoruz
    for (int i = 0; i < 100; i++) { // 100 adet konfeti parçası
        int x = random.nextInt(getWidth()); // Rastgele X pozisyonu
        int y = random.nextInt(getHeight()); // Rastgele Y pozisyonu
        int size = random.nextInt(10) + 5; // Rastgele boyut (5 ile 15 arasında)
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // Rastgele renk
        g.setColor(color);
        g.fillOval(x, y, size, size); // Konfeti parçasını çizin
        try {
            Thread.sleep(15); // Hafif bir gecikme
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

private void rotateTriangle() {
    double centerX = (rotatingTriangleVertices[0][0] + rotatingTriangleVertices[1][0] + rotatingTriangleVertices[2][0]) / 3.0;
    double centerY = (rotatingTriangleVertices[0][1] + rotatingTriangleVertices[1][1] + rotatingTriangleVertices[2][1]) / 3.0;
    rotationAngle += Math.PI / 180; // Döndürme açısı

    for (int i = 0; i < rotatingTriangleVertices.length; i++) {
        double dx = rotatingTriangleVertices[i][0] - centerX;
        double dy = rotatingTriangleVertices[i][1] - centerY;

        rotatingTriangleVertices[i][0] = (int) (centerX + dx * Math.cos(rotationAngle) - dy * Math.sin(rotationAngle));
        rotatingTriangleVertices[i][1] = (int) (centerY + dx * Math.sin(rotationAngle) + dy * Math.cos(rotationAngle));
    }
}


private void resetRotatingTriangle() {
    // Dönen üçgenin başlangıç pozisyonunu sıfırlama
    rotatingTriangleVertices = new int[][]{{180, 500}, {305, 284}, {430, 500}}; // Başlangıç köşe koordinatları
    rotationAngle = 0; // Başlangıç açısını sıfırla
    historyIndex = 0; // Geçmişi sıfırla
    // Geçmiş koordinatlarını sıfırlayın
    for (int i = 0; i < rotatingTriangleHistory.length; i++) {
        for (int j = 0; j < 3; j++) {
            rotatingTriangleHistory[i][j][0] = 0;
            rotatingTriangleHistory[i][j][1] = 0;
        }
    }
}

@Override
public void paint(Graphics g) {
    super.paint(g);

    // Topu çiz
    g.setColor(Color.BLUE);
    g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

    // Statik üçgenleri çiz
    g.setColor(Color.BLACK);
    for (int y = 0; y < getHeight(); y++) {
        for (int x = 0; x < getWidth(); x++) {
            if (trianglePixels[x][y]) {
                g.drawLine(x, y, x, y);
            }
        }
    }

    // Dönen üçgeni çiz
    g.setColor(Color.RED);
    int[] xPoints = {rotatingTriangleVertices[0][0], rotatingTriangleVertices[1][0], rotatingTriangleVertices[2][0]};
    int[] yPoints = {rotatingTriangleVertices[0][1], rotatingTriangleVertices[1][1], rotatingTriangleVertices[2][1]};
    g.fillPolygon(xPoints, yPoints, 3);

    // Dönen üçgenin merkezini hesapla
    double centerX = (rotatingTriangleVertices[0][0] + rotatingTriangleVertices[1][0] + rotatingTriangleVertices[2][0]) / 3.0;
    double centerY = (rotatingTriangleVertices[0][1] + rotatingTriangleVertices[1][1] + rotatingTriangleVertices[2][1]) / 3.0;

    // Resmi yerleştir (resmin merkezini üçgenin merkezine denk getirecek şekilde)
    int imageWidth = triangleImage.getWidth(this);
    int imageHeight = triangleImage.getHeight(this);
    int imageX = (int)(centerX - imageWidth / 2);
    int imageY = (int)(centerY - imageHeight / 2);
    
    g.drawImage(triangleImage, imageX, imageY, this);
}
}
