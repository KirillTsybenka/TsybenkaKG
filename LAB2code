import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FiltersApp extends JFrame {
    private JLabel originalImageLabel;
    private JLabel filteredImageLabel;
    private Mat originalImage;
    private Mat filteredImage;

    static {
        // Загрузка библиотеки OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public FiltersApp() {
        setTitle("Image Filter Application");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель с полями и кнопками для изображений
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        // Поля для изображения
        originalImageLabel = new JLabel();
        filteredImageLabel = new JLabel();
        panel.add(new JScrollPane(originalImageLabel));
        panel.add(new JScrollPane(filteredImageLabel));

        // Кнопки для фильтров и выбора изображения
        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Загрузить изображение");
        JButton linearContrastButton = new JButton("Линейное контрастирование");
        JButton equalizeHistGrayButton = new JButton("Гистограмма (серый)");
        JButton equalizeHistHSVButton = new JButton("Гистограмма (HSV)");
        JButton equalizeHistRGBButton = new JButton("Гистограмма (RGB)");
        JButton equalizeHistLowPassButton = new JButton("Низкочастотные фильтры"); //

        // Добавляем кнопки на панель
        buttonPanel.add(loadButton);
        buttonPanel.add(linearContrastButton);
        buttonPanel.add(equalizeHistGrayButton);
        buttonPanel.add(equalizeHistHSVButton);
        buttonPanel.add(equalizeHistRGBButton);
        buttonPanel.add(equalizeHistLowPassButton);

        // Обработчик кнопки выравнивания гистограммы в RGB
        equalizeHistRGBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    filteredImage = applyHistogramEqualizationRGB(originalImage);
                    displayFilteredImage();
                }
            }
        });

        // Добавляем панели на главное окно
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчик кнопки загрузки изображения
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    originalImage = Imgcodecs.imread(file.getAbsolutePath());
                    if (originalImage.empty()) {
                        JOptionPane.showMessageDialog(null, "Не удалось загрузить изображение.");
                    } else {
                        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                        originalImageLabel.setIcon(icon);
                        filteredImageLabel.setIcon(null);  // Очищаем поле с фильтром
                    }
                }
            }
        });

        // Обработчик кнопки линейного контрастирования
        linearContrastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    filteredImage = applyLinearContrast(originalImage);
                    displayFilteredImage();
                }
            }
        });

        // Обработчик кнопки выравнивания гистограммы для серого
        equalizeHistGrayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    filteredImage = applyHistogramEqualizationGray(originalImage);
                    displayFilteredImage();
                }
            }
        });

        // Обработчик кнопки выравнивания гистограммы в HSV
        equalizeHistHSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    filteredImage = applyHistogramEqualizationHSV(originalImage);
                    displayFilteredImage();
                }
            }
        });

        // Обработчик кнопки низкочастотных фильтров
        equalizeHistLowPassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    filteredImage = LowPassFilter(originalImage);
                    displayFilteredImage();
                }
            }
        });
    }

    // Метод для линейного контрастирования
    private Mat applyLinearContrast(Mat img) {
        Mat gray = new Mat();
        if (img.channels() > 1) {
            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            gray = img;
        }

        Mat result = new Mat(gray.size(), gray.type());
        Core.MinMaxLocResult minMax = Core.minMaxLoc(gray);
        double minVal = minMax.minVal;
        double maxVal = minMax.maxVal;

        gray.convertTo(result, -1, 255.0 / (maxVal - minVal), -255.0 * minVal / (maxVal - minVal));
        return result;
    }

    // Метод для выравнивания гистограммы для черно-белого изображения
    private Mat applyHistogramEqualizationGray(Mat img) {
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Mat equalized = new Mat();
        Imgproc.equalizeHist(gray, equalized);
        return equalized;
    }

    // Метод для выравнивания гистограммы для цветного изображения (HSV)
    private Mat applyHistogramEqualizationHSV(Mat img) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        java.util.List<Mat> hsvChannels = new java.util.ArrayList<>();
        Core.split(hsv, hsvChannels);

        Imgproc.equalizeHist(hsvChannels.get(2), hsvChannels.get(2));
        Core.merge(hsvChannels, hsv);

        Mat result = new Mat();
        Imgproc.cvtColor(hsv, result, Imgproc.COLOR_HSV2BGR);
        return result;
    }

    // Метод для отображения фильтрованного изображения
    private void displayFilteredImage() {
        if (filteredImage != null) {
            ImageIcon icon = new ImageIcon(MatToBufferedImage(filteredImage));
            filteredImageLabel.setIcon(icon);
        }
    }

    // Преобразование Mat в BufferedImage для отображения в JLabel
    private Image MatToBufferedImage(Mat mat) {
        int type = (mat.channels() > 1) ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }

    // Метод для выравнивания гистограммы в пространстве RGB
    private Mat applyHistogramEqualizationRGB(Mat img) {
        java.util.List<Mat> rgbChannels = new java.util.ArrayList<>();
        Core.split(img, rgbChannels); // Разделяем на три канала: B, G, R

        // Применяем выравнивание гистограммы ко всем каналам
        Imgproc.equalizeHist(rgbChannels.get(0), rgbChannels.get(0)); // Синий канал
        Imgproc.equalizeHist(rgbChannels.get(1), rgbChannels.get(1)); // Зелёный канал
        Imgproc.equalizeHist(rgbChannels.get(2), rgbChannels.get(2)); // Красный канал

        // Объединяем каналы обратно
        Mat result = new Mat();
        Core.merge(rgbChannels, result);

        return result;
    }

    // Метод для выравнивания низкочастотных фильтров
    private Mat LowPassFilter(Mat mat) {
        // Средний фильтр
        Mat meanFilteredImage = new Mat();
        Imgproc.blur(mat, meanFilteredImage, new org.opencv.core.Size(5, 5)); // Размер ядра 5x5

        // Гауссовский фильтр
//        Mat gaussianFilteredImage = new Mat();
//        Imgproc.GaussianBlur(mat, gaussianFilteredImage, new org.opencv.core.Size(5, 5), 1.5); // Размер ядра 5x5, sigma = 1.5

        return meanFilteredImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FiltersApp().setVisible(true);
            }
        });
    }
}
