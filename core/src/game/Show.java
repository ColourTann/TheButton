package game;

import java.util.ArrayList;

import tools.WavInfo;
import tools.WavInfo.Tag;
import util.Fonts;
import util.TextWisp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;

public class Show{
	ArrayList<Byte> waveHeight= new ArrayList<Byte>();
	ArrayList<Music> musics= new ArrayList<Music>();
	ArrayList<Tag> tags= new ArrayList<Tag>();
	float totalTime=0;
	float currentTime=0;
	Waveform waveform;
	static int waveFormHeight=3;
	static float speed=3;
	boolean cheating=true;
	Tag nextTag;
	public Show(){
		
	}
	
	public void addInfo(WavInfo info){
		for(byte b:info.bonkBytes){
			waveHeight.add(b);
		}
		musics.add(info.getAudio());
		tags.addAll(info.getOffsetTags(totalTime));
		totalTime+=info.getLength();
	}
	
	public void update(float delta){
		currentTime+=delta;
		if(Gdx.input.isButtonPressed(0)){
			scramble(delta);
			prevIndex=getSampleIndexAt(waveform.getBroadcastX());
		}
		else prevIndex=-1;
		
		if(nextTag==null){
			nextTag=getNextTag();
		}
		
		if(positionToTime(waveform.getWidth()/2)>nextTag.end){
			String wispTitle="ERROR";
			Color wispCol=Color.MAGENTA;
			if(nextTag.isCensored()){
				//good//
				wispTitle=nextTag.getName()+" censored!";
				wispCol=Color.GREEN;
			}
			else{
				//bad
				wispTitle=nextTag.getName()+" missed!";
				wispCol=Color.RED;
			}
			TextWisp wisp = new TextWisp(wispTitle, Fonts.font, wispCol, waveform.getWidth()/2, 400, 100, 1);
			waveform.addActor(wisp);
			nextTag=null;
		}
		
	}

	
	private Tag getNextTag() {
		for(Tag t:tags){
			if(t.start>currentTime){
				return t;
			}
		}
		return null; //TODO return an empty one maybe?
	}


	int prevIndex=-1;
	public void scramble(float delta) {
		int index=getSampleIndexAt(waveform.getBroadcastX());
		if(index<0)return;
		waveHeight.set(index, getBleepHeight());
		if(prevIndex!=-1) for(int i=prevIndex;i<index;i++) waveHeight.set(i,getBleepHeight()); //Bleeping all samples between last frame and now if holding //
		Tag t = getTagAt(waveform.getBroadcastX());
		if(t!=null){
			t.censored+=delta;
		}
	}
                                                                                        	

	//Getters//

	public int getSample(float x) {
		int index = getSampleIndexAt(x);
		
		if (index<0)return 0;
		return waveHeight.get(getSampleIndexAt(x));
	}

	public boolean showSwears() {
		return true;
	}

	public boolean bleepedAt(float x) {
		return getSample(x)==getBleepHeight();
	}
	
	private int getSampleIndexAt(float x){
		return timeToSampleIndex(positionToTime(x));
	}
	
	private byte getBleepHeight() {
		return 110;
	}
	
	private float positionToTime(float x){
		return (x-waveform.getWidth())/speed/WavInfo.bonkSamplesPerSecond+currentTime;
	}
	
	private int timeToSampleIndex(float time){
		return (int)(time*WavInfo.bonkSamplesPerSecond);
	}
	
	Tag getTagAt(float x) {
		float time = positionToTime(x);
		for(Tag t: tags){
			if(t.isAt(time))return t;
		}
		return null;
	}
	
	public Waveform getWaveForm(){
		if(waveform==null)waveform= new Waveform(this);
		return waveform;
	}

	
}
