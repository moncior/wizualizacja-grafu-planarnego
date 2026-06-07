import model.Graph;
import io.GraphLoader;
import view.GraphPanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame {
    private GraphPanel graphPanel;
    private Graph graph;
    private GraphLoader graphLoader;

    public Main() {
        setTitle("Wizualizacja Grafów - JIMP2");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        graph = new Graph();
        graphLoader = new GraphLoader();

        graphPanel = new GraphPanel();
        graphPanel.setGraph(graph);
        add(graphPanel, BorderLayout.CENTER);

        setupMenuBar();
        setupToolBar();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Plik");

        JMenuItem loadEdgesItem = new JMenuItem("1. Wczytaj strukturę (data.txt)");
        JMenuItem loadCoordsItem = new JMenuItem("2a. Wczytaj pozycje (results.txt)");
        JMenuItem loadCoordsBinItem = new JMenuItem("2b. Wczytaj pozycje binarne (results.bin)");
        JMenuItem clearGraphItem = new JMenuItem("Wyczyść graf (Nowy)");
        JMenuItem saveImageItem = new JMenuItem("3. Eksportuj jako obraz (PNG)");
        JMenuItem exitItem = new JMenuItem("Wyjście");

        loadEdgesItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(".");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    graphLoader.loadEdges(fc.getSelectedFile().getAbsolutePath(), graph);
                    graphPanel.resetView();
                    JOptionPane.showMessageDialog(this, "Struktura grafu wczytana.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadCoordsItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(".");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    graphLoader.loadCoordinates(fc.getSelectedFile().getAbsolutePath(), graph);
                    graphPanel.resetView();
                    JOptionPane.showMessageDialog(this, "Współrzędne zaktualizowane z pliku tekstowego.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadCoordsBinItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(".");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    graphLoader.loadCoordinatesBinary(fc.getSelectedFile().getAbsolutePath(), graph);
                    graphPanel.resetView();
                    JOptionPane.showMessageDialog(this, "Współrzędne zaktualizowane z pliku binarnego (BIN).");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd BIN: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearGraphItem.addActionListener(e -> {
            graph = new Graph();
            graphPanel.setGraph(graph);
            graphPanel.resetView();
        });

        saveImageItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(".");
            fc.setSelectedFile(new File("graf.png"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith(".png")) {
                        file = new File(file.getAbsolutePath() + ".png");
                    }
                    graphPanel.saveToPNG(file.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Wizualizacja została pomyślnie zapisana do pliku PNG!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd generowania obrazu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(loadEdgesItem);
        fileMenu.addSeparator();
        fileMenu.add(loadCoordsItem);
        fileMenu.add(loadCoordsBinItem);
        fileMenu.addSeparator();
        fileMenu.add(clearGraphItem);
        fileMenu.addSeparator();
        fileMenu.add(saveImageItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void setupToolBar() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(250, 900));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Panel Narzędziowy"));
        controlPanel.setLayout(new GridLayout(10, 1, 5, 5));

        JCheckBox showNodes = new JCheckBox("Pokaż pomocnicze współrzędne", true);
        showNodes.addActionListener(e -> graphPanel.setShowLabels(showNodes.isSelected()));
        controlPanel.add(showNodes);

        JButton centerButton = new JButton("Resetuj widok (Centruj)");
        centerButton.addActionListener(e -> graphPanel.resetView());
        controlPanel.add(centerButton);

        JButton clearButton = new JButton("Wyczyść płótno");
        clearButton.addActionListener(e -> {
            graph = new Graph();
            graphPanel.setGraph(graph);
            graphPanel.resetView();
        });
        controlPanel.add(clearButton);

        add(controlPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}