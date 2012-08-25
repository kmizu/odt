/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005, Kota Mizushima, All rights reserved.       *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package org.onion_lang.odt.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IColorManagerExtension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class OnionColorManager implements IColorManager, IColorManagerExtension {
  protected Map keys = new HashMap(10);
  protected Map displays = new HashMap(2);

  public OnionColorManager() {
  }

  public void dispose(Display display) {
    Map colors = (Map) displays.get(display);
    if (colors != null) {
      Iterator e = colors.values().iterator();
      while (e.hasNext()) {
        Color color = (Color) e.next();
        if (color != null && !color.isDisposed())
          color.dispose();
      }
    }
  }

  /*
   * @see IColorManager#getColor(RGB)
   */
  public Color getColor(RGB rgb) {
    if (rgb == null)
      return null;

    final Display display = Display.getCurrent();
    Map colors = (Map) displays.get(display);
    if (colors == null) {
      colors = new HashMap(10);
      displays.put(display, colors);
      display.disposeExec(new Runnable() {
        public void run() {
          dispose(display);
        }
      });
    }

    Color color = (Color) colors.get(rgb);
    if (color == null) {
      color = new Color(Display.getCurrent(), rgb);
      colors.put(rgb, color);
    }

    return color;
  }

  public void dispose() {/* NOTHING TO DO */
  }

  /*
   * (”ñ Javadoc)
   * 
   * @see org.eclipse.jdt.ui.text.IColorManager#getColor(java.lang.String)
   */
  public Color getColor(String key) {
    if (key == null)
      return null;

    RGB rgb = (RGB) keys.get(key);
    return getColor(rgb);
  }

  /*
   * (”ñ Javadoc)
   * 
   * @see org.eclipse.jdt.ui.text.IColorManagerExtension#bindColor(java.lang.String,
   *      org.eclipse.swt.graphics.RGB)
   */
  public void bindColor(String key, RGB rgb) {
    Object value = keys.get(key);
    if (value != null)
      throw new UnsupportedOperationException();
    keys.put(key, rgb);
  }

  /*
   * (”ñ Javadoc)
   * 
   * @see org.eclipse.jdt.ui.text.IColorManagerExtension#unbindColor(java.lang.String)
   */
  public void unbindColor(String key) {
    keys.remove(key);
  }
}
