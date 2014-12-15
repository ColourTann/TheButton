package util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class TextWisp extends Actor{
	BitmapFont font;
	String text;
	Color col;
	public TextWisp (String s, BitmapFont f, Color c, float x, float y, float moveDist, float delay){		
		setPosition(x, y, Align.center);
		addAction(Actions.delay(delay, Actions.fadeOut(.5f)));
		addAction(Actions.delay(delay,Actions.moveTo(getX(), getY()+moveDist, 1)));
		font=f;
		text=s;
		col=c;
	}
	@Override
	public void draw(Batch batch, float parentAlpha){
		font.setColor(Colours.withAlpha(col, getColor().a));
		Fonts.drawFontCentered(batch, text, font, getX(), getY());
	}
}
