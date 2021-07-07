package com.sonicether.soundphysics;

public class Int3 
{
	public int x;
	public int y;
	public int z;
	
	public static Int3 create(int x, int y, int z)
	{
		return new Int3(x, y, z);
	}

	public Int3(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override public boolean equals(Object b)
	{
		Int3 i = (Int3)b;
		return (this.x == i.x && this.y == i.y && this.z == i.z);
	}
}
