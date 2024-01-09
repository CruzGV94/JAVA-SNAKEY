
import javax.swing.*;

class VentanaSnake extends JFrame {

    MenuInicial menuInicial = new MenuInicial(this);
    PanelJuego panelJuego = new PanelJuego(this);
    PanelClasficacion panelClasficacion = new PanelClasficacion(this);
    private boolean addPanelClasificacion = false;

    public VentanaSnake() { // método para la ventana inicial (Constructor)

        this.add(menuInicial);

        this.setTitle("Java Snakey");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.add(panelJuego);
    }

    public void switchAPanelJuego() {
        menuInicial.setVisible(false);
        panelJuego.setVisible(true);
        panelJuego.requestFocus();
        panelJuego.startGame();
    }
    
    // de panel juego a menu inicial 
    public void switchAmenuInicial(){
        panelJuego.gameOver();
        panelJuego.setVisible(false);
        menuInicial.setVisible(true);
        menuInicial.requestFocus();
    }
    
    public void panelClasificaionAvs() {
        panelClasficacion.setVisible(false);
        panelJuego.setVisible(true);
        panelJuego.requestFocus();
    }
    
    public void panelClasificiacionAjuego(){
        panelClasficacion.setVisible(false);
        panelJuego.setVisible(true);
        panelJuego.requestFocus();
        panelJuego.startGame();
    }

    public void switchApanelClasificacion() {
        if (addPanelClasificacion) {
            menuInicial.setVisible(false);
            panelClasficacion.CargarPuntuacionList();
            panelClasficacion.setVisible(true);
            panelClasficacion.requestFocus();
        } else {
            menuInicial.setVisible(false);
            panelJuego.setVisible(false);
            this.add(panelClasficacion);
            panelClasficacion.CargarPuntuacionList();
            panelClasficacion.requestFocus();
            addPanelClasificacion = true;
        }
    }
}
