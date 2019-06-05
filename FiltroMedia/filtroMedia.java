

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class filtroMedia {

    public static double arredondar(double x){
        int aux = (int)x;
        double count = x-aux;
        if(count<=0.5)
            return Math.floor(x);
        return  Math.ceil(x);
    }

    public static int[][] FiltMedia(int[][] m_img1,int[][] m_result, int[][] mask, int width,int height){
        int x00,x01,x02,x10,x11,x12,x20,x21,x22;
        double soma, gray;
        for(int i=1; i<height-1; i++){
                for(int j=1; j<width-1; j++){
                    x00 =(m_img1[i-1][j-1])&0xff;
                    x01 =(m_img1[i][j-1])&0xff;
                    x02 =(m_img1[i+1][j-1])&0xff;                   
                    x10 =(m_img1[i-1][j])&0xff;
                    x11 =(m_img1[i][j])&0xff;
                    x12 =(m_img1[i+1][j])&0xff;
                    x20 =(m_img1[i-1][j+1])&0xff;
                    x21 =(m_img1[i][j+1])&0xff;
                    x22 =(m_img1[i+1][j+1])&0xff;
                    
                    soma = (mask[0][0])*x00 + (mask[0][1])*x01 + (mask[0][2]) *x02 +
                           (mask[1][0])*x10 + (mask[1][1])*x11 + (mask[1][2]) *x12 +
                           (mask[2][0])*x20 + (mask[2][1])*x21 + (mask[2][2]) *x22;
                    gray = arredondar(soma/9);
                    
                    m_result[i][j] =   0xff000000 | ((((int)gray)<<16)&0xff0000) |
                                    ((((int)gray)<<8)&0xff00) | ((int)gray)&0xff ;
                }
        }
        return m_result;
    }
    
    public static int[][] getMatrix(BufferedImage input){
             int height = input.getHeight(); 
             int width = input.getWidth();
             int [][] array = new int[height][width];
             for(int i=0; i<height; i++)
                 for(int j=0; j<width; j++)
                     array[i][j] = input.getRGB(j, i);
             return array;
     }
    public static int[] toArray(int[][] mO){ // mO: Matriz Original
        int[] retorno = new int[mO.length*mO[0].length];
        for (int i = 0; i < mO.length; i++) 
            for (int j = 0; j < mO[0].length; j++)
                retorno[(i*mO[0].length)+j] = mO[i][j];
        return retorno;
    }

    private static BufferedImage toGrayscale(BufferedImage img) { 
        int pixel[] = new int[3];
        int rgb;
        for(int i=0; i<img.getWidth(); i++){
            for(int j=0; j<img.getHeight(); j++){

                pixel = img.getRaster().getPixel(i, j, pixel);

                int k =(int)((1*pixel[0])+(1*pixel[1])+(1*pixel[2]))/3;

                Color color = new Color(k, k, k);

                rgb = color.getRGB();

                img.setRGB(i, j, rgb);

            }

        }
        return img;
    }
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("aa.PNG"));
        //image = toGrayscale(image);
        //ImageIO.write(image, "PNG", new File("LennaGray.png"));  
        int width = image.getWidth();
	int height = image.getHeight();

       
        int[][] m_img1 = getMatrix(image);
        int mask[][] =	{	{1, 1, 1},
                                {1, 1, 1},
                                {1, 1, 1},
                                            };
        
        int[][] resultante = FiltMedia(m_img1, m_img1, mask, width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setRGB(0, 0, width, height, toArray(resultante), 0, width);
        ImageIO.write(bufferedImage, "PNG", new File("Media.png"));
    }
    
}