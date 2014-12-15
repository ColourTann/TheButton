package game;

import java.util.BitSet;

import tools.WavInfo;
import tools.WavInfo.Tag;
import util.Colours;
import util.Draw;
import util.Fonts;
import util.TextWisp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Waveform extends Group{
	static int waveFormHeight=3;
	static int speedDivider=3;
	WavInfo info;
	byte[] bytes;
	float ticks=0;
	static int bleepHeight=110;
	boolean cheating=true;
	public Waveform(WavInfo info){
		this.info=info;
		bytes=info.bonkBytes;
		setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(new InputProcessor() {
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				Main.beep.stop();
				return false;
			}
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Main.beep.play();
				return true;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}
		});
	}

	@Override
	public void act(float delta){
		super.act(delta);
		ticks+=delta;
		if(Gdx.input.isButtonPressed(0)){
			scramble(delta);
		}
		addActor(new TextWisp("hi", Fonts.font, Colours.blue, 50, 50, 500, 0));
	}

	private float getOffset(){
		return ticks*WavInfo.bonkSamplesPerSecond;
	}

	BitSet set= new BitSet();
	private void scramble(float delta) {
		while(delta>0){
			int index= (int) (-Gdx.graphics.getWidth()/2/speedDivider+getOffset()-delta*WavInfo.bonkSamplesPerSecond)-1;
			if(index<0)return;
			if(set.get(index)){
				delta-=.01f;
				continue;
			}
			set.set(index);
			bytes[index]=(byte) (bleepHeight);
			delta-=.01f;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		for(float x=getWidth();x>0;x--){
			int index=(int) (-getWidth()/speedDivider+x/speedDivider+getOffset());
			int sample=0;
			if(index>0){
				sample = (int)bytes[index];
				sample*=waveFormHeight;
			}
			float audioPosition =ticks+(x-getWidth())/speedDivider/WavInfo.bonkSamplesPerSecond;
			batch.setColor(1,1,1,1);
			if(info.tagAt(audioPosition)){
				if(cheating){
					batch.setColor(1,1,0,1);
				}
				if(sample==bleepHeight*waveFormHeight){
					batch.setColor(0,1,0,1);
				}
			}
			Draw.drawScaled(batch, Draw.getSq(), x, getHeight()/2-sample/2, 1, sample);
		}
		batch.setColor(1,0,0,1);
		Draw.drawScaled(batch, Draw.getSq(), Gdx.graphics.getWidth()/2-1, 0, 1, Gdx.graphics.getHeight());
		super.draw(batch, parentAlpha);
	}

}
