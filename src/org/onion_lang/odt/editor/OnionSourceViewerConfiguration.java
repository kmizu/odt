/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005, Kota Mizushima, All rights reserved.       *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package org.onion_lang.odt.editor;

import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class OnionSourceViewerConfiguration extends SourceViewerConfiguration {
  private IColorManager manager;
  private ITokenScanner scanner;

  public OnionSourceViewerConfiguration(IColorManager manager) {
    this.manager = manager;
    this.scanner = new OnionTokenScanner(manager);
  }

  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
    return new String[] {IDocument.DEFAULT_CONTENT_TYPE};
  }

  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler reconciler = new PresentationReconciler();

    DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(scanner);
    reconciler.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);
    reconciler.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);

    return reconciler;
  }
}
