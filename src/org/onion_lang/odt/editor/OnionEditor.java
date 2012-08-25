/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005, Kota Mizushima, All rights reserved.       *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package org.onion_lang.odt.editor;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * @author Kota Mizushima Date: 2005/03/31
 */
public class OnionEditor extends TextEditor {
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
    super.init(site, input);
    JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
    setSourceViewerConfiguration(new OnionSourceViewerConfiguration(tools.getColorManager()));
  }
}
