import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.applet.AudioClip;

class PanelJuego extends JPanel implements ActionListener {
    // tamaño por defecto de del frame 
    public static final int ANCHO= 600; // ancho 
    public static final int ALTO = 600; // alto 
    public static final int UNIT_SIZE = 25; // tamaño unicial de cada elemento en unidad
    // Ventana cuadrada.
    // Tamño en unidades para dibujar los elementos del juego, se usan operaciones basicas para calcular la posicion de cada elemento 
    public static final int GAME_UNITS = (int) (ANCHO / UNIT_SIZE) * (ALTO / UNIT_SIZE); // Tamaño en unidades de todos los elementos 
    public static final int HORIZONTAL_UNITS = ANCHO / UNIT_SIZE; // tamaño en horizontal 
    public static final int VERTICAL_UNITS = ALTO / UNIT_SIZE; // tamaño en vertical
    public static final int DELAY = 100; // tiempo en milisegundos entre cada evento 
    public static final int TAMAÑO_INICIAL_SNAKE = 6; 
    //private boolean running = false;
    private int appleX; // pisicion de la manzana en X
    private int appleY; // posicion de la manzana en Y
    private Timer timer = new Timer(DELAY, this);
    private char direction;
    private int[] snakeX = new int[GAME_UNITS]; // posicion de la serpiente en x 
    private int[] snakeY = new int[GAME_UNITS]; // pisicion de la serpiente en Y
    private int snaketamaño;
    private int manzanasTragadas;
    VentanaSnake ventanaSnake;
    boolean keyInput = false;
    private int lowestScore;// puntaje mas bajo
    private ArrayList<Score> scoreList = new ArrayList<Score>(); // Lista pata guardar el puntaje de la clase Score
    private boolean showJTextField = false;
    private String playerName = "";
    String[] gameOverMessages = { "No te tocaba carnal"
            , "suerte la proxima!", "Lo sentimos perro!"
            , "Ow :(", "Uhh eso dolio! Manco"};
    
    String randomGameOverMessage = ""; // variable para mostrar los mensajes, se inicializa con comillas pq los mensajes ya estan declarados 
    private Score actualScore;

    PanelJuego(JFrame frame) {
        ventanaSnake = (VentanaSnake) frame;

        // focusable para capturar las teclas presionadas
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(new MyKeyAdapter());
        this.setPreferredSize(new Dimension(ANCHO, ALTO));
        this.setBackground(Color.white);
        // timer es una clase de java swing que activa un actioEvent a cada intervalo de milisegundos  
        // en este caso se activa a cada cuarto de segundo
    }
    
    public void audioChoque(){
        AudioClip sound;
        sound = java.applet.Applet.newAudioClip(getClass().getResource("/gameOver.wav"));
        sound.play();
    }
    
    public void audioComer(){
        AudioClip sound;
        sound = java.applet.Applet.newAudioClip(getClass().getResource("/collision.wav"));
        sound.play();
    }
    
    public void audioMover(){
        AudioClip sound;
        sound = java.applet.Applet.newAudioClip(getClass().getResource("/mover2.wav"));
        sound.play();
    }
    
    public void audioStart(){
        AudioClip sound;
        sound = java.applet.Applet.newAudioClip(getClass().getResource("/start.wav"));
        sound.play();
    }

    public void startGame() {
        snaketamaño = TAMAÑO_INICIAL_SNAKE;
        manzanasTragadas = 0;
        for (int i = 0; i < snaketamaño; i++) {
            snakeX[i] = 0;
            snakeY[i] = 0;
        }
        direction = 'R';
        timer.start();
        nuevaManzana();
        System.out.println("Inicializar panel juego startGame()");
        //cargarPuntuacionLista();
        //cargarPuntuacionBaja();
        audioStart();
        randomGameOverMessage = gameOverMessages[random(gameOverMessages.length)];
    }

    // mismo metodo que panel clasificacion 
    public void cargarPuntuacionLista() {
        try {
            scoreList.clear();
            BufferedReader buffer = new BufferedReader(new FileReader(new File("puntuaciones.data")));
            String line;
            String[] nameScore;
            Score aux;
            while ((line = buffer.readLine()) != null) {
                nameScore = line.split(",");
                aux = new Score(nameScore[0], Integer.parseInt(nameScore[1]));
                scoreList.add(aux);
            }
            System.out.println("ArrayList cargada exitosamente");
            System.out.println(scoreList);
        } catch (Exception ex) {
            System.out.println("Error al cargar el archivo de puntuacion");
        }
    }

    public void cargarPuntuacionBaja() {
        // vamos a ordenar una vez por las dudas, oredena de mayor a menor 
        scoreList.sort(Comparator.reverseOrder());
        lowestScore = scoreList.get(9).getScore();
        System.out.println("lowestScore: " + lowestScore);
    }

    public void actionPerformed(ActionEvent ev) {
        mover();
        choque();
        tragaManzana();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        // Dibujar manzana en X y en Y 
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        // Dibujar cabeza de la serpiente 
        g.setColor(Color.green);
        g.fillRect(snakeX[0], snakeY[0], UNIT_SIZE, UNIT_SIZE);
        // cuerpo de la sepiente 
        for (int i = 1; i < snaketamaño; i++) {
            g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
        }

        // Dibujar puntuacion
        g.setColor(Color.black);
        g.setFont(new Font("MS Gothic", Font.PLAIN, 25));
        FontMetrics fontSize = g.getFontMetrics();
        int fontX = ANCHO - fontSize.stringWidth("Puntaje: " + manzanasTragadas) - 10;
        int fontY = fontSize.getHeight();
        g.drawString("Puntaje: " + manzanasTragadas, fontX, fontY);

        if (!timer.isRunning()) {
            // dibujar game over 
            g.setColor(Color.black);
            g.setFont(new Font("MS Gothic", Font.PLAIN, 58));

            String message = randomGameOverMessage;
            message = gameOverMessages[random(gameOverMessages.length)];
            fontSize = g.getFontMetrics();
            fontX = (ANCHO - fontSize.stringWidth(message)) / 2;
            fontY = (ALTO - fontSize.getHeight()) / 2;
            g.drawString(message, fontX, fontY);

            g.setFont(new Font("MS Gothic", Font.PLAIN, 14));
            message = "Presiona F2 para reiniciar";
            fontSize = g.getFontMetrics();
            fontX = (ANCHO - fontSize.stringWidth(message)) / 2;
            fontY = fontY + fontSize.getHeight() + 20;
            g.drawString(message, fontX, fontY);

            if (showJTextField) {
                drawJTextField(g);
                drawPlayerName(g);
            }
        }
    }

    // textfield para ingresar nombre de los jugadores 
    public void drawJTextField(Graphics g) {
        g.setFont(new Font("MS Gothic", Font.PLAIN, 24));
        String message = "Ingresa tu nombre:";
        FontMetrics fontSize = g.getFontMetrics();
        // Horizontal center
        int fontX = (ANCHO - fontSize.stringWidth(message)) / 2;
        g.drawString(message, fontX, 350);
    }

    public void drawPlayerName(Graphics g) {
        g.setFont(new Font("MS Gothic", Font.PLAIN, 24));
        FontMetrics fontSize = g.getFontMetrics();
        // Horizontal center
        int fontX = (ANCHO - fontSize.stringWidth(playerName)) / 2;
        g.drawString(playerName, fontX, 400);
    }

    public void nuevaManzana() {
        // numero random entre 0 y 23 * unit size
        int x = random(HORIZONTAL_UNITS) * UNIT_SIZE;
        int y = random(VERTICAL_UNITS) * UNIT_SIZE;
        Point provisional = new Point(x, y); // puntos en el plano
        Point snakePos = new Point();
        boolean newApplePermission = true;
        for (int i = 0; i < snaketamaño; i++) {
            snakePos.setLocation(snakeX[i], snakeY[i]);
            if (provisional.equals(snakePos)) {
                newApplePermission = false;
            }
        }

        if (newApplePermission) {
            appleX = x;
            appleY = y;
        } else {
            nuevaManzana();
        }
    }

    public void choque() {
        if (snakeX[0] >= (ANCHO) || snakeX[0] < 0 || snakeY[0] >= (ALTO) || snakeY[0] < 0) {
            audioChoque();
            gameOver();
        }
        for (int i = 1; i < snaketamaño; i++) {
            if ((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
                gameOver();
                audioChoque();
            }
        }
    }

    public void tragaManzana() {
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            audioComer();
            snaketamaño++;
            manzanasTragadas++;
            nuevaManzana();
        }
    }

    public void mover() {
        // Este metodo se ejecuta cada vez que timer nos lo permite
        // Hay que recorrer la serpiente de atras para adelante
        for (int i = snaketamaño; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        // System.out.println("snakeX[0] = " + snakeX[0]);
        switch (direction) {
            case 'R':
                snakeX[0] += UNIT_SIZE;
                break;
            case 'L':
                snakeX[0] -= UNIT_SIZE;
                break;
            case 'U':
                snakeY[0] -= UNIT_SIZE;
                break;
            case 'D':
                snakeY[0] += UNIT_SIZE;
                break;
        }

        keyInput = false;
    }

    public void gameOver() {
        timer.stop();
        if (manzanasTragadas > lowestScore) {
            showJTextField = true;
        }

    }

    public int random(int range) {
        // devuelve un valor desde cero hasta el rango
        return (int) (Math.random() * range);
    }

    class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent k) {

            switch (k.getKeyCode()) {
                case (KeyEvent.VK_DOWN):
                    if (direction != 'U' && keyInput == false) {
                        direction = 'D';
                        keyInput = true;
                        audioMover();
                    }
                    break;
                case (KeyEvent.VK_UP):
                    if (direction != 'D' && !keyInput) {
                        direction = 'U';
                        keyInput = true;
                        audioMover();
                    }
                    break;
                case (KeyEvent.VK_LEFT):
                    if (direction != 'R' && keyInput == false) {
                        direction = 'L';
                        keyInput = true;
                        audioMover();
                    }
                    break;
                case (KeyEvent.VK_RIGHT):
                    if (direction != 'L' && keyInput == false) {
                        direction = 'R';
                        keyInput = true;
                        audioMover();
                    }
                    break;
                case (KeyEvent.VK_F2):
                    if (!timer.isRunning()) {
                        startGame();
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    ventanaSnake.switchAmenuInicial();
                    break;
            }

            if (showJTextField) {
                if (k.getKeyCode() == KeyEvent.VK_ENTER) {
                    actualScore = new Score(playerName, manzanasTragadas);
                    scoreList.add(actualScore);
                    playerName = "";
                    sortAndSave();
                    showJTextField = false;
                    ventanaSnake.switchAPanelJuego();
                } else if (k.getKeyCode() == KeyEvent.VK_BACK_SPACE && playerName.length() > 0) {
                    StringBuilder sb = new StringBuilder(playerName);
                    sb.deleteCharAt(sb.length() - 1);
                    playerName = sb.toString();
                } else {
                    if (!k.isActionKey() && k.getKeyCode() != KeyEvent.VK_SHIFT
                            && k.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                        playerName = playerName + k.getKeyChar();
                    }
                }

                repaint();
            }
            // System.out.println(direction);
        }
    }

    // ordenamiento
    public void sortAndSave() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("puntuacion.data")));
            scoreList.sort(Comparator.reverseOrder());
            for (int i = 0; i < 10; i++) {
                Score element = scoreList.get(i);
                bw.write(element.name + "," + String.valueOf(element.score) + "\n");

            }
            bw.flush();
        } catch (IOException ex) {
            System.out.println("Error al leer archivo");
        }

    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
            System.out.println("Slept");
        } catch (Exception ex) {
            System.out.println("Error falt en el metodo sleep()");
        }
    }

}