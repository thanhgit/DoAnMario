package com.example.doanmario;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Player extends AnimatedSprite {
	private Body body;
//	private boolean canRun = false;

//	private int footContacts = 0;

	protected Action lastdirection;

	private boolean isMoving = false;

//	private boolean isJumping = true;

	public Player(float pX, float pY, VertexBufferObjectManager vbo,
			BoundCamera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().playerTextureRegion, vbo);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}

	public abstract void onDie();

	private void createPhysics(final BoundCamera camera,
			PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("player");
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (getY() <= 0) {
					onDie();
				}
			}
		});
	}

	public void jump() {
		if (body.getLinearVelocity().y == 0) {
			if (lastdirection == Action.MOVERIGHT)
				this.stopAnimation(5);
			else if (lastdirection == Action.MOVELEFT)
				this.stopAnimation(11);

			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
		}
	}

	public void move(Action to) {

		if (to == Action.MOVELEFT) {

			this.isMoving = true;

			lastdirection = to;

			this.animate(new long[] { 30, 30, 30, 30 }, 6, 9, true);

			body.setLinearVelocity(-1 * GameConstants.PLAYER_VELOCITY, 0);

		} else if (to == Action.MOVERIGHT) {

			this.isMoving = true;

			lastdirection = to;

			this.animate(new long[] { 30, 30, 30, 30 }, 0, 3, true);

			body.setLinearVelocity(1 * GameConstants.PLAYER_VELOCITY, 0);
		} else if (to == Action.STOP) {
			this.isMoving = false;
			body.setLinearVelocity(0, 0);

			if (lastdirection == Action.MOVELEFT) {
				this.stopAnimation(6);
			} else if (lastdirection == Action.MOVERIGHT) {
				this.stopAnimation(0);
			}
		}

	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(body.getLinearVelocity().y==0 && body.getLinearVelocity().x==0)
		{
			if(lastdirection==Action.MOVELEFT)
				this.stopAnimation(6);
			else if(lastdirection==Action.MOVERIGHT)
				this.stopAnimation(0);
			
		}
	}
	
	
}
