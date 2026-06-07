import view.GraphPanel;
import model.Graph;
import io.GraphLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {

    private GraphPanel graphPanel;
    private Graph currentGraph;
    private GraphLoader loader;

    public Main() {
        currentGraph = new Graph();
        loader = new GraphLoader();

        setTitle("Wizualizacja Grafów");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        graphPanel = new GraphPanel();
        graphPanel.setGraph(currentGraph);
        add(graphPanel, BorderLayout.CENTER);

        setupMenuBar();
        setupToolBar();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Plik");

        JMenuItem itemLoadEdges = new JMenuItem("Wczytaj krawędzie (TXT)...");
        itemLoadEdges.addActionListener(e -> loadFile(true));

        JMenuItem itemLoadCoords = new JMenuItem("Wczytaj współrzędne (TXT)...");
        itemLoadCoords.addActionListener(e -> loadFile(false));

        JMenuItem itemExit = new JMenuItem("Zakończ");
        itemExit.addActionListener(e -> System.exit(0));

        menuFile.add(itemLoadEdges);
        menuFile.add(itemLoadCoords);
        menuFile.addSeparator();
        menuFile.add(itemExit);

        menuBar.add(menuFile);
        setJMenuBar(menuBar);
    }

    private void loadFile(boolean isEdges) {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(this);

        if (response == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                if (isEdges) {
                    loader.loadEdges(file.getAbsolutePath(), currentGraph);
                    JOptionPane.showMessageDialog(this, "Struktura krawędzi została poprawnie wczytana.");
                } else {
                    loader.loadCoordinates(file.getAbsolutePath(), currentGraph);
                    JOptionPane.showMessageDialog(this, "Współrzędne węzłów zostały zaktualizowane.");
                }
                graphPanel.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Wystąpił błąd podczas wczytywania: \n" + ex.getMessage(),
                        "Błąd odczytu", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupToolBar() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(new Color(230, 230, 230));

        JCheckBox labelsCheckBox = new JCheckBox("Pokaż etykiety", true);
        labelsCheckBox.setBackground(toolBar.getBackground());
        labelsCheckBox.addActionListener(e -> graphPanel.setShowLabels(labelsCheckBox.isSelected()));

        JButton resetButton = new JButton("Resetuj pozycje");
        resetButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Wczytaj ponownie plik współrzędnych, aby zresetować układ.");
        });

        toolBar.add(labelsCheckBox);
        toolBar.add(resetButton);

        add(toolBar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main window = new Main();
            window.setVisible(true);
        });
    }
}