package com.compress.algocompressor.algorithms;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
public class VectorQuantization{
    public static int height, width, vectorHeight, vectorWidth, numVectors, compressedHeight, compressedWidth;
    public static float[][] originalImage;
    public static String[][] compressedImage;
    public static float[][] NewImage;
    public static ArrayList<float[][]> originalBlocks = new ArrayList<>();
    public static ArrayList<float[][]> AverageBlocks = new ArrayList<>();
    public static Map<float[][], ArrayList<float[][]>> nearestVectors = new HashMap<>();
    public static TreeMap<String, float[][]> codeBook = new TreeMap<>();

    public static float[][] readImage(String filePath) throws IOException{
        File file = new File(filePath);
        BufferedImage image;
        image = ImageIO.read(file);
        width = image.getWidth();
        height = image.getHeight();
        float[][] pixels = new float[height][width];
        int rgb;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb = image.getRGB(i, j);
                int red = (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue = (rgb & 0x000000ff);
                pixels[j][i] = Math.max(Math.max(red, green), blue);
            }
        }
        return pixels;
    }

    public static void writeImage(float[][] pixels, String filePath){
        File fileOut = new File(filePath);
        int w, h;
        h = pixels.length;
        w = pixels[0].length;
        BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        for(int x = 0; x < h; x++)
            for(int y = 0; y < w; y++){
                int value = 0xff000000 | ((int) pixels[x][y] << 16) | ((int) pixels[x][y] << 8) | (int) pixels[x][y];
                image2.setRGB(y, x, value);
            }
        try {
            ImageIO.write(image2, "jpg", fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void divideImage(){
        height = originalImage.length;
        width = originalImage[0].length;
        int h = height % vectorHeight;
        int w = width % vectorWidth;

        if((h != 0) || (w != 0)){
            height -= h;
            width -= w;
            float[][] copyImage = new float[height][width];
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    copyImage[i][j] = originalImage[i][j];
            originalImage = copyImage;
        }
        w = h = 0;
        for (int i = 0; i < height; i += vectorHeight) {
            for (int j = 0; j < width; j += vectorWidth) {
                float[][] vector = new float[vectorHeight][vectorWidth];
                h = i;
                for (int x = 0; x < vectorHeight; x++, h++) {
                    w = j;
                    for (int y = 0; y < vectorWidth; y++, w++) {
                        vector[x][y] = originalImage[h][w];
                    }
                }
                originalBlocks.add(vector);
            }
        }
    }
    public static float[][] getAverage(ArrayList<float[][]> blocks){
        // calculate average block
        float[][] average = new float[vectorHeight][vectorWidth];
        for (int i = 0; i < vectorHeight; i++)
            for (int j = 0; j < vectorWidth; j++){
                average[i][j] = 0;
                for (int x = 0; x < blocks.size(); x++)
                    // sum all blocks.
                    average[i][j] +=  blocks.get(x)[i][j];
                // divide on number of blocks
                average[i][j] /= (float) blocks.size();
            }
        return average;
    }
    public static void quantize(){
        // splitting average block into two blocks.
        int size = AverageBlocks.size();
        for (int i = 0; i < size; i++) {
            float[][] low = new float[vectorHeight][vectorWidth];
            float[][] high = new float[vectorHeight][vectorWidth];
            for (int x = 0; x < vectorHeight; x++) {
                for (int y = 0; y < vectorWidth; y++) {
                    float l = (float) Math.floor(AverageBlocks.get(i)[x][y]);
                    float h = l + 1;
                    low[x][y] = l;
                    high[x][y] = h;
                }
            }
            // adding new average blocks.
            AverageBlocks.add(low);
            AverageBlocks.add(high);
        }
        // remove previous average.
        while (size != 0){
            AverageBlocks.remove(0);
            size--;
        }
    }
    public static void getNearestVectors ()
    {
        for (int i = 0 ; i < originalBlocks.size() ; i++){
            ArrayList <Double> distances = new ArrayList<>();
            for (int j = 0; j < AverageBlocks.size(); j++) {
                double distance = 0 ;
                for (int x = 0; x < vectorHeight; x++) {
                    for (int y = 0; y < vectorWidth; y++) {
                        // calculate distance between average block and vectorBlock.
                        distance += Math.abs(((double)AverageBlocks.get(j)[x][y] - (double)originalBlocks.get(i)[x][y]));
                    }
                }
                // adding distance to distances array
                distances.add(distance);
            }
            // get minimum distance.
            double min = Collections.min(distances);
            // get index of minimum distance
            int index = distances.indexOf(min);
            // associate each block to one average block.
            if (nearestVectors.containsKey(AverageBlocks.get(index)))
                nearestVectors.get(AverageBlocks.get(index)).add(originalBlocks.get(i));
            else {
                // if average block doesn't exist  before , put  average block into nearestVectors map
                ArrayList <float[][]> temp = new ArrayList<>();
                temp.add(originalBlocks.get(i));
                nearestVectors.put(AverageBlocks.get(index), temp );
            }
        }
    }
    public static void compression(){
        divideImage();
        AverageBlocks.add(getAverage(originalBlocks));
        nearestVectors.put(AverageBlocks.get(0), originalBlocks);
        while (AverageBlocks.size() < numVectors){
            quantize();
            nearestVectors.clear();
            getNearestVectors();
            AverageBlocks.clear();
            for(float[][] vec : nearestVectors.keySet()){
                float[][] avg = getAverage(nearestVectors.get(vec));
                AverageBlocks.add(avg);
            }
        }
        while (true){
            int counter = 0;
            for (int i = 0; i < AverageBlocks.size(); i++) {
                float[][] averageBlock = new float[vectorHeight][vectorWidth];
                averageBlock = AverageBlocks.get(i);
                for (float[][] vector: nearestVectors.keySet()){
                    int c = 0;
                    for (int x = 0; x < vectorHeight; x++) {
                        for (int y = 0; y < vectorWidth; y++) {
                            if(vector[x][y] == averageBlock[x][y]) {
                                c++;
                            }
                        }
                    }
                    if(c == (vectorHeight * vectorWidth)) {
                        counter++;
                    }
                }
            }
            if(counter == AverageBlocks.size())
                break;

            nearestVectors.clear();
            getNearestVectors();
            AverageBlocks.clear();
            for(float[][] vec : nearestVectors.keySet())
                AverageBlocks.add(getAverage(nearestVectors.get(vec)));
        }
        encode();
    }
    public static void encode(){
        int numBits = (int) Math.ceil((Math.log(numVectors)/Math.log(2)));

        for (int i = 0; i < AverageBlocks.size(); i++) {
            String binary = Integer.toBinaryString(i);
            while (binary.length() != numBits)
                binary = "0" + binary;
            codeBook.put(binary, AverageBlocks.get(i));
        }
        compressedHeight = height /vectorHeight;
        compressedWidth = width/vectorWidth;
        compressedImage = new String[compressedHeight][compressedWidth];
        int index = 0;
        for (int i = 0; i < compressedHeight; i++) {
            for (int j = 0; j < compressedWidth; j++) {
                for(float[][] vec : nearestVectors.keySet()){
                    // if this block is associated to spilted average put the code in compressedImage.
                    if(nearestVectors.get(vec).contains(originalBlocks.get(index))){
                        compressedImage[i][j] = getCode(vec);
                    }
                }
                index++;
            }
        }
    }
    public static String getCode(float[][] codebookBlock){
        // search on map for codebookBlock and return the code that matched.
        for(String code : codeBook.keySet()){
            int counter = 0;
            for (int i = 0; i < vectorHeight; i++) {
                for (int j = 0; j < vectorWidth; j++) {
                    //
                    if(codebookBlock[i][j] == codeBook.get(code)[i][j])
                        counter++;
                }
            }
            if(counter == (vectorHeight * vectorWidth)) {
                return code;
            }
        }
        return null;
    }
    public static void decompression(){
        // get height and width of codebook block
        for(String code : codeBook.keySet()){
            vectorHeight = codeBook.get(code).length;
            vectorWidth = codeBook.get(code)[0].length;
            break;
        }
        // original height
        height = compressedHeight * vectorHeight;
        // original width
        width = compressedWidth * vectorWidth;
        // build new image with the height and width
        NewImage = new float[height][width];
        int h, w;
        for (int i = 0, a = 0; i < height; i += vectorHeight, a++) {
            for (int j = 0, b = 0; j < width; j += vectorWidth, b++) {
                h = i;
                // codebook block that matches with compressedImage code
                float[][] vector = codeBook.get(compressedImage[a][b]);
                for (int x = 0; x < vectorHeight; x++, h++) {
                    w = j;
                    for (int y = 0; y < vectorWidth; y++, w++) {
                        NewImage[h][w] = vector[x][y];
                    }
                }
            }
        }
    }
    public static void writeToFile(String fileName){
        String image = "";
        for (int i = 0; i < compressedHeight; i++) {
            for (int j = 0; j < compressedWidth; j++)
                image += compressedImage[i][j] + " ";
            image += "\n";
        }

        image = image.substring(0, image.length()-1);
        for(String code : codeBook.keySet()){
            image += code + "\n";
            String temp = code;
            temp.replaceAll("0", "1");
            for (int i = 0; i < vectorHeight; i++) {
                for (int j = 0; j < vectorWidth; j++) {
                    image += codeBook.get(code)[i][j] + " ";
                }
                image += "\n";
            }
        }

        image = image.substring(0, image.length()-2);

        File file = new File(fileName);
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(image);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readFromFile(String fileName) throws FileNotFoundException{
        compressedImage = new String[compressedHeight][compressedWidth];
        codeBook.clear();
        File file = new File(fileName);
        if(file.exists()){
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < compressedHeight; i++) {
                for (int j = 0; j < compressedWidth; j++) {
                    compressedImage[i][j] = scanner.next();
                }
            }
            while (scanner.hasNextLine()){
                String code = scanner.next();

                try {
                    float[][] vector = new float[vectorHeight][vectorWidth];
                    for (int i = 0; i < vectorHeight; i++) {
                        for (int j = 0; j < vectorWidth; j++) {
                            vector[i][j] = Float.parseFloat(scanner.next());
                        }
                    }
                    codeBook.put(code, vector);
                }catch(Exception e){
                    break;
                }
            }
        }
        else{
            System.out.println("File not found");
        }
    }

}
