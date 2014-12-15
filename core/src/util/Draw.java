package util;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Draw {
	//Non-centered stuff//
	
	public static void draw(Batch batch, Texture t, float x, float y){
		drawRotatedScaled(batch, t, x, y, 1, 1, 0);
	}

	public static void drawScaled(Batch batch, Texture t, float x, float y, float scaleX, float scaleY){
		drawRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}

	public static void drawRotatedScaled(Batch batch, Texture t, float x, float y, float scaleX, float scaleY, float radianRotation){
		drawRotatedScaledFlipped(batch, t, x, y, scaleX, scaleY, radianRotation, false, false);
	}

	public static void drawRotatedScaledFlipped(Batch batch, Texture t, float x, float y, float scaleX, float scaleY, float radianRotation, boolean xFlip, boolean yFlip){
		batch.draw(t, x, y, 0, 0, t.getWidth(), t.getHeight(), scaleX, scaleY, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),xFlip,yFlip);
	}
	
	//Centered stuff//
	
	public static void drawCentered(Batch batch, Texture t, float x, float y){
		drawCenteredRotatedScaled(batch, t, x, y, 1, 1, 0);
	}

	public static void drawCenteredScaled(Batch batch, Texture t, float x, float y, float scaleX, float scaleY){
		drawCenteredRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}

	public static void drawCenteredRotated(Batch batch, Texture t, float x, float y, float radianRotation){
		drawCenteredRotatedScaled(batch, t, x, y, 1, 1, radianRotation);
	}

	public static void drawCenteredRotatedScaled(Batch batch, Texture t, float x, float y, float xScale, float yScale, float radianRotation){
		drawCenteredRotatedScaledFlipped(batch, t, x, y, xScale, yScale, radianRotation, false, false);
	}

	public static void drawCenteredRotatedScaledFlipped(Batch batch, Texture t, float x, float y, float xScale, float yScale, float radianRotation, boolean xFlip, boolean yFlip){
		batch.draw(t, (int)(x-t.getWidth()/2), (int)(y-t.getHeight()/2), t.getWidth()/2f, t.getHeight()/2f, t.getWidth(), t.getHeight(), xScale, yScale, rad2deg(radianRotation),0,0,t.getWidth(),t.getHeight(),xFlip,yFlip);
	}



	//Blending Junk
	public enum BlendType{Normal, Additive, MaxBuggy}
	public static void setBlend(Batch batch, BlendType type){
		switch(type){
		case Additive:
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		case Normal:
			Gdx.gl20.glBlendEquation(GL20.GL_FUNC_ADD);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case MaxBuggy:
			Gdx.gl20.glBlendEquation(0x8008);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		}	
	}

	public static float rad2deg(float rad){
		return (float) (rad*180f/Math.PI);
	}
	
	private static Texture wSq;
	public static Texture getSq(){
		if(wSq==null){
			Pixmap map = new Pixmap(1, 1, Format.RGBA8888);
			map.setColor(1,1,1,1);
			map.drawPixel(0, 0);
			wSq=new Texture(map);
		}
		return wSq;
	}
}
