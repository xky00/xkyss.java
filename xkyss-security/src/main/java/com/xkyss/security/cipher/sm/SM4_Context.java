package com.xkyss.security.cipher.sm;
/**
 * 
  * Copyright (c)2014,灵创智恒
  * All rights reserved. 
  * 
  * @file文件名 
  * @date 2016-10-26 下午2:31:27
  * @brief 
  * 对类进行详细描述 
  * 
  * @author 张德强
 */
public class SM4_Context
{
	public int mode;
	
	public long[] sk;
	
	public boolean isPadding;

	public SM4_Context() 
	{
		this.mode = 1;
		this.isPadding = true;
		this.sk = new long[32];
	}
}
