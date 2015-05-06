package com.manydesigns.portofino.shiro;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.format.HashFormat;

public class PlaintextHashFormat implements HashFormat {

	@Override
	public String format(Hash hash) {
		return new String(hash.getBytes());
	}
}