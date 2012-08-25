/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005, Kota Mizushima, All rights reserved.       *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package org.onion_lang.odt.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class OnionPartitionScanner extends RuleBasedPartitionScanner {
  public OnionPartitionScanner() {
    setRules(makeRules());
  }

  private IRule[] makeRules() {
    List rules = new ArrayList();

    /*
     * TODO ÉãÅ[ÉãÇí«â¡Ç∑ÇÈ
     */
    return (IRule[]) rules.toArray(new IRule[0]);
  }
}
