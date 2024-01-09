import java.awt.*;
import java.awt.desktop.ScreenSleepEvent;
import javax.swing.*;
import java.awt.event.*;

class MenuInicial extends JPanel {
    // Variables Globales y estaticas 
    public static final String TITULO = "Java Snake";
    public static final String CREADOR = "Recreado por @KrossGV";
    public static final Font TIT_FON = new Font("Playball", 0, 62);
    public static final Font MENU_FONDO = new Font("Arial", 0, 40);
    public static final Font CREADOR_FONDO = new Font("Arial", 0, 14);
    public static final String[] MENU_ITEMS = {"Jugar", "Mejores Jugadores", "Salir"}; // Aqui van las opciones que tendrá nuestro menú inicial, como una matriz
    public static final int SCREEN_WIDTH = PanelJuego.ANCHO;
    public static final int SCREEN_HEIGHT = PanelJuego.ALTO;
    private int selectedItem = 0;
    VentanaSnake ventanaPrincipal;
    
    public MenuInicial(JFrame frame) {
        ventanaPrincipal = (VentanaSnake) frame;
        this.addKeyListener(new MyKeyAdapter());
        this.setBackground(Color.green);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true); // focusable para que capture las teclas 
        this.requestFocus();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g); // super para no perder el backgroud color seteado
        dibujarTitulo(g);
        dibujarMenu(g);
        dibujarCreador(g);
    }
    
    private void dibujarTitulo(Graphics g){
        g.setColor(Color.red);
        g.setFont(TIT_FON);
        
        FontMetrics metrics = g.getFontMetrics();
        int x = (SCREEN_WIDTH - metrics.stringWidth(TITULO))/2; // /2 para centrar
        int y = metrics.getHeight() + 100;
        
        g.drawString(TITULO, x, y);
    }
    
    private void dibujarMenu (Graphics g){
        g.setColor(Color.white);
        g.setFont(MENU_FONDO);
        
        FontMetrics metrics = g.getFontMetrics();
        for(int i = 0; i < MENU_ITEMS.length; i++){
            int x = (SCREEN_WIDTH - metrics.stringWidth(MENU_ITEMS[i]))/2;
            int y = metrics.getHeight() + 200 + (i * (metrics.getHeight() + 20)); 
            g.drawString(MENU_ITEMS[i], x, y);
            if(selectedItem == i){ dibujarTriangulo(x -30, y - 20, g);}
        }
    }
    
    private void dibujarTriangulo (int x, int y, Graphics g){
        g.setColor(Color.white);
        int[] xPoints = {x, x + 20, x};
        int[] yPoints = {y, y+10, y+20};
        g.fillPolygon(xPoints, yPoints, 3);
    }
    
    public void dibujarCreador (Graphics g){
        g.setColor(Color.red);
        g.setFont(CREADOR_FONDO);
        
        FontMetrics metrics = g.getFontMetrics();
        int x = SCREEN_WIDTH - metrics.stringWidth(CREADOR) - 10;
        int y = SCREEN_WIDTH - metrics.getHeight();
        
        g.drawString(CREADOR, x, y);
    }
    
    private class MyKeyAdapter extends KeyAdapter { // keyAdapter para movernos entre items 
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    decrementarMenu();
                    repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    incrementarMenu();
                    repaint();
                    break;
                    case KeyEvent.VK_ENTER:
                        cambiarPaneles();
                    break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
            }
        }
    } // Bloque KeyAdapter
    
    private void cambiarPaneles(){
        switch(selectedItem){
            case 0:
                ventanaPrincipal.switchAPanelJuego();
                break;
            case 1:
                ventanaPrincipal.switchApanelClasificacion();
                break;
                case 2:
                    System.exit(0);
                    break;
        }
    }
    
    private void incrementarMenu(){ // metodo para movernos hacia arriba 
        int ultItem = MENU_ITEMS.length-1;
        if(selectedItem < ultItem){
        selectedItem++;
        }else{selectedItem = 0;}
    }
    
    private void decrementarMenu(){
    int ultItem = MENU_ITEMS.length-1;
    if(selectedItem > 0){selectedItem--;}else{selectedItem = ultItem;}
    }
    
    public int getSeleccionarMenuItem(){
        return selectedItem;
    }
}
