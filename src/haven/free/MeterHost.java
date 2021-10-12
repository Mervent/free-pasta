package haven.free;

import haven.*;

import java.util.*;


public class MeterHost {
        private List<Widget> meters = new LinkedList<Widget>();
        private GameUI ui;

        public MeterHost(GameUI ui) {
                this.ui = ui;
        }

        public void addcmeter(Widget meter) {
                ui.ulpanel.add(meter);
                meters.add(meter);
                updcmeters();
        }
        
        public <T extends Widget> void delcmeter(Class<T> cl) {
                Widget widget = null;
                for (Widget meter : meters) {
                        if (cl.isAssignableFrom(meter.getClass())) {
                                widget = meter;
                                break;
                        }
                }
                if (widget != null) {
                        meters.remove(widget);
                        widget.destroy();
                        updcmeters();
                }
        }
        
        public void updcmeters() {
                int i = ui.meters.size();
                for (Widget meter : meters) {
                        int x = ( i % 3) * (IMeter.fsz.x + 5);
                        int y = (i / 3) * (IMeter.fsz.y + 2);
                        meter.c = new Coord(ui.portrait.c.x + ui.portrait.sz.x + 10 + x, ui.portrait.c.y + y);
                        i++;
                }
        }
}
