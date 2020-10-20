package haven.purus.customres.ui.tt.q.quality;

/* Preprocessed source code */
/* $use: ui/tt/q/qbuff */
import haven.*;
import haven.purus.Config;
import haven.purus.customres.ui.tt.q.qbuff.QBuff;
import java.awt.Color;
import java.awt.image.BufferedImage;

/* >tt: Quality */
public class Quality extends QBuff implements GItem.OverlayInfo<Tex> {
	public static boolean show = false;
	private static BufferedImage icon = Resource.remote().loadwait("ui/tt/q/quality").layer(Resource.imgc, 0).scaled();

	public Quality(Owner owner, double q) {
		super(owner, icon, "Quality", q);
	}

	public static class Fac implements ItemInfo.InfoFactory {
		public Fac() {}
		public ItemInfo build(Owner owner, Object... args) {
			return Quality.mkinfo(owner, args);
		}
	}

	public static ItemInfo mkinfo(Owner owner, Object... args) {
		return(new Quality(owner, ((Number)args[1]).doubleValue()));
	}

	public Tex overlay() {
		return(new TexI(GItem.NumberInfo.numrender((int)Math.round(q), new Color(255, 255, 255, 255))));
	}

	public void drawoverlay(GOut g, Tex ol) {
		if(Config.displayQuality.val)
			g.aimage(ol, new Coord(0, g.sz().y), 0, 1);
	}
}

/* >pagina: ShowQuality$Fac */