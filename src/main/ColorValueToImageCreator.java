package main;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ColorValueToImageCreator {

    private static BufferedImage image;
    private static Graphics2D graphics2D;

    public static void main(String[] args) throws IOException {

        // make an image and create a graphics from it to so can be manipulated using AWT methods
        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        graphics2D = image.createGraphics();


        // input color codes
        Path rgbFile = Paths.get("Book1.csv");
        BufferedReader reader = Files.newBufferedReader(rgbFile);
        String rgbMatcherString = "\\d{1,3},\\d{1,3},\\d{1,3}";


        // read each line from the file
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {

            // fix current line formatting
            // remove extra spaces if any
            // remove # from hex value if any
            // remove ; from hex value if any
            currentLine = currentLine.trim();
            currentLine = currentLine.replaceAll("#", "");
            currentLine = currentLine.replaceAll(" ", "");
            currentLine = currentLine.replaceAll(";", "");


            // find if the value is RGB or 3 digit or 6 digit HEX value
            // then call proper method to convert string to rgb
            if (currentLine.matches(rgbMatcherString)) {

                createImageFromRGB(currentLine);

            } else if (currentLine.length() == 0) {
                continue;
            } else if (currentLine.length() == 3) {

                // if current line is 3 digit hex value, it converts it
                // to 6 digit hex value and call method so it can convert hex to RGB

                currentLine = currentLine.substring(0, 1) + currentLine.substring(0, 1)
                        + currentLine.substring(1, 2) + currentLine.substring(1, 2)
                        + currentLine.substring(2, 3) + currentLine.substring(2, 3);
                createImageFromHEX(currentLine);

            } else if (currentLine.length() == 5) {

                createImageFromHEX("0" + currentLine);

            } else {

                // if current line 6 digit hex value it call method so it can convert hex to RGB

                createImageFromHEX(currentLine);
            }

        }


    }

    //  convert HEX string to int RGB and then call method to create image
    private static void createImageFromHEX(String hexValue) {

        int red = Integer.parseInt(hexValue.substring(0, 2), 16);
        int green = Integer.parseInt(hexValue.substring(2, 4), 16);
        int blue = Integer.parseInt(hexValue.substring(4, 6), 16);

        createImage(red, green, blue);

    }

    //  convert RGB string to int RGB and then call method to create image
    private static void createImageFromRGB(String rgbValue){
        String[] rgb = rgbValue.split(",");

        int red = Integer.parseInt(rgb[0]);
        int green = Integer.parseInt(rgb[1]);
        int blue = Integer.parseInt(rgb[2]);

        createImage(red, green, blue);

    }

    private static void createImage(int red, int green, int blue){
        graphics2D.setPaint(new Color(red, green, blue));
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());

        try {
            ImageIO.write(image, "JPG", new File("images/" + red + "-" + green + "-" + blue + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
