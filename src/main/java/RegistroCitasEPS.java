import javax.swing.*;
import java.awt.event.*;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

class Paciente implements Comparable<Paciente> {
    String nombre;
    int edad;
    String afiliacion; // "POS" o "PC"
    String condicion; // "embarazo" o "limitacion"
    int prioridad;

    public Paciente(String nombre, int edad, String afiliacion, String condicion) {
        this.nombre = nombre;
        this.edad = edad;
        this.afiliacion = afiliacion;
        this.condicion = condicion;
        this.prioridad = calcularPrioridad();
    }

    private int calcularPrioridad() {
        if (edad >= 60 || edad <= 12) {
            return 1; // Mayor prioridad
        } else if ("embarazo".equalsIgnoreCase(condicion) || "limitacion".equalsIgnoreCase(condicion)) {
            return 2;
        } else if ("PC".equalsIgnoreCase(afiliacion)) {
            return 3;
        } else {
            return 4; // Menor prioridad
        }
    }

    @Override
    public int compareTo(Paciente otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }

    @Override
    public String toString() {
        return nombre;
    }
}

public class RegistroCitasEPS {
    private JFrame frame;
    private JTextField txtNombre, txtEdad, txtAfiliacion, txtCondicion;
    private JLabel lblTurnoActual, lblTiempoRestante, lblTurnosPendientes;
    private JButton btnExtender;

    private PriorityQueue<Paciente> colaPacientes = new PriorityQueue<>();
    private Paciente turnoEnCurso = null;
    private int tiempoRestante = 5; // Tiempo en segundos
    private Timer timer = new Timer();

    public RegistroCitasEPS() {
        // Crear la ventana
        frame = new JFrame("Asignación de Turnos EPS");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Crear los elementos de la interfaz gráfica
        JLabel lblNombre = new JLabel("Nombre y Apellidos:");
        lblNombre.setBounds(10, 10, 150, 20);
        frame.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(160, 10, 200, 20);
        frame.add(txtNombre);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setBounds(10, 40, 150, 20);
        frame.add(lblEdad);

        txtEdad = new JTextField();
        txtEdad.setBounds(160, 40, 200, 20);
        frame.add(txtEdad);

        JLabel lblAfiliacion = new JLabel("Afiliación (POS/PC):");
        lblAfiliacion.setBounds(10, 70, 150, 20);
        frame.add(lblAfiliacion);

        txtAfiliacion = new JTextField();
        txtAfiliacion.setBounds(160, 70, 200, 20);
        frame.add(txtAfiliacion);

        JLabel lblCondicion = new JLabel("Condición especial:");
        lblCondicion.setBounds(10, 100, 150, 20);
        frame.add(lblCondicion);

        txtCondicion = new JTextField();
        txtCondicion.setBounds(160, 100, 200, 20);
        frame.add(txtCondicion);

        JButton btnRegistrar = new JButton("Registrar Paciente");
        btnRegistrar.setBounds(10, 130, 350, 30);
        frame.add(btnRegistrar);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarPaciente();
            }
        });

        lblTurnoActual = new JLabel("Turno en curso: Ninguno");
        lblTurnoActual.setBounds(10, 170, 350, 20);
        frame.add(lblTurnoActual);

        lblTiempoRestante = new JLabel("Tiempo restante: 0");
        lblTiempoRestante.setBounds(10, 190, 350, 20);
        frame.add(lblTiempoRestante);

        btnExtender = new JButton("Extender Tiempo");
        btnExtender.setBounds(10, 210, 350, 30);
        btnExtender.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extenderTiempo();
            }
        });
        frame.add(btnExtender);

        lblTurnosPendientes = new JLabel("Turnos pendientes: 0");
        lblTurnosPendientes.setBounds(10, 240, 350, 20);
        frame.add(lblTurnosPendientes);

        // Inicializar pacientes de prueba
        inicializarPacientes();

        // Iniciar el temporizador que actualiza el tiempo cada segundo
        iniciarSimulacion();

        frame.setVisible(true);
    }

    private void registrarPaciente() {
        String nombre = txtNombre.getText();
        int edad = Integer.parseInt(txtEdad.getText());
        String afiliacion = txtAfiliacion.getText();
        String condicion = txtCondicion.getText();

        Paciente paciente = new Paciente(nombre, edad, afiliacion, condicion);
        colaPacientes.offer(paciente);

        lblTurnosPendientes.setText("Turnos pendientes: " + colaPacientes.size());
        JOptionPane.showMessageDialog(frame, "Paciente " + nombre + " registrado exitosamente");
    }

    private void llamarSiguientePaciente() {
        if (!colaPacientes.isEmpty()) {
            turnoEnCurso = colaPacientes.poll();
            lblTurnoActual.setText("Turno en curso: " + turnoEnCurso.nombre);
            tiempoRestante = 5; // Reiniciar el tiempo para el siguiente paciente
        } else {
            turnoEnCurso = null;
            lblTurnoActual.setText("Turno en curso: Ninguno");
        }
        lblTurnosPendientes.setText("Turnos pendientes: " + colaPacientes.size());
    }

    private void iniciarSimulacion() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (turnoEnCurso != null) {
                    tiempoRestante--;
                    lblTiempoRestante.setText("Tiempo restante: " + tiempoRestante);
                    if (tiempoRestante <= 0) {
                        llamarSiguientePaciente();
                    }
                } else {
                    llamarSiguientePaciente();
                }
            }
        }, 0, 1000); // Actualizar cada segundo
    }

    private void extenderTiempo() {
        if (turnoEnCurso != null) {
            tiempoRestante += 5;
            JOptionPane.showMessageDialog(frame, "Tiempo extendido en 5 segundos");
        } else {
            JOptionPane.showMessageDialog(frame, "No hay un turno en curso para extender.");
        }
    }

    private void inicializarPacientes() {
        // Datos de 10 pacientes de prueba
        Paciente[] pacientesPrueba = new Paciente[]{
            new Paciente("Juan Pérez", 30, "POS", "ninguna"),
            new Paciente("Ana Gómez", 65, "PC", "limitacion"),
            new Paciente("Luis Martínez", 15, "POS", "embarazo"),
            new Paciente("María López", 10, "PC", "ninguna"),
            new Paciente("Carlos Sánchez", 55, "POS", "ninguna"),
            new Paciente("Laura Rodríguez", 70, "PC", "limitacion"),
            new Paciente("Jorge Torres", 45, "POS", "ninguna"),
            new Paciente("Sofía Morales", 25, "PC", "embarazo"),
            new Paciente("Diego Ramírez", 12, "POS", "ninguna"),
            new Paciente("Clara Fernández", 50, "PC", "limitacion")
        };

        // Registrar los pacientes de prueba
        for (Paciente paciente : pacientesPrueba) {
            colaPacientes.offer(paciente);
        }

        lblTurnosPendientes.setText("Turnos pendientes: " + colaPacientes.size());
    }

    public static void main(String[] args) {
        new RegistroCitasEPS();
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
