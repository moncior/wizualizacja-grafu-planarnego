import view.GraphPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private GraphPlaner graphPlaner;

    public Main(){
        setTitle("Wizualizacja Grafów - JIMP2");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);


        setupMenuBar();
        setupToolBar();
    }
}