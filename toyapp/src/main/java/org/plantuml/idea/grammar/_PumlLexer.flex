package org.plantuml.idea.grammar;
                                  
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.plantuml.idea.grammar.psi.PumlTypes;

%%

%{
  public PumlLexer() {
    this((java.io.Reader)null);
  }
  
  private IElementType itemType() {
       if(org.plantuml.idea.util.Utils.containsLettersOrNumbers(yytext())) {
            return PumlTypes.IDENTIFIER; 
        } else {
            return PumlTypes.OTHER; 
        }
  }
%}

%public
%class PumlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
            
LINE_COMMENT=\s*'[^\r\n]*
BLOCK_COMMENT="/'"[^'/]*"'/"           

BRACKET_1=\[[^\]\r\n]+\]     // [foo bar]
BRACKET_2=\([^\)\r\n,]+\)  //without ',' -> do not eat multiple items: (ProductOfferingPrice, ProductUsageSpec) 
DIVIDER===[^=]+==         

QUOTE_1=\"[^\"\r\n]+\"  // "foo bar"
//QUOTE_2=\'[^\'\r\n]+\'  // 'foo bar'  - perhaps better without it, it breaks brackets - [x]a'[y]b' (gantt sample) 
                      
COMPLEX_WORD=[_\p{L}0-9@&%$#][._\-'\p{L}0-9@&%$#]*[\p{L}0-9@&%$#]
WORD_CHARACTER=[\p{L}0-9]
PREPROC="!"[A-Za-z]+
//TAG=@[A-Za-z]+
SPECIAL_CHARACTER=[^\p{L}0-9\s/\[\(\"'=!]   //except quotes, brackets start,...
NEW_LINE=\R
WHITE_SPACE=[\ \t\f]

%xstate LINE_START_STATE,IN_COMMENT
   
%%  


<LINE_START_STATE>{LINE_COMMENT}                                       { yybegin(YYINITIAL);        return PumlTypes.COMMENT; }
<YYINITIAL, LINE_START_STATE>{BLOCK_COMMENT}                           { yybegin(YYINITIAL);        return PumlTypes.COMMENT; }
<YYINITIAL, LINE_START_STATE>{BRACKET_1}                               { yybegin(YYINITIAL);        return itemType(); }
<YYINITIAL, LINE_START_STATE>{BRACKET_2}                               { yybegin(YYINITIAL);        return itemType(); }
<LINE_START_STATE>{DIVIDER}                                            { yybegin(YYINITIAL);        return itemType(); }
<LINE_START_STATE>{PREPROC}                                            { yybegin(YYINITIAL);        return itemType(); }
<YYINITIAL, LINE_START_STATE>{QUOTE_1}                                 { yybegin(YYINITIAL);        return itemType(); }
//<YYINITIAL, LINE_START_STATE>{QUOTE_2}                                 { yybegin(YYINITIAL);        return itemType(); }
<YYINITIAL, LINE_START_STATE>{COMPLEX_WORD}                            { yybegin(YYINITIAL);        return itemType(); }
<YYINITIAL, LINE_START_STATE>{WORD_CHARACTER}+                         { yybegin(YYINITIAL);        return itemType(); }
//<YYINITIAL, LINE_START_STATE>{TAG}                                     { yybegin(YYINITIAL);        return PumlTypes.IDENTIFIER; }
<YYINITIAL, LINE_START_STATE>{SPECIAL_CHARACTER}+                      { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"/"                                       { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"["                                       { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"("                                       { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"\""                                      { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"'"                                       { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"="                                       { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>"!"                                       { yybegin(YYINITIAL);        return PumlTypes.OTHER; }
<YYINITIAL, LINE_START_STATE>{NEW_LINE}+                               { yybegin(LINE_START_STATE); return PumlTypes.NEW_LINE; }
<YYINITIAL, LINE_START_STATE>{WHITE_SPACE}+                            { return TokenType.WHITE_SPACE; }

[^] { return TokenType.BAD_CHARACTER; }
