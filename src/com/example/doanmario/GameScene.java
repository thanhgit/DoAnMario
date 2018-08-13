package com.example.doanmario;

import java.io.IOException;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.sensor.SensorDelay;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.Constants;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.doanmario.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnAreaTouchListener {
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM4 = "platform4";
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM5 = "platform5";
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM6 = "platform6";
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM7 = "platform7";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PIPE1 = "pipe1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY = "enemy";

	private Player player;
	private Enemy enemy;
	private HUD gameHUD;
	private PhysicsWorld physicsWorld;

	private Sprite jumpButton;
	private Sprite leftArrowButton;
	private Sprite rightArrowButton;

	private Text scoreText;
	private int score = 0;
	private EnemySensors koopa1;

	@Override
	public void createScene() {
		// setOnSceneTouchListener((IOnSceneTouchListener) this);
		koopa1 = EnemySensors.LEFT;
		createBackground();
		createHUD();
		createPhysics();
		loadLevel(1);
	}

	private void createHUD() {
		gameHUD = new HUD();
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		scoreText = new Text(20, 420, resourcesManager.font,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				vbom);
		scoreText.setAnchorCenter(0, 0);
		scoreText.setText("Gold: 1");

		// jump button
		jumpButton = new Sprite(GameConstants.CAMERA_WIDTH + 10
				- ResourcesManager.getInstance().tiledTextureJump.getWidth(),
				GameConstants.CAMERA_HEIGHT
						- ResourcesManager.getInstance().tiledTextureJump
								.getHeight() + 10,
				ResourcesManager.getInstance().tiledTextureJump, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.jump();

				} else if (pSceneTouchEvent.isActionUp()) {

				}

				return true;
			};
		};

		// left button
		leftArrowButton = new Sprite(20,
				GameConstants.CAMERA_HEIGHT
						- ResourcesManager.getInstance().tiledTextureleftarrow
								.getHeight() - 30,
				ResourcesManager.getInstance().tiledTextureleftarrow, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.move(Action.MOVELEFT);

				} else if (pSceneTouchEvent.isActionUp()) {

					player.move(Action.STOP);

				}

				return true;
			};
		};
		// right button
		rightArrowButton = new Sprite(
				ResourcesManager.getInstance().tiledTextureleftarrow.getWidth()
						+ (60 + 20),
				GameConstants.CAMERA_HEIGHT
						- ResourcesManager.getInstance().tiledTextureleftarrow
								.getHeight() - 30,
				ResourcesManager.getInstance().tiledTexturerightarrow, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.move(Action.MOVERIGHT);

				} else if (pSceneTouchEvent.isActionUp()) {

					player.move(Action.STOP);

				}

				return true;
			};
		};

		gameHUD.attachChild(jumpButton);
		gameHUD.attachChild(leftArrowButton);
		gameHUD.attachChild(rightArrowButton);
		gameHUD.attachChild(scoreText);

		gameHUD.registerTouchArea(jumpButton);
		gameHUD.registerTouchArea(leftArrowButton);
		gameHUD.registerTouchArea(rightArrowButton);
		camera.setHUD(gameHUD);
	}

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Gold: " + score);
	}

	private void loadLevel(int levelID) {

		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,
				0.01f, 0.5f);

		levelLoader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						LevelConstants.TAG_LEVEL) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {
						final int width = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
						final int height = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
						camera.setBounds(0, 0, width, height); // here we set
																// camera bounds
						camera.setBoundsEnabled(true);

						return GameScene.this;
					}
				});

		levelLoader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						TAG_ENTITY) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {
						final int x = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_X);
						final int y = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
						final String type = SAXUtils.getAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

						final Sprite levelObject;

						if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1)) {
							levelObject = new Sprite(x, y,
									resourcesManager.platform1TextureRegion,
									vbom){

										@Override
										protected void onManagedUpdate(
												float pSecondsElapsed) {
											// TODO Auto-generated method stub
											super.onManagedUpdate(pSecondsElapsed);
											if(player.collidesWith(this))
											{
												player.move(Action.STOP);
											}
										}
								
							};
							final Body body = PhysicsFactory.createBoxBody(
									physicsWorld, levelObject,
									BodyType.StaticBody, FIXTURE_DEF);
							body.setUserData("platform1");
							physicsWorld
									.registerPhysicsConnector(new PhysicsConnector(
											levelObject, body, true, false));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
							levelObject = new Sprite(x, y,
									resourcesManager.platform2TextureRegion,
									vbom);
							final Body body = PhysicsFactory.createBoxBody(
									physicsWorld, levelObject,
									BodyType.StaticBody, FIXTURE_DEF);
							body.setUserData("platform2");
							physicsWorld
									.registerPhysicsConnector(new PhysicsConnector(
											levelObject, body, true, false));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3)) {
							levelObject = new Sprite(x, y,
									resourcesManager.platform3TextureRegion,
									vbom);
							final Body body = PhysicsFactory.createBoxBody(
									physicsWorld, levelObject,
									BodyType.StaticBody, FIXTURE_DEF);
							body.setUserData("platform3");
							physicsWorld
									.registerPhysicsConnector(new PhysicsConnector(
											levelObject, body, true, false));
						}
						// else if
						// (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM4))
						// {
						// levelObject = new Sprite(x, y,
						// resourcesManager.platform4TextureRegion, vbom);
						// final Body body =
						// PhysicsFactory.createBoxBody(physicsWorld,
						// levelObject, BodyType.StaticBody, FIXTURE_DEF);
						// body.setUserData("platform4");
						// physicsWorld.registerPhysicsConnector(new
						// PhysicsConnector(levelObject, body, true, false));
						// }
						// else if
						// (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM5))
						// {
						// levelObject = new Sprite(x, y,
						// resourcesManager.platform5TextureRegion, vbom);
						// final Body body =
						// PhysicsFactory.createBoxBody(physicsWorld,
						// levelObject, BodyType.StaticBody, FIXTURE_DEF);
						// body.setUserData("platform5");
						// physicsWorld.registerPhysicsConnector(new
						// PhysicsConnector(levelObject, body, true, false));
						// }
						// else if
						// (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM6))
						// {
						// levelObject = new Sprite(x, y,
						// resourcesManager.platform6TextureRegion, vbom);
						// final Body body =
						// PhysicsFactory.createBoxBody(physicsWorld,
						// levelObject, BodyType.StaticBody, FIXTURE_DEF);
						// body.setUserData("platform6");
						// physicsWorld.registerPhysicsConnector(new
						// PhysicsConnector(levelObject, body, true, false));
						// }
						// else if
						// (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM7))
						// {
						// levelObject = new Sprite(x, y,
						// resourcesManager.platform7TextureRegion, vbom);
						// final Body body =
						// PhysicsFactory.createBoxBody(physicsWorld,
						// levelObject, BodyType.StaticBody, FIXTURE_DEF);
						// body.setUserData("platform7");
						// physicsWorld.registerPhysicsConnector(new
						// PhysicsConnector(levelObject, body, true, false));
						// }
						else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)) {
							levelObject = new Sprite(x, y,
									resourcesManager.coinTextureRegion, vbom) {
								@Override
								protected void onManagedUpdate(
										float pSecondsElapsed) {
									super.onManagedUpdate(pSecondsElapsed);
									if (player.collidesWith(this)) {
										addToScore(1);
										this.setVisible(false);
										this.setIgnoreUpdate(true);
									}
								}
							};
							levelObject
									.registerEntityModifier(new LoopEntityModifier(
											new ScaleModifier(1, 1, 1.3f)));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PIPE1)) {
							levelObject = new Sprite(x, y,
									resourcesManager.pipe1TextureRegion, vbom) {
								@Override
								protected void onManagedUpdate(
										float pSecondsElapsed) {
									super.onManagedUpdate(pSecondsElapsed);
								}

							};
							final Body body = PhysicsFactory.createBoxBody(
									physicsWorld, levelObject,
									BodyType.StaticBody, FIXTURE_DEF);
							body.setUserData("pipe1");
							physicsWorld
									.registerPhysicsConnector(new PhysicsConnector(
											levelObject, body, true, false));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
							player = new Player(x, y, vbom, camera,
									physicsWorld) {
								@Override
								public void onDie() {
									scoreText.setText("You are died!");
									scoreText.setPosition(400, 240);
									player.setPosition(x, y);
									jumpButton.setVisible(false);
									leftArrowButton.setVisible(false);
									rightArrowButton.setVisible(false);
									scoreText.setVisible(false);
									SceneManager.getInstance().loadGameScene(
											engine);
								}

								@Override
								protected void onManagedUpdate(
										float pSecondsElapsed) {
									super.onManagedUpdate(pSecondsElapsed);
								}
							};
							levelObject = player;
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY)) {
							enemy = new Enemy(x, y, vbom, camera, physicsWorld) {
								@Override
								public void onDie() {
									final PhysicsConnector physicsConnector =
											physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(enemy);
											engine.runOnUpdateThread(new Runnable() 
											{
											    @Override
											    public void run() 
											    {
											        if (physicsConnector != null)
											        {
											             physicsWorld.unregisterPhysicsConnector(physicsConnector);
											             body.setActive(false);
											             physicsWorld.destroyBody(enemy.body);
											             detachChild(enemy);
											        }
											    }
											});
								}

								@Override
								public void IA() {
									//move(koopa1);
								}

								@Override
								protected void onManagedUpdate(
										float pSecondsElapsed) {
									super.onManagedUpdate(pSecondsElapsed);
//									if (player.collidesWith(this)) {
//										if (player.getY() > (this.getY()+ player.getHeight()+this.getHeight()/2)) {
//											//this.setVisible(false);
//											//onDie();
//										} else {
//											//player.onDie();
//										}
//									}
									if(this.getX()==0)
										this.setPosition(0, this.getY());
									
									if(this.getX()==150)
									{
										koopa1=EnemySensors.STOP;
									}
									else if(this.getX()==10)
									{
										koopa1=EnemySensors.RIGHT;
									}
									else{
										enemy.move(koopa1);
									}
								}

							};
							levelObject = enemy;
						} else {
							throw new IllegalArgumentException();
						}

						levelObject.setCullingEnabled(true);

						return levelObject;
					}
				});

		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/level"
				+ levelID + ".lvl");
	}

	@Override
	public void onBackKeyPressed() {
		scoreText.detachSelf();
		scoreText.dispose();
		jumpButton.detachSelf();
		jumpButton.dispose();
		leftArrowButton.detachSelf();
		leftArrowButton.dispose();
		rightArrowButton.detachSelf();
		rightArrowButton.dispose();
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	private void createBackground() {
		for (int i = 0; i < 10; i++) {
			AnimatedSprite cloud = new AnimatedSprite(0, 0,
					resourcesManager.cloudTextureRegion, vbom);
			cloud.animate(new long[] { 200, 200 }, 0, 1, true);
			cloud.setPosition(300 + i * 30, 300 + i * 10);
			attachChild(cloud);
		}
		for (int i = 0; i < 5; i++) {
			AnimatedSprite as = new AnimatedSprite(0, 0,
					resourcesManager.cloudTextureRegion, vbom);
			as.animate(new long[] { 200, 200 }, 2, 3, true);
			as.setPosition(300 + i * 100, 57);
			attachChild(as);
		}
		setBackground(new Background(Color.BLUE));
	}

	@Override
	public void disposeScene() {

	}

	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -20), false);
		registerUpdateHandler(physicsWorld);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()) {

		}
		return false;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

	}

}
