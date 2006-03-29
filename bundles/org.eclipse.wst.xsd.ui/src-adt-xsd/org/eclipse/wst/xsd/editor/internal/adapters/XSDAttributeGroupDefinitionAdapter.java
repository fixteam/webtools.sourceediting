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
package org.eclipse.wst.xsd.editor.internal.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xsd.adt.actions.BaseSelectionAction;
import org.eclipse.wst.xsd.adt.design.editparts.model.IActionProvider;
import org.eclipse.wst.xsd.adt.facade.IModel;
import org.eclipse.wst.xsd.adt.facade.IStructure;
import org.eclipse.wst.xsd.adt.outline.ITreeElement;
import org.eclipse.wst.xsd.editor.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.common.actions.AddXSDAttributeDeclarationAction;
import org.eclipse.wst.xsd.ui.common.actions.DeleteXSDConcreteComponentAction;
import org.eclipse.xsd.XSDAttributeGroupDefinition;
import org.eclipse.xsd.XSDWildcard;

public class XSDAttributeGroupDefinitionAdapter extends XSDBaseAdapter implements IStructure, IActionProvider
{

  public XSDAttributeGroupDefinitionAdapter()
  {
    super();
  }

  public XSDAttributeGroupDefinition getXSDAttributeGroupDefinition()
  {
    return (XSDAttributeGroupDefinition) target;
  }

  public Image getImage()
  {
    XSDAttributeGroupDefinition xsdAttributeGroupDefinition = (XSDAttributeGroupDefinition) target;
    if (xsdAttributeGroupDefinition.isAttributeGroupDefinitionReference())
    {
      return XSDEditorPlugin.getXSDImage("icons/XSDAttributeGroupRef.gif");
    }
    else
    {
      return XSDEditorPlugin.getXSDImage("icons/XSDAttributeGroup.gif");
    }
  }

  public String getText()
  {
    XSDAttributeGroupDefinition xsdAttributeGroupDefinition = (XSDAttributeGroupDefinition) target;
    String result = xsdAttributeGroupDefinition.isAttributeGroupDefinitionReference() ? xsdAttributeGroupDefinition.getQName() : xsdAttributeGroupDefinition.getName();
    return result == null ? "'absent'" : result;
  }

  public ITreeElement[] getChildren()
  {
    XSDAttributeGroupDefinition xsdAttributeGroup = (XSDAttributeGroupDefinition) target;
    List list = new ArrayList();
    list.addAll(xsdAttributeGroup.getContents());
    XSDWildcard wildcard = xsdAttributeGroup.getAttributeWildcardContent();
    if (wildcard != null)
    {
      list.add(wildcard);
    }
    List adapterList = new ArrayList();
    populateAdapterList(list, adapterList);
    return (ITreeElement[]) adapterList.toArray(new ITreeElement[0]);
  }
  
  public String[] getActions(Object object)
  {
    List list = new ArrayList();
    list.add(AddXSDAttributeDeclarationAction.ID);
    list.add(BaseSelectionAction.SEPARATOR_ID);
    list.add(DeleteXSDConcreteComponentAction.DELETE_XSD_COMPONENT_ID);
    return (String [])list.toArray(new String[0]);
  }

  public Command getAddNewFieldCommand(String fieldKind)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Command getDeleteCommand()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List getFields()
  {
    // TODO (cs) ... review this    
    ITreeElement[] chidlren = getChildren();
    return Arrays.asList(chidlren);
  }

  public IModel getModel()
  {
    Adapter adapter = XSDAdapterFactory.getInstance().adapt(getXSDAttributeGroupDefinition().getSchema());
    return (IModel)adapter;
  }

  public String getName()
  {
    // TODO (cs) ... review this
    return getText();
  }
}
