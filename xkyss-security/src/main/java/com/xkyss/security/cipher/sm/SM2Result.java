package com.xkyss.security.cipher.sm;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;
/**
 * 
  * Copyright (c)2014,灵创智恒
  * All rights reserved. 
  * 
  * @file文件名 
  * @date 2016-10-26 下午2:30:59
  * @brief 
  * 对类进行详细描述 
  * 
  * @author 张德强
 */
public class SM2Result 
{
	public SM2Result() {
	}

	// 签名/验签
	public BigInteger r;
	public BigInteger s;
	public BigInteger R;

	// 密钥交换
	public byte[] sa;
	public byte[] sb;
	public byte[] s1;
	public byte[] s2;

	public ECPoint keyra;
	public ECPoint keyrb;
}
