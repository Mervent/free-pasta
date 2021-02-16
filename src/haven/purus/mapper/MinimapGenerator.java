package haven.purus.mapper;

import haven.*;
import haven.resutil.Ridges;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author APXEOLOG (Artyom Melnikov), at 28.01.2019
 */
public class MinimapGenerator {

	private static String tileres(int t, MCache map) {
		Resource res = map.tilesetr(t);
		return (map.tilesetr(t) == null ? null : res.name);
	}

	public static class Tileinfo {

		int[][] tiles = new int[MCache.cmaps.x][MCache.cmaps.y];
		List<Coord> ridges = new ArrayList<>();
		HashMap<Integer, String> tileset = new HashMap<>();

		public Tileinfo(MCache map, MCache.Grid grid) {
			for(int y=0; y<MCache.cmaps.y; y++) {
				for(int x=0; x<MCache.cmaps.x; x++){
					tiles[x][y] = grid.tiles[y*MCache.cmaps.x + x];
				}
			}
			Coord c = new Coord();
			for (c.y = 0; c.y < MCache.cmaps.y; c.y++) {
				for (c.x = 0; c.x < MCache.cmaps.x; c.x++) {
					int t = grid.gettile(c);
					if(!tileset.containsKey(t))
						tileset.put(t, tileres(t, map));
				}
			}
			for (c.y = 1; c.y < MCache.cmaps.y - 1; c.y++) {
				for (c.x = 1; c.x < MCache.cmaps.x - 1; c.x++) {
					int t = grid.gettile(c);
					Tiler tl = map.tiler(t);
					if ((tl instanceof Ridges.RidgeTile && Ridges.brokenp(map, c, grid)))
						ridges.add(new Coord(c));
				}
			}
		}
	}

	public static Tileinfo getTileinfo(MCache map, MCache.Grid grid) {
		return new Tileinfo(map, grid);
	}

	private static BufferedImage tileimg(int t, BufferedImage[] texes, MCache map) {
		BufferedImage img = texes[t];
		if (img == null) {
			Resource r = map.tilesetr(t);
			if (r == null)
				return (null);
			Resource.Image ir = r.layer(Resource.imgc);
			if (ir == null)
				return (null);
			img = ir.img;
			texes[t] = img;
		}
		return (img);
	}

	public static BufferedImage drawmap(MCache map, MCache.Grid grid) {
		BufferedImage[] texes = new BufferedImage[256];
		BufferedImage buf = TexI.mkbuf(MCache.cmaps);
		Coord c = new Coord();
		for (c.y = 0; c.y < MCache.cmaps.y; c.y++) {
			for (c.x = 0; c.x < MCache.cmaps.x; c.x++) {
				BufferedImage tex = tileimg(grid.gettile(c), texes, map);
				int rgb = 0;
				if (tex != null)
					rgb = tex.getRGB(Utils.floormod(c.x, tex.getWidth()),
							Utils.floormod(c.y, tex.getHeight()));
				buf.setRGB(c.x, c.y, rgb);
			}
		}
		for (c.y = 1; c.y < MCache.cmaps.y - 1; c.y++) {
			for (c.x = 1; c.x < MCache.cmaps.x - 1; c.x++) {
				int t = grid.gettile(c);
				Tiler tl = map.tiler(t);
				if (tl instanceof Ridges.RidgeTile) {
					if (Ridges.brokenp(map, c, grid)) {
						for (int y = c.y - 1; y <= c.y + 1; y++) {
							for (int x = c.x - 1; x <= c.x + 1; x++) {
								Color cc = new Color(buf.getRGB(x, y));
								buf.setRGB(x, y, Utils.blendcol(cc, Color.BLACK, ((x == c.x) && (y == c.y)) ? 1 : 0.1).getRGB());
							}
						}
					}
				}
			}
		}
		for (c.y = 0; c.y < MCache.cmaps.y; c.y++) {
			for (c.x = 0; c.x < MCache.cmaps.x; c.x++) {
				try {
					int t = grid.gettile(c);
					Coord r = c.add(grid.ul);
					if ((map.gettile(r.add(-1, 0)) > t) ||
							(map.gettile(r.add(1, 0)) > t) ||
							(map.gettile(r.add(0, -1)) > t) ||
							(map.gettile(r.add(0, 1)) > t)) {
						buf.setRGB(c.x, c.y, Color.BLACK.getRGB());
					}
				} catch (Exception e) {
				}
			}
		}
		return buf;
	}
}
