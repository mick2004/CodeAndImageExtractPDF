//package com.memorynotfound.pdf.pdfbox;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import static javax.swing.UIManager.put;

public class ExtractImage {

    private static final String OUTPUT_DIR = "/Users/a.v.pratap.singh/Desktop/pdf/op/";
    private static final Map<Integer, Integer> blackListImageSizes = new HashMap<Integer, Integer>() {{

        put(92, 87);
        put(77, 103);
    }};

    public static void main(String[] args) throws Exception{

        File f = new File(OUTPUT_DIR);
        FileUtils.cleanDirectory(f); //clean out directory (this is optional -- but good know)
        FileUtils.forceDelete(f); //delete directory
        FileUtils.forceMkdir(f); //create directory

        try (final PDDocument document = PDDocument.load(new File("/Users/a.v.pratap.singh/Desktop/pdf/1.pdf"))){

            PDPageTree list = document.getPages();
            int pn = 1;
            for (PDPage page : list) {

                PDResources pdResources = page.getResources();
                int i = 1;
                for (COSName name : pdResources.getXObjectNames()) {
                    PDXObject o = pdResources.getXObject(name);
                    if (o instanceof PDImageXObject) {
                        PDImageXObject image = (PDImageXObject)o;

                        if(!(blackListImageSizes.containsKey(image.getWidth()) && blackListImageSizes.containsValue(image.getHeight()))) {
                            String filename = OUTPUT_DIR + "page-" + pn + "image-" + i + ".png";


                            ImageIO.write(image.getImage(), "png", new File(filename));
                            System.out.println("Page Number is " + pn + ".Image number is" + i + ".ImageHeight is "+image.getHeight());

                        }
                        i++;
                    }
                }
                pn++;

            }

        } catch (IOException e){
            System.out.println("Exception while trying to create pdf document - " + e);
        }
    }

}
