package org.vandy.client;

import org.joml.Vector4f;

import com.polaris.engine.App;
import com.polaris.engine.LogicGui;

public class VandyApp extends App
{
	
	public static final Vector4f lightest = new Vector4f(1, 170 / 255f, 163 / 255f, 1f);
	public static final Vector4f light = new Vector4f(245 / 255f, 125 / 255f, 115/ 255f, 1f);
	public static final Vector4f normal = new Vector4f(211 /255f, 81 / 255f, 71 / 255f, 1f);
	public static final Vector4f dark = new Vector4f(181 / 255f, 49 / 255f, 38 / 255f, 1f);
	public static final Vector4f darkest = new Vector4f(143 / 255f, 22 / 255f, 13 / 255f, 1f);

	public static void main(String[] args)
	{
		VandyApp app = new VandyApp(true);
		app.run();
	}
	
	public VandyApp(boolean debug) 
	{
		super(debug);
	}

	@Override
	public long createWindow()
	{
		return this.createWindow(1080, 640, "Vandy", 0);
	}

	@Override
	protected LogicGui getStartGui() 
	{
		return new MainLogic(this.getLogicHandler());
	}

}
