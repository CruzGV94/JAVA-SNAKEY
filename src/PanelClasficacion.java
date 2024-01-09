
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class PanelClasficacion extends JPanel{
    // variables globales 
    VentanaSnake ventanaSnake; // Instancia de la ventana principal
    public static final int SCREEN_ANCHO = 600; // ancho
    public static final int SCREEN_ALTO = 600; // alto
    public static final Font TITUTLO_FONDO = new Font("Arial", 0, 42); // formato del titulo
    public static final Font LINE_FONT = new Font("Arial", 0, 32);
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 32);
    ArrayList<Score> scoreList = new ArrayList<Score>(); // Lista de las puntuaciones 
    JPanel scores;

    public PanelClasficacion(JFrame frame) {
        ventanaSnake = (VentanaSnake) frame;

        this.setPreferredSize(new Dimension(SCREEN_ANCHO, SCREEN_ALTO));
        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        this.addKeyListener(new MyKeyAdapter());

        JLabel title = new JLabel("Top 10 Jugadores"); 
        title.setForeground(Color.white);
        title.setFont(TITUTLO_FONDO);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(SCREEN_ANCHO, 100));

        this.add(title, BorderLayout.NORTH);

        GridLayout grid = new GridLayout(11, 2);
        scores = new JPanel(grid);
        scores.setBackground(new Color(0, 0, 0, 0));
        scores.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }
    
    	class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent ev){
			int keyCode = ev.getKeyCode();
			if (keyCode == KeyEvent.VK_ESCAPE){
                            ventanaSnake.panelClasificaionAvs();
			}
		}
	}
        
        private JLabel getLabelItem(String text){
            JLabel aux = new JLabel(text, SwingConstants.CENTER);
            
            aux.setForeground(Color.white);
            aux.setFont(LINE_FONT);
            
            return aux;
        }

    public void CargarPuntuacionList() {
        try {
            scoreList.clear();
            BufferedReader buffer = new BufferedReader(new FileReader(new File("scores.data")));
            String line;
            String[] nameScore;
            Score aux;
            while ((line = buffer.readLine()) != null) {
                nameScore = line.split(",");
                aux = new Score(nameScore[0], Integer.parseInt(nameScore[1]));
                scoreList.add(aux);
            }
            System.out.println("ArrayList cargada exitosamente");
        } catch (Exception ex) {
            System.out.println("Error al leer el archivo de clasificaion");
        } finally {
            scores.removeAll();
            scores.repaint();
            JLabel nombreLabel = getLabelItem("Nombre");
            JLabel puntuacionLabel = getLabelItem("Puntaje");
            nombreLabel.setFont(HEADER_FONT);
            puntuacionLabel.setFont(HEADER_FONT);

            scores.add(nombreLabel);
            scores.add(puntuacionLabel);

            for (int i = 0; i < 10; i++) {
                Score score1 = scoreList.get(i);
                JLabel nameLabel1 = getLabelItem(score1.name);
                JLabel scoreLabel1 = getLabelItem(String.valueOf(score1.score));

                scores.add(nameLabel1);
                scores.add(scoreLabel1);
            }
            this.add(scores, BorderLayout.CENTER);
        }
    }
}

    // poo para el jugador (se hace en la misma clase)
    class Score implements Comparable {

        String name;
        int score;

        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String toString() {
            return "name: " + this.name + " score: " + this.score;
        }

        public int getScore() {
            return score;
        }

        public String getName() {
            return name;
        }

        public int compareTo(Object o) {
            Score b = (Score) o;
            return this.score - b.score;
        }
    }
