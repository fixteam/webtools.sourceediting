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
package org.eclipse.wst.xsd.ui.common.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.xsd.editor.internal.adapters.XSDBaseAdapter;
import org.eclipse.wst.xsd.ui.common.commands.SetMultiplicityCommand;
import org.eclipse.xsd.XSDConcreteComponent;

public class SetMultiplicityAction extends XSDBaseAction
{
  public static String REQUIRED_ID = "org.eclipse.wst.xsd.ui.common.actions.SetMultiplicity.REQUIRED_ID";
  public static String ZERO_OR_ONE_ID = "org.eclipse.wst.xsd.ui.common.actions.SetMultiplicity.ZERO_OR_ONE_ID";
  public static String ZERO_OR_MORE_ID = "org.eclipse.wst.xsd.ui.common.actions.SetMultiplicity.ZERO_OR_MORE_ID";
  public static String ONE_OR_MORE_ID = "org.eclipse.wst.xsd.ui.common.actions.SetMultiplicity.ONE_OR_MORE_ID";
  
  SetMultiplicityCommand command;
  
  public SetMultiplicityAction(IWorkbenchPart part, String label, String ID)
  {
    super(part);
    setText(label);
    setId(ID);
    command = new SetMultiplicityCommand(label);
  }
  
  public void setMaxOccurs(int i)
  {
    command.setMaxOccurs(i);
  }

  public void setMinOccurs(int i)
  {
    command.setMinOccurs(i);
  }

  public void run()
  {
    Object selection = ((IStructuredSelection) getSelection()).getFirstElement();

    XSDConcreteComponent xsdConcreteComponent = null;
    if (selection instanceof XSDBaseAdapter)
    {
      xsdConcreteComponent = (XSDConcreteComponent)((XSDBaseAdapter) selection).getTarget();
    }
    if (xsdConcreteComponent != null)
    {
      command.setXSDConcreteComponent(xsdConcreteComponent);
      getCommandStack().execute(command);
    }
  }
}
