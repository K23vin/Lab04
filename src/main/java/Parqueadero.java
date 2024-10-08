import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Parqueadero extends JFrame {
    // Atributos
    private List<Vehicle> vehicleList = new ArrayList<>(); // Lista para todos los vehículos
    private Stack<Vehicle> twoWheelStack = new Stack<>(); // Pila para vehículos de 2 ruedas
    private Stack<Vehicle> fourWheelStack = new Stack<>(); // Pila para vehículos de 4 ruedas
    private int vehicleCounter = 1; // Contador para el número de vehículo

    private DefaultTableModel tableModel;
    private JTable table;

    // Datos precargados (placa, tipo, hora de ingreso)
    private String[] placas = {"ABC123", "XYZ456", "MOT789", "CIC321", "CAR654"};
    private String[] tipos = {"Carro", "Carro", "Motocicleta", "Bicicleta/Ciclomotor", "Carro"};
    private String[] horasIngreso = {"10:30", "11:00", "09:45", "08:30", "12:15"};

    public Parqueadero() {
        setTitle("Administración de Parqueadero");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Tabla
        String[] columnNames = {"Número", "Placa", "Tipo", "Hora Ingreso", "Valor a Pagar"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Agregar Vehículo");
        JButton showTwoWheelsButton = new JButton("Mostrar 2 ruedas");
        JButton showFourWheelsButton = new JButton("Mostrar 4 ruedas");
        JButton removeButton = new JButton("Eliminar Vehículo");
        JButton summaryButton = new JButton("Resumen Total");
        JButton exitButton = new JButton("Salir");

        buttonPanel.add(addButton);
        buttonPanel.add(showTwoWheelsButton);
        buttonPanel.add(showFourWheelsButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(summaryButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Cargar datos predefinidos
        cargarDatosPredefinidos();

        // Acción para agregar vehículo
        addButton.addActionListener(e -> agregarVehiculo());
        // Acción para mostrar vehículos de 2 ruedas
        showTwoWheelsButton.addActionListener(e -> mostrarDosRuedas());
        // Acción para mostrar vehículos de 4 ruedas
        showFourWheelsButton.addActionListener(e -> mostrarCuatroRuedas());
        // Acción para eliminar vehículo
        removeButton.addActionListener(e -> eliminarVehiculo());
        // Acción para mostrar resumen
        summaryButton.addActionListener(e -> mostrarResumen());
        // Acción para salir del programa
        exitButton.addActionListener(e -> System.exit(0));
    }

    // Método para cargar datos predefinidos
    private void cargarDatosPredefinidos() {
        for (int i = 0; i < placas.length; i++) {
            String placa = placas[i];
            String tipo = tipos[i];
            String horaIngreso = horasIngreso[i];

            int tarifa = obtenerTarifa(tipo);

            Vehicle vehiculo = new Vehicle(vehicleCounter++, placa, tipo, horaIngreso, tarifa);

            // Añadir a la lista general
            vehicleList.add(vehiculo);

            // Añadir a la pila correspondiente
            if (tipo.equals("Bicicleta/Ciclomotor") || tipo.equals("Motocicleta")) {
                twoWheelStack.push(vehiculo);
            } else {
                fourWheelStack.push(vehiculo);
            }
        }
        actualizarTabla();
    }

    // Método para obtener la tarifa según el tipo de vehículo
    private int obtenerTarifa(String tipo) {
        switch (tipo) {
            case "Bicicleta/Ciclomotor":
                return 20;
            case "Motocicleta":
                return 30;
            case "Carro":
                return 60;
            default:
                return 0;
        }
    }

    // Método para actualizar la tabla de vehículos
    private void actualizarTabla() {
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Vehicle vehiculo : vehicleList) {
            Object[] rowData = {vehiculo.getNumero(), vehiculo.getPlaca(), vehiculo.getTipo(),
                    vehiculo.getHoraIngreso(), vehiculo.getValorPagar()};
            tableModel.addRow(rowData);
        }
    }

    // Método para agregar un vehículo
    private void agregarVehiculo() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la placa del vehículo:");
        String[] opciones = {"Bicicleta/Ciclomotor", "Motocicleta", "Carro"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de vehículo:", "Tipo de Vehículo",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        String horaIngreso = JOptionPane.showInputDialog(this, "Ingrese la hora de ingreso (HH:mm):");

        int tarifa = obtenerTarifa(tipo);

        Vehicle vehiculo = new Vehicle(vehicleCounter++, placa, tipo, horaIngreso, tarifa);

        // Añadir a la lista general
        vehicleList.add(vehiculo);

        // Añadir a la pila correspondiente
        if (tipo.equals("Bicicleta/Ciclomotor") || tipo.equals("Motocicleta")) {
            twoWheelStack.push(vehiculo);
        } else {
            fourWheelStack.push(vehiculo);
        }

        actualizarTabla();
    }

    // Método para mostrar vehículos de 2 ruedas
    private void mostrarDosRuedas() {
        StringBuilder sb = new StringBuilder("Vehículos de 2 ruedas:\n");
        int total = 0;
        for (Vehicle vehiculo : twoWheelStack) {
            sb.append(vehiculo.toString()).append("\n");
            total += vehiculo.getValorPagar();
        }
        sb.append("Total a pagar: ").append(total).append(" COP");
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // Método para mostrar vehículos de 4 ruedas
    private void mostrarCuatroRuedas() {
        StringBuilder sb = new StringBuilder("Vehículos de 4 ruedas:\n");
        int total = 0;
        for (Vehicle vehiculo : fourWheelStack) {
            sb.append(vehiculo.toString()).append("\n");
            total += vehiculo.getValorPagar();
        }
        sb.append("Total a pagar: ").append(total).append(" COP");
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // Método para eliminar un vehículo
    private void eliminarVehiculo() {
        String numero = JOptionPane.showInputDialog(this, "Ingrese el número del vehículo a eliminar:");
        int num = Integer.parseInt(numero);

        // Buscar el vehículo por número
        Vehicle vehiculoAEliminar = null;
        for (Vehicle vehiculo : vehicleList) {
            if (vehiculo.getNumero() == num) {
                vehiculoAEliminar = vehiculo;
                break;
            }
        }

        if (vehiculoAEliminar != null) {
            String horaSalida = JOptionPane.showInputDialog(this, "Ingrese la hora de salida (HH:mm):");
            long minutos = calcularMinutos(vehiculoAEliminar.getHoraIngreso(), horaSalida);

            int totalPagar = (int) (minutos * vehiculoAEliminar.getTarifaPorMinuto());
            JOptionPane.showMessageDialog(this, "El total a pagar es: " + totalPagar + " COP");

            // Eliminar el vehículo de la lista
            vehicleList.remove(vehiculoAEliminar);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Vehículo no encontrado");
        }
    }

    // Método para calcular la cantidad de minutos entre la hora de ingreso y la hora de salida
    private long calcularMinutos(String horaIngreso, String horaSalida) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime tiempoIngreso = LocalTime.parse(horaIngreso, formatter);
        LocalTime tiempoSalida = LocalTime.parse(horaSalida, formatter);

        return Duration.between(tiempoIngreso, tiempoSalida).toMinutes();
    }

    // Método para mostrar resumen de vehículos y valor total
    private void mostrarResumen() {
        int totalVehiculos = vehicleList.size();
        int totalPagar = vehicleList.stream().mapToInt(Vehicle::getValorPagar).sum();
        JOptionPane.showMessageDialog(this, "Cantidad de vehículos: " + totalVehiculos +
                "\nValor total a pagar: " + totalPagar + " COP");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Parqueadero parqueadero = new Parqueadero();
            parqueadero.setVisible(true);
        });
    }
}

// Clase Vehículo
class Vehicle {
    private int numero;
    private String placa;
    private String tipo;
    private String horaIngreso;
    private int tarifaPorMinuto;

    public Vehicle(int numero, String placa, String tipo, String horaIngreso, int tarifaPorMinuto) {
        this.numero = numero;
        this.placa = placa;
        this.tipo = tipo;
        this.horaIngreso = horaIngreso;
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public int getNumero() {
        return numero;
    }

    public String getPlaca() {
        return placa;
    }

    public String getTipo() {
        return tipo;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public int getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public int getValorPagar() {
        return 0; // Placeholder
    }

    @Override
    public String toString() {
        return "Número: " + numero + ", Placa: " + placa + ", Tipo: " + tipo + ", Hora Ingreso: " + horaIngreso;
    }
}

