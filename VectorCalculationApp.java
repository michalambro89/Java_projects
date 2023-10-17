import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VectorCalculationApp extends JFrame {
    private final JLabel resultLabel, crossProductLabel, sarrusResultLabel;

    public VectorCalculationApp() {
        super("Vector Calculation App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton calculateButton = new JButton("Calculate");

        resultLabel = new JLabel("");
        crossProductLabel = new JLabel("");
        sarrusResultLabel = new JLabel("");

        add(new JLabel("Enter coordinates for points A, B, and P:"));
        add(calculateButton);
        add(resultLabel);
        add(crossProductLabel);
        add(sarrusResultLabel);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputCoordinates();
            }
        });

        pack();
        setVisible(true);
    }

    public class CustomInputDialog extends JDialog {
        private JButton okButton, cancelButton;

        public CustomInputDialog(JFrame parent, String message) {
            super(parent, "Oops...", true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout());

            JPanel panel = new JPanel();
            JLabel label = new JLabel(message);
            okButton = new JButton("OK");
            cancelButton = new JButton("Cancel");

            panel.add(label);
            panel.add(okButton);
            panel.add(cancelButton);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            getContentPane().add(panel, BorderLayout.CENTER);
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    private void inputCoordinates() {

        double[] coordinatesA = inputCoordinates("Enter coordinates for point A (x y):");
        while(coordinatesA == null) {
            coordinatesA = inputCoordinates("Enter coordinates for point A (x y):");
        }
        double[] coordinatesB = inputCoordinates("Enter coordinates for point B (x y):");
        while(coordinatesB == null) {
            coordinatesB = inputCoordinates("Enter coordinates for point B (x y):");
        }
        double[] coordinatesP = inputCoordinates("Enter coordinates for point P (x y):");
        while(coordinatesP == null) {
            coordinatesP = inputCoordinates("Enter coordinates for point P (x y):");
        }

            double xA = coordinatesA[0];
            double yA = coordinatesA[1];
            double xB = coordinatesB[0];
            double yB = coordinatesB[1];
            double xP = coordinatesP[0];
            double yP = coordinatesP[1];

            double vectorAB_x = xB - xA;
            double vectorAB_y = yB - yA;
            double vectorAP_x = xP - xA;
            double vectorAP_y = yP - yA;

            double crossProduct = (vectorAB_x * vectorAP_y) - (vectorAB_y * vectorAP_x);

            double sarrusResult = (xA * yB) + (xB * yP) + (xP * yA) - (xP * yB) - (xB * yA) - (xA * yP);

            resultLabel.setText("");
            crossProductLabel.setText("Cross Product: " + crossProduct);
            sarrusResultLabel.setText("Sarrus Result: " + sarrusResult);

    }

    private double[] inputCoordinates(String message) {
        try {
            String input = JOptionPane.showInputDialog(null, message);

            if (input == null || input.isEmpty()) {
                String input2 = JOptionPane.showInputDialog(null, "You did not enter any coordinates. Please try again.");
                return null;
            }
            String[] coordinates = input.split(" ");
            if (coordinates.length != 2) {
                JOptionPane.showMessageDialog(this, "Invalid input format. Please try again.");
                return null;
            }

            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);

            return new double[]{x, y};
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Please enter numeric coordinates.");
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VectorCalculationApp();
            }
        });
    }
}