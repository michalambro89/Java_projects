import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import static javax.swing.JOptionPane.showMessageDialog;

class LineDrawingPanel extends JPanel {
    private ArrayList<Line2D> lines = new ArrayList<>();
    private double Sxy;
    private double height;
    private double width;
    static public double xMin;
    static public double xMax;
    static public double yMin;
    static public double yMax;

    public LineDrawingPanel(int provisional_width, int provisional_height) {
        super();
        setPreferredSize(new Dimension(provisional_width, provisional_height));
        setBackground(Color.WHITE);
        this.width = provisional_width;
        this.height = provisional_height;
    }

    public void coordinate_system_scale() {
        double xRange = xMax - xMin;
        double yRange = yMax - yMin;

        if (xRange > yRange) {
            Sxy = width / xRange;
        } else {
            Sxy = height / yRange;
        }
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        coordinate_system_scale();
        double scaledX1 = ((y1 - yMin) * Sxy);
        double scaledY1 = height - (x1 - xMin) * Sxy;
        double scaledX2 = (y2 - yMin) * Sxy;
        double scaledY2 = height - (x2 - xMin) * Sxy;
        System.out.println(scaledX1 + " " + scaledY1 + " " + scaledX2 + " " + scaledY2);
        Line2D line = new Line2D.Double(scaledX1, scaledY1, scaledX2, scaledY2);
        lines.add(line);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Line2D line : lines) {
            g2d.setColor(Color.BLACK);
            g2d.draw(line);
        }
    }
}

public class IntersectingLinesApp extends JFrame {
    private static JTextField xa = new JTextField("", 7);
    private static JTextField ya = new JTextField("", 7);
    private static JTextField xb = new JTextField("", 7);
    private static JTextField yb = new JTextField("", 7);
    private static JTextField xc = new JTextField("", 7);
    private static JTextField yc = new JTextField("", 7);
    private static JTextField xd = new JTextField("", 7);
    private static JTextField yd = new JTextField("", 7);
    private static JTextField xp = new JTextField("", 10);
    private static JTextField yp = new JTextField("", 10);
    private static String file_name;
    private static ArrayList<Double> d_coordinates = new ArrayList<>();
    private static LineDrawingPanel drawing_panel;

    public IntersectingLinesApp() {
        super("Intersecting Lines App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLayout(new BorderLayout());

        Color light_yellow = new Color(255, 255, 153);
        Color lighter_yellow = new Color(255, 255, 204);
        Color light_green = new Color(204, 255, 153);

        drawing_panel = new LineDrawingPanel(750, 750);
        JButton calculate_button = new JButton("Oblicz");
        calculate_button.setPreferredSize(new Dimension(250, 50));
        JButton load_data_button = new JButton("Wczytaj dane z pliku");
        JButton save_data_button = new JButton("Zapisz raport do pliku tekstowego");
        JButton clear_button = new JButton("Wyczyść");
        JButton change_line_color_1 = new JButton("Zmień kolor linii 1");
        JButton change_line_color_2 = new JButton("Zmień kolor linii 2");
        JButton draw_button = new JButton("Rysuj / Odśwież");
        draw_button.setPreferredSize(new Dimension(250, 50));
        JButton load_image_button = new JButton("Wczytaj obraz z pliku");
        JButton save_image_button = new JButton("Zapisz obraz");

        JPanel main_panel = new JPanel();
        JPanel data_panel = new JPanel();
        JPanel set_drawing_panel = new JPanel();
        JPanel input_fields_panel = new JPanel();
        JPanel output_fields_panel = new JPanel();

        main_panel.add(data_panel);
        main_panel.add(drawing_panel);
        main_panel.setBackground(lighter_yellow);

        data_panel.add(input_fields_panel);
        data_panel.add(calculate_button);
        data_panel.add(load_data_button);
        data_panel.add(output_fields_panel);
        data_panel.add(save_data_button);
        data_panel.add(clear_button);
        data_panel.setBackground(light_yellow);
        data_panel.setPreferredSize(new Dimension(300, 100));
        data_panel.add(set_drawing_panel);

        set_drawing_panel.add(draw_button);
        set_drawing_panel.add(change_line_color_1);
        set_drawing_panel.add(change_line_color_2);
        set_drawing_panel.add(load_image_button);
        set_drawing_panel.add(save_image_button);
        set_drawing_panel.setBackground(light_green);
        set_drawing_panel.setPreferredSize(new Dimension(300, getMaximumSize().height));

        input_fields_panel.setLayout(new GridLayout(4, 2));
        input_fields_panel.add(new JLabel("Xa:", SwingConstants.CENTER));
        input_fields_panel.add(xa);
        input_fields_panel.add(new JLabel("Ya:", SwingConstants.CENTER));
        input_fields_panel.add(ya);
        input_fields_panel.add(new JLabel("Xb:", SwingConstants.CENTER));
        input_fields_panel.add(xb);
        input_fields_panel.add(new JLabel("Yb:", SwingConstants.CENTER));
        input_fields_panel.add(yb);
        input_fields_panel.add(new JLabel("Xc:", SwingConstants.CENTER));
        input_fields_panel.add(xc);
        input_fields_panel.add(new JLabel("Yc:", SwingConstants.CENTER));
        input_fields_panel.add(yc);
        input_fields_panel.add(new JLabel("Xd:", SwingConstants.CENTER));
        input_fields_panel.add(xd);
        input_fields_panel.add(new JLabel("Yd:", SwingConstants.CENTER));
        input_fields_panel.add(yd);

        output_fields_panel.add(new JLabel("Xp:", SwingConstants.CENTER));
        output_fields_panel.add(xp);
        output_fields_panel.add(new JLabel("Yp:", SwingConstants.CENTER));
        output_fields_panel.add(yp);

        add(main_panel, BorderLayout.CENTER);
        add(data_panel, BorderLayout.WEST);

        draw_button.addActionListener(e -> {
            draw_lines();
        });

        calculate_button.addActionListener(e -> {
            input_coordinates();
        });

        load_data_button.addActionListener(e -> {
            input_file_name();
            file_input_cooridinates();
        });

        save_data_button.addActionListener(e -> saveResultToFile());

        setVisible(true);
    }

    static public void draw_lines() {
        double xa = Double.parseDouble(IntersectingLinesApp.xa.getText());
        double ya = Double.parseDouble(IntersectingLinesApp.ya.getText());
        double xb = Double.parseDouble(IntersectingLinesApp.xb.getText());
        double yb = Double.parseDouble(IntersectingLinesApp.yb.getText());
        double xc = Double.parseDouble(IntersectingLinesApp.xc.getText());
        double yc = Double.parseDouble(IntersectingLinesApp.yc.getText());
        double xd = Double.parseDouble(IntersectingLinesApp.xd.getText());
        double yd = Double.parseDouble(IntersectingLinesApp.yd.getText());
        double xp = Double.parseDouble(IntersectingLinesApp.xp.getText());
        double yp = Double.parseDouble(IntersectingLinesApp.yp.getText());

        drawing_panel.drawLine(xa, ya, xb, yb);
        drawing_panel.drawLine(xc, yc, xd, yd);
        drawing_panel.drawLine(xp, yp, xp, yp);
        drawing_panel.repaint();
    }

    static public void input_file_name() {
        file_name = JOptionPane.showInputDialog(null, "Podaj nazwę pliku: ", "Wczytywanie danych z pliku", JOptionPane.QUESTION_MESSAGE);
    }

    static public void file_input_cooridinates() {
        try {
            File file = new File(file_name);
            Scanner file_scanner = new Scanner(file);
            String[] s_coordinates;
            int counter = 0;

            while(file_scanner.hasNextLine()) {
                counter++;
                if(counter <= 4) {
                    String line = file_scanner.nextLine();
                    s_coordinates = line.split(" ");
                    if(s_coordinates.length == 2) {
                        if (is_valid(s_coordinates[0]) && is_valid(s_coordinates[1])) {
                            d_coordinates.add(Double.parseDouble(s_coordinates[0]));
                            d_coordinates.add(Double.parseDouble(s_coordinates[1]));
                        }
                        else {
                            showMessageDialog(null, "W pliku znajdują się niepoprawne dane!" + "\n" + "Sprawdź poprawność danych wejściowych.", "Błąd danych wejściowych", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                    } else {
                        showMessageDialog(null, "W pliku znajdują się niepoprawne dane!" + "\n" + "Sprawdź poprawność danych wejściowych.", "Błąd danych wejściowych", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                } else {
                    showMessageDialog(null, "W pliku znajduje się zbyt wiele danych!" + "\n" + "Sprawdź wielkość danych wejściowych.", "Błąd danych wejściowych", JOptionPane.ERROR_MESSAGE);
                    d_coordinates.clear();
                    break;
                }
            }
        } catch (Exception e) {
            showMessageDialog(null, "Nie znaleziono pliku o podanej nazwie!" + "\n" + "Spróbuj ponownie podać nazwę pliku.", "Błąd wczytywania danych", JOptionPane.ERROR_MESSAGE);
        }

        if(!d_coordinates.isEmpty()) {
            for(int i = 0; i < 8; i++) {
                if(i == 0)
                    xa.setText(String.valueOf(d_coordinates.get(i)));
                else if(i == 1)
                    ya.setText(String.valueOf(d_coordinates.get(i)));
                else if(i == 2)
                    xb.setText(String.valueOf(d_coordinates.get(i)));
                else if(i == 3)
                    yb.setText(String.valueOf(d_coordinates.get(i)));
                else if(i == 4)
                    xc.setText(String.valueOf(d_coordinates.get(i)));
                else if(i == 5)
                    yc.setText(String.valueOf(d_coordinates.get(i)));
                else if(i == 6)
                    xd.setText(String.valueOf(d_coordinates.get(i)));
                else
                    yd.setText(String.valueOf(d_coordinates.get(i)));
            }
        }
        d_coordinates.clear();
    }

    static public void input_coordinates() {
        String[] s_coordinates = {xa.getText(), ya.getText(), xb.getText(), yb.getText(), xc.getText(), yc.getText(), xd.getText(), yd.getText()};
        Map<String, Double> coordinates = new HashMap<>();
        Map<Integer, String> c_dic = new HashMap<>();

        c_dic.put(1, "Xa");
        c_dic.put(2, "Ya");
        c_dic.put(3, "Xb");
        c_dic.put(4, "Yb");
        c_dic.put(5, "Xc");
        c_dic.put(6, "Yc");
        c_dic.put(7, "Xd");
        c_dic.put(8, "Yd");

        boolean can_get_coordinates = true;

        if(is_full(s_coordinates)) {
            for(int i = 0; i < s_coordinates.length; i++) {
                if(is_valid(s_coordinates[i]))
                    coordinates.put(c_dic.get(i + 1), Double.parseDouble(s_coordinates[i]));
                else {
                    showMessageDialog(null, "Wprowadzono niepoprawne dane w polu " + (c_dic.get(i + 1)) + "!" + "\n" + "Wprowadź liczby zmiennoprzecinkowe.", "Błąd danych wejściowych", JOptionPane.ERROR_MESSAGE);
                    can_get_coordinates = false;
                    break;
                }
            }

            if(can_get_coordinates) {
                double xa = coordinates.get("Xa");
                double ya = coordinates.get("Ya");
                double xb = coordinates.get("Xb");
                double yb = coordinates.get("Yb");
                double xc = coordinates.get("Xc");
                double yc = coordinates.get("Yc");
                double xd = coordinates.get("Xd");
                double yd = coordinates.get("Yd");

                double t1 = ((xc - xa) * (yd - yc) - (yc - ya) * (xd - xc)) / ((xb - xa) * (yd - yc) - (yb - ya) * (xd - xc));
                double t2 = ((xc - xa) * (yb - ya) - (yc - ya) * (xb - xa)) / ((xb - xa) * (yd - yc) - (yb - ya) * (xd - xc));

                double xp = xa + t1 * (xb - xa);
                double yp = ya + t1 * (yb - ya);

                IntersectingLinesApp.xp.setText(String.valueOf(xp));
                IntersectingLinesApp.yp.setText(String.valueOf(yp));

                double x_temp_max = Double.NEGATIVE_INFINITY;
                double y_temp_max = Double.NEGATIVE_INFINITY;
                double x_temp_min = Double.POSITIVE_INFINITY;
                double y_temp_min = Double.POSITIVE_INFINITY;

                for (double i : coordinates.values()) {
                    if (i % 2 == 0) {
                        x_temp_max = Math.max(x_temp_max, i);
                        x_temp_min = Math.min(x_temp_min, i);
                    } else {
                        y_temp_max = Math.max(y_temp_max, i);
                        y_temp_min = Math.min(y_temp_min, i);
                    }
                }

                LineDrawingPanel.xMin = x_temp_min;
                LineDrawingPanel.xMax = x_temp_max;
                LineDrawingPanel.yMin = y_temp_min;
                LineDrawingPanel.yMax = y_temp_max;

                if(t1 >= 0 && t1 <= 1 && t2 >= 0 && t2 <= 1) {
                    showMessageDialog(null, "Podane odcinki przecinają się!", "Przecięcie", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showMessageDialog(null, "Podane odcinki nie przecinają się!", "Brak przecięcia", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            showMessageDialog(null, "Wprowadź wszystkie dane!", "Błąd danych wejściowych", JOptionPane.ERROR_MESSAGE);
        }
    }

    static public boolean is_full(String[] a) {
        for (String s : a) {
            if (s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    static public boolean is_valid(String a) {
        if(a == null || a.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(a);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    static public void saveResultToFile() {
        try {
            String xpValue = xp.getText();
            String ypValue = yp.getText();

            if (!xpValue.isEmpty() && !ypValue.isEmpty()) {
                File outputFile = new File("raport.txt");
                FileWriter fileWriter = new FileWriter(outputFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                String resultLine = xpValue + " " + ypValue;
                bufferedWriter.write(resultLine);

                bufferedWriter.close();
                fileWriter.close();
                showMessageDialog(null, "Wynik zapisano w pliku 'raport.txt'.", "Zapisano", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessageDialog(null, "Pola 'Xp' i 'Yp' nie mogą być puste.", "Błąd zapisu", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            showMessageDialog(null, "Wystąpił błąd podczas zapisywania danych.", "Błąd zapisu", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IntersectingLinesApp();
        });
    }
}

