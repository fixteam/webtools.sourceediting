/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.internal.actions;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.common.contentmodel.util.DOMNamespaceInfoManager;
import org.eclipse.wst.common.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.eclipse.wst.xml.ui.actions.ReplacePrefixAction;
import org.eclipse.wst.xml.ui.util.XMLCommonResources;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.SchemaPrefixChangeHandler;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.TargetNamespaceChangeHandler;
import org.eclipse.wst.xsd.ui.internal.util.XSDSchemaHelper;
import org.eclipse.wst.xsd.ui.internal.widgets.XSDEditSchemaInfoDialog;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class XSDEditNamespacesAction extends Action {
	private Element element;
	private Node node;
	private String resourceLocation;
	private XSDSchema xsdSchema;
    private DOMNamespaceInfoManager namespaceInfoManager = new DOMNamespaceInfoManager();
	
	public XSDEditNamespacesAction(String label, Element element, Node node) {
		super();
		setText(label);
		
		this.element = element;
		this.node = node;
		///////////////////// This needs to be changed....
		this.resourceLocation = "dummy";		
	}
	
	public XSDEditNamespacesAction(String label, Element element, Node node, XSDSchema schema) {
		this (label, element, node);
		xsdSchema = schema;
	}
	
	public void run() {
		if (element != null)
		{   
		      Shell shell = XMLCommonResources.getInstance().getWorkbench().getActiveWorkbenchWindow().getShell();
		      String targetNamespace = null;
		      if (xsdSchema != null) {
		      	targetNamespace = xsdSchema.getTargetNamespace();
		      }
		      XSDEditSchemaInfoDialog dialog = new XSDEditSchemaInfoDialog(shell, new Path(resourceLocation), targetNamespace); 

		      List namespaceInfoList = namespaceInfoManager.getNamespaceInfoList(element);
		      List oldNamespaceInfoList = NamespaceInfo.cloneNamespaceInfoList(namespaceInfoList);

		      // here we store a copy of the old info for each NamespaceInfo
		      // this info will be used in createPrefixMapping() to figure out how to update the document 
		      // in response to these changes
		      for (Iterator i = namespaceInfoList.iterator(); i.hasNext(); )
		      {
		        NamespaceInfo info = (NamespaceInfo)i.next();
		        NamespaceInfo oldCopy = new NamespaceInfo(info);
		        info.setProperty("oldCopy", oldCopy);
		      }
		                              
		      dialog.setNamespaceInfoList(namespaceInfoList);   
		      dialog.create();      
		      //dialog.getShell().setSize(500, 300);
		      dialog.getShell().setText(XMLCommonResources.getInstance().getString("_UI_MENU_EDIT_SCHEMA_INFORMATION_TITLE"));
		      dialog.setBlockOnOpen(true);                                 
		      dialog.open();
          String xsdPrefix = "";    

		      if (dialog.getReturnCode() == Window.OK)
		      {
            Element xsdSchemaElement = xsdSchema.getElement();
            DocumentImpl doc = (DocumentImpl) xsdSchemaElement.getOwnerDocument();
            
            List newInfoList = dialog.getNamespaceInfoList();

		        // see if we need to rename any prefixes
		        Map prefixMapping = createPrefixMapping(oldNamespaceInfoList, namespaceInfoList);
            
            Map map2 = new Hashtable();
            String xsdNS = "";
            for (Iterator iter = newInfoList.iterator(); iter.hasNext(); )
            {
              NamespaceInfo ni = (NamespaceInfo)iter.next();
              String pref = ni.prefix;
              String uri = ni.uri;
              if (pref == null) pref = "";
              if (uri == null) uri = "";
              if (XSDConstants.isSchemaForSchemaNamespace(uri))
              {
                xsdPrefix = pref;
              }
              map2.put(pref, uri);
            }
           
		        if (map2.size() > 0)
		        {
		        	try {
                
                doc.getModel().beginRecording(this, XSDEditorPlugin.getXSDString("_UI_NAMESPACE_CHANGE"));

                if (xsdPrefix != null && xsdPrefix.length() == 0)
                {
                  xsdSchema.setSchemaForSchemaQNamePrefix(null);
                }
                else
                {
                  xsdSchema.setSchemaForSchemaQNamePrefix(xsdPrefix);
                }
                
                SchemaPrefixChangeHandler spch = new SchemaPrefixChangeHandler(xsdSchema, xsdPrefix);
                spch.resolve();

                xsdSchema.setTargetNamespace(dialog.getTargetNamespace());
                
                namespaceInfoManager.removeNamespaceInfo(element);
                namespaceInfoManager.addNamespaceInfo(element, newInfoList, false);
               
//                manager.getModel().aboutToChangeModel();
			          ReplacePrefixAction replacePrefixAction = new ReplacePrefixAction(null, element, prefixMapping);
			          replacePrefixAction.run();
                
                TargetNamespaceChangeHandler targetNamespaceChangeHandler = new TargetNamespaceChangeHandler(xsdSchema, targetNamespace, dialog.getTargetNamespace());
                targetNamespaceChangeHandler.resolve();

				    	} catch (Exception e){ e.printStackTrace(); }finally {
//				      manager.getModel().changedModel();

                XSDSchemaHelper.updateElement(xsdSchema);
                doc.getModel().endRecording(this);
			     		}
		        }
		   }      
          
		}
	}
	
	 protected Map createPrefixMapping(List oldList, List newList)
	  {          
	    Map map = new Hashtable();

	    Hashtable oldURIToPrefixTable = new Hashtable();
	    for (Iterator i = oldList.iterator(); i.hasNext(); )
	    {    
	      NamespaceInfo oldInfo = (NamespaceInfo)i.next();                    
	      oldURIToPrefixTable.put(oldInfo.uri, oldInfo);
	    }
	    
	    for (Iterator i = newList.iterator(); i.hasNext(); )
	    {
	      NamespaceInfo newInfo = (NamespaceInfo)i.next();
	      NamespaceInfo oldInfo = (NamespaceInfo)oldURIToPrefixTable.get(newInfo.uri != null ? newInfo.uri : ""); 


	      // if oldInfo is non null ... there's a matching URI in the old set
	      // we can use its prefix to detemine out mapping
	      //
	      // if oldInfo is null ...  we use the 'oldCopy' we stashed away 
	      // assuming that the user changed the URI and the prefix
	      if (oldInfo == null)                                            
	      {
	        oldInfo = (NamespaceInfo)newInfo.getProperty("oldCopy");           
	      } 

	      if (oldInfo != null)
	      {
	        String newPrefix = newInfo.prefix != null ? newInfo.prefix : "";
	        String oldPrefix = oldInfo.prefix != null ? oldInfo.prefix : "";
	        if (!oldPrefix.equals(newPrefix))
	        {
	          map.put(oldPrefix, newPrefix);    
	        }
	      }      
	    }        
	    return map;
	  }
   
//    private void updateAllNodes(Element element, String prefix)
//    {
//      element.setPrefix(prefix);
//      NodeList list = element.getChildNodes();
//      if (list != null)
//      {
//        for (int i=0; i < list.getLength(); i++)
//        {
//          Node child = list.item(i);
//          if (child != null && child instanceof Element)
//          {
//            child.setPrefix(prefix);
//            if (child.hasChildNodes())
//            {
//              updateAllNodes((Element)child, prefix);
//            }
//          }
//        }
//      }   
//    }

}
