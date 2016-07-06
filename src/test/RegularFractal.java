package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

import javax.imageio.ImageIO;

import fractals.FractalImage;
import fractals.interfaces.FractalResultInterface;

public class RegularFractal {

	public static void main(String[] args) {
		FractalImage fi = new FractalImage();
		fi.addProcessingListener(new FractalResultInterface(){
			@Override
			public void processImage(BufferedImage image) {
				File f = new File(System.getProperty("user.home") + "/Desktop/Fractals/" + System.currentTimeMillis() + ".bmp");
				try {
					ImageIO.write(image, "bmp", f);
                                        Desktop.getDesktop().open(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		fi.start();
		fi.join();
	}

}
