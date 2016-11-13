package org.vandy.client;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.vandy.client.login.LoginLogic;
import org.vandy.client.packet.LoadBankPacket;

import com.polaris.engine.App;
import com.polaris.engine.LogicGui;

public class VandyApp extends App
{
	public static final Vector3f dark = new Vector3f(181 / 255f, 49 / 255f, 38 / 255f);

	public static final Vector3f darkest = new Vector3f(143 / 255f, 22 / 255f, 13 / 255f);

	public static final Vector3f fontDarkest = new Vector3f(48 / 255f, 41 / 255f, 41 / 255f);
	
	public static void main(String[] args)
	{
		VandyApp app = new VandyApp(false);
		app.run();
	}
	
	public VandyApp(boolean debug) 
	{
		super(debug);
	}
	
	public void init()
	{
		this.sendPacket(new LoadBankPacket(this, this.getLogicHandler()));
	}
	
	@Override
	public long createWindow()
	{
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, 0);
		return this.createWindow(1080, 640, "Vandy", 0);
	}

	@Override
	public LogicGui getStartGui() 
	{
		return new LoginLogic(this.getLogicHandler());
	}

}
