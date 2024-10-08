
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lab04 extends JFrame {
    public Lab04() {
        setTitle("Menú Principal");
        setSize(250, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        // Botón 1: Programa del Registro de citas en una EPS
        JButton btnEPS = new JButton("Registro de citas EPS");
        add(btnEPS);
        
        // Botón 2: Programa de gestion de parqueadero publico
        JButton btnParking = new JButton("Gestor de parqueadero público");
        add(btnParking);
        
        // Acción del botón 1: abrir el programa de Registro de citas en una EPS
        btnEPS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistroCitasEPS EPS = new RegistroCitasEPS();
                EPS.setVisible(true);
            }
        });
        // Acción del botón 2: abrir el programa de gestion de parqueadero publico
        btnParking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parqueadero Parking = new Parqueadero();
                Parking.setVisible(true);
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Lab04().setVisible(true);
        });
    }
} 
