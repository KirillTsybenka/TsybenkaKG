package KG1lab;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

class Rgb {
    int red, green, blue;

    public Rgb(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}

class Cmyk {
    double c, m, y, k;

    public Cmyk(double c, double m, double y, double k) {
        this.c = c;
        this.m = m;
        this.y = y;
        this.k = k;
    }
}

class Hls {
    double h, l, s;

    public Hls(double h, double l, double s) {
        this.h = h;
        this.l = l;
        this.s = s;
    }
}

public class ColorConverter extends JFrame {
    private JSlider redSlider, greenSlider, blueSlider, cyanSlider, magentaSlider, yellowSlider, blackSlider, hueSlider, lightnessSlider, saturationSlider;
    private JTextField redField, greenField, blueField, cyanField, magentaField, yellowField, blackField, hueField, lightnessField, saturationField;
    private JPanel rgbPanel, cmykPanel, hlsPanel, colorDisplayPanel;
    private Rgb rgb = new Rgb(255, 0, 0);
    private Cmyk cmyk = rgbToCmyk(rgb.red, rgb.green, rgb.blue);
    private Hls hls = rgbToHls(rgb.red, rgb.green, rgb.blue);
    private boolean updating = false;

    public ColorConverter() {
        setTitle("Color Converter");
        setSize(600, 800);
        setLayout(new GridLayout(4, 1));

        rgbPanel = new JPanel();
        cmykPanel = new JPanel();
        hlsPanel = new JPanel();
        colorDisplayPanel = new JPanel();

        setupRgbControls();
        setupCmykControls();
        setupHlsControls();
        setupColorDisplay();

        add(rgbPanel);
        add(cmykPanel);
        add(hlsPanel);
        add(colorDisplayPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupRgbControls() {
        rgbPanel.setLayout(new GridLayout(3, 3));

        redSlider = createSlider(0, 255, rgb.red, e -> updateFromRgb());
        greenSlider = createSlider(0, 255, rgb.green, e -> updateFromRgb());
        blueSlider = createSlider(0, 255, rgb.blue, e -> updateFromRgb());

        redField = createTextField(rgb.red, e -> updateFromRgb());
        greenField = createTextField(rgb.green, e -> updateFromRgb());
        blueField = createTextField(rgb.blue, e -> updateFromRgb());

        rgbPanel.add(new JLabel("Red"));
        rgbPanel.add(redSlider);
        rgbPanel.add(redField);

        rgbPanel.add(new JLabel("Green"));
        rgbPanel.add(greenSlider);
        rgbPanel.add(greenField);

        rgbPanel.add(new JLabel("Blue"));
        rgbPanel.add(blueSlider);
        rgbPanel.add(blueField);
    }

    private void setupCmykControls() {
        cmykPanel.setLayout(new GridLayout(4, 3));

        cyanSlider = createSlider(0, 100, (int) (cmyk.c * 100), e -> updateFromCmyk());
        magentaSlider = createSlider(0, 100, (int) (cmyk.m * 100), e -> updateFromCmyk());
        yellowSlider = createSlider(0, 100, (int) (cmyk.y * 100), e -> updateFromCmyk());
        blackSlider = createSlider(0, 100, (int) (cmyk.k * 100), e -> updateFromCmyk());

        cyanField = createTextField((int) (cmyk.c * 100), e -> updateFromCmyk());
        magentaField = createTextField((int) (cmyk.m * 100), e -> updateFromCmyk());
        yellowField = createTextField((int) (cmyk.y * 100), e -> updateFromCmyk());
        blackField = createTextField((int) (cmyk.k * 100), e -> updateFromCmyk());

        cmykPanel.add(new JLabel("Cyan"));
        cmykPanel.add(cyanSlider);
        cmykPanel.add(cyanField);

        cmykPanel.add(new JLabel("Magenta"));
        cmykPanel.add(magentaSlider);
        cmykPanel.add(magentaField);

        cmykPanel.add(new JLabel("Yellow"));
        cmykPanel.add(yellowSlider);
        cmykPanel.add(yellowField);

        cmykPanel.add(new JLabel("Black"));
        cmykPanel.add(blackSlider);
        cmykPanel.add(blackField);
    }

    private void setupHlsControls() {
        hlsPanel.setLayout(new GridLayout(3, 3));

        hueSlider = createSlider(0, 360, (int) hls.h, e -> updateFromHls());
        lightnessSlider = createSlider(0, 100, (int) (hls.l * 100), e -> updateFromHls());
        saturationSlider = createSlider(0, 100, (int) (hls.s * 100), e -> updateFromHls());

        hueField = createTextField((int) hls.h, e -> updateFromHls());
        lightnessField = createTextField((int) (hls.l * 100), e -> updateFromHls());
        saturationField = createTextField((int) (hls.s * 100), e -> updateFromHls());

        hlsPanel.add(new JLabel("Hue"));
        hlsPanel.add(hueSlider);
        hlsPanel.add(hueField);

        hlsPanel.add(new JLabel("Lightness"));
        hlsPanel.add(lightnessSlider);
        hlsPanel.add(lightnessField);

        hlsPanel.add(new JLabel("Saturation"));
        hlsPanel.add(saturationSlider);
        hlsPanel.add(saturationField);
    }

    private void setupColorDisplay() {
        colorDisplayPanel.setLayout(new GridLayout(1, 3));
        updateColorDisplays();
    }

    private JSlider createSlider(int min, int max, int value, ChangeListener listener) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.addChangeListener(listener);
        return slider;
    }

    private JTextField createTextField(int value, ActionListener listener) {
        JTextField textField = new JTextField(String.valueOf(value), 5);
        textField.addActionListener(listener);
        return textField;
    }

    private void updateFromRgb() {
        if (updating) return;
        updating = true;

        rgb.red = redSlider.getValue();
        rgb.green = greenSlider.getValue();
        rgb.blue = blueSlider.getValue();

        redField.setText(String.valueOf(rgb.red));
        greenField.setText(String.valueOf(rgb.green));
        blueField.setText(String.valueOf(rgb.blue));

        cmyk = rgbToCmyk(rgb.red, rgb.green, rgb.blue);
        hls = rgbToHls(rgb.red, rgb.green, rgb.blue);

        updateSlidersAndFields();
        updateColorDisplays();

        updating = false;
    }

    private void updateFromCmyk() {
        if (updating) return;
        updating = true;

        cmyk.c = cyanSlider.getValue() / 100.0;
        cmyk.m = magentaSlider.getValue() / 100.0;
        cmyk.y = yellowSlider.getValue() / 100.0;
        cmyk.k = blackSlider.getValue() / 100.0;

        cyanField.setText(String.valueOf((int) (cmyk.c * 100)));
        magentaField.setText(String.valueOf((int) (cmyk.m * 100)));
        yellowField.setText(String.valueOf((int) (cmyk.y * 100)));
        blackField.setText(String.valueOf((int) (cmyk.k * 100)));

        rgb = cmykToRgb(cmyk.c, cmyk.m, cmyk.y, cmyk.k);
        hls = rgbToHls(rgb.red, rgb.green, rgb.blue);

        updateSlidersAndFields();
        updateColorDisplays();

        updating = false;
    }

    private void updateFromHls() {
        if (updating) return;
        updating = true;

        hls.h = hueSlider.getValue();
        hls.l = lightnessSlider.getValue() / 100.0;
        hls.s = saturationSlider.getValue() / 100.0;

        hueField.setText(String.valueOf((int) hls.h));
        lightnessField.setText(String.valueOf((int) (hls.l * 100)));
        saturationField.setText(String.valueOf((int) (hls.s * 100)));

        rgb = hlsToRgb(hls.h, hls.l, hls.s);
        cmyk = rgbToCmyk(rgb.red, rgb.green, rgb.blue);

        updateSlidersAndFields();
        updateColorDisplays();

        updating = false;
    }

    private void updateSlidersAndFields() {
        redSlider.setValue(rgb.red);
        greenSlider.setValue(rgb.green);
        blueSlider.setValue(rgb.blue);

        cyanSlider.setValue((int) (cmyk.c * 100));
        magentaSlider.setValue((int) (cmyk.m * 100));
        yellowSlider.setValue((int) (cmyk.y * 100));
        blackSlider.setValue((int) (cmyk.k * 100));

        hueSlider.setValue((int) hls.h);
        lightnessSlider.setValue((int) (hls.l * 100));
        saturationSlider.setValue((int) (hls.s * 100));

        redField.setText(String.valueOf(rgb.red));
        greenField.setText(String.valueOf(rgb.green));
        blueField.setText(String.valueOf(rgb.blue));

        cyanField.setText(String.valueOf((int) (cmyk.c * 100)));
        magentaField.setText(String.valueOf((int) (cmyk.m * 100)));
        yellowField.setText(String.valueOf((int) (cmyk.y * 100)));
        blackField.setText(String.valueOf((int) (cmyk.k * 100)));

        hueField.setText(String.valueOf((int) hls.h));
        lightnessField.setText(String.valueOf((int) (hls.l * 100)));
        saturationField.setText(String.valueOf((int) (hls.s * 100)));
    }

    private void updateColorDisplays() {
        colorDisplayPanel.setBackground(new Color(rgb.red, rgb.green, rgb.blue));
    }

    private Cmyk rgbToCmyk(int r, int g, int b) {
        double rNorm = r / 255.0;
        double gNorm = g / 255.0;
        double bNorm = b / 255.0;

        double k = 1 - Math.max(Math.max(rNorm, gNorm), bNorm);
        double c = (1 - rNorm - k) / (1 - k);
        double m = (1 - gNorm - k) / (1 - k);
        double y = (1 - bNorm - k) / (1 - k);

        return new Cmyk(c, m, y, k);
    }

    private Rgb cmykToRgb(double c, double m, double y, double k) {
        int r = (int) ((1 - c) * (1 - k) * 255);
        int g = (int) ((1 - m) * (1 - k) * 255);
        int b = (int) ((1 - y) * (1 - k) * 255);
        return new Rgb(r, g, b);
    }

    private Hls rgbToHls(int r, int g, int b) {
        double rNorm = r / 255.0;
        double gNorm = g / 255.0;
        double bNorm = b / 255.0;

        double max = Math.max(rNorm, Math.max(gNorm, bNorm));
        double min = Math.min(rNorm, Math.max(gNorm, bNorm));
        double l = (max + min) / 2;
        double s, h;

        if (max == min) {
            h = s = 0;
        } else {
            double delta = max - min;

            s = l > 0.5 ? delta / (2.0 - max - min) : delta / (max + min);

            if (max == rNorm) {
                h = (gNorm - bNorm) / delta + (gNorm < bNorm ? 6 : 0);
            } else if (max == gNorm) {
                h = (bNorm - rNorm) / delta + 2;
            } else {
                h = (rNorm - gNorm) / delta + 4;
            }

            h /= 6;
        }

        return new Hls(h * 360, l, s);
    }

    private Rgb hlsToRgb(double h, double l, double s) {
        h /= 360;
        double r, g, b;

        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;

            r = hueToRgb(p, q, h + 1.0 / 3);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1.0 / 3);
        }

        return new Rgb((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    private double hueToRgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0 / 6) return p + (q - p) * 6 * t;
        if (t < 1.0 / 3) return q;
        if (t < 2.0 / 3) return p + (q - p) * (2.0 / 3 - t) * 6;
        return p;
    }

    public static void main(String[] args) {
        new ColorConverter();
    }
}
