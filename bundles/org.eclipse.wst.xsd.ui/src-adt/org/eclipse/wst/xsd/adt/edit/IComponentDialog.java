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
package org.eclipse.wst.xsd.adt.edit;

import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;

public interface IComponentDialog  {
  
	/*
	 * Set the Object being set
	 */
	public void setInitialSelection(ComponentSpecification componentSpecification);
	
	/*
	 * Return the Object which should be used as the type.
	 */
	public ComponentSpecification getSelectedComponent();
	
	/*
	 * Used to open the Dialog
	 */
	public int createAndOpen();
}