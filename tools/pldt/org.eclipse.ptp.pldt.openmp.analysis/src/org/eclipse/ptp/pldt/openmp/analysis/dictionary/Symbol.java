/**********************************************************************
 * Copyright (c) 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.pldt.openmp.analysis.dictionary;

import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IScope;


/**
 * Holds a symbol definition
 * @author pazel
 *
 */
public class Symbol
{
    protected IASTDeclarator      declarator_  = null;  // what is being defined
    // below can be either IASTDeclaration or IASTParameterDeclaration
    protected IASTNode            declaration_ = null;  // broader containing statement
    
    /**
     * Symbol - constructor
     * @param declarator  - IASTDeclarator
     * @param declaration - IASTDeclartion (for this declarator - and possibly others
     */
    public Symbol( IASTDeclarator declarator, IASTDeclaration declaration)
    {
        declarator_  = declarator;
        declaration_ = declaration;
    }
    
    /**
     * Symbol - constructor
     * @param declarator  - IASTDeclarator
     * @param declaration - IASTParameterDeclartion (for this declarator - and possibly others
     */
    public Symbol( IASTDeclarator declarator, IASTParameterDeclaration declaration)
    {
        declarator_  = declarator;
        declaration_ = declaration;
    }
    
    /**
     * getName - get the name of the declarator
     * @return IASTName
     */
    public IASTName getName()
    {
        return declarator_.getName();
    }
    
    /**
     * getDeclartor - accessor to declarator
     * @return - IASTDeclarator
     */
    public IASTDeclarator getDeclarator()
    {
        return declarator_;
    }
    
    /**
     * getDeclaration - get the related declaration
     * @return - IASTNode  (can be IASTDeclaration or IASTParameterDeclaration)
     */
    public IASTNode getDeclaration()
    {
        return declaration_;
    }
    
    /**
     * getScope - get the scope for this declarator
     * @return IScope
     */
    public IScope getScope()
    {
        // find the unique scope for this name
        IASTName name = declarator_.getName();
        name.resolveBinding();
        IBinding binding = name.getBinding();
        if (binding==null)  return null;
        
        IScope scope = null;
        try {
            scope = binding.getScope();
        }
        catch(DOMException e) { System.out.println("SymbolBucket.find: exception "+e);  return null; }
        
        return scope;
    }
    
    /**
     * getDefiningFunction - get the function in which declartor is defined
     * @return IASTNode - either IASTTranslationUnit or IASTFunctionDefinition
     */
    public IASTNode getDefiningFunction()
    {
        IScope scope = getScope();
        if (scope==null)  return null;
        
        IASTNode node = null;
        try {
            node = scope.getPhysicalNode();
        }
        catch(DOMException e) { 
            System.out.println("Symbol.getFunction exception "+e);
            return null;
        }
        
        // keep moving up the tree until we find the node
        while(true) {
            if (node==null)  return null;
            if (node instanceof IASTTranslationUnit) return node;      // global dict
            if (node instanceof IASTFunctionDefinition)  return node;  // our function
            node = node.getParent();
        }
    }
}

