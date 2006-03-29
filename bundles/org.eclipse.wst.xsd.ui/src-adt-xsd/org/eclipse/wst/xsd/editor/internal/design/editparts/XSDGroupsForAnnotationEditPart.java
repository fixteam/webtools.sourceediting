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
package org.eclipse.wst.xsd.editor.internal.design.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.wst.xsd.adt.design.editparts.BaseEditPart;
import org.eclipse.wst.xsd.adt.design.editparts.model.Annotation;
import org.eclipse.wst.xsd.adt.facade.IComplexType;
import org.eclipse.wst.xsd.adt.facade.IStructure;
import org.eclipse.wst.xsd.editor.internal.adapters.XSDComplexTypeDefinitionAdapter;
import org.eclipse.wst.xsd.editor.internal.adapters.XSDAdapterFactory;
import org.eclipse.wst.xsd.editor.internal.adapters.XSDModelGroupDefinitionAdapter;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;

public class XSDGroupsForAnnotationEditPart extends BaseEditPart
{
  IComplexType complexType;
  public XSDGroupsForAnnotationEditPart()
  {
    super();
  }

  protected IFigure createFigure()
  {
    Figure fig = new Figure();
    fig.setLayoutManager(new ToolbarLayout());
    return fig;
  }

  protected void createEditPolicies()
  {

  }

  protected List getModelChildren()
  {
    List xsdModelGroupList = new ArrayList();
    List adapterList = new ArrayList();
    
    IStructure structure =  ((Annotation)getModel()).getOwner();
    if (structure instanceof IComplexType)
    {  
      complexType = (IComplexType)structure;
      if (complexType instanceof XSDComplexTypeDefinitionAdapter)
      {
        XSDComplexTypeDefinitionAdapter adapter = (XSDComplexTypeDefinitionAdapter) complexType;
        xsdModelGroupList = adapter.getModelGroups();
      }
      
      for (Iterator i = xsdModelGroupList.iterator(); i.hasNext(); )
      {
        Object obj = i.next();
        if (obj instanceof XSDModelGroup)
        {
          adapterList.add(XSDAdapterFactory.getInstance().adapt((XSDModelGroup)obj));
        }
        else if (obj instanceof XSDModelGroupDefinition)
        {
          adapterList.add(XSDAdapterFactory.getInstance().adapt((XSDModelGroupDefinition)obj));
        }
      }
    }
    else if (structure instanceof XSDModelGroupDefinitionAdapter)
    {
      XSDModelGroupDefinitionAdapter adapter = (XSDModelGroupDefinitionAdapter) structure;
      XSDModelGroup group = adapter.getXSDModelGroupDefinition().getModelGroup();
      if (group != null)
      {
        adapterList.add(XSDAdapterFactory.getInstance().adapt(group));
      }
    }
    
    return adapterList;
  }
}
