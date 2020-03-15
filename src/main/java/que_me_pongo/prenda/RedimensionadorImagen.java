package que_me_pongo.prenda;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RedimensionadorImagen {

    private int ancho = 100;
    private int alto = 100;

    private static RedimensionadorImagen instancia;

    private RedimensionadorImagen(){

    }

    public static RedimensionadorImagen getInstance(){
        if(instancia == null){
            instancia = new RedimensionadorImagen();
        }
        return instancia;
    }

    public BufferedImage redimensionar(String path){
        BufferedImage img = null;
        try {
        	img = new BufferedImage(ancho,alto,BufferedImage.TYPE_INT_ARGB);
					img = ImageIO.read(new File(path));
					img = Thumbnails.of(img).forceSize(ancho, alto).asBufferedImage();
				} catch (IOException e) {
					throw new ImagenNoPudoRedimesionarseException();
				}
        
        /*con forceSize se fuerza la imagen a esos valores perdiendo calidad,
        en cambio con size solo se fuerza algun valor y se preserva el aspecto*/

        return img;
    }
}

