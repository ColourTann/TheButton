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
	Show show;
	public Waveform(Show show){
		this.show=show;
		setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(new WaveformProcessor());
	}


	
	@Override
	public void act(float delta){
		super.act(delta);
		
		//addActor(new TextWisp("hi", Fonts.font, Colours.blue, 50, 50, 500, 0));
	}

	BitSet set= new BitSet();

	@Override
	public void draw(Batch batch, float parentAlpha){
		for(float x=getWidth();x>0;x--){
			
			int sample=show.getSample(x);
			batch.setColor(1,1,1,1);
			if(show.getTagAt(x)!=null){
				if(show.showSwears()){
					batch.setColor(1,1,0,1);
				}
				if(show.bleepedAt(x)){
					batch.setColor(0,1,0,1);
				}
			}
			Draw.drawScaled(batch, Draw.getSq(), x, getHeight()/2-sample/2, 1, sample);
		}
		batch.setColor(1,0,0,1);
		Draw.drawScaled(batch, Draw.getSq(), Gdx.graphics.getWidth()/2-1, 0, 1, Gdx.graphics.getHeight());
		super.draw(batch, parentAlpha);
	}

	public float getBroadcastX() {
		return getWidth()/2f;
	}

}
