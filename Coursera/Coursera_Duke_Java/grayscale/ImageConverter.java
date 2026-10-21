import edu.duke.*;
import java.io.File;

public class ImageConverter {

    // Converts an image to grayscale using the average of RGB values
    public ImageResource makeGray(ImageResource inImage) {
        ImageResource outImage = new ImageResource(inImage.getWidth(), inImage.getHeight());
        for (Pixel pixel : outImage.pixels()) {
            Pixel inPixel = inImage.getPixel(pixel.getX(), pixel.getY());
            int average = (inPixel.getRed() + inPixel.getBlue() + inPixel.getGreen()) / 3;
            pixel.setRed(average);
            pixel.setGreen(average);
            pixel.setBlue(average);
        }
        return outImage;
    }

    // Converts an image to a photographic negative (inverted colors)
    public ImageResource makeInverted(ImageResource inImage) {
        ImageResource outImage = new ImageResource(inImage.getWidth(), inImage.getHeight());
        for (Pixel pixel : outImage.pixels()) {
            Pixel inPixel = inImage.getPixel(pixel.getX(), pixel.getY());
            pixel.setRed(255 - inPixel.getRed());
            pixel.setGreen(255 - inPixel.getGreen());
            pixel.setBlue(255 - inPixel.getBlue());
        }
        return outImage;
    }

    // Selects multiple images, converts them to grayscale, and saves them
    public void batchGrayscale() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource inImage = new ImageResource(f);
            ImageResource gray = makeGray(inImage);
            
            // Save the new image with "gray-" prepended to the original filename
            String newName = "gray-" + inImage.getFileName();
            gray.setFileName(newName);
            gray.save();
            gray.draw();
            System.out.println("Saved: " + newName);
        }
    }

    // Selects multiple images, inverts their colors, and saves them
    public void batchInvert() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource inImage = new ImageResource(f);
            ImageResource inverted = makeInverted(inImage);
            
            // Save the new image with "inverted-" prepended to the original filename
            String newName = "inverted-" + inImage.getFileName();
            inverted.setFileName(newName);
            inverted.save();
            inverted.draw();
            System.out.println("Saved: " + newName);
        }
    }
    
    // Converts an image using the (127 - original) mod 256 wrapping effect
    public ImageResource makeSolarized(ImageResource inImage) {
        ImageResource outImage = new ImageResource(inImage.getWidth(), inImage.getHeight());
        for (Pixel pixel : outImage.pixels()) {
            Pixel inPixel = inImage.getPixel(pixel.getX(), pixel.getY());
            
            // Math.floorMod handles negative results correctly, wrapping them to positive RGB values
            int newRed = Math.floorMod(127 - inPixel.getRed(), 256);
            int newGreen = Math.floorMod(127 - inPixel.getGreen(), 256);
            int newBlue = Math.floorMod(127 - inPixel.getBlue(), 256);
            
            pixel.setRed(newRed);
            pixel.setGreen(newGreen);
            pixel.setBlue(newBlue);
        }
        return outImage;
    }

    // Selects multiple images, applies the solarization effect, and saves them
    public void batchSolarize() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource inImage = new ImageResource(f);
            ImageResource solarized = makeSolarized(inImage);
            
            // Save the new image with "solarized-" prepended to the original filename
            String newName = "solarized-" + inImage.getFileName();
            solarized.setFileName(newName);
            solarized.save();
            solarized.draw();
            System.out.println("Saved: " + newName);
        }
    }

    // Main driver method
    public static void main(String[] args) {
        ImageConverter converter = new ImageConverter();
        
        System.out.println("Please select images to convert to grayscale:");
        converter.batchGrayscale();
        
        System.out.println("\nPlease select images to invert:");
        converter.batchInvert();
        
        System.out.println("\nPlease select images to solarize:");
        converter.batchSolarize();
    }
}
