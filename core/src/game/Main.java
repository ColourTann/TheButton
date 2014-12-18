package game;

import tools.WavInfo;
import util.Draw;
import util.Fonts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Music m;
	WavInfo info;
	byte[] bytes;
	
	boolean createBonks=false;
	public static AssetManager manager;
	String path;
	FileHandle handle;
	float ticks=0;
	
	Music bg;
	public static Sound beep;
	Stage stage;
	Show show;
	@Override
	public void create () {
		stage=new Stage(new ScreenViewport());
		Fonts.init(false);
		manager=new AssetManager();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		info=new WavInfo("buttontutorial", createBonks);
		handle = Gdx.files.internal("buttontutorial.mp3");
		Main.manager.load(handle.path(), Music.class);
		Main.manager.load("beep.mp3", Sound.class);
		Main.manager.load("bg.mp3", Music.class);
		System.out.println("starting finish load");
		manager.finishLoading();
		System.out.println("finished finish load");
		bg=manager.get("bg.mp3", Music.class);
		bg.setVolume(.3f);
		bg.play();
		beep=manager.get("beep.mp3", Sound.class);
		manager.get(handle.path(), Music.class).play();
		bytes= info.bonkBytes;
		Gdx.input.setInputProcessor(stage);
		show = new Show();
		show.addInfo(info);
		stage.addActor(show.getWaveForm());
	}

	public void update(float delta){
		if(delta>.2){
			manager.update();
			System.out.println("eeek high delta: "+delta);
			return;
		}
		show.update(delta);
		stage.act(delta);
	}

	
	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.setColor(1,1,1,1);
		Fonts.font.setColor(1,1,1,1);
		Fonts.font.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 0,Gdx.graphics.getHeight());
		batch.flush();
		stage.draw();
		
		
		batch.end();
	}
}
