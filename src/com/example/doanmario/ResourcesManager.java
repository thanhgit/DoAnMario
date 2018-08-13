package com.example.doanmario;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

public class ResourcesManager {
	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;

	// Splash scene
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	// Tutorial Scene
	public ITextureRegion tutorial_background_region;
	private BitmapTextureAtlas tutorialTextureAtlas;

	// Menu Scene
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion tutorial_region;
	public ITextureRegion exit_region;
	private BuildableBitmapTextureAtlas menuTextureAtlas;

	// Control
	public TextureRegion tiledTextureleftarrow;
	public TextureRegion tiledTexturerightarrow;
	public TextureRegion tiledTextureJump;

	// Font
	public Font font;

	// Game Texture Regions
	// public ITextureRegion platform1_region;
	// public ITextureRegion platform2_region;
	// public ITextureRegion platform3_region;
	// public ITextureRegion coin_region;
	// public ITiledTextureRegion player_region;
	//
	// game textures
	// public ITiledTextureRegion marioTextureRegion;
	// public ITiledTextureRegion goombaTextureRegion;
	// public ITiledTextureRegion koopaTextureRegion;

	 public ITextureRegion pipe1TextureRegion;
	// public ITextureRegion pipe2TextureRegion;
	// public ITextureRegion pipe3TextureRegion;
	public ITextureRegion platform1TextureRegion;
	public ITextureRegion platform2TextureRegion;
	public ITextureRegion platform3TextureRegion;
	// public ITextureRegion platform4TextureRegion;
	// public ITextureRegion platform5TextureRegion;
	// public ITextureRegion platform6TextureRegion;
	// public ITextureRegion platform7TextureRegion;

	public ITextureRegion coinTextureRegion;
	
	public ITiledTextureRegion cloudTextureRegion;

	public ITiledTextureRegion playerTextureRegion;
	
	//Enemy Texture
	public ITiledTextureRegion koopaTextureRegion;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity, "options.png");
		tutorial_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity, "tutorial.png");
		exit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "exit.png");
		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadMenuAudio() {

	}

	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 50, true,
				Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		platform1TextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "platform1.png");
		 platform2TextureRegion =
		 BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
		 activity, "platform2.png");
		 platform3TextureRegion =
		 BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
		 activity, "platform3.png");
		// platform4TextureRegion =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
		// activity, "platform4.png");
		// platform5TextureRegion =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
		// activity, "platform5.png");
		// platform6TextureRegion =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
		// activity, "platform6.png");
		// platform7TextureRegion =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
		// activity, "platform7.png");
		 
		 pipe1TextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameTextureAtlas, activity, "pipe1.png");
		 
		coinTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"coin.png");
		
		cloudTextureRegion=BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity,
						"cloud.png", 2, 2);

		playerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity,
						"mariobig.png", 6, 2);
		
		//enemy texture
		koopaTextureRegion=BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity,
						"koopa.png", 2, 2);

		/* leftarrow button */

		this.tiledTextureleftarrow = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.gameTextureAtlas, activity,
						"leftControl.png");

		/* rightarrow button */

		this.tiledTexturerightarrow = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.gameTextureAtlas, activity,
						"rightControl.png");

		/* jump button */

		this.tiledTextureJump = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.gameTextureAtlas, activity,
						"jumpbutton.png");

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}
	

	private void loadGameFonts() {

	}

	private void loadGameAudio() {

	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	public void loadTutorialScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		tutorialTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		tutorial_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(tutorialTextureAtlas, activity,
						"tutorial.png", 0, 0);
		tutorialTextureAtlas.load();
	}

	public void unloadTutorialScreen() {
		tutorialTextureAtlas.unload();
		tutorial_background_region = null;
	}

	public static void prepareManager(Engine engine, GameActivity activity,
			BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	public void unloadGameTextures() {
		gameTextureAtlas.unload();
		platform1TextureRegion=null;
		tiledTexturerightarrow=null;
		tiledTextureleftarrow=null;
		tiledTextureJump=null;
	}
}
