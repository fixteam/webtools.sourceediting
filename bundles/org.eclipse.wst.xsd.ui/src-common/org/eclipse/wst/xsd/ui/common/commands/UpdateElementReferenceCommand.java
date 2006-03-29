/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.common.commands;

import org.eclipse.xsd.XSDElementDeclaration;

public class UpdateElementReferenceCommand extends BaseCommand
{
  XSDElementDeclaration element, ref;

  public UpdateElementReferenceCommand(String label, XSDElementDeclaration element, XSDElementDeclaration ref)
  {
    super(label);
    this.element = element;
    this.ref = ref;
  }

  public void execute()
  {
    element.setResolvedElementDeclaration(ref);
  }
  
}
