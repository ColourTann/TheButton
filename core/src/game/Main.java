package game;

import tools.WavInfo;
import util.Fonts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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
	public static Music beep;
	Stage stage;
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
		Main.manager.load("beep.mp3", Music.class);
		Main.manager.load("bg.mp3", Music.class);
		manager.finishLoading();
		bg=manager.get("bg.mp3", Music.class);
		bg.setVolume(.3f);
		bg.play();
		beep=manager.get("beep.mp3", Music.class);
		beep.setVolume(.3f);
		manager.get(handle.path(), Music.class).play();;
		bytes= info.bonkBytes;
		Gdx.input.setInputProcessor(stage);
		stage.addActor(new Waveform(info));
	}

	public void update(float delta){
		stage.act(delta);
		manager.update();
		
	}

	
	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		stage.draw();
		batch.setColor(1,1,1,1);
		Fonts.font.draw(batch, ""+Gdx.graphics.getFramesPerSecond(), 50,50);
		batch.end();
	}
}
