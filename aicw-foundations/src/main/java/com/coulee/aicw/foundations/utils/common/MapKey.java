package com.coulee.aicw.foundations.utils.common;

import java.io.Serializable;
/**
 * map主键构造
 * @author zyj
 *
 */
public class MapKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object keys[];

	public int hashCode() {
		int h = 0;
		for (int i = 0; i < keys.length; i++)
			h = 31 * h + (keys[i] != null ? keys[i].hashCode() : 0);

		return h;
	}

	public void setKeys(Object keys[]) {
		this.keys = keys;
	}

	public Object[] getKeys() {
		return keys;
	}

	public MapKey(Object keys[]) {
		this.keys = keys;
	}

	public boolean equals(Object o) {
		if (o instanceof MapKey) {
			MapKey aimKey = (MapKey) o;
			// if (getKeys() == null && aimKey == null) {
			// return true;
			if (getKeys() == null || aimKey == null) {
				return false;
			}

			if (getKeys().length != aimKey.getKeys().length) {
				return false;
			}
			boolean equ = true;
			for (int i = 0; i < getKeys().length; i++) {
				if (getKeys()[i] == null) {
					equ = aimKey.getKeys()[i] == null;
				}

				else {
					equ = getKeys()[i].equals(aimKey.getKeys()[i]);
				}

				if (!equ) {
					return false;
				}

			}

			return equ;
		} else {
			return super.equals(o);
		}
	}

	public Object getValue(int col) {
		return keys[col];
	}
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(keys == null)
        {
        	return "";
        }
        for(Object temp :keys )
        {
        	sb.append(""+temp);
        }
        return sb.toString();
	}
}
