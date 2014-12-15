package util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Fonts {
	public static BitmapFont font;
	public static void init(boolean retina){
		font=new BitmapFont();
	}
	public static void drawFontCentered(Batch batch, String s, BitmapFont f, float x, float y){
		f.draw(batch, s, (int)(x-f.getBounds(s).width/2), (int)(y-f.getBounds(s).height/2));
	}
}

