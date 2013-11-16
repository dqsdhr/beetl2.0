parser  grammar BeetlParser;
options { 
    tokenVocab=BeetlLexer;
}


// starting point for parsing a java file
prog
    :    statement*
        EOF
    ;


block
    :   LEFT_BRACE statement* RIGHT_BRACE
    ;


// STATMENT
statement
  
    :   block   #blockSt
    |   textStatment    #textOutputSt
    |   constantsTextStatment #staticOutputSt
    |   COMMENT_TAG commentTypeTag  #commentTagSt
    |   If parExpression statement (Else statement)? #ifSt
    |   For LEFT_PAR forControl RIGHT_PAR statement    #forSt
    |   While parExpression statement   #whileSt
    |   Switch parExpression switchBlock    #siwchSt
    |   Select g_switchStatment #selectSt
    |   Try NOT? block (Catch  LEFT_PAR Identifier RIGHT_PAR  block   )?    #trySt
    |   Return expression? END  #returnSt
    |   Break END   #breakSt
    |   Continue END    #continueSt
    |   Var varDeclareList END  #varSt
    |   Directive  directiveExp #directiveSt 
    |   assignMent END  #assignSt
    |   functionTagCall #functionSt
    |   statementExpression END   #statmentExpSt
     
    ;

commentTypeTag:Identifier1 commentTypeItemTag (COMMA1 commentTypeItemTag);
commentTypeItemTag:   (LEFT_PAR1 Identifier1 (PERIOD1 Identifier1 )*)  RIGHT_PAR1  Identifier1    
                  ;
                      
directiveExp:  Identifier (StringLiteral)? END;

g_switchStatment
	:	 (LEFT_PAR base=expression RIGHT_PAR)? LEFT_BRACE g_caseStatment* g_defaultStatment? RIGHT_BRACE
	 ;

g_caseStatment 
	:	Case expression (COMMA expression)* COLON statement ;

g_defaultStatment
	:	Default COLON statement;	

varDeclareList :  assignMent (COMMA assignMent)* 
;	
assignMent	
	:	Identifier                  #assignId
	|	Identifier ASSIN expression #assignGeneral 
	|	Identifier ASSIN  block     #assignTemplateVar


;

switchBlock
    :   LEFT_BRACE switchBlockStatementGroup* switchLabel* RIGHT_BRACE
    ;

switchBlockStatementGroup
    :   switchLabel+ statement*
    ;

switchLabel
    :   Case expression COLON   
    |   Default COLON
    ;

forControl
    :    Var?   Identifier FOR_IN expression ;
   



// EXPRESSIONS

parExpression
    :   LEFT_PAR expression RIGHT_PAR
    ;

expressionList
    :   expression (COMMA expression)*
    ;

statementExpression
    :   expression
    ;
textStatment:   //����ʽ���
       LEFT_TOKEN textVar RIGHT_TOKEN
      |      LEFT_TOKEN NOT LEFT_PAR textVar RIGHT_PAR RIGHT_TOKEN ;
textVar	
:	b=expression  (COMMA textformat)?
;
textformat:
        fm=functionNs (ASSIN StringLiteral)
        | StringLiteral  ;

constantsTextStatment
	:	LEFT_TEXT_TOKEN  integerLiteral RIGHT_TOKEN   ; // �������,����������Ӧ


constantExpression
    :   expression
    ;

 expression
    :   literal     
    |   AT nativeCall   
    |   functionCall
    |   varRef
    |   json
    |   expression (INCREASE | DECREASE) 
    |   (ADD|MIN) expression
    |    (INCREASE|DECREASE) expression
    |   NOT expression
    |   expression (MUlTIP|DIV|MOD) expression
    |   expression (ADD|MIN) expression
    |   expression (LESS ASSIN | LARGE ASSIN |LARGE |LESS) expression
    |   expression (EQUAL | NOT_EQUAL) expression
    |   expression AND expression
    |   expression OR expression
    |   expression QUESTOIN expression? COLON expression? 
    |   expression NOT expression? //SAFE OUTPUT
    |  LEFT_PAR expression RIGHT_PAR
  
    ;
    

varRef:Identifier ( varAttribute)*  
;
varAttribute :PERIOD Identifier   #varAttributeGeneral
             | VIRTUAL Identifier #varAttributeVirtual
             |LEFT_SQBR expression RIGHT_SQBR #varAttributeArrayOrMap
             ;


functionCall: functionNs LEFT_PAR expressionList? RIGHT_PAR (varAttribute)* ; 
functionTagCall:functionNs  LEFT_PAR expressionList? RIGHT_PAR  block ;

functionNs:Identifier (PERIOD Identifier)*;

// NATIVE

nativeCall: nativeVarRefChain (nativeMethod|nativeArray| PERIOD nativeVarRefChain)*;
nativeMethod:  LEFT_PAR (expression (COMMA expression) *)? RIGHT_PAR ;
nativeArray: LEFT_SQBR expression RIGHT_SQBR;
nativeVarRefChain:Identifier (PERIOD Identifier)*;        


json	:	LEFT_SQBR (expression  (',' expression)*)? RIGHT_SQBR
	|	LEFT_BRACE (jsonKeyValue (',' jsonKeyValue)*)? RIGHT_BRACE
	;
jsonKeyValue
	:	StringLiteral ':' expression  
	|	Identifier ':' expression 
	;
literal
    :   integerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   booleanLiteral
    |   NULL
    ;

integerLiteral
    :   HexLiteral
    |   OctalLiteral
    |   DecimalLiteral
    ;

booleanLiteral
    :   TRUE
    |   FALSE
    ;



arguments
    :   LEFT_PAR expressionList? RIGHT_PAR
    ;

