package haven.purus;

import haven.KeyBinding;
import haven.KeyMatch;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

public class Config {
	public static final boolean iswindows = System.getProperty("os.name").startsWith("Windows");
	public static Preferences pref = SQLPreferences.factory.userRoot().node("puruspasta");

	public static class Setting<T> {
		public T val;
		public String name;

		public Setting(String name, T defaultVal) {
			this.name = name;
			this.val = defaultVal;
			getVal();
		}

		private void getVal() {
			if(val instanceof Boolean) {
				this.val = (T) val.getClass().cast(pref.getBoolean(name, (Boolean) val));
			} else if(val instanceof String) {
				this.val = (T) val.getClass().cast(pref.get(name, (String) val));
			} else if(val instanceof Integer) {
				this.val = (T) val.getClass().cast(pref.getInt(name, (Integer) val));
			} else if(val instanceof Float) {
				this.val = (T) val.getClass().cast(pref.getFloat(name, (Float) val));
			} else if(val instanceof Serializable) {
				try {
					byte[] arr = pref.getByteArray(name, null);
					if(arr == null)
						return;
					ByteArrayInputStream bis = new ByteArrayInputStream(arr);
					ObjectInputStream ois = new ObjectInputStream(bis);
					this.val = (T) val.getClass().cast(ois.readObject());
				} catch(IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				throw(new RuntimeException("Cannot get unknown type " + val.getClass() + " to config!"));
			}
		}

		public void setVal(T val) {
			this.val = val;
			if(val instanceof Boolean) {
				pref.putBoolean(name, (Boolean) val);
			} else if(val instanceof String) {
				pref.put(name, (String) val);
			} else if(val instanceof Integer) {
				pref.putInt(name, (Integer) val);
			} else if(val instanceof Float) {
				pref.putFloat(name, (Float) val);
			} else if(val instanceof Serializable) {
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(bos);
					oos.writeObject(val);
					pref.putByteArray(name, bos.toByteArray());
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else {
				throw(new RuntimeException("Cannot set unknown type " + val.getClass() + " to config!"));
			}
			this.val = val;
		}
	}

	// Name of the variable and preference key should always be the same
	public static Setting<Boolean> toggleTracking = new Setting<>("toggleTracking", true);
	public static Setting<Boolean> toggleCriminalacts = new Setting<>("toggleCriminalacts", false);
	public static Setting<Boolean> toggleSiege = new Setting<>("toggleSiege", true);
	public static Setting<Boolean> hwcursor = new Setting<>("hwcursor", true);
	public static Setting<Boolean> debugRescode = new Setting<>("debugRescode", false);
	public static Setting<Boolean> debugWdgmsg = new Setting<>("debugWdgmsg", false);
	public static Setting<Boolean> displayQuality = new Setting<>("displayQuality", true);
	public static Setting<Float> cameraScrollSensitivity = new Setting<>("cameraScrollSensitivity", 1.0f);
	public static Setting<Boolean> tileGrid = new Setting<>("tileGrid", false);
	public static Setting<Boolean> showGobDecayNum = new Setting<>("showGobDecayNum", true);
	public static Setting<Boolean> growthStages = new Setting<>("growthStages", false);
	public static Setting<Integer> speedOnLogin = new Setting<>("speedOnLogin", 2);
	public static Setting<Float> flowermenuSpeed = new Setting<>("flowermenuSpeed", 0.25f);
	public static Setting<String> mapperToken = new Setting<>("mapperToken", "");
	public static Setting<Integer> bbDisplayState = new Setting<>("bbDisplayState", 0);
	public static Setting<Boolean> ttfHighlight = new Setting<>("ttfHighlight", true);
	public static Setting<Boolean> invShowLogin = new Setting<>("invShowLogin", true);
	public static Setting<Boolean> beltShowLogin = new Setting<>("beltShowLogin", true);
	public static Setting<Boolean> studyLock = new Setting<>("studyLock", false);


	public static Setting<Boolean> pathfinder = new Setting<>("pathfinder", false);


	public static Setting<ConcurrentHashMap<String, Boolean>> flowerOptOpens = new Setting<>("flowerOptOpens", new ConcurrentHashMap<>());
	public static Setting<ConcurrentHashMap<String, Float>> customVolumes = new Setting<>("customVolumes", new ConcurrentHashMap<>());

	public static Setting<Boolean> proximityPlayerAggro = new Setting<>("proximityPlayerAggro", false);
	public static Setting<Boolean> proximityKritterAggro = new Setting<>("proximityKritterAggro", true);


	public static Setting<Boolean> resinfo = new Setting<>("resinfo", false);

	public static Setting<ArrayList<String>> scriptsKeybinded = new Setting<>("scriptsKeybinded", new ArrayList<>());

	public static KeyBinding kb_growthStages = KeyBinding.get("kb_growthStages", KeyMatch.forcode(KeyEvent.VK_P, KeyMatch.C));
	public static KeyBinding kb_resinfo = KeyBinding.get("kb_resinfo", KeyMatch.forcode(KeyEvent.VK_I, KeyMatch.S));

	public static KeyBinding kb_bbtoggle = KeyBinding.get("kb_bbtoggle", KeyMatch.forcode(KeyEvent.VK_B, KeyMatch.S));
	public static KeyBinding kb_pathfinder = KeyBinding.get("kb_pathfinder", KeyMatch.forcode(KeyEvent.VK_S, KeyMatch.C));

}
