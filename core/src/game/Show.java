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
	ArrayList<Float> trackLengths= new ArrayList<Float>();
	float totalTime=0;
	float currentTime=0;
	float timeThroughSong=0;
	Waveform waveform;
	static int waveFormHeight=4;
	static float speed=1f;
	boolean cheating=true;
	Tag nextTag;
	int musicPlaying=-1;
	public Show(){

	}

	private static int variations=3; 
	public void setupShow(String path, int length){
		String delim = Main.delim;
		path=path.replaceAll("/", delim);
		addInfo(WavInfo.getInfo(path+delim+"intro"));	//add intro
		for(int i=0;i<length;i++){
			if(Main.createBonks){ 	//setup all the bonks
				for(int v=0;v<variations;v++){
					addInfo(WavInfo.getInfo(path+delim+"guest"+delim+i+v));
					addInfo(WavInfo.getInfo(path+delim+"host"+delim+i+v));
				}
			}
			else{
				int random = (int) (Math.random()*variations);
				addInfo(WavInfo.getInfo(path+delim+"guest"+delim+i+random));	//guest random
				addInfo(WavInfo.getInfo(path+delim+"host"+delim+i+random));	//host random
			}
		}
		addInfo(WavInfo.getInfo(path+delim+"outro"));	//add outro
	}

	public void addInfo(WavInfo info){	
		for(byte b:info.bonkBytes){
			waveHeight.add(b);
		}
		musics.add(info.getAudio());
		tags.addAll(info.getOffsetTags(totalTime));
		totalTime+=info.getLength();
		trackLengths.add(info.getLength());
	}

	public void update(float delta){
		currentTime+=delta;
		timeThroughSong+=delta;
		if(musicPlaying==-1||timeThroughSong>=trackLengths.get(musicPlaying)){
			timeThroughSong=0;
			musicPlaying++;
			musics.get(musicPlaying).stop();
			musics.get(musicPlaying).play();
		}
		if(Gdx.input.isButtonPressed(0)){
			scramble(delta);
			prevIndex=getSampleIndexAt(waveform.getBroadcastX());
		}
		else prevIndex=-1;

		if(nextTag==null){
			nextTag=getNextTag();
		}

		if(nextTag!=null&&positionToTime(waveform.getWidth()/2)>nextTag.end){
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
				System.out.println(t);
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

		if (index<0||index>=waveHeight.size())return 0;
		return waveHeight.get(index);
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
