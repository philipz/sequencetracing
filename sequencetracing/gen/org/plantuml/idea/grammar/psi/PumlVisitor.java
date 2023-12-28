// This is a generated file. Not intended for manual editing.
package org.plantuml.idea.grammar.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PumlVisitor extends PsiElementVisitor {

    public void visitInclude(@NotNull PumlInclude o) {
        visitNavigatablePsiElement(o);
    }

    public void visitItem(@NotNull PumlItem o) {
        visitNamedElement(o);
    }

    public void visitNavigatablePsiElement(@NotNull NavigatablePsiElement o) {
        visitElement(o);
    }

    public void visitNamedElement(@NotNull PumlNamedElement o) {
        visitPsiElement(o);
    }

    public void visitPsiElement(@NotNull PsiElement o) {
        visitElement(o);
    }

}
