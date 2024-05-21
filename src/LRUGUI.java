import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LRUGUI extends JFrame {
    private LRU<Integer, String> LRU;
    private JTextField inputField;
    private JTextPane outputPane;
    private StyledDocument doc;
    private JLabel fallosLabel;
    private JLabel aciertosLabel;
    private int fallos;
    private int aciertos;
    private final int capacidad = 3;

    public LRUGUI() {
        LRU = new LRU<>(capacidad);
        fallos = 0;
        aciertos = 0;
        initUI();
    }

    private void initUI() {
        setTitle("Simulador de Cache LRU");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setBackground(Color.LIGHT_GRAY);
        inputField.setForeground(Color.BLACK);
        inputField.addActionListener(new InsertListener());

        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setBackground(Color.LIGHT_GRAY);
        outputPane.setForeground(Color.BLACK);
        doc = outputPane.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(outputPane);

        JButton insertButton = new JButton("Insertar");
        insertButton.addActionListener(new InsertListener());

        JButton resetButton = new JButton("Reiniciar");
        resetButton.addActionListener(new ResetListener());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(new JLabel("Ingresa un número: "), BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(insertButton, BorderLayout.EAST);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.add(inputPanel);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 2));
        fallosLabel = new JLabel("Fallos de página: 0");
        aciertosLabel = new JLabel("Aciertos de página: 0");
        statsPanel.add(fallosLabel);
        statsPanel.add(aciertosLabel);
        topPanel.add(statsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(resetButton, BorderLayout.SOUTH);
    }

    private class InsertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String entrada = inputField.getText();
            if (entrada.equalsIgnoreCase("salir")) {
                System.exit(0);
            }

            try {
                int numero = Integer.parseInt(entrada);
                if (LRU.obtener(numero) == null) {
                    if (LRU.memoria.size() >= capacidad) {
                        Integer claveMasAntigua = LRU.getClaveMasAntigua();
                        appendToPane("Fallo de pagina\n", Color.RED);
                        appendToPane("Reemplazando página: " + claveMasAntigua + "\n", Color.RED);
                    } else {
                        appendToPane("Fallo de pagina\n", Color.RED);
                    }
                    fallos++;
                    LRU.insertar(numero, "Página " + numero);
                } else {
                    appendToPane("Acierto de pagina\n", new Color(0, 100, 0)); // Verde oscuro
                    appendToPane("La página " + numero + " ya está en la caché.\n", new Color(0, 100, 0)); // Verde oscuro
                    aciertos++;
                }
                actualizarEstadisticas();
                mostrarCache(numero);
            } catch (NumberFormatException ex) {
                appendToPane("Por favor, ingresa un número válido.\n", Color.BLACK);
            }

            inputField.setText("");
        }
    }

    private class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LRU = new LRU<>(capacidad);
            fallos = 0;
            aciertos = 0;
            actualizarEstadisticas();
            outputPane.setText("");
            appendToPane("La caché ha sido reiniciada.\n", Color.BLACK);
        }
    }

    private void mostrarCache(int ultimoNumero) {
        StringBuilder estadoCache = new StringBuilder("Estado de la caché: ");
        for (Map.Entry<Integer, String> entrada : LRU.memoria.entrySet()) {
            if (entrada.getKey() == ultimoNumero) {
                appendToPane("[" + entrada.getKey() + "=" + entrada.getValue() + "] ", Color.BLUE);
            } else {
                appendToPane("[" + entrada.getKey() + "=" + entrada.getValue() + "] ", Color.BLACK);
            }
        }
        appendToPane("\n", Color.BLACK);
    }

    private void appendToPane(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        try {
            doc.insertString(doc.getLength(), msg, aset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void actualizarEstadisticas() {
        fallosLabel.setText("Fallos de página: " + fallos);
        aciertosLabel.setText("Aciertos de página: " + aciertos);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LRUGUI ex = new LRUGUI();
            ex.setVisible(true);
        });
    }
}
