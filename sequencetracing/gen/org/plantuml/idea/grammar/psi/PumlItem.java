// This is a generated file. Not intended for manual editing.
package org.plantuml.idea.grammar.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface PumlItem extends PumlNamedElement {

    String getName();

    PsiElement setName(String newName);

    PsiElement getNameIdentifier();

    ItemPresentation getPresentation();

    PsiReference getReference();

}
